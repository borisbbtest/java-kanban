import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Status;
import org.yapr.sprint4.task.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskImmutabilityTest {
    @Test
    public void testTaskImmutability() {
        TaskManager manager = Managers.getDefault();

        // Создаем оригинальную задачу
        Task originalTask = new Task(1, "Задача", "Описание", Status.DONE, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        manager.createTask(originalTask);

        // Получаем оригинальную задачу из менеджера
        Task taskFromManager = manager.getTaskById(1);

        // Создаем измененную задачу с тем же ID
        Task modifiedTask = new Task(1, "Измененная задача", "Новое описание", Status.DONE, Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(45), 1);
        manager.createTask(modifiedTask);

        assertEquals(originalTask.getId(), taskFromManager.getId(), "ID задачи должен остаться неизменным.");
        assertEquals(originalTask.getTitle(), taskFromManager.getTitle(), "Название оригинальной задачи должно остаться неизменным.");
        assertEquals(originalTask.getDescription(), taskFromManager.getDescription(), "Описание оригинальной задачи должно остаться неизменным.");
        assertEquals(originalTask.getStatus(), taskFromManager.getStatus(), "Статус оригинальной задачи должен остаться неизменным.");
        assertEquals(originalTask.getDuration(), taskFromManager.getDuration(), "Продолжительность оригинальной задачи должна остаться неизменной.");
        assertEquals(originalTask.getStartTime(), taskFromManager.getStartTime(), "Время начала оригинальной задачи должно остаться неизменным.");
    }
}
