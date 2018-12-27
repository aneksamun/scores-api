package integration

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared

@Testcontainers
class HealthSpec extends BaseSpec {

    @Shared
    GenericContainer container = newScoresApiContainer()

    def 'should connect service' () {
        setup: 'prerequisites'
        def client = HttpClients.createDefault()

        when: 'requesting index page'
        def response = client.execute new HttpGet(baseUrl(container))

        then: 'succeeds'
        response.getStatusLine().statusCode == 200

        cleanup: 'resources'
        client.close()
    }
}
