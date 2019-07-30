package com.example.demo.controllers;

import com.example.demo.entities.Module;

import com.example.demo.services.ModuleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("/api/module")
@RequestMapping(value = "/api/module")
public class ModuleController {

    @Autowired
    ModuleServices moduleServices;


    public Module findByName(String name){return moduleServices.findByName(name);}

    public Module findByTeacherName(String name){return moduleServices.findByTeacherName(name);}

    public Module addModule(String name, String teacherName){return moduleServices.addModule(name,teacherName);}


}
