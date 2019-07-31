package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServices {

    @Autowired
    ModuleRepository moduleRepository;


    public Module findByName(String name){return moduleRepository.findByName(name);}

    public Module findByTeacherName(String name){
        Module m = moduleRepository.findByName(name);
        return m;
    }

    public Module addModule(String name, String teacherName){
        Module module = new Module(name);
        moduleRepository.save(module);

        return moduleRepository.findByName(name);
    }

    public Module editModuleName(String curr_name, String new_name)
    {
        Module m = findByName(curr_name);
        m.name = new_name;
        moduleRepository.save(m);

        return (moduleRepository.findByName(new_name));
    }
}
