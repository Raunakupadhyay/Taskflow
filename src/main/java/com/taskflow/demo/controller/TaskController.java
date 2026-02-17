package com.taskflow.demo.controller;

import com.taskflow.demo.entity.Task;
import com.taskflow.demo.entity.User;
import com.taskflow.demo.repository.TaskRepository;
import com.taskflow.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // 🔹 Get logged-in user
    private User getLoggedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ GET ALL TASKS (ONLY USER'S TASKS)
    @GetMapping
    public List<Task> getTasks(Authentication authentication) {
        User user = getLoggedUser(authentication);
        return taskRepository.findByUser(user);
    }

    // ✅ CREATE TASK
    @PostMapping
    public Task createTask(@RequestBody Task task,
                           Authentication authentication) {
        User user = getLoggedUser(authentication);
        task.setUser(user);
        return taskRepository.save(task);
    }

    // ✅ UPDATE TASK (ONLY OWNER)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody Task updatedTask,
                           Authentication authentication) {

        User user = getLoggedUser(authentication);

        Task task = taskRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Task not found or not authorized"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());

        return taskRepository.save(task);
    }

    // ✅ DELETE TASK (ONLY OWNER)
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id,
                             Authentication authentication) {

        User user = getLoggedUser(authentication);

        Task task = taskRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Task not found or not authorized"));

        taskRepository.delete(task);

        return "Task deleted successfully";
    }
}
