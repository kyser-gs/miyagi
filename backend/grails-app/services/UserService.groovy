package backend

import grails.gorm.transactions.Transactional

class UserService {
    def getUsers() {
        def users = User.list()
        return users.collect {
            user -> new UserViewModel(
                    name: "${user.firstName} ${user.lastName}",
                    dateOfBirth: user.dateOfBirth,
                    address: "${user.street} ${user.apt ? ', ' + user.apt : ''}, ${user.city}, ${user.state} ${user.zipCode}"
            )
        }
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