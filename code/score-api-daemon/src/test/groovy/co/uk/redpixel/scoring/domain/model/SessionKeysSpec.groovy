package co.uk.redpixel.scoring.domain.model

import spock.lang.Specification
import spock.lang.Unroll

class SessionKeysSpec extends Specification {

    def 'should create a new session key'() {
        given: 'keys'
        def keys = SessionKeys.getInstance()

        when: 'I request key'
        def key = keys.generate()

        then: 'the new key is created'
        keys.contains key

        and: 'it has valid format'
        !key.contains('-')
    }

    def 'should not contain invalid key'() {
        given: 'system keys'
        def keys = SessionKeys.getInstance()

        and: 'invalid key'
        def key = '266e80e478b34769b212aaa666615884'

        when: 'I match key'
        def valid = keys.contains key

        then: 'it should fail'
        !valid
    }

    @Unroll
    def 'should not expire: #key'() {
        when: 'I check key validity'
        def expired = SessionKeys.getInstance().isExpired key, 6000

        then: 'it should be alright'
        !expired

        where:
        key                                  | _
        '266e80e478b34769b212aaa666615884'   | _
        SessionKeys.getInstance().generate() | _
    }

    def 'should expire'() {
        given: 'system keys'
        def keys = SessionKeys.getInstance()

        and: 'expired key'
        def key = keys.generate()

        when: 'I check key validity'
        def immediately = 0
        def expired = keys.isExpired key, immediately

        then: 'it should be invalid'
        expired
    }
}
