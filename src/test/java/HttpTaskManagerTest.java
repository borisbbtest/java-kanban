import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks(); // Удаляем все задачи перед тестами
        manager.deleteAllEpics();  // Удаляем все эпики перед тестами
        manager.deleteAllSubtasks(); // Удаляем все подзадачи перед тестами
        taskServer.start();       // Запускаем сервер
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop(); // Останавливаем сервер после каждого теста
    }

    // Тесты для задач
    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Задача не была создана");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Task", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Get Task", "Testing task retrieval", TaskStatus.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        manager.createTask(task);
        int taskId = task.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Задача не была найдена");

        Task taskFromResponse = gson.fromJson(response.body(), Task.class);
        assertNotNull(taskFromResponse, "Задача не возвращается");
        assertEquals("Get Task", taskFromResponse.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Initial Task", "Initial description", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.createTask(task);
        task.setName("Updated Task");
        task.setDescription("Updated description");
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Задача не была обновлена");

        Task updatedTask = manager.getTaskById(task.getId());
        assertEquals("Updated Task", updatedTask.getName(), "Имя задачи не обновилось");
        assertEquals("Updated description", updatedTask.getDescription(), "Описание задачи не обновилось");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task to delete", "This task will be deleted", TaskStatus.NEW, Duration.ofMinutes(45), LocalDateTime.now());
        manager.createTask(task);
        int taskId = task.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Задача не была удалена");

        assertNull(manager.getTaskById(taskId), "Задача всё ещё существует");
    }

    // Тесты для эпиков
    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic description");
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Эпик не был создан");

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Epic 1", epicsFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Get Epic", "Testing epic retrieval");
        manager.createEpic(epic);
        int epicId = epic.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Эпик не был найден");

        Epic epicFromResponse = gson.fromJson(response.body(), Epic.class);
        assertNotNull(epicFromResponse, "Эпик не возвращается");
        assertEquals("Get Epic", epicFromResponse.getName(), "Некорректное имя эпика");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Initial Epic", "Initial description");
        manager.createEpic(epic);
        epic.setName("Updated Epic");
        epic.setDescription("Updated description");
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Эпик не был обновлён");

        Epic updatedEpic = manager.getEpicById(epic.getId());
        assertEquals("Updated Epic", updatedEpic.getName(), "Имя эпика не обновилось");
        assertEquals("Updated description", updatedEpic.getDescription(), "Описание эпика не обновилось");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to delete", "This epic will be deleted");
        manager.createEpic(epic);
        int epicId = epic.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Эпик не был удалён");

        assertNull(manager.getEpicById(epicId), "Эпик всё ещё существует");
    }

    // Тесты для подзадач
    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic for Subtask", "Epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Testing subtask", TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now(), epic.getId());
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Подзадача не была создана");

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Subtask 1", subtasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic for Subtask", "Epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Get Subtask", "Testing subtask retrieval", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now(), epic.getId());
        manager.createSubtask(subtask);
        int subtaskId = subtask.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Подзадача не была найдена");

        Subtask subtaskFromResponse = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(subtaskFromResponse, "Подзадача не возвращается");
        assertEquals("Get Subtask", subtaskFromResponse.getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic for Update", "Epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Initial Subtask", "Initial description", TaskStatus.NEW, Duration.ofMinutes(20), LocalDateTime.now(), epic.getId());
        manager.createSubtask(subtask);
        subtask.setName("Updated Subtask");
        subtask.setDescription("Updated description");
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Подзадача не была обновлена");

        Subtask updatedSubtask = manager.getSubtaskById(subtask.getId());
        assertEquals("Updated Subtask", updatedSubtask.getName(), "Имя подзадачи не обновилось");
        assertEquals("Updated description", updatedSubtask.getDescription(), "Описание подзадачи не обновилось");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic for Deletion", "Epic description");
        manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask to delete", "This subtask will be deleted", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), epic.getId());
        manager.createSubtask(subtask);
        int subtaskId = subtask.getId();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newHttpClient().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Подзадача не была удалена");

        assertNull(manager.getSubtaskById(subtaskId), "Подзадача всё ещё существует");
    }
}
