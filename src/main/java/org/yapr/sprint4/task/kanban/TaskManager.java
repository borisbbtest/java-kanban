package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

public class TaskManager {
    private int idCounter = 1; // Счётчик для генерации уникальных ID
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    // Метод для создания задачи
    public void createTask(Task task) {
        task.setId(generateId());  // Генерация ID при создании новой задачи
        tasks.put(task.getId(), task);
    }

    // Метод для создания эпика
    public void createTask(Epic epic) {
        epic.setId(generateId());  // Генерация ID при создании нового эпика
        epics.put(epic.getId(), epic);
    }

    // Метод для создания подзадачи
    public void createTask(Subtask subtask) {
        subtask.setId(generateId());  // Генерация ID при создании новой подзадачи
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        } else {
            System.out.println("Эпик с ID " + subtask.getEpicId() + " не найден.");
        }
    }

    // Метод для генерации уникального идентификатора
    private int generateId() {
        return idCounter++;
    }

    // Получение списка всех задач
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    // Получение задачи по ID
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            return subtasks.get(id);
        }
    }

    // Обновление задачи
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else if (epics.containsKey(id)) {
            Epic epic = (Epic) task;
            epics.put(id, epic);
            epic.updateStatus();
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = (Subtask) task;
            subtasks.put(id, subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.updateStatus();
            }
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    // Удаление задачи по ID
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
            subtasks.entrySet().removeIf(entry -> entry.getValue().getEpicId() == id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                epic.updateStatus();
            }
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    // Получение списка подзадач для эпика
    public List<Subtask> getSubtasksForEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        } else {
            return Collections.emptyList();
        }
    }
}

