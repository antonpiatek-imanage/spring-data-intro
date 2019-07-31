package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.repositories.ModuleRepository;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Set;

@Service
public class PersonServices {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EntityManager em;

    @Autowired
    ModuleServices moduleServices;


    //region GET  http associated methods
    //These are primarily used for finding and retrieving data
    public Person findByName(String name){return personRepository.findByName(name);}

    public Set<Module> findAllModules(String name){
        return personRepository.findAllModulesByName(name);}

    //endregion

    //region POST  http associated methods
    //These are primarily used for adding new data
    public Person addPerson(String name, String phoneNum, Set<String> moduleNames){
        Person person = new Person(name,phoneNum);
        person = personRepository.save(person);
        Module md;
        for(String str:moduleNames){
//             md = moduleServices.findByName(str); //TODO shouldnt be needed
//             person.addModule(md);
            person.addModule(moduleServices.findByName(str));
        }

        personRepository.save(person);

        //this is done (instead of just returning person) because the unique id is only generated when the person is stored in database.
        return personRepository.findByName(person.name);
    }

    //endregion

    //region PUT http associated methods
    //These are primarily used for editing data
    public Person editPersonName(long id,String name){
        personRepository.editPersonName(id,name);
        return personRepository.findById(id);
    }

    public Person editPersonPhoneNum(long id, String phoneNum){
        personRepository.editPersonPhoneNum(id,phoneNum);
        return personRepository.findById(id);
    }


    //endregion
}
