import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.model.*;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        taskManager.deleteTaskById(task.getId());

        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void shouldNotAllowDuplicateTasksInHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task.getId());  // Второй запрос не должен дублировать задачу в истории.

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void shouldTrackHistoryInCorrectOrder() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }
}
