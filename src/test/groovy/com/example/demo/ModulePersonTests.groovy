package com.example.demo

import com.example.demo.controllers.ExampleController
import com.example.demo.controllers.ModuleController
import com.example.demo.controllers.PersonController
import com.example.demo.entities.Entity
import com.example.demo.entities.Module
import com.example.demo.entities.Person
import com.example.demo.repositories.EntityRepository
import com.example.demo.repositories.ModuleRepository
import com.example.demo.repositories.PersonRepository
import com.example.demo.services.ModuleServices
import com.sun.org.apache.xpath.internal.operations.Mod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(classes = DemoApplication.class)
@ContextConfiguration
class ModulePersonTests extends Specification{

    @Autowired PersonController personController
    @Autowired PersonRepository personRepository

    @Autowired ModuleController moduleController
    @Autowired ModuleRepository moduleRepository

    def "Create a Person"(){
        given: "an entity"
            def e1 = personRepository.save(new Person("TesterName","TesterPhoneNum")) //.save() is crudRepositroy, which is a parent of the ExampleController.

        when: "controller is called with an name"
            def result = personController.findByName(e1.name) //Creates another instance of entity, setting it equal to el
        then: "result is an entity with an id"
            result.personID == e1.personID//Will be truex
        when: "findall is called on the repository"
            def resultList = personRepository.findAll() //Shouldnt return result, since result isnt saved in the repository
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.personID in resultList*.personID //Will be true since result.id = el.id, el.id is in the repository
            for(i in result){
                println("-----------------------------------------\nTesting\n"+i.personID)
            }
    }

    def "Create a Module"(){
        given: "an entity"
        def e1 = moduleRepository.save(new Module("TesterModuleName")) //.save() is crudRepositroy, which is a parent of the ExampleController.

        when: "controller is called with an name"
        def result = moduleController.findByName(e1.name) //Creates another instance of entity, setting it equal to el
        then: "result is an entity with an id"
        result.moduleID == e1.moduleID//Will be truex
        when: "findall is called on the repository"
        def resultList = moduleRepository.findAll() //Shouldnt return result, since result isnt saved in the repository
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        result.moduleID in resultList*.moduleID //Will be true since result.id = el.id, el.id is in the repository
        for(i in result){
            println("-----------------------------------------\nTesting\n"+i.moduleID)
        }
    }

    def "Add a Module"(){
        given: " an entity"
        def el = moduleController.addModule("ModuleName","TeacherName")
        when :"controller is called with that module name"
        def result = moduleController.findByName(el.name)
        then:"result list shouldn't be empty"
        result.moduleID == el.moduleID
    }

    def "Create a Student-Module link"(){
        given: "A module exists"
        def moduleName = "TesterModuleName2"
        def e1 = moduleRepository.save(new Module(moduleName)) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "A Person is created"
        def result = personController.addPerson("StudentName","StudentPhoneNumber",[moduleName] as Set<String>)
        then: "controller is called to fetch modules that person is associated with"
        def modules  = personController.findAllModules(result.name) //Creates another instance of entity, setting it equal to el
        then: "result is a list of modules"
        !modules.isEmpty()
        for(i in modules){
            println("-----------------------------------------\nTesting\n"+i.name)
        }
    }

//    def "Create a Teacher"(){
//        given :"A Module"
//        def module = moduleRepository.save(new Module("TesterModuleName")) //.save() is crudRepositroy, which is a parent of the ExampleController.
//        when: "We add that module ot a student"
//        def e1 = personController.addPerson("TesterName","TesterPhoneNum",["TesterModuleName"] as Set<String>) //.save() is crudRepositroy, which is a parent of the ExampleController.
//        then :"it shouldnt crash"
//    }

    def "Edit a person name"(){
        given: "A Person"
        def el = personController.addPerson("TesterName2","TesterPhoneNum2",["TesterModuleName"] as Set<String>) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "We try to edit the name"
        def editedPerson = personController.editPersonName(el.personID,"I hope this name change works")
        then : "We fecth the person again from the database"
        def newerDatabaseInstanceOfPerson = personController.findByName("I hope this name change works")
    }

    def "Edit a person phone number"(){
        given: "A Person"
        def el = personController.addPerson("TesterName3","TesterPhoneNum3",["TesterModuleName"] as Set<String>) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "We try to edit the phoneNum"
        def editedPerson = personController.editPersonPhoneNum(el.personID,"999")
        then : "We fecth the person again from the database"
        def newerDatabaseInstanceOfPerson = personController.findByName("999")
    }

}