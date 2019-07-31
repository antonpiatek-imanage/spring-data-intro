package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.repositories.ModuleRepository;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class PersonServices {

    @Autowired
    PersonRepository personRepository;


    @Autowired
    ModuleServices moduleServices;


    public Person findByName(String name){return personRepository.findByName(name);}

    public Set<Module> findAllModules(String name)
    {
        Set<Module> list = personRepository.findAllModulesByName(name);
        return personRepository.findAllModulesByName(name);
    }

    public Person addPerson(String name, String phoneNum, Set<String> moduleNames){
        Person person = new Person(name,phoneNum);
        person = personRepository.save(person);
        Module md;
        for(String str:moduleNames){
             md = moduleServices.findByName(str);
             person.addModule(md);
        }
        personRepository.save(person);

        //this is done (instead of just returning person) because the unique id is only generated when the person is stored in database.
        return personRepository.findByName(person.name);
    }
}
