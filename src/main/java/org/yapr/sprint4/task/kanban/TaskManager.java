package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int idCounter = 1; // Счётчик для генерации уникальных идентификаторов

    // Методы для работы с Task

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
    }

    // Методы для работы с Epic

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if (epics.containsKey(id)) {
            epics.put(id, epic);
            epic.updateStatus(); // Обновляем статус эпика на основе подзадач
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
            subtasks.entrySet().removeIf(entry -> entry.getValue().getEpicId() == id);
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }

    // Методы для работы с Subtask

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.updateStatus(); // Обновляем статус эпика после добавления подзадачи
        }
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.updateStatus(); // Обновляем статус эпика, если подзадача обновлена
            }
        } else {
            System.out.println("Подзадача с ID " + id + " не найдена.");
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                epic.updateStatus(); // Обновляем статус эпика, если подзадача удалена
            }
        } else {
            System.out.println("Подзадача с ID " + id + " не найдена.");
        }
    }

    // Дополнительные методы

    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .collect(Collectors.toList());
    }

    public List<Subtask> getSubtasksForEpic(int epicId) {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    // Метод для удаления всех задач и эпиков (и связанных подзадач)
    public void deleteAllTasksAndEpics() {
        tasks.clear();
        epics.clear();
        subtasks.clear(); // Удаляем также все подзадачи, связанные с эпиками
    }
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    private int generateId() {
        return idCounter++;
    }
}