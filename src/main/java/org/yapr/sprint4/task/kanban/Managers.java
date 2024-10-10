package org.yapr.sprint4.task.kanban;

import java.io.File;
import java.io.IOException;

public class Managers {
    // Метод getDefault возвращает экземпляр InMemoryTaskManager,
    // но типом возвращаемого значения является TaskManager
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
    // Метод для получения FileBackedTaskManager с временным файлом
    public static FileBackedTaskManager getFileBackedTaskManager() {
        try {
            File tempFile = File.createTempFile("task_manager", ".csv");
            return new FileBackedTaskManager(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        }
    }
}
