package co.uk.redpixel.scoring.core;

import co.uk.redpixel.scoring.core.config.Routes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Optional;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.util.Collections.unmodifiableSet;

@Slf4j
public final class HttpServer implements Closeable {

    private final Selector multiplexer;

    private final ServerSocketChannel channel;

    private final Routes routes = Routes.empty();

    @SneakyThrows
    private HttpServer(int port) {
        this.multiplexer = Selector.open();
        this.channel = ServerSocketChannel.open();

        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);

        val requestAcceptToken = channel.register(multiplexer, OP_ACCEPT);
        requestAcceptToken.attach(new HttpRequestDispatcher(multiplexer, channel, unmodifiableSet(routes)));
    }

    @SneakyThrows
    public int getPort() {
        return channel.socket().getLocalPort();
    }

    public void start() {
        while (!Thread.interrupted()) {
            try {
                multiplexer.select();

                val iterator = multiplexer.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey token = iterator.next();
                    iterator.remove();
                    Optional.ofNullable((Handler) token.attachment())
                            .ifPresent(Handler::handle);
                }
            } catch (Exception exception) {
                log.error("An error occurred processing request: {}", exception);
            }
        }
    }

    @SneakyThrows
    public void close() {
        TaskExecutor.getInstance().close();
        multiplexer.close();
        channel.close();
    }

    public static HttpServerBuilder on(int port) {
        return new HttpServerBuilder(port);
    }

    public static class HttpServerBuilder {

        private final HttpServer httpServer;

        private HttpServerBuilder(int port) {
            this.httpServer = new HttpServer(port);
        }

        public HttpServerBuilder with(Routes routes) {
            this.httpServer.routes.addAll(routes);
            return this;
        }

        public HttpServer create() {
            return httpServer;
        }
    }
}
