package org.yapr.sprint4.task.model;

import java.util.Objects;

// Класс для подзадач
public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

// --Commented out by Inspection START (10/12/24, 2:01 PM):
//    public Subtask(int id, String title, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
//        super(id, title, description, status, duration, startTime);
//        this.epicId = epicId;
//    }
// --Commented out by Inspection STOP (10/12/24, 2:01 PM)

    public int getEpicId() {
        return epicId;
    }

// --Commented out by Inspection START (10/12/24, 2:01 PM):
//    public void setEpicId(int epicId) {
//        this.epicId = epicId;
//    }
// --Commented out by Inspection STOP (10/12/24, 2:01 PM)

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
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
