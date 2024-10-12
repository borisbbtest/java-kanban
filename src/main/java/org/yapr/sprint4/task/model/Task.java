package org.yapr.sprint4.task.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected Status status;
    protected Duration duration; // Продолжительность задачи
    protected LocalDateTime startTime; // Время начала задачи
    protected int priority;

    // Конструктор без времени и продолжительности (для совместимости)
    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Конструктор с временем начала и продолжительностью
    public Task(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    // Геттеры и сеттеры для startTime и duration

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    // Метод для получения времени окончания задачи
    public LocalDateTime getEndTime() {
        return startTime != null && duration != null ? startTime.plus(duration) : null;
    }

    // Геттеры и сеттеры для остальных полей

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // Переопределение equals и hashCode для корректного сравнения задач

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Переопределение toString для сериализации задачи
    @Override
    public String toString() {
        return id + "," + title + "," + description + "," + status + "," +
                (startTime != null ? startTime : "") + "," +
                (duration != null ? duration.toMinutes() : "");
    }

    // Метод для десериализации задачи из строки
    public static Task fromString(String value) {
        String[] fields = value.split(",");
        Task task = new Task(Integer.parseInt(fields[0]), fields[1], fields[2], Status.valueOf(fields[3]));

        if (!fields[4].isEmpty()) {
            task.setStartTime(LocalDateTime.parse(fields[4]));
        }

        if (!fields[5].isEmpty()) {
            task.setDuration(Duration.ofMinutes(Long.parseLong(fields[5])));
        }

        return task;
    }
}
