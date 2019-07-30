package com.example.demo.controllers;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController("/api/person")
@RequestMapping(value = "/api/module")
public class PersonController {


    @Autowired
    PersonServices personServices;


    public Person findByName(String name){return personServices.findByName(name);}


    public Set<Module> findAllModules(String name){return personServices.findAllModules(name);}

    public Person addPerson(String name, String phoneNum, Set<String> moduleNames){return personServices.addPerson(name,phoneNum,moduleNames);}

}
