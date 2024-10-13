package org.yapr.sprint4.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Класс для эпиков
public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(int id, String title, String description) {
        // Передаем null для duration и startTime
        super(id, title, description, Status.NEW, null, null, null);
        this.subtasks = new ArrayList<>();
    }

    // Метод для добавления подзадачи
    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    // Метод для обновления статуса эпика на основе статусов подзадач
    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
        } else {
            boolean allDone = true;
            boolean allNew = true;
            for (Subtask subtask : subtasks) {
                if (subtask.getStatus() != Status.NEW) {
                    allNew = false;
                }
                if (subtask.getStatus() != Status.DONE) {
                    allDone = false;
                }
            }
            if (allDone) {
                setStatus(Status.DONE);
            } else if (allNew) {
                setStatus(Status.NEW);
            } else {
                setStatus(Status.IN_PROGRESS);
            }
        }
    }

    // Метод для удаления подзадачи
    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        updateStatus();
    }

    // Метод для получения подзадач эпика
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(","); // id
        sb.append("EPIC").append(","); // type
        sb.append(getTitle()).append(","); // name
        sb.append(getStatus()).append(","); // status
        sb.append(getDescription()).append(","); // description
        sb.append("0").append(","); // duration
        sb.append(","); // startTime
        sb.append(","); // priority
        sb.append(""); // epic
        return sb.toString();
    }
}
