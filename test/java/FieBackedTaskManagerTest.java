import org.yapr.sprint4.task.kanban.FileBackedTaskManager;
import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FieBackedTaskManagerTest {

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("empty_tasks", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        // Сохраняем пустой менеджер задач
        manager.deleteAllTasks();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что после загрузки список задач пуст
        assertTrue(loadedManager.getAllTasks().isEmpty(), "Список задач должен быть пустым");
    }

    @Test
    void shouldSaveAndLoadMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("multiple_tasks", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        // Создаём задачи, эпики и подзадачи
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1234);
        manager.createTask(task1);

        Epic epic1 = new Epic(0, "Epic 1", "Description Epic 1");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask(0, "Subtask 1", "Description Subtask 1", Status.NEW, epic1.getId(),Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(40), 213);
        manager.createSubtask(subtask1);

        // Сохраняем состояние
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что все задачи восстановлены
        assertEquals(3, loadedManager.getAllTasks().size(), "Все задачи должны быть восстановлены");
        assertEquals(task1, loadedManager.getTaskById(task1.getId()), "Задача должна быть восстановлена");
        Epic epic2 =loadedManager.getEpicById(epic1.getId());
        assertEquals(epic1, epic2, "Эпик должен быть восстановлен");
        Subtask subtask2 = loadedManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, subtask2, "Подзадача должна быть восстановлена");
    }

    @Test
    void shouldSaveAndLoadSingleTask() throws IOException {
        File tempFile = File.createTempFile("single_task", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        // Создаём одну задачу
        Task task1 = new Task(0, "Task 1", "Description 1", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        manager.createTask(task1);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что задача восстановлена
        assertEquals(1, loadedManager.getAllTasks().size(), "Одна задача должна быть восстановлена");
        assertEquals(task1, loadedManager.getTaskById(task1.getId()), "Задача должна быть восстановлена");
    }
}
