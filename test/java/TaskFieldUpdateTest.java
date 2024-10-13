import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskFieldUpdateTest {

    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldUpdateTaskFieldsAndReflectChangesInManager() {
        Task task = new Task(22, "Initial Title", "Initial Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(44), 1);
        taskManager.createTask(task);

        task.setTitle("Updated Title");
        task.setDescription("Updated Description");
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTaskById(task.getId());
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
    }

    @Test
    void shouldNotAllowStaleDataAfterFieldUpdate() {
        Epic epic = new Epic(1, "Epic Title", "Epic Description");
        taskManager.createEpic(epic);

        epic.setTitle("Updated Epic Title");
        taskManager.updateEpic(epic);

        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertEquals("Updated Epic Title", updatedEpic.getTitle());
    }
}
