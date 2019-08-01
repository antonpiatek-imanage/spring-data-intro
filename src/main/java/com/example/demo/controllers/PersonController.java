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


    //region GET
    @GetMapping(path = "/findByName",  produces = "application/json")
    public Person findByName(@RequestParam("name") String name){return personServices.findByName(name);}

    @GetMapping(path = "/findAllModules", produces = "application/json")
    public Set<Module> findAllModules(@RequestParam("name") String name){return personServices.findAllModules(name);}

    //endregion


    //region POST
    @PostMapping(path="/addPerson", consumes = "application/json", produces = "application/json")
    public Person addPerson(@RequestParam("name") String name, @RequestParam("phonenum") String phoneNum, @RequestParam("modules") Set<String> moduleNames){ return personServices.addPerson(name,phoneNum,moduleNames);}

    //TODO try this out yourself
    public Person addPersonModules(long id, Set<String> moduleNames){return personServices.addModulesToExistingPerson(id, moduleNames);}
    //endregion


    //region PUT
    @PutMapping(path="/editPersonName", consumes = "application/json", produces = "application/json")
    public Person editPersonName(@RequestParam("id") long id,@RequestParam("name") String name){return personServices.editPersonName(id,name);}

    //TODO try this out yourself
    public Person editPersonPhoneNum(long id, String phoneNum){return personServices.editPersonPhoneNum(id,phoneNum);}
    //endregion

    //region DELETE
    @DeleteMapping(path="/deletePerson", consumes = "application/json", produces="application/json")
    public Person deletePerson(@RequestParam("id") long id){return personServices.deletePerson(id);}

    //TODO try this yourself
    public Set<Module> deleteModules(long id, Set<String> moduleNames){return personServices.deleteModules(id, moduleNames);}

    //endregion


}
