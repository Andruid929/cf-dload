package net.druidlabs.cfdload.concurrency;

import net.druidlabs.cfdload.errorhandling.ErrorLogger;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public final class Session {

    private static byte activeInstances = 0;

    private final ExecutorService taskService;

    private Session() {
        taskService = Executors.newVirtualThreadPerTaskExecutor();
        activeInstances++;

        Runtime.getRuntime().addShutdownHook(
                Thread.ofVirtual()
                        .name("graceful-shutdown-thread")
                        .uncaughtExceptionHandler((t, e) -> ErrorLogger.logError(e))
                        .unstarted(initiateGracefulShutdown())
        );
    }

    public <T> @NotNull Future<T> addTask(Callable<T> task) {
        return taskService.submit(task);
    }

    public List<Runnable> endSession() throws InterruptedException {
        taskService.shutdown();

        if (taskService.awaitTermination(15, TimeUnit.SECONDS)) {
            System.err.println("Graceful shutdown failed, murdering session initiated");

            return taskService.shutdownNow();
        }

        return Collections.emptyList();
    }

    @Contract(pure = true)
    private @NotNull Runnable initiateGracefulShutdown() {
        return () -> {
            try {
                List<Runnable> failedTasks = endSession();

                String outputMessage;

                if (failedTasks.isEmpty()) {
                    outputMessage = "Session terminated with no failed tasks";
                } else {
                    String message = (failedTasks.size() == 1) ? "failed task" : "failed tasks";

                    outputMessage = "Session terminated with " + failedTasks.size() + message;
                }

                System.out.println(outputMessage);

            } catch (InterruptedException e) {
                System.err.println("Session failed to terminate correctly");

                ErrorLogger.logError(e);
            }
        };
    }


    @Contract(" -> new")
    public static @NotNull Session createSession() {
        if (activeInstances > 1) {
            throw new IllegalStateException("Cannot have multiple session instances");
        }

        return new Session();
    }
}
