package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); // Добавляет задачу в историю
    List<Task> getHistory(); // Возвращает список просмотренных задач
}