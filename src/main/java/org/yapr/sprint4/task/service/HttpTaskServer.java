package org.yapr.sprint4.task.service;

import com.sun.net.httpserver.HttpServer;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.service.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080; // Порт для сервера
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        initContexts();
    }

    // Инициализация обработчиков и привязка к соответствующим контекстам
    private void initContexts() {
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
    }

    // Запуск сервера
    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    // Остановка сервера
    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }


}
