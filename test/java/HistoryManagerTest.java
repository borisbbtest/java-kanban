import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.model.*;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.kanban.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final TaskManager taskManager = Managers.getDefault();
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = Managers.getDefaultHistoryManager();
    }


    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        taskManager.deleteTaskById(task.getId());

        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void shouldNotAllowDuplicateTasksInHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task.getId());  // Второй запрос не должен дублировать задачу в истории.

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void shouldTrackHistoryInCorrectOrder() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS,Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(100), 1);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }


    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testAddTask() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void testDuplicateTask() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void testRemoveFromHistory() {
        Task task1 = new Task(1, "Test Task 1", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = new Task(2, "Test Task 2", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
    }

    @Test
    void testRemoveFromHistoryEdgeCases() {
        Task task1 = new Task(1, "Test Task 1", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = new Task(2, "Test Task 2", "Description", Status.NEW,Duration.ofMinutes(30), LocalDateTime.now(), 1);
        historyManager.add(task1);
        historyManager.add(task2);

        // Удаление начала
        historyManager.remove(task1.getId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));

        // Удаление конца
        historyManager.remove(task2.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
