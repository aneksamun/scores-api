package integration

import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared

import static integration.Path.LOGIN

@Testcontainers
class ScoresApiSpec extends BaseSpec {

    @Shared
    String sessionKey

    @Shared
    CloseableHttpClient client = HttpClients.createDefault()

    @Shared
    GenericContainer container = newScoresApiContainer()

    def 'should generate session key'() {
        when:
        def response = client.execute new HttpPost(baseUrl(container).concat(LOGIN.value()))

        and:
        sessionKey = EntityUtils.toString response.entity

        then:
        sessionKey?.trim()
    }

    def cleanupSpec() {
        client.close()
    }
}
