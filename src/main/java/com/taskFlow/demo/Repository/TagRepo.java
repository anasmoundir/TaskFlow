package com.taskFlow.demo.Repository;

import com.taskFlow.demo.Model.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TagRepo extends JpaRepository<Tag,Long> {
}
