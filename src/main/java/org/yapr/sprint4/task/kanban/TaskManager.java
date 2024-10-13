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

    boolean deleteTaskById(int id);

    boolean isTimeOverlap(Task newTask);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Subtask> getSubtasksForEpic(int epicId);

    void deleteAllTasks();

    List<Task> getHistory();  // Метод для получения истории просмотров
}
