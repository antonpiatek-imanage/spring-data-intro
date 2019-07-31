package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServices {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    PersonServices personServices;

    public Module findByName(String name){return moduleRepository.findByName(name);}

    public Module findByTeacherName(String name){return moduleRepository.findByName(name);}

    public Module addModule(String name, String teacherName){
        Module module = new Module(name);
        module.setTeacher(personServices.findByName(teacherName));

        moduleRepository.save(module);
        return moduleRepository.findByName(name);
    }

}
