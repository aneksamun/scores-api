package co.uk.redpixel.scoring.core;

import co.uk.redpixel.scoring.core.config.Endpoint;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_READ;

@Slf4j
final class HttpRequestHandler implements Handler {

    private static final int READ_BUF_SIZE = 1024;
    private static final int WRITE_BUF_SIZE = 1024;

    private final SelectionKey operationToken;
    private final SocketChannel clientChannel;
    private final Set<Endpoint> endpoints;

    private ByteBuffer readBuffer = ByteBuffer.allocate(READ_BUF_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(WRITE_BUF_SIZE);

    @SneakyThrows
    HttpRequestHandler(Selector multiplexer, SocketChannel clientChannel, Set<Endpoint> endpoints) {
        this.clientChannel = clientChannel;
        this.clientChannel.configureBlocking(false);

        this.operationToken = clientChannel.register(multiplexer, OP_READ);
        this.operationToken.attach(this);

        this.endpoints = endpoints;

        multiplexer.wakeup();
    }

    @Override
    public void handle() {
        if (operationToken.isReadable()) {
            read();
        } else if (operationToken.isWritable()) {
            write();
        }
    }

    @SneakyThrows
    private synchronized void read() {
        int count = clientChannel.read(readBuffer);
        if (count == -1) {
            operationToken.cancel();
            clientChannel.close();
        } else {
            TaskExecutor.getInstance().execute(this::process);
        }
    }

    @SneakyThrows
    private void write() {
        int count = clientChannel.write(writeBuffer);
        if (count > 0) {
            readBuffer.clear();
            writeBuffer.clear();
            operationToken.interestOps(OP_READ);
            operationToken.selector().wakeup();
        }
    }

    private synchronized void process() {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes, 0, bytes.length);

        val socketInputData = new String(bytes, Charset.forName("UTF-8"));
        val request = HttpRequest.of(socketInputData);

        log.debug("Processing request\n{}", socketInputData);

        val response = HttpResponse.of(request)
                .with(endpoints)
                .build();

        val socketOutputData = response.toString();

        log.debug("Sending response\n{}", socketOutputData);

        writeBuffer = ByteBuffer.wrap(socketOutputData.getBytes());

        operationToken.interestOps(SelectionKey.OP_WRITE);
        operationToken.selector().wakeup();
    }
}
