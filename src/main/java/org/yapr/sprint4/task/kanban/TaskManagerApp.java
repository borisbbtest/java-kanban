package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class TaskManagerApp {
    private static final TaskManager taskManager = Managers.getFileBackedTaskManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        boolean exit = false;

        while (!exit) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createTask();
                case 2 -> createEpic();
                case 3 -> createSubtask();
                case 4 -> updateTask();
                case 5 -> deleteTaskById();
                case 6 -> deleteAllTasks();
                case 7 -> getTaskById();
                case 8 -> getAllTasks();
                case 9 -> getSubtasksForEpic();
                case 10 -> deleteAllTasksAndEpics();
                case 11 -> printHistory();
                case 0 -> exit = true;
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nВыберите действие:");
        System.out.println("1. Создать задачу");
        System.out.println("2. Создать эпик");
        System.out.println("3. Создать подзадачу");
        System.out.println("4. Обновить задачу");
        System.out.println("5. Удалить задачу по идентификатору");
        System.out.println("6. Удалить все задачи");
        System.out.println("7. Получить задачу по идентификатору");
        System.out.println("8. Получить список всех задач");
        System.out.println("9. Получить список подзадач определённого эпика");
        System.out.println("10. Удалить все задачи и эпики");
        System.out.println("11. Показать историю просмотров задач");
        System.out.println("0. Выйти");
    }

    private static void createTask() {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        System.out.println("Введите статус задачи (NEW, IN_PROGRESS, DONE):");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());

        // Ввод для Duration и LocalDateTime
        System.out.println("Введите продолжительность задачи в минутах (0, если не задана):");
        long durationInput = Long.parseLong(scanner.nextLine());
        Duration duration = durationInput > 0 ? Duration.ofMinutes(durationInput) : null;

        System.out.println("Введите дату и время начала задачи в формате YYYY-MM-DDTHH:MM (оставьте пустым, если не задана):");
        String startTimeInput = scanner.nextLine();
        LocalDateTime startTime = startTimeInput.isEmpty() ? null : LocalDateTime.parse(startTimeInput);

        System.out.println("Введите приоритет задачи (целое число, 1 - низкий, 10 - высокий):");
        int priority = Integer.parseInt(scanner.nextLine());

        Task task = new Task(0, title, description, status, duration, startTime, priority);
        if (taskManager.isTimeOverlap(task)) {
            System.out.println("Ошибка: Задача пересекается с другой задачей по времени.");
        } else {
            taskManager.createTask(task);
            System.out.println("Задача создана с ID: " + task.getId());
        }
    }

    private static void createEpic() {
        System.out.println("Введите название эпика:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();

        Epic epic = new Epic(0, title, description);
        taskManager.createEpic(epic);
        System.out.println("Эпик создан с ID: " + epic.getId());
    }

    private static void createSubtask() {
        System.out.println("Введите ID эпика для подзадачи:");
        int epicId = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите название подзадачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine();
        System.out.println("Введите статус подзадачи (NEW, IN_PROGRESS, DONE):");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());

        // Ввод для Duration и LocalDateTime
        System.out.println("Введите продолжительность подзадачи в минутах (0, если не задана):");
        long durationInput = Long.parseLong(scanner.nextLine());
        Duration duration = durationInput > 0 ? Duration.ofMinutes(durationInput) : null;

        System.out.println("Введите дату и время начала подзадачи в формате YYYY-MM-DDTHH:MM (оставьте пустым, если не задана):");
        String startTimeInput = scanner.nextLine();
        LocalDateTime startTime = startTimeInput.isEmpty() ? null : LocalDateTime.parse(startTimeInput);

        System.out.println("Введите приоритет подзадачи (целое число, 1 - низкий, 10 - высокий):");
        int priority = Integer.parseInt(scanner.nextLine());

        Subtask subtask = new Subtask(0, title, description, status, epicId, duration, startTime, priority);
        if (taskManager.isTimeOverlap(subtask)) {
            System.out.println("Ошибка: Подзадача пересекается с другой задачей по времени.");
        } else {
            taskManager.createSubtask(subtask);
            System.out.println("Подзадача создана с ID: " + subtask.getId());
        }
    }

    private static void updateTask() {
        System.out.println("Введите ID задачи для обновления:");
        int id = Integer.parseInt(scanner.nextLine());

        Task task = taskManager.getTaskById(id);
        if (task != null) {
            System.out.println("Введите новое название задачи:");
            String title = scanner.nextLine();
            System.out.println("Введите новое описание задачи:");
            String description = scanner.nextLine();
            System.out.println("Введите новый статус задачи (NEW, IN_PROGRESS, DONE):");
            Status status = Status.valueOf(scanner.nextLine().toUpperCase());

            System.out.println("Введите новую продолжительность задачи в минутах (0, если не задана):");
            long durationInput = Long.parseLong(scanner.nextLine());
            Duration duration = durationInput > 0 ? Duration.ofMinutes(durationInput) : null;

            System.out.println("Введите новое время начала задачи в формате YYYY-MM-DDTHH:MM (оставьте пустым, если не задана):");
            String startTimeInput = scanner.nextLine();
            LocalDateTime startTime = startTimeInput.isEmpty() ? null : LocalDateTime.parse(startTimeInput);

            System.out.println("Введите новый приоритет задачи (целое число, 1 - низкий, 10 - высокий):");
            int priority = Integer.parseInt(scanner.nextLine());

            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(status);
            task.setDuration(duration);
            task.setStartTime(startTime);
            task.setPriority(priority);

            if (taskManager.isTimeOverlap(task)) {
                System.out.println("Ошибка: Задача пересекается с другой задачей по времени.");
            } else {
                taskManager.updateTask(task);
                System.out.println("Задача обновлена.");
            }
        } else {
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                System.out.println("Введите новое название эпика:");
                String title = scanner.nextLine();
                System.out.println("Введите новое описание эпика:");
                String description = scanner.nextLine();

                epic.setTitle(title);
                epic.setDescription(description);
                taskManager.updateEpic(epic);
                System.out.println("Эпик обновлён.");
            } else {
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask != null) {
                    System.out.println("Введите новое название подзадачи:");
                    String title = scanner.nextLine();
                    System.out.println("Введите новое описание подзадачи:");
                    String description = scanner.nextLine();
                    System.out.println("Введите новый статус подзадачи (NEW, IN_PROGRESS, DONE):");
                    Status status = Status.valueOf(scanner.nextLine().toUpperCase());

                    System.out.println("Введите новую продолжительность подзадачи в минутах (0, если не задана):");
                    long durationInput = Long.parseLong(scanner.nextLine());
                    Duration duration = durationInput > 0 ? Duration.ofMinutes(durationInput) : null;

                    System.out.println("Введите новое время начала подзадачи в формате YYYY-MM-DDTHH:MM (оставьте пустым, если не задана):");
                    String startTimeInput = scanner.nextLine();
                    LocalDateTime startTime = startTimeInput.isEmpty() ? null : LocalDateTime.parse(startTimeInput);

                    System.out.println("Введите новый приоритет подзадачи (целое число, 1 - низкий, 10 - высокий):");
                    int priority = Integer.parseInt(scanner.nextLine());

                    subtask.setTitle(title);
                    subtask.setDescription(description);
                    subtask.setStatus(status);
                    subtask.setDuration(duration);
                    subtask.setStartTime(startTime);
                    subtask.setPriority(priority);

                    if (taskManager.isTimeOverlap(subtask)) {
                        System.out.println("Ошибка: Подзадача пересекается с другой задачей по времени.");
                    } else {
                        taskManager.updateSubtask(subtask);
                        System.out.println("Подзадача обновлена.");
                    }
                } else {
                    System.out.println("Задача с указанным ID не найдена.");
                }
            }
        }
    }

    private static void deleteTaskById() {
        System.out.println("Введите ID задачи для удаления:");
        int id = Integer.parseInt(scanner.nextLine());
        if (taskManager.deleteTaskById(id)) {
            System.out.println("Задача удалена.");
        } else {
            System.out.println("Задача с указанным ID не найдена.");
        }
    }

    private static void deleteAllTasks() {
        taskManager.deleteAllTasks();
        System.out.println("Все задачи удалены.");
    }

    private static void getTaskById() {
        System.out.println("Введите ID задачи для получения:");
        int id = Integer.parseInt(scanner.nextLine());
        Task task = taskManager.getTaskById(id);
        if (task != null) {
            System.out.println("Задача: " + task);
        } else {
            System.out.println("Задача с указанным ID не найдена.");
        }
    }

    private static void getAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();
        System.out.println("Все задачи:");
        tasks.forEach(System.out::println);
    }

    private static void getSubtasksForEpic() {
        System.out.println("Введите ID эпика для получения подзадач:");
        int epicId = Integer.parseInt(scanner.nextLine());
        List<Subtask> subtasks = taskManager.getSubtasksForEpic(epicId);
        System.out.println("Подзадачи для эпика с ID " + epicId + ":");
        subtasks.forEach(System.out::println);
    }

    private static void deleteAllTasksAndEpics() {
        taskManager.deleteAllTasks();
        System.out.println("Все задачи и эпики удалены.");
    }

    private static void printHistory() {
        List<Task> history = taskManager.getHistory();
        System.out.println("История просмотров:");
        history.forEach(System.out::println);
    }

    public static void main(String[] args) {
        run();
    }
}
