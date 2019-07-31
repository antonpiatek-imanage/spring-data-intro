package com.example.demo.controllers;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/person")
public class PersonController {


    @Autowired
    PersonServices personServices;


    // Working
    @GetMapping(path = "/findByName",  produces = "application/json")
    public Person findByName(@RequestParam("name") String name){return personServices.findByName(name);}

    @GetMapping(path = "/findAllModules", produces = "application/json")
    public Set<Module> findAllModules(@RequestParam("name") String name){return personServices.findAllModules(name);}

    @PostMapping(path="/addPerson", consumes = "application/json", produces = "application/json")
    public Person addPerson(@RequestParam("name") String name, @RequestParam("phonenum") String phoneNum, @RequestParam("modules") Set<String> moduleNames){
        return personServices.addPerson(name,phoneNum,moduleNames);}

    public Person editPersonName(long id, String name){return personServices.editPersonName(id,name);}

    //Left undone for a reason, try to add API implementation yourself
    public Person editPersonPhoneNum(long id, String phoneNum){return personServices.editPersonPhoneNum(id,phoneNum);}
}
