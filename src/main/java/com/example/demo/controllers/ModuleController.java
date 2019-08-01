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


    //region GET
    @GetMapping(path = "/findByName", produces = "application/json")
    public Module findByName(@RequestParam("name") String name) { return moduleServices.findByName(name); }

    @GetMapping(path = "/findByTeacherName", produces = "application/json")
    public Module findByTeacherName(@RequestParam("teacher_name") String name){return moduleServices.findByTeacherName(name);}

    @GetMapping(path = "/findById", produces = "application/json")
    public Module findById(@RequestParam("id") long id){return moduleServices.findById(id);}
    //endregion

    //region POST
    @PostMapping(path = "/createModuleWithTeacher")
    public Module addModule(@RequestParam("name") String name, @RequestParam("teacherID") long teacherID){return moduleServices.createModuleWithTeacher(name,teacherID);}

    @PostMapping(path = "/createModuleWithoutTeacher")
    public Module addModule(@RequestParam("name") String name) { return moduleServices.createModuleWithoutTeacher(name);}

    //endregion

    //region PUT
    @PutMapping(path="/editModuleName", produces = "application/json")
    public Module editModuleName(@RequestParam("currName") String curr_name, @RequestParam("newName") String new_name){return moduleServices.editModuleName(curr_name, new_name);}

    @PutMapping(path = "/editModuleTeacher", produces = "application/json")
    public Module editModuleTeacher(@RequestParam("moduleID")long moduleID, @RequestParam("teacherID") long personID){return moduleServices.editModuleTeacher(moduleID,personID);}
    //endregion

    //TODO try to make delete
}
