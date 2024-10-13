package org.yapr.sprint4.task.kanban;

// Исключение, возникающее при ошибке сохранения менеджера
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }
}
