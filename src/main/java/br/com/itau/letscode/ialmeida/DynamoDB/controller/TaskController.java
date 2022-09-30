package br.com.itau.letscode.ialmeida.DynamoDB.controller;

import br.com.itau.letscode.ialmeida.DynamoDB.model.Task;
import br.com.itau.letscode.ialmeida.DynamoDB.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> findAll() {
        return ResponseEntity.ok().body(taskService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Task> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(taskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Task> insert(@RequestBody Task task) {
        task = taskService.insert(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Task> update(@PathVariable UUID id, @RequestBody Task task) {
        return ResponseEntity.ok().body(taskService.update(id, task));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
