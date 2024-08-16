

import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Status;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void testTasksEqualityById() {
        Task task1 = new Task(1, "Task 1", "Description 1",Status.DONE);
        Task task2 = new Task(1, "Task 2", "Description 1", Status.DONE);

        assertEquals(task1, task2, "Tasks with the same ID should be equal.");
    }

    @Test
    void testEpicEqualityById() {
        Epic epic1 = new Epic(1, "Epic 1", "none");
        Epic epic2 = new Epic(1, "Epic 2", "none"); // Same ID, different name and status

        assertEquals(epic1, epic2, "Epics with the same ID should be equal, regardless of status");
    }

    @Test
    void testSubtaskEqualityById() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", "none", Status.NEW,1);
        Subtask subtask2 = new Subtask(1, "Subtask 2", "none", Status.DONE,1); // Same ID, different epic ID and status

        assertEquals(subtask1, subtask2, "Subtasks with the same ID should be equal, regardless of status");
    }


}
