package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class AsyncTaskService {

    @Async
    public <R> CompletableFuture<R> performTask(Supplier<R> task) {
        R result = task.get();
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public <T, R> CompletableFuture<R> performTask(Function<T, R> task, T input) {
        R result = task.apply(input);
        return CompletableFuture.completedFuture(result);
    }
}