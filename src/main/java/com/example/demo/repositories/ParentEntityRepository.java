package com.example.demo.repositories;

import com.example.demo.entities.ParentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface ParentEntityRepository extends CrudRepository<ParentEntity, Long> {
}
