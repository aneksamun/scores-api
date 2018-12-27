package integration

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import spock.lang.Specification

import java.nio.file.Paths

abstract class BaseSpec extends Specification {

    private static final EXPOSED_PORT = 8090

    static enum Path {
        LOGIN('/login'),
        POST_SCORES('/levels/%d/users/%s'),
        GET_SCORES('/levels/%d/scores')

        private final String message

        Path(message) {
            this.message = message
        }

        def value() {
            message
        }

        def value(Object... args) {
            sprintf message, args
        }
    }

    static GenericContainer newScoresApiContainer() {
        new GenericContainer<>(
                new ImageFromDockerfile('scores-api-container')
                        .withFileFromPath('Dockerfile', Paths.get('Dockerfile'))
                        .withFileFromPath('target/score-api-daemon-1.0.jar', Paths.get('target/score-api-daemon-1.0.jar')))
                .withExposedPorts EXPOSED_PORT
    }

    static String baseUrl(container) {
        "http://localhost:${container.getMappedPort EXPOSED_PORT}"
    }
}
