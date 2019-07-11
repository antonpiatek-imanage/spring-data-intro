package com.example.demo.services;

import com.example.demo.entities.ParentEntity;
import com.example.demo.repositories.ParentEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    @Autowired ParentEntityRepository parentEntityRepository;

    public ParentEntity findById(Long id) {
        return parentEntityRepository.findById(id).orElse(null);
    }
}
