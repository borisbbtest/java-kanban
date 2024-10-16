package org.yapr.sprint4.task.service.handler;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private  Gson gson;

    public TaskHandler(TaskManager taskManager,  Gson gson ) {
        this.taskManager = taskManager;
        this.gson =  gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("/tasks/\\d+")) {
                    handleGetTaskById(exchange);
                } else {
                    handleGetAllTasks(exchange);
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                handleCreateOrUpdateTask(exchange);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                handleDeleteTask(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal server error
            e.printStackTrace();
        }
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        Task task = taskManager.getTaskById(id);
        if (task != null) {
            sendText(exchange, gson.toJson(task), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Task task = gson.fromJson(requestBody, Task.class);
        int id= task.getId();

        try {
            if (taskManager.getTaskById(id) == null) {
                taskManager.createTask(task);
                sendText(exchange, gson.toJson(task), 201); // Created
            } else {
                taskManager.updateTask(task);
                sendText(exchange, gson.toJson(task), 200); // OK
            }
        } catch (IllegalArgumentException e) {
            sendHasTimeIntersection(exchange);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        if (taskManager.deleteTaskById(id)) {
            exchange.sendResponseHeaders(200, -1); // OK
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }
}
