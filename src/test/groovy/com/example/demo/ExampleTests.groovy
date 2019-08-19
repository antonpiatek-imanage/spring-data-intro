package com.example.demo

import com.example.demo.controllers.ExampleController
import com.example.demo.entities.ParentEntity
import com.example.demo.repositories.ParentEntityRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@ContextConfiguration
@SpringBootTest(classes = DemoApplication.class)
class ExampleTests extends Specification {

    MockMvc mockMvc

    @Autowired ExampleController exampleController
    @Autowired ParentEntityRepository parentEntityRepository
    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def "Example test"() {
        given: "an entity"
            def e1 = parentEntityRepository.save(new ParentEntity())
        when: "controller is called with an id"
            def result = exampleController.findById(e1.id)
        then: "result is an entity with an id"
            result.id == e1.id
            result.id > 0
        when: "findAll is called on the repository"
            def resultList = parentEntityRepository.findAll()
        then: "some results"
            !resultList.isEmpty()
        and: "including our earlier id"
            result.id in resultList*.id
            for (i in result) {
                println("-----------------------------------------\nTesting\n" + i.id)
            }
    }

    def "Example test - REST"() {
        given: "an entity"
            def e1 = parentEntityRepository.save(new ParentEntity()) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
            def result = mockMvc.perform(get("/api/findById?id=$e1.id")).andReturn().response.contentAsString
            def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
            json.id == e1.id
            json.id > 0
    }
}
