import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;
import static org.junit.jupiter.api.Assertions.*;

public class TaskImmutabilityTest {
    @Test
    public void testTaskImmutability() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task(1, "Task", "Description",Status.DONE);
        manager.createTask(task);

        Task originalTask = manager.getTaskById(1);
        Task modifiedTask = new Task(1, "Modified Task", "New Description",Status.DONE);
        manager.createTask(modifiedTask);

        Task taskFromManager = manager.getTaskById(1);
        assertEquals(originalTask, taskFromManager, "Original task should remain unchanged.");
    }
}
