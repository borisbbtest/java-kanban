import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
    public void testAddAndFindTasksById() {
        // Получаем менеджер задач
        TaskManager manager = Managers.getDefault();

        // Создаем и добавляем новую задачу
        Task task = new Task(1, "Task", "Description", Status.DONE, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        manager.createTask(task);

        // Проверяем, что задача успешно добавлена и найдена по ID
        Task foundTask = manager.getTaskById(1);
        assertEquals(task, foundTask, "Задача должна быть найдена по ID.");
        assertNotNull(foundTask, "Задача не должна быть null после добавления.");
        assertEquals("Task", foundTask.getTitle(), "Название задачи должно совпадать.");
        assertEquals(Status.DONE, foundTask.getStatus(), "Статус задачи должен совпадать.");

        // Создаем и добавляем новый эпик
        Epic epic = new Epic(2, "Epic", "Description");
        manager.createEpic(epic);

        // Проверяем, что эпик успешно добавлен и найден по ID
        Epic foundEpic = manager.getEpicById(2);
        assertEquals(epic, foundEpic, "Эпик должен быть найден по ID.");
        assertNotNull(foundEpic, "Эпик не должен быть null после добавления.");
        assertEquals("Epic", foundEpic.getTitle(), "Название эпика должно совпадать.");
        assertEquals(0, foundEpic.getSubtasks().size(), "Эпик не должен содержать подзадач на момент создания.");

        // Создаем и добавляем новую подзадачу
        Subtask subtask = new Subtask(3, "Subtask", "Description", Status.DONE, 2, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(90), 1);
        manager.createSubtask(subtask);

        // Проверяем, что подзадача успешно добавлена и найдена по ID
        Subtask foundSubtask = manager.getSubtaskById(3);
        assertEquals(subtask, foundSubtask, "Подзадача должна быть найдена по ID.");
        assertNotNull(foundSubtask, "Подзадача не должна быть null после добавления.");
        assertEquals("Subtask", foundSubtask.getTitle(), "Название подзадачи должно совпадать.");
        assertEquals(2, foundSubtask.getEpicId(), "Подзадача должна быть связана с правильным эпиком.");
    }
}
