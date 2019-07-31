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

    // Add the required API method/calls to use the find module by teacher name you have created

    @PostMapping(path = "/addModule")
    public Module addModule(@RequestParam("name") String name, @RequestParam("teacher_name") String teacherName){return moduleServices.addModule(name,teacherName);}
    @PutMapping(path="/editModuleName")
    public void editModuleName(@RequestParam("currName") String curr_name, @RequestParam("newName") String new_name){ moduleServices.editModuleName(curr_name, new_name);}

}
