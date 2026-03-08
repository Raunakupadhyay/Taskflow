package com.taskflow.demo.repository;

import com.taskflow.demo.entity.Task;
import com.taskflow.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 🔥 Get all tasks of specific user
    List<Task> findByUser(User user);

    // 🔥 Find task by id AND user (for update/delete security)
    Optional<Task> findByIdAndUser(Long id, User user);

}
