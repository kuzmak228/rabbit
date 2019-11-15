package com.kuzmak.rabbit.job;

import com.kuzmak.rabbit.events.publishers.TaskEventPublisher;
import com.kuzmak.rabbit.model.TaskMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public abstract class DefaultTask {

    private final TaskEventPublisher eventPublisher;

    protected void receiveMessage(final TaskMessage taskMessage) {
        log.info("Received message {}", taskMessage);
        createAndExecute(taskMessage, this::execute);
    }

    private void createAndExecute(final TaskMessage taskMessage,
                                  final Function<TaskMessage, Boolean> execute) {
        execute.apply(taskMessage);
    }

    protected void schedule() {
        eventPublisher.publishTask(new TaskMessage(getTaskName(), LocalDateTime.now()));
    }

    protected abstract boolean execute(final TaskMessage message);

    protected abstract String getTaskName();
}
