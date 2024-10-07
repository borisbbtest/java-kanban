package org.yapr.sprint4.task.kanban;

import org.yapr.sprint4.task.model.Task;

class Node {
    Task task; // Храним задачу
    Node prev; // Ссылка на предыдущий узел
    Node next; // Ссылка на следующий узел

    public Node(Task task) {
        this.task = task;
    }
}