package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LIMIT = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= HISTORY_LIMIT) {
            history.removeFirst(); // Удаляем самый старый элемент, если история переполнена
        }
        history.add(task); // Добавляем новую задачу в конец списка
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history); // Возвращаем копию списка истории
    }
}