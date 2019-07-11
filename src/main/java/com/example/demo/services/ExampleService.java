package com.example.demo.services;

import com.example.demo.entities.Entity;
import com.example.demo.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExampleService {

    @Autowired EntityRepository entityRepository;

    public Entity findById(Long id) {
        return entityRepository.findById(id).orElse(null);
    }
}
