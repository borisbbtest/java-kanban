package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.service.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new InMemoryTaskManager(); // Инициализация менеджера задач (можно подменить на другой)
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}
