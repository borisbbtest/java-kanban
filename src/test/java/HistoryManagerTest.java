import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.InMemoryTaskManager;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    @Test
    public void testHistoryPreservesPreviousVersions() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task = new Task(1, "Task", "Description",Status.NEW);
        Task task2 = new Task(2, "Task", "Description",Status.NEW);
        manager.createTask(task);
        manager.createTask(task2);

        manager.getTaskById(1);  // Добавляем в историю
        manager.getTaskById(2);  // Добавляем в историю


        List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "History should contain one task.");
        assertEquals(1, history.get(0).getId(), "History should preserve the original version of the task.");
        assertEquals(2, history.get(1).getId(), "History should preserve the original version of the task.");
    }
}
