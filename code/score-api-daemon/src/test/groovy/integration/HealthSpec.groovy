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

    def 'should establish connection with service' () {
        setup:
        def client = HttpClients.createDefault()

        when:
        def response = client.execute new HttpGet(baseUrl(container))

        then:
        response.getStatusLine().statusCode == 200

        cleanup:
        client.close()
    }
}
