package com.example.trelloapi.repository;

import com.example.trelloapi.model.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findByBoardId(Long boardId);
}
