package org.yapr.sprint4.task.kanban;
import org.yapr.sprint4.task.model.*;

import java.util.List;
import java.util.Scanner;

public class TaskManagerApp {
    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void Run() {
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
        System.out.println("0. Выйти");
    }

    private static void createTask() {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        System.out.println("Введите статус задачи (NEW, IN_PROGRESS, DONE):");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());

        SimpleTask task = new SimpleTask(0, title, description, status);
        taskManager.createTask(task);
        System.out.println("Задача создана с ID: " + task.getId());
    }

    private static void createEpic() {
        System.out.println("Введите название эпика:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();

        Epic epic = new Epic(0, title, description);
        taskManager.createTask(epic);
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

        Subtask subtask = new Subtask(0, title, description, status, epicId);
        taskManager.createTask(subtask);
        System.out.println("Подзадача создана с ID: " + subtask.getId());
    }

    private static void updateTask() {
        System.out.println("Введите ID задачи для обновления:");
        int id = Integer.parseInt(scanner.nextLine());
        Task task = taskManager.getTaskById(id);

        if (task == null) {
            System.out.println("Задача с таким ID не найдена.");
            return;
        }

        System.out.println("Введите новое название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите новое описание задачи:");
        String description = scanner.nextLine();
        System.out.println("Введите новый статус задачи (NEW, IN_PROGRESS, DONE):");
        Status status = Status.valueOf(scanner.nextLine().toUpperCase());

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        taskManager.updateTask(task);
        System.out.println("Задача обновлена.");
    }

    private static void deleteTaskById() {
        System.out.println("Введите ID задачи для удаления:");
        int id = Integer.parseInt(scanner.nextLine());
        taskManager.deleteTaskById(id);
        System.out.println("Задача удалена.");
    }

    private static void deleteAllTasks() {
        taskManager.deleteAllTasks();
        System.out.println("Все задачи удалены.");
    }

    private static void getTaskById() {
        System.out.println("Введите ID задачи:");
        int id = Integer.parseInt(scanner.nextLine());
        Task task = taskManager.getTaskById(id);

        if (task != null) {
            System.out.println(task);
        } else {
            System.out.println("Задача с таким ID не найдена.");
        }
    }

    private static void getAllTasks() {
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Задач нет.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void getSubtasksForEpic() {
        System.out.println("Введите ID эпика:");
        int epicId = Integer.parseInt(scanner.nextLine());
        List<Subtask> subtasks = taskManager.getSubtasksForEpic(epicId);

        if (subtasks.isEmpty()) {
            System.out.println("Подзадач для этого эпика нет.");
        } else {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask);
            }
        }
    }
}
