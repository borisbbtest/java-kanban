package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getPrioritizedTasks();

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksForEpic(int epicId);

    void deleteAllTasks();

    List<Task> getHistory();  // Метод для получения истории просмотров

// --Commented out by Inspection START (10/12/24, 2:01 PM):
//    // Добавьте метод для получения задач в приоритетном порядке
//    List<Task> getPrioritizedTasks();  // Новый метод для получения приоритетных задач
// --Commented out by Inspection STOP (10/12/24, 2:01 PM)
}
