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


    def "Edit a module's name"(){
        given:
        def moduleName = "Mod1"
        def e1 = moduleRepository.save(new Module(moduleName))

        when:
        def result = moduleController.editModuleName(moduleName, "Mod2")

        then:
        result.moduleID == e1.moduleID
    }



//    def "Example queries"(){
//        given: "two entities"
//            def e1 = entityRepository.save([name: "test1"] as Entity)
//            def e2 = entityRepository.save([name: "test2"] as Entity)
//        expect: "lookup by name to return values"
//            entityRepository.findByName("test1")*.id == [e1.id] //el is the varaible name that has name = "test1"
//            entityRepository.findByName(e2.name)*.id == [e2.id]
//
//        and: "wildcard search"
//            entityRepository.findByNameLike("test%")*.id == [e1.id,e2.id] //The set return will equal the set [el.id,e2,id]
//        and: "ordered search"
//            entityRepository.findByNameLikeOrderByIdAsc ("test%")*.id        == [e1.id,e2.id]
//            entityRepository.findByNameLikeOrderByIdDesc("test%")*.id        == [e2.id,e1.id]
//            entityRepository.findByNameLikeOrderByIdDesc("test%")*.id as Set == [e1.id,e2.id] as Set
//        and: "first only"
//            entityRepository.findFirstByNameLikeOrderByIdAsc("test%").id == e1.id
//    }
//
//    def "Example delete"(){
//        given: "two entities"
//            def e1 = entityRepository.save([name: "deleteMe"] as Entity)
//            def e2 = entityRepository.save([name: "deleteMe"] as Entity)
//        expect: "Both to exit"
//            entityRepository.findByNameLike("deleteMe%")*.id == [e1.id,e2.id]
//        when: "delete called for e1"
//            entityRepository.delete(e1)
//        then: "expect it to be gone"
//            entityRepository.findByNameLike("deleteMe%")*.id == [e2.id]
//            entityRepository.findById(e1.id) == Optional.empty()
//            ! entityRepository.existsById(e1.id)
//        when: "delete called for r2"
//            entityRepository.deleteById(e2.id)
//        then: "expect it to be gone"
//            entityRepository.findByNameLike("deleteMe%")*.id == []
//    }
//
//
//    def "Example childEntities"(){
//        given: "An entity with two children"
//            def e1 = [name: "deleteMe"] as Entity
//            e1.children=[ [parent:e1, name:"child 1"] as ChildEntity, [parent:e1, name:"child 2"] as ChildEntity]
//            e1 = entityRepository.save(e1)
//        expect: "Both to exit"
//            entityRepository.findById(e1.id).value.children*.name.sort() == ["child 1", "child 2"]
//        when: "Searching with a query"
//            def results = entityRepository.findByChildName("child 1")
//        then: "expect parent and children returned"
//            results*.id == [e1.id]
//        and: "same for a native query"
//            entityRepository.findByChildNameNative("child 1")*.id == [e1.id]
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