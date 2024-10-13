package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    // TreeSet для хранения задач, отсортированных по времени начала
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    private int idCounter = 1;

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeIntersection(Task newTask) {
        return prioritizedTasks.stream().anyMatch(task -> task.getStartTime() != null && task.getEndTime() != null &&
                newTask.getStartTime() != null &&
                task.getStartTime().isBefore(newTask.getEndTime()) &&  newTask.getStartTime().isBefore(task.getEndTime()));
    }

    @Override
    public void createTask(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null && hasTimeIntersection(task)) {
            throw new IllegalArgumentException("Задача пересекается с уже существующей задачей по времени.");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    // Метод для проверки пересечения по времени
    public boolean isTimeOverlap(Task newTask) {
        return prioritizedTasks.stream()
                .filter(existingTask -> existingTask.getStartTime() != null && newTask.getStartTime() != null)
                .anyMatch(existingTask ->
                        newTask.getStartTime().isBefore(existingTask.getStartTime().plus(newTask.getDuration())) &&
                                existingTask.getStartTime().isBefore(newTask.getStartTime().plus(existingTask.getDuration()))
                );
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            // Удаляем задачу из списка приоритезированных задач перед проверкой
            prioritizedTasks.remove(tasks.get(task.getId()));

            // Проверка на пересечение только с другими задачами
            if (task.getStartTime() != null && task.getEndTime() != null && hasTimeIntersection(task)) {
                // Добавляем задачу обратно, если проверка не прошла
                prioritizedTasks.add(tasks.get(task.getId()));
                throw new IllegalArgumentException("Задача пересекается с уже существующей задачей по времени.");
            }

            // Обновляем задачу в менеджере
            tasks.put(task.getId(), task);

            // Если у задачи есть время начала, добавляем её обратно в приоритезированный список
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Задача с ID " + task.getId() + " не найдена.");
        }
    }

    @Override
    public boolean deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task removedTask = tasks.remove(id);
            prioritizedTasks.remove(removedTask);
            historyManager.remove(id);
            return  true;
        } else {
            System.out.println("Задача с ID " + id + " не найдена.");
        }
        return false;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с ID " + epic.getId() + " не найден.");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
            subtasks.values().removeIf(subtask -> subtask.getEpicId() == id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getEndTime() != null && hasTimeIntersection(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается с уже существующей задачей по времени.");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            // Удаляем подзадачу из списка приоритезированных задач перед проверкой на пересечение
            prioritizedTasks.remove(subtasks.get(subtask.getId()));

            // Проверка на пересечение только с другими задачами (исключая саму себя)
            if (subtask.getStartTime() != null && subtask.getEndTime() != null && hasTimeIntersection(subtask)) {
                // Добавляем подзадачу обратно, если проверка не прошла
                prioritizedTasks.add(subtasks.get(subtask.getId()));
                throw new IllegalArgumentException("Подзадача пересекается с уже существующей задачей по времени.");
            }

            // Обновляем подзадачу
            subtasks.put(subtask.getId(), subtask);

            // Если есть время начала, добавляем задачу обратно в приоритезированный список
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        } else {
            System.out.println("Подзадача с ID " + subtask.getId() + " не найдена.");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask removedSubtask = subtasks.remove(id);
            prioritizedTasks.remove(removedSubtask);
            historyManager.remove(id);
        } else {
            System.out.println("Подзадача с ID " + id + " не найдена.");
        }
    }

    @Override
    public List<Subtask> getSubtasksForEpic(int epicId) {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritizedTasks.clear();
        historyManager.clear();
    }

    private int generateId() {
        return idCounter++;
    }

    @Override
    public List<Task> getAllTasks() {
        return Stream.concat(Stream.concat(tasks.values().stream(), epics.values().stream()), subtasks.values().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}