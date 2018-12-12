package co.uk.redpixel.scoring.core;

import co.uk.redpixel.scoring.core.config.Endpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
final class HttpRequestDispatcher implements Handler {

    private final Selector multiplexer;

    private final ServerSocketChannel serverSocketChannel;

    private final Set<Endpoint> endpoints;

    @Override
    @SneakyThrows
    public void handle() {
        Optional.ofNullable(serverSocketChannel.accept())
                .ifPresent(clientSocketChannel -> new HttpRequestHandler(
                        multiplexer,
                        clientSocketChannel,
                        endpoints
                ));
    }
}
