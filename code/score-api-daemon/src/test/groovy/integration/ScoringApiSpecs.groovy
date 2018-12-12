package integration

import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class ScoringApiSpecs extends Specification {

    @Shared
    def container = new DockerComposeContainer(new File("docker-compose.yaml"))

    def 'test container' () {
        setup:
        def client = HttpClients.createDefault()
        container.start()

        given:

        when:
        client.execute(new HttpPost('http://localhost:8090/login'))

        then:
        container.getServiceHost('scoresApi', 8090)

        cleanup:
        client.close()
        container.stop()
    }
}
