import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    @Test
    public void testAddAndFindTasksById() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task(1, "Task", "Description",Status.DONE);
        manager.createTask(task);

        Task foundTask = manager.getTaskById(1);
        assertEquals(task, foundTask, "Should find the task by ID.");

        Epic epic = new Epic(2, "Epic", "Description");
        manager.createEpic(epic);

        Epic foundEpic = manager.getEpicById(2);
        assertEquals(epic, foundEpic, "Should find the epic by ID.");

        Subtask subtask = new Subtask(3, "Subtask", "Description", Status.DONE,1);
        manager.createSubtask(subtask);

        Subtask foundSubtask = manager.getSubtaskById(3);
        assertEquals(subtask, foundSubtask, "Should find the subtask by ID.");
    }
}
