package backend

class AccountRole implements Serializable {

    Account account
    Role role

    static mapping = {
        id composite: ['account', 'role']
        version false
    }
}
