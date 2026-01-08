package backend

import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat

class UserService {
    ElasticService elasticService

    def getUsers(String name = null, Date startDate = null, Date endDate = null, int page = 0, int size = 10) {
        def searchResponse = elasticService.searchUsers(name, startDate, endDate, page, size).hits

        def total = searchResponse.total.value

        def users = searchResponse.hits.collect {
            hit -> new UserViewModel(
                    name: hit._source.fullName,
                    dateOfBirth: hit._source.dateOfBirth,
                    address: hit._source.address.fullAddress
            )
        }

        def allUsers = elasticService.searchUsers(name, startDate, endDate, 0, 10000)
        def sdf = new SimpleDateFormat("yyyy-MM-dd")

        int[] monthCounts = new int[12]
        for (user in allUsers.hits.hits){
            def date = sdf.parse(user._source.dateOfBirth)
            monthCounts[date.month]++
        }

        return [
                users: users,
                total: total,
                monthCounts: monthCounts
        ]
    }

    @Transactional
    def saveUser(request) {
        def user = new User(request)

        if (user.save(flush: true)){
            return user
        }
        else{
            return null
        }
    }
}