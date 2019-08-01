package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.repositories.PersonRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityManager;
import javax.xml.ws.http.HTTPException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonServices {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EntityManager em;

    @Autowired
    ModuleServices moduleServices;


    //region GET http associated methods
    public Person findByName(String name){
        Person result = personRepository.findByName(name).orElse(null);
        return checkNullPerson(result, name);
    }


    public Set<Module> findAllModules(String name){
        Set<Module> result = personRepository.findAllModulesByName(name);
        if (result.size() == 0){
            throw new ResourceNotFoundException("Person with name: '" + name + "' isn't enrolled in any modules");
        }
        return result;
    }

    public Person findByID(long id){
        Person person = personRepository.findById(id).orElse(null);
        return checkNullPerson(person,"Person with ID: '" + id + "' isn't on the system");
    }

    private Person checkNullPerson(Person person, String errorMessage) {
        if (person == null) {
            throw new ResourceNotFoundException("Person with name: '" + errorMessage + "' not found");
        }
        return person;
    }

    //endregion

    //region POST  http associated methods
    //These are primarily used for adding new data
    public Person addPerson(String name, String phoneNum, Set<String> moduleNames){

        //Save a new person with person.name = name, person.phoneNum = phoneNum. Then it passes it to 'addModules' method to 'pick apart' and add all the modules the person is associated with
        Person result = addModules(moduleNames, personRepository.save(new Person(name,phoneNum)));

        personRepository.save(result);

        //Person already has been saved to database, so had unique id. Modules are also added. Therefore can just return object, instead of from db
        return result;
    }

    private Person addModules(Set<String> moduleNames, Person person) {
        for(String str:moduleNames){
            Module module = moduleServices.findByName(str);
            person.addModule(module);
        }
        return person;
    }

    public Person addModulesToExistingPerson(long id, Set<String> moduleNames){ return addModules(moduleNames,findByID(id)); }

    //endregion

    //region PUT http associated methods
    //These are primarily used for editing data
    public Person editPersonName(long id,String name){
        findByID(id); //Checks person exists on database
        personRepository.editPersonName(id,name);
        return findByID(id);
    }

    public Person editPersonPhoneNum(long id, String phoneNum){
        findByID(id); //checks person exists on database
        personRepository.editPersonPhoneNum(id,phoneNum);
        return findByID(id);
    }
    //endregion

    //region DELETE

    public Person deletePerson(long id){
        Person result = findByID(id);
        personRepository.delete(result);
        return result;
    }

    public Set<Module> deleteModules(long id,Set<String> moduleNames){
        Set<Module> result = getModulesFromSet(moduleNames);
        Person person = findByID(id);

        checkModulesSetMatchUp(result,findAllModules(person.name));

        for (Module modu:result){
            person.deleteModule(modu);
        }
        return result;
    }

    //endregion

    private Set<Module> getModulesFromSet(Set<String> moduleNames){
        Set<Module> result = new HashSet<>();
        for (String str:moduleNames){
            result.add(moduleServices.findByName(str));
        }
        return result;
    }

    private void checkModulesSetMatchUp(Set<Module> modules, Set<Module> personModules){
        Set<Long> ids = modules.stream().map(Module::getModuleID).collect(Collectors.toSet());
        Set<Long> pids = personModules.stream().map(Module::getModuleID).collect(Collectors.toSet());
        if (!pids.containsAll(ids)){
            throw new ResourceNotFoundException("Student doesn't attend some of these modules");
        }

    }
}
