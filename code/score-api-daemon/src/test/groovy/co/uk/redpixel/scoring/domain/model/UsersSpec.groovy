package co.uk.redpixel.scoring.domain.model

import spock.lang.Specification
import spock.lang.Subject

class UsersSpec extends Specification {

    @Subject
    def users = new Users()

    def 'should list top scores'() {
        given: 'scores'
        users.add('user3', 3)
             .add('user2', 2)
             .add('user1', 1)
             .add('user0', 0)

        and: 'limit'
        def limit = 3

        when: 'I get top scores'
        def top = users.getTopScores limit

        then: 'I got limited highest available scores'
        top == 'user3:3,user2:2,user1:1'
    }
}
