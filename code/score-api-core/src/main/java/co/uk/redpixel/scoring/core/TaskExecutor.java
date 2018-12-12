package co.uk.redpixel.scoring.core;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
final class TaskExecutor implements Closeable {

    private final ExecutorService executor = Executors.newWorkStealingPool();

    private static class TaskExecutorConstructor {
        private static final TaskExecutor INSTANCE = new TaskExecutor();
    }

    static TaskExecutor getInstance() {
        return TaskExecutorConstructor.INSTANCE;
    }

    void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void close() {
        try {
            executor.shutdown();
            executor.awaitTermination(3, SECONDS);
        } catch (Exception exception ) {
            log.error("An error occurred awaiting task completion: {}", exception);
        }
    }
}
