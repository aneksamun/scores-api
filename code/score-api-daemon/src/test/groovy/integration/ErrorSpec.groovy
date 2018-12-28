package integration

import co.uk.redpixel.scoring.core.http.Header
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Unroll

import java.nio.charset.Charset

import static integration.BaseSpec.Path.POST_SCORES
import static java.util.UUID.randomUUID

@Testcontainers
class ErrorSpec extends BaseSpec {

    @Shared
    GenericContainer container = newScoresApiContainer()

    @Unroll
    def 'must require valid session key to post scores: #sessionKey'() {
        setup: 'prerequisites'
        def client = HttpClients.createDefault()

        and: 'request with invalid session key header'
        def request = new HttpPost(baseUrl(container).concat(POST_SCORES.value(1, 'Eminem')))
        request.setEntity new StringEntity('3', Charset.forName('UTF-8'))
        request.addHeader Header.AUTHORIZATION.getKey(), sessionKey

        when: 'requesting to post scores'
        def response = client.execute request

        then: 'fails to authorize user'
        response.getStatusLine().statusCode == 401

        cleanup: 'resources'
        client.close()

        where: 'invalid keys'
        sessionKey              | _
        null                    | _
        ''                      | _
        ' '                     | _
        randomUUID().toString() | _
    }

    def 'should not post scores when request has no session key included'() {
        setup: 'prerequisites'
        def client = HttpClients.createDefault()

        and: 'request with no session key header'
        def request = new HttpPost(baseUrl(container).concat(POST_SCORES.value(1, 'Eminem')))
        request.setEntity new StringEntity('3', Charset.forName('UTF-8'))

        when: 'requesting to post scores'
        def response = client.execute request

        then: 'fails to authorize user'
        response.getStatusLine().statusCode == 401

        cleanup: 'resources'
        client.close()
    }
}
