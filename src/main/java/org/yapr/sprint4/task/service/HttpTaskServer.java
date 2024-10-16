package org.yapr.sprint4.task.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.service.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080; // Порт для сервера
    private final HttpServer server;
    private final TaskManager taskManager;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        initContexts();
    }

    public Gson getGson(){
        return gson;
    }

    // Инициализация обработчиков и привязка к соответствующим контекстам
    private void initContexts() {
        server.createContext("/tasks", new TaskHandler(taskManager,gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager,gson));
        server.createContext("/epics", new EpicHandler(taskManager,gson));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager,gson));
        server.createContext("/history", new HistoryHandler(taskManager,gson));
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
