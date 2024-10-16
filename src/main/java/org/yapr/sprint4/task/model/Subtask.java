package org.yapr.sprint4.task.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// Класс для подзадач
public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, String title, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask subtask)) return false;
        if (!super.equals(o)) return false;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(","); // id
        sb.append("SUBTASK").append(","); // type
        sb.append(getTitle()).append(","); // name
        sb.append(getStatus()).append(","); // status
        sb.append(getDescription()).append(","); // description
        sb.append(getDuration() != null ? getDuration().toMinutes() : "0").append(","); // duration
        sb.append(getStartTime() != null ? getStartTime() : "").append(","); // startTime
        sb.append(epicId); // epicId
        return sb.toString();
    }
}
