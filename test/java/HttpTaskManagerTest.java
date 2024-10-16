import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yapr.sprint4.task.kanban.Managers;
import org.yapr.sprint4.task.kanban.TaskManager;
import org.yapr.sprint4.task.model.Epic;
import org.yapr.sprint4.task.model.Subtask;
import org.yapr.sprint4.task.model.Task;
import org.yapr.sprint4.task.model.Status;
import org.yapr.sprint4.task.service.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {

    private final TaskManager manager;
    private final HttpTaskServer taskServer;
    private Gson gson;

    public HttpTaskManagerTest() throws IOException {
        manager = Managers.getDefault();
        taskServer = new HttpTaskServer(manager);
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        taskServer.start();
        gson = taskServer.getGson();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = createTask();
        HttpResponse<String> response = sendPostRequest("http://localhost:8080/tasks", gson.toJson(task));
        assertEquals(201, response.statusCode(), "Задача должна быть успешно создана.");

        Task foundTask = manager.getTaskById(task.getId());
        assertNotNull(foundTask, "Задача не должна быть null после добавления.");
        assertEquals(task.getTitle(), foundTask.getTitle(), "Название задачи должно совпадать.");
        assertEquals(task.getStatus(), foundTask.getStatus(), "Статус задачи должен совпадать.");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = createEpic();
        HttpResponse<String> response = sendPostRequest("http://localhost:8080/epics", gson.toJson(epic));
        assertEquals(201, response.statusCode(), "Эпик должен быть успешно создан.");
        Epic epic_res = gson.fromJson(response.body(), Epic.class);

        Epic foundEpic = manager.getEpicById(epic_res.getId());
        assertNotNull(foundEpic, "Эпик не должен быть null после добавления.");
        assertEquals(epic.getTitle(), foundEpic.getTitle(), "Название эпика должно совпадать.");
        assertEquals(0, foundEpic.getSubtasks().size(), "Эпик не должен содержать подзадач на момент создания.");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = createEpic();
        sendPostRequest("http://localhost:8080/epics", gson.toJson(epic)); // Добавляем эпик, прежде чем добавлять подзадачу.

        Subtask subtask = createSubtask();
        HttpResponse<String> response = sendPostRequest("http://localhost:8080/subtasks", gson.toJson(subtask));
        assertEquals(201, response.statusCode(), "Подзадача должна быть успешно создана.");
        Subtask subtask_res = gson.fromJson(response.body(), Subtask.class);

        Subtask foundSubtask = manager.getSubtaskById(subtask_res.getId());
        assertNotNull(foundSubtask, "Подзадача не должна быть null после добавления.");
        assertEquals(subtask.getTitle(), foundSubtask.getTitle(), "Название подзадачи должно совпадать.");
        assertEquals(subtask.getEpicId(), foundSubtask.getEpicId(), "Подзадача должна быть связана с правильным эпиком.");
    }

    private Task createTask() {
        return new Task(1, "Task", "Description", Status.DONE, Duration.ofMinutes(30), LocalDateTime.now());
    }

    private Epic createEpic() {
        return new Epic(2, "Epic", "Description");
    }

    private Subtask createSubtask() {
        return new Subtask(3, "Subtask", "Description", Status.DONE, 2, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(90));
    }

    private HttpResponse<String> sendPostRequest(String url, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
