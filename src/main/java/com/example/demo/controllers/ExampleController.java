package com.example.demo.controllers;

import com.example.demo.entities.ParentEntity;
import com.example.demo.services.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/api")
@RequestMapping(value = "/api")
public class ExampleController {

    @Autowired ExampleService exampleService;

    @ResponseBody
    @RequestMapping(path = "/findById", method = RequestMethod.GET)
    public ParentEntity findById(@RequestParam("id") Long id){
        return exampleService.findById(id);
    }
}
