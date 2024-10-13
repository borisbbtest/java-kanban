import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.InMemoryTaskManager;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskIdConflictTest {
    @Test
    public void testIdConflict() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task taskWithGeneratedId = new Task(1, "Задача", "Описание", Status.DONE, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        manager.createTask(taskWithGeneratedId);

        Task taskWithSpecificId = new Task(1, "Измененная задача", "Новое описание", Status.DONE, Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(45), 1);
        manager.createTask(taskWithSpecificId);

        assertEquals(2, manager.getAllTasks().size(), "Tasks with generated and specific IDs should not conflict.");
    }
}
