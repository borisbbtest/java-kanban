package org.yapr.sprint4.task.kanban;

public class Managers {
    // Метод getDefault возвращает экземпляр InMemoryTaskManager,
    // но типом возвращаемого значения является TaskManager
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
