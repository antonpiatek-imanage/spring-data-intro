package com.example.demo.services;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import com.example.demo.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ModuleServices {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    PersonServices personServices;

    //region findBy___
    public Module findByName(String name) {
        Module module = moduleRepository.findByName(name).orElse(null);
        return checkNullModule(module, "Module with name: '"+name+"' not found");
    }

    public Module findByTeacherName(String name) {
        Module module = moduleRepository.findByName(name).orElse(null);
        return checkNullModule(module, "No modules taught by: '" + name + "' were not found");
    }

    public Module findById(long id){
        Module module = moduleRepository.findById(id).orElse(null);
        return checkNullModule(module,"No modules with id: '"  + id + "' were found");
    }

    private Module checkNullModule( Module module,String errorMessage) {
        if (module == null) {
            throw new ResourceNotFoundException(errorMessage);
        }
        return module;
    }

    //endregion


    //region creation methods
    //Associated with POST
    public Module createModuleWithTeacher(String name, long teacherID){
        Module result = setTeacher(createModuleWithoutTeacher(name),teacherID);

        return moduleRepository.save(result); //Module already has id because its been saved to the repository
    }

    public Module createModuleWithoutTeacher(String name){
        moduleRepository.save(new Module(name));
        return findByName(name);
    }

    //endregion

    //region editing Methods
    //associated with PUT
    public Module editModuleTeacher(long moduleID, long personID){
        Module module = findById(moduleID);
        return setTeacher(module,personID);
    }

    private Module setTeacher(Module module, long teacherID) {
        Person teacher;
        try {
            teacher = personServices.findByID(teacherID);
        } catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException(ex.getMessage()+"\nModule was Created, but no teacher has been assigned to it");
        }
        module.setTeacher(teacher);
        return module;
    }

    public Module editModuleName(String curr_name, String new_name)
    {
        Module m = findByName(curr_name);
        m.name = new_name;
        moduleRepository.save(m);

        return (moduleRepository.findByName(new_name).orElse(null));
    }

    //endregion

    //TODO TRY AND WRITE THE FUNCTION TO DELETE MODULES

}
