package backend

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='username')
class Account implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password

    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false

    Date dateCreated
    Date lastUpdated

    static transients = ['springSecurityService']

    Set<Role> getAuthorities() {
        (AccountRole.findAllByAccount(this) as List<AccountRole>)*.role as Set<Role>
    }

    static constraints = {
        username nullable: false, blank: false, unique: true, maxSize: 255
        password nullable: false, blank: false, minSize: 8, maxSize: 255
    }

    static mapping = {
        table 'accounts'
        password column: '`password`'
        version false
    }
}
