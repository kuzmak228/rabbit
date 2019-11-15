package com.kuzmak.rabbit.events.publishers;

import com.kuzmak.rabbit.model.TaskMessage;

public interface TaskEventPublisher {

    void publishTask(TaskMessage taskMessage);
}
