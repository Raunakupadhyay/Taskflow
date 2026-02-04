package com.taskflow.demo.controller;
import com.taskflow.demo.entity.Task;
import com.taskflow.demo.entity.User;
import com.taskflow.demo.repository.TaskRepository;
import com.taskflow.demo.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // constructor injection
    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // 🔗 CREATE TASK FOR A SPECIFIC USER
    @PostMapping("/user/{userId}")
    public Task createTaskForUser(
            @PathVariable Long userId,
            @RequestBody Task task) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(user);
        return taskRepository.save(task);
    }

    // 🔹 GET ALL TASKS
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}


