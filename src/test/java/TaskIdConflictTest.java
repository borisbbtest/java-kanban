import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.InMemoryTaskManager;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;

import static org.junit.jupiter.api.Assertions.*;

public class TaskIdConflictTest {
    @Test
    public void testIdConflict() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task taskWithGeneratedId = new Task(1, "Description","teart",Status.DONE);
        manager.createTask(taskWithGeneratedId);

        Task taskWithSpecificId = new Task(2, "Specific Task", "Description",Status.DONE);
        manager.createTask(taskWithSpecificId);

        assertEquals(2, manager.getAllTasks().size(), "Tasks with generated and specific IDs should not conflict.");
    }
}
