package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;

@Service
public class ModuleServices {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    PersonServices personServices;
    @Autowired
    EntityManager em;

    public Module findByName(String name){return moduleRepository.findByName(name);}

    /*
    Try this one yourself
    public Module findByTeacherName(String name){

    }
    */
    public Module addModule(String name, String teacherName){
        Module module = new Module(name);
        module.setTeacher(personServices.findByName(teacherName));

        moduleRepository.save(module);

        return moduleRepository.findByName(name);
    }

    public void editModuleName(String curr_name, String new_name)
    {
        moduleRepository.editModuleName(curr_name, new_name);
    }
}
