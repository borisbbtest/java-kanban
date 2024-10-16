package org.yapr.sprint4.task.service.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Subtask;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches("/subtasks/\\d+")) {
                    handleGetSubtaskById(exchange);
                } else {
                    handleGetAllSubtasks(exchange);
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                handleCreateOrUpdateSubtask(exchange);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                handleDeleteSubtask(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal server error
            e.printStackTrace();
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask != null) {
            sendText(exchange, gson.toJson(subtask), 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getSubtasksForEpic(0)), 200); // Epic ID может быть передан
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Subtask subtask = gson.fromJson(requestBody, Subtask.class);

        try {
            if (subtask.getId() == 0) {
                taskManager.createSubtask(subtask);
                sendText(exchange, gson.toJson(subtask), 201); // Created
            } else {
                taskManager.updateSubtask(subtask);
                sendText(exchange, gson.toJson(subtask), 200); // OK
            }
        } catch (IllegalArgumentException e) {
            sendHasTimeIntersection(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        if (taskManager.deleteSubtaskById(id)) {
            exchange.sendResponseHeaders(200, -1); // OK
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }
}
