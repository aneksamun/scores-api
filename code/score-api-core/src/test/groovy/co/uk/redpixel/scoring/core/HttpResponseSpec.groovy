package co.uk.redpixel.scoring.core

import co.uk.redpixel.scoring.core.config.Endpoint
import co.uk.redpixel.scoring.core.config.Routes
import spock.lang.Specification
import spock.lang.Unroll

import static co.uk.redpixel.scoring.core.HttpResponse.ok
import static co.uk.redpixel.scoring.core.HttpResponse.notFound
import static co.uk.redpixel.scoring.core.HttpResponse.badRequest
import static co.uk.redpixel.scoring.core.HttpResponse.unauthorized

class HttpResponseSpec extends Specification {

    @Unroll
    def 'test response conversion: #representation'() {
        when: 'response is formatted to string'
        def actual = response.toString()

        then: 'should have expected format'
        actual =~ representation

        where: 'data'
        response             | representation
        notFound()           | 'HTTP/1.1 404 Not Found\r\nDate: .* GMT\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: 0\r\n'
        badRequest('Test')   | 'HTTP/1.1 400 Bad Request\r\nDate: .* GMT\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: 4\r\n\r\nTest'
        unauthorized('Test') | 'HTTP/1.1 401 Unauthorized\r\nDate: .* GMT\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: 4\r\n\r\nTest'
        ok(100)              | 'HTTP/1.1 200 OK\r\nDate: .* GMT\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: 3\r\n\r\n100'
    }

    def 'should successfully match request path'() {
        given: 'request'
        def request = HttpRequest.of 'GET /levels/1/scores HTTP/1.1'

        and: 'routes'
        def routes = Routes.of Endpoint.<String>get("/levels/[0-9]*/scores\$", { HttpRequest r -> ok("Great success!") })

        when: 'response object is built'
        def response = HttpResponse.of request with routes build()

        then:
        response == ok("Great success!")
    }

    def 'should respond with NOT FOUND'() {
        given: 'request'
        def request = HttpRequest.of 'POST /login HTTP/1.1'

        when: 'response object is built with no request path matching routes'
        def response = HttpResponse.of request build()

        then: 'should respond with NOT FOUND on request'
        response == notFound()
    }
}
