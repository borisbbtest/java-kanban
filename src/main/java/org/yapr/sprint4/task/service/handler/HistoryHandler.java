package org.yapr.sprint4.task.service.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.yapr.sprint4.task.kanban.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private Gson gson;

    public HistoryHandler(TaskManager taskManager,Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleGetHistory(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // Internal server error
            e.printStackTrace();
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
    }
}
