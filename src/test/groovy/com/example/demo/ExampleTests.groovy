package com.example.demo

import com.example.demo.controllers.ExampleController
import com.example.demo.entities.ChildEntity
import com.example.demo.entities.Entity
import com.example.demo.repositories.EntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.devtools.autoconfigure.RemoteDevToolsAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(classes = DemoApplication.class)
@ContextConfiguration
class ExampleTests extends Specification{

    @Autowired ExampleController exampleController
    @Autowired EntityRepository entityRepository

    def "Example test"(){
        given: "an entity"
            def e1 = entityRepository.save(new Entity()) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = exampleController.findById(e1.id) //Creates another instance of entity, setting it equal to el
        then: "result is an entity with an id"
            result.id == e1.//Will be true
            result.id > 0 //Shoudl be true, idk about validation
        when: "findall is called on the repository"
            def resultList = entityRepository.findAll() //Shouldnt return result, since result isnt saved in the repository
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id //Will be true since result.id = el.id, el.id is in the repository
            for(i in result){
                println("-----------------------------------------\nTesting\n"+i.id)
            }
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
//    }


}
