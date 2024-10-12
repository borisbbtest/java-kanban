import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.InMemoryTaskManager;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    @Test
    public void testGetDefaultReturnsInitializedManager() {
        TaskManager manager = Managers.getDefault();

        assertNotNull(manager, "TaskManager should not be null.");
        assertInstanceOf(InMemoryTaskManager.class, manager, "TaskManager should be an instance of InMemoryTaskManager.");
    }
}
