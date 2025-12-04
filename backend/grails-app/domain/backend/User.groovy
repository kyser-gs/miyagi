package backend

class User {
    String firstName
    String lastName
    Date dateOfBirth
    String street
    String apt
    String city
    String state
    String zipCode

    Date dateCreated
    Date lastUpdated

    static constraints = {
        firstName blank: false, maxSize: 100
        lastName blank: false, maxSize: 100
        dateOfBirth nullable: false
        street blank: false, maxSize: 255
        apt nullable: true, maxSize: 50
        city blank: false, maxSize: 100
        state blank: false, size: 2..2
        zipCode blank: false, matches: /\d{5}/
    }

    static mapping = {
        table 'users'
    }
}
