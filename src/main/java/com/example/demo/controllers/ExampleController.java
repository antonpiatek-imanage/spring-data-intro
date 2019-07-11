package com.example.demo.controllers;

import com.example.demo.entities.Entity;
import com.example.demo.services.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class ExampleController {

    @Autowired ExampleService exampleService;

    public Entity findById(@RequestParam("id") Long id){
        return exampleService.findById(id);
    }
}
