package backend

import grails.gorm.transactions.Transactional
import java.text.SimpleDateFormat

class UserService {
    def getUsers(String name = null, Date startDate = null, Date endDate = null, int page = 0, int size = 10) {
        def c = User.createCriteria()
        def users = c.list(max: size, offset: page * size) {
            if (name) {
                or {
                    ilike("firstName", "%${name}%")
                    ilike("lastName", "%${name}%")
                }
            }

            if (startDate) {
                ge("dateOfBirth", startDate)
            }
            if (endDate) {
                le("dateOfBirth", endDate)
            }
        }

        def matchingUsers = User.createCriteria().list() {
            if (name) {
                or {
                    ilike("firstName", "%${name}%")
                    ilike("lastName", "%${name}%")
                }
            }

            if (startDate) {
                ge("dateOfBirth", startDate)
            }
            if (endDate) {
                le("dateOfBirth", endDate)
            }
        }

        def sdf = new SimpleDateFormat("yyyy-MM-dd")

        int[] monthCounts = new int[12]
        for (user in matchingUsers){
            monthCounts[user.dateOfBirth.month]++
        }

        return [
                users: users.collect {
                    user -> new UserViewModel(
                            name: "${user.firstName} ${user.lastName}",
                            dateOfBirth: sdf.format(user.dateOfBirth),
                            address: "${user.street} ${user.apt ? ', ' + user.apt : ''}, ${user.city}, ${user.state} ${user.zipCode}"
                    )},
                total: users.totalCount,
                monthCounts: monthCounts
        ]
    }

    @Transactional
    def saveUser(request) {

        def user = new User(request)

        if (user.save()){
            return true
        }
        else{
            return false
        }
    }
}