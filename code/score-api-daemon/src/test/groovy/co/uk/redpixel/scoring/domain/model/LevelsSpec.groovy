package co.uk.redpixel.scoring.domain.model

import spock.lang.Specification

class LevelsSpec extends Specification {

    def 'should list top scores'() {
        given: 'levels'
        def levels = new Levels()
        levels.records.clear()

        and: 'scores'
        levels.add 1, 'user1', 3
        levels.add 2, 'user1', 2
        levels.add 1, 'user2', 1
        levels.add 1, 'user2', 1

        when: 'I view scores'
        def list = levels.listHighScores 1, 2

        then: 'I see top results'
        list == 'user1:3,user2:2'
    }

    def 'should list empty string'() {
        given: 'levels'
        def levels = new Levels()
        levels.records.clear()

        when: 'I view scores'
        def list = levels.listHighScores 1, 1

        then: 'I got empty string'
        list.isEmpty()
    }

    def 'should add new level'() {
        given: 'levels'
        def levels = new Levels()
        levels.records.clear()

        when: 'I add a new level'
        levels.add 1, 'champion', 3

        then: 'level is created'
        levels.listHighScores (1, 10) == 'champion:3'
    }

    def 'should update scores for user at the same level'() {
        given: 'levels'
        def levels = new Levels()
        levels.records.clear()

        when: 'I add scores'
        levels.add 1, 'champion', 3
        levels.add 1, 'champion', 4

        then: 'scores are updated'
        levels.listHighScores (1, 10) == 'champion:7'
    }
}
