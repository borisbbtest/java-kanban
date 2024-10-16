package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Status;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод для загрузки менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(Path.of(file.getPath()));
            for (String line : lines) {
                if (!line.startsWith("id")) {
                    Task task = manager.fromString(line);
                    if (task instanceof Epic) {
                        manager.createEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        manager.createSubtask((Subtask) task);
                    } else {
                        manager.createTask(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла: " + e.getMessage());
        }
        return manager;
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public boolean deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
        return  true;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public boolean deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
        return false;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public boolean deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
        return false;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    // Метод для сохранения состояния менеджера в файл
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    // Восстановление задачи из строки
    private Task fromString(String line) {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String title = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = null;
        LocalDateTime startTime = null;

         if (!type.equals("EPIC") && !fields[6].isEmpty()) {
            startTime = LocalDateTime.parse(fields[6]);
        }

        if (!type.equals("EPIC") && !fields[5].isEmpty()) {
            duration = Duration.ofMinutes(Long.parseLong(fields[5]));
        }


        return switch (type) {
            case "TASK" -> new Task(id, title, description, status,duration,startTime);
            case "EPIC" -> new Epic(id, title, description);
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(fields[7]);
                yield new Subtask(id, title, description, status, epicId,duration,startTime);
            }
            default -> throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        };
    }
}
