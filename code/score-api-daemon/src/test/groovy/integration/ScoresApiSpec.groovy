package integration

import co.uk.redpixel.scoring.core.http.Header
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Stepwise

import java.nio.charset.Charset

import static integration.BaseSpec.Path.*

@Stepwise
@Testcontainers
class ScoresApiSpec extends BaseSpec {

    final LEVEL = 1, SCORES = [ 'James': 12, 'Ted': 7, 'Will': 7 ]

    @Shared
    String sessionKey

    @Shared
    GenericContainer container = newScoresApiContainer()

    @Shared
    CloseableHttpClient client = HttpClients.createDefault()

    def 'creates a new session key for user'() {
        when:
        def response = client.execute new HttpPost(baseUrl(container).concat(LOGIN.value()))

        and:
        sessionKey = EntityUtils.toString response.entity

        then:
        sessionKey?.trim()
    }

    def 'posts user scores to a level'() {
        expect:
        SCORES.every {
            final user = it.key
            final score = it.value
            final body = score as String

            def request = new HttpPost(baseUrl(container).concat(POST_SCORES.value(LEVEL, user)))
            request.setEntity new StringEntity(body, Charset.forName('UTF-8'))
            request.addHeader(Header.AUTHORIZATION.getKey(), sessionKey)

            def response = client.execute request
            response.statusLine.statusCode == 200
        }
    }

    def 'lists highest scores for a level'() {
        given:
        final expected = SCORES.entrySet().collect { "${it.key}:${it.value}" }.join(',')

        when:
        def response = client.execute new HttpGet(baseUrl(container).concat(GET_SCORES.value(LEVEL)))

        then:
        response.statusLine.statusCode == 200

        and:
        def actual = EntityUtils.toString response.entity
        actual == expected
    }

    def cleanupSpec() {
        client.close()
    }
}
