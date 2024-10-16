package org.yapr.sprint4.task.service.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("/epics/\\d+")) {
                    handleGetEpicById(exchange);
                } else {
                    handleGetAllEpics(exchange);
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                handleCreateOrUpdateEpic(exchange);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                handleDeleteEpic(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal server error
            e.printStackTrace();
        }
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        Epic epic = taskManager.getEpicById(id);
        if (epic != null) {
            sendText(exchange, gson.toJson(epic), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200); // Собираем все задачи, включая эпики
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Epic epic = gson.fromJson(requestBody, Epic.class);

        if (epic.getId() == 0) {
            taskManager.createEpic(epic);
            sendText(exchange, gson.toJson(epic), 201); // Created
        } else {
            taskManager.updateEpic(epic);
            sendText(exchange, gson.toJson(epic), 200); // OK
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        if (taskManager.deleteEpicById(id)) {
            exchange.sendResponseHeaders(200, -1); // OK
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }
}
