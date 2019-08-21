package com.blocks.rental

import com.blocks.rental.controllers.RentalController
import com.blocks.rental.entities.Car
import com.blocks.rental.entities.Customer
import com.blocks.rental.entities.Location
import com.blocks.rental.repositories.CarRepository
import com.blocks.rental.repositories.CustomerRepository
import com.blocks.rental.repositories.LocationRepository
import com.blocks.rental.services.CarService
import com.blocks.rental.services.CustomerService
import com.blocks.rental.services.LocationService
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ContextConfiguration
@SpringBootTest(classes = CarRentalApplication.class)
class CarRentalTests extends Specification {

    MockMvc mockMvc

    @Autowired RentalController rentalController

    @Autowired LocationService locationService
    @Autowired LocationRepository locationRepository

    @Autowired CustomerService customerService
    @Autowired CustomerRepository customerRepository

    @Autowired CarService carService
    @Autowired CarRepository carRepository

    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }


    ////////////////////////////////////////////////
    ////// Location Info
    ////////////////////////////////////////////////

    def "Test Add Location"() {
        given: "an entity"
        def country = "UAE"
        when: "controller is called with a country"
        def result = rentalController.putLocation(country)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id

    }

    def "Test Add Location REST"() {
        given: "an entity"
        def country = "UAE"
        when: "REST is called with a country"
        def result = mockMvc.perform(post("/location?country=$country")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)

        then: "result is an entity with an id"
        json.id > 0
        def id = json.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id
        then: "country matches country inserted"
        repoReturned.country.equals(country)

    }

    def "Test Get Location"() {
        given: "an entity"
        def e1 = locationRepository.save(new Location("USA"))
        when: "controller is called with an id"
        def result = rentalController.getLocationById(e1.id)
        then: "result is an entity with an id"
        result.id == e1.id
        result.id > 0
        then: "result's country is USA"
        result.country.equals("USA")
        result.country.equals(e1.country)
        when: "findAll is called on the repository"
        def resultList = locationRepository.findAll()
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        result.id in resultList*.id
        for (i in result) {
            println("-----------------------------------------\nTesting\n" + i.id)
        }
    }

    def "Test Get Location REST"() {
        given: "an entity"
        def e1 = locationRepository.save(new Location("UK")) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/location?id=$e1.id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == e1.id
        json.id > 0
    }

    def "Test Edit Location"(){
        given: "an entity"
        def country = "UAE"
        when: "controller is called with a country"
        def result = rentalController.putLocation(country)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        result.country="USA"
        rentalController.editLocation(result.id, result.country)
        then: "original value has been changed"
        locationService.findById(id).country.equals("USA")

    }

    def "Test Edit Location REST"(){
        given: "an entity"
        def country = "UAE"
        when: "controller is called with a country"
        def result = rentalController.putLocation(country)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id

        when: "values changed, and put back in repo"
        result.country = "USA"
        mockMvc.perform(post(String.format("/location/edit?id=$result.id&country=$result.country")))
        then: "original value has changed"
        locationService.findById(id).country.equals("USA")

    }
    def "Test Delete Location"(){
        given: "an entity"
        def country = "UAE"
        when: "controller is called with a country"
        def result = rentalController.putLocation(country)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = rentalController.deleteLocation(id)
        returned!= rentalController.getLocationById(id)
    }

    def "Test Delete Location REST"(){
        given: "an entity"
        def country = "UAE"
        when: "controller is called with a country"
        def result = rentalController.putLocation(country)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = locationService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = mockMvc.perform(post("/location/delete?id=$id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json.id != locationService.findById(id)
    }

    ////////////////////////////////////////////////
    ////// Customer Info
    ////////////////////////////////////////////////
    def "Test Add Customer"() {
        given: "an entity"
        def name = "John Smith"
        when: "controller is called with a name"
        def result = rentalController.putCustomer(name)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id

    }

    def "Test Add Customer REST"() {
        given: "an entity"
        def name = "John Smith"
        when: "REST is called with a name"
        def result = mockMvc.perform(post("/customer?name=$name")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)

        then: "result is an entity with an id"
        json.id > 0
        def id = json.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id
        then: "country matches country inserted"
        repoReturned.name.equals(name)

    }

    def "Test Get Customer"() {
        given: "an entity"
        def name = "John Smith"
        def e1 = customerRepository.save(new Customer(name))
        when: "controller is called with an id"
        def result = rentalController.getCustomerById(e1.id)
        then: "result is an entity with an id"
        result.id == e1.id
        result.id > 0
        then: "result's country is USA"
        result.name.equals(name)
        result.name.equals(e1.name)
        when: "findAll is called on the repository"
        def resultList = customerRepository.findAll()
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        result.id in resultList*.id
        for (i in result) {
            println("-----------------------------------------\nTesting\n" + i.id)
        }
    }

    def "Test Get Customer REST"() {
        given: "an entity"
        name = "John Smith"
        def e1 = customerRepository.save(new Customer(name)) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/customer?id=$e1.id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == e1.id
        json.id > 0
    }

    def "Test Edit Customer"(){
        given: "an entity"
        def name = "John Smith"
        when: "controller is called with a country"
        def result = rentalController.putCustomer(name)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        result.name="Joe Smyth"
        rentalController.editCustomer(result.id, result.name)
        then: "original value has been changed"
        customerService.findById(id).name.equals("Joe Smyth")

    }

    def "Test Edit Customer REST"(){
        given: "an entity"
        def name = "John Smith"
        when: "controller is called with a country"
        def result = rentalController.putCustomer(name)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id

        when: "values changed, and put back in repo"
        result.name = "Joe Smyth"
        mockMvc.perform(post(String.format("/customer/edit?id=$result.id&name=$result.name")))
        then: "original value has changed"
        customerService.findById(id).name.equals("Joe Smyth")

    }
    def "Test Delete Customer"(){
        given: "an entity"
        def name = "John Smith"
        when: "controller is called with a country"
        def result = rentalController.putCustomer(name)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = rentalController.deleteCustomer(id)
        returned!= rentalController.getCustomerById(id)
    }

    def "Test Delete Customer REST"(){
        given: "an entity"
        def name = "John Smith"
        when: "controller is called with a country"
        def result = rentalController.putCustomer(name)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = customerService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = mockMvc.perform(post("/customer/delete?id=$id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json.id != customerService.findById(id)
    }



    ////////////////////////////////////////////////
    ////// Car Info
    ////////////////////////////////////////////////

    def "Test Add Car"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "controller is called with a name"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

    }

    def "Test Add Car REST"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "REST is called with a name"
        def result = mockMvc.perform(post(
                "/car?name=$name&registration=$registration&manufacturer=$manufacturer&model=$model&" +
                        "transmission=$transmission&category=$category")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)

        then: "result is an entity with an id"
        json.id > 0
        def id = json.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id
        then: "name matches name inserted"
        repoReturned.name.equals(name)

    }

    def "Test Get Car"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category))
        when: "controller is called with an id"
        def result = rentalController.getCarById(e1.id)
        then: "result is an entity with an id"
        result.id == e1.id
        result.id > 0
        then: "result's name is John Smith"
        result.name.equals(name)
        result.name.equals(e1.name)
        when: "findAll is called on the repository"
        def resultList = customerRepository.findAll()
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        result.id in resultList*.id
        for (i in result) {
            println("-----------------------------------------\nTesting\n" + i.id)
        }
    }

    def "Test Get Car REST"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category)) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/car?id=$e1.id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)
        then: "result is an entity with an id"
        json.id == e1.id
        json.id > 0
    }

    def "Test Edit Car"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "controller is called with car info"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        result.name="Joe Smyth"
        rentalController.editCar(id, result.name, result.manufacturer, result.model, result.transmission, result.category)
        then: "original value has been changed"
        carService.findById(id).name.equals("Joe Smyth")

    }

    def "Test Edit Car REST"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "controller is called with a country"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        when: "values changed, and put back in repo"
        result.name = "Joe Smyth"
        mockMvc.perform(post(String.format("/car/edit?id=$result.id&name=$result.name")))
        then: "original value has changed"
        carService.findById(id).name.equals("Joe Smyth")

    }

    def "Test Delete Car"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "controller is called with a car"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = rentalController.deleteCar(id)
        returned!= rentalController.getCarById(id)
    }

    def "Test Delete Car REST"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = "manual"
        def category = "car"

        when: "controller is called with a country"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = mockMvc.perform(post("/car/delete?id=$id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json.id != carService.findById(id)
    }
}
