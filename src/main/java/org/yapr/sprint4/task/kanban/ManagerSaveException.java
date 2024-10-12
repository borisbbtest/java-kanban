package org.yapr.sprint4.task.kanban;

// Исключение, возникающее при ошибке сохранения менеджера
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }

// --Commented out by Inspection START (10/12/24, 2:01 PM):
//    public ManagerSaveException(String message, Throwable cause) {
//        super(message, cause);
//    }
// --Commented out by Inspection STOP (10/12/24, 2:01 PM)
}
