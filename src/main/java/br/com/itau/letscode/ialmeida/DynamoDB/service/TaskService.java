package br.com.itau.letscode.ialmeida.DynamoDB.service;

import br.com.itau.letscode.ialmeida.DynamoDB.exception.TaskNotFoundException;
import br.com.itau.letscode.ialmeida.DynamoDB.model.Task;
import br.com.itau.letscode.ialmeida.DynamoDB.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
        return tasks;
    }

    public Task findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id = " + id + " not found."));
    }

    public Task insert(Task task) {
        return taskRepository.save(task);
    }

    public Task update(UUID id, Task task) {
        Task entity = this.findById(id);
        this.updateData(entity, task);
        return taskRepository.save(entity);
    }

    private void updateData(Task task, Task updatedTask) {
        if (Optional.ofNullable(updatedTask.getTitle()).isPresent()) {
            task.setTitle(updatedTask.getTitle());
        }

        if (Optional.ofNullable(updatedTask.getDetail()).isPresent()) {
            task.setDetail(updatedTask.getDetail());
        }

        if (Optional.ofNullable(updatedTask.getPriority()).isPresent()) {
            task.setPriority(updatedTask.getPriority());
        }

        if (Optional.ofNullable(updatedTask.getCreateDate()).isPresent()) {
            task.setCreateDate(updatedTask.getCreateDate());
        }

        if (Optional.ofNullable(updatedTask.getExpectDate()).isPresent()) {
            task.setExpectDate(updatedTask.getExpectDate());
        }

        if (Optional.ofNullable(updatedTask.getEndDate()).isPresent()) {
            task.setEndDate(updatedTask.getEndDate());
        }
    }

    public void deleteById(UUID id) {
        taskRepository.deleteById(this.findById(id).getId());
    }

}
