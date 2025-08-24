package com.temporal.spring_temporal.config;

import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TemporalWorkerStarter {

    private final WorkerFactory workerFactory;

    public TemporalWorkerStarter(WorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
    }

    @PostConstruct
    public void startWorker() {
        workerFactory.start();
    }
}
