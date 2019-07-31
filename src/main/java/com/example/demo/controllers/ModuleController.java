package com.example.demo.controllers;

import com.example.demo.entities.Module;

import com.example.demo.services.ModuleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/module")
public class ModuleController {

    @Autowired
    ModuleServices moduleServices;


    @GetMapping(path = "/findByName", produces = "application/json")
    public Module findByName(@RequestParam("name") String name){return moduleServices.findByName(name);}
    @GetMapping(path = "/findByTeacherName", produces = "application/json")
    public Module findByTeacherName(@RequestParam("teacher_name") String name){return moduleServices.findByTeacherName(name);}
    @PostMapping(path = "/addModule")
    public Module addModule(@RequestParam("name") String name, @RequestParam("teacher_name") String teacherName){return moduleServices.addModule(name,teacherName);}


}
