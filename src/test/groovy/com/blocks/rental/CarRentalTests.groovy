package com.blocks.rental

import com.blocks.rental.controllers.RentalController
import com.blocks.rental.dtos.LocationDto
import com.blocks.rental.entities.Car
import com.blocks.rental.entities.Customer
import com.blocks.rental.entities.Location
import com.blocks.rental.repositories.BookingRepository
import com.blocks.rental.repositories.CarRepository
import com.blocks.rental.repositories.CustomerRepository
import com.blocks.rental.repositories.LocationRepository
import com.blocks.rental.services.BookingService
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

import java.text.SimpleDateFormat

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

    @Autowired BookingService bookingService
    @Autowired BookingRepository bookingRepository

    @Autowired WebApplicationContext context

    def setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    def cleanup() {
        bookingRepository.deleteAll()
        carRepository.deleteAll()
        customerRepository.deleteAll()
        locationRepository.deleteAll()
    }



    ////////////////////////////////////////////////
    ////// Location Info
    ////////////////////////////////////////////////


    def "Test Get Locations"() {
        given: "entities"

        def location = locationService.add("USA")
        def location2 = locationService.add("IRL")

        when: "controller is called"
        def result = rentalController.getLocations(1,25,"id",true)
        then: "result has elements"
        result.size() >0
        result.get(0).id > 0
        then: "result contains both inserted elements"
        result.get(0).id == location.id
        result.get(1).id == location2.id

    }

    def "Test Get Locations REST"() {
        given: "entities"

        def location = locationService.add("USA")
        locationService.add("IRL")

        locationService.mapToDto(location)
        when: "controller is called"
        def e1 = mockMvc.perform(get("/locations?id=$location.id")).andReturn().response.contentAsString
        def resultLists = new JsonSlurper().parseText(e1)
        assert resultLists instanceof List
        assert (LocationDto)resultLists[0] instanceof LocationDto
        then: "result has elements"
        resultLists.size() >0
        resultLists.get(0).id > 0
        then: "result contains both inserted elements"

        resultLists.collect{[it.country]}.sort() == [["IRL"], ["USA"]].sort()

    }


    def "Test Detailed Location"(){
        given: "entities"

        def location = locationService.add("USA")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)

        when: "controller is called"
        def result = rentalController.getDetailedDto(location.id)
        then: "result has elements"
        result.id > 0
        then: "result contains summaries of inserted elements"
        result.id == location.id
        car.id in result.carsInLocation*.id
    }

    def "Test Detailed Location REST"(){
        given: "entities"

        def location = locationService.add("USA")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)

        when: "controller is called"
        def json = mockMvc.perform(get("/location/$location.id/detailed")).andReturn().response.contentAsString
        def result = new JsonSlurper().parseText(json)
        then: "result has elements"
        result.id > 0
        then: "result contains summaries of inserted elements"
        result.id == location.id
        car.id in result.carsInLocation*.id[0]
    }

    def "Test Location Summary"(){
        given: "entities"

        def location = locationService.add("USA")
        def location2 = locationService.add("IRL")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)
        def customer = rentalController.putCustomer("John Smith")

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-04")
        rentalController.createBooking(car.id,customer.id,startDate,endDate)

        when: "controller is called"
        def result = rentalController.getLocationSummary()
        then: "result has elements"
        result.size() >0
        result.get(0).locationId > 0
        then: "result contains summaries of inserted elements"
        result.get(0).locationId == location.id
        result.get(1).locationId == location2.id
        result.get(0).activeBookings == 1
        result.get(0).totalBookings == 1
        result.get(1).totalBookings == 0
    }

    def "Test Location Summary REST"(){
        given: "entities"

        def location = locationService.add("USA")
        def location2 = locationService.add("IRL")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)
        def customer = rentalController.putCustomer("John Smith")

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-04")
        rentalController.createBooking(car.id,customer.id,startDate,endDate)

        when: "controller is called"
        def json = mockMvc.perform(get("/locations/summary")).andReturn().response.contentAsString
        def result = new JsonSlurper().parseText(json)
        then: "result has elements"
        result.size() >0
        result.get(0).locationId > 0
        then: "result contains summaries of inserted elements"
        result.get(0).locationId == location.id
        result.get(1).locationId == location2.id
        result.get(0).activeBookings == 1
        result.get(0).totalBookings == 1
        result.get(1).totalBookings == 0
    }

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
        repoReturned.country == country

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
        result.country == "USA"
        result.country == e1.country
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
        def result = mockMvc.perform(get("/location/$e1.id")).andReturn().response.contentAsString
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
        locationService.findById(id).country == "USA"

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
        mockMvc.perform(post(String.format("/location/$result.id/edit?country=$result.country")))
        then: "original value has changed"
        locationService.findById(id).country == "USA"

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
        def returned = mockMvc.perform(post("/location/$id/delete")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json.id != locationService.findById(id)
    }

    ////////////////////////////////////////////////
    ////// Customer Info
    ////////////////////////////////////////////////


    def "Test Get Customers"() {
        given: "entities"

        def customer = customerService.add("John Smith")
        def customer2 = customerService.add("Patrick Murphy")

        when: "controller is called"
        def result = rentalController.getCustomers(1,25,"id",true)
        then: "result has elements"
        result.size() >0
        result.get(0).id > 0
        then: "result contains both inserted elements"
        result.get(0).id == customer.id
        result.get(1).id == customer2.id

    }


    def "Test Get Customers REST"() {
        given: "entities"


        def customer = customerService.add("John Smith")
        def customer2 = customerService.add("Patrick Murphy")

        when: "controller is called"
        def e1 = mockMvc.perform(get("/customers")).andReturn().response.contentAsString
        def result = new JsonSlurper().parseText(e1)
        then: "result has elements"
        result.size() >0
        result.get(0).id > 0
        then: "result contains both inserted elements"
        result.get(0).id == customer.id
        result.get(1).id == customer2.id

    }


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
        repoReturned.name == name

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
        result.name == name
        result.name == e1.name
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
        def name = "John Smith"
        def e1 = customerRepository.save(new Customer(name)) //.save() is crudRepositroy, which is a parent of the ExampleController.
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/customer/$e1.id")).andReturn().response.contentAsString
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
        customerService.findById(id).name == "Joe Smyth"

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
        mockMvc.perform(post(String.format("/customer/$result.id/edit?name=$result.name")))
        then: "original value has changed"
        customerService.findById(id).name == "Joe Smyth"

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
        def returned = mockMvc.perform(post("/customer/$id/delete")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json.id != customerService.findById(id)
    }



    ////////////////////////////////////////////////
    ////// Car Info
    ////////////////////////////////////////////////



    def "Test Get Cars"() {
        given: "entities"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def category2 = Car.Category.van
        def location = locationService.add("USA")
        def location2 = locationService.add("IE")

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category, location))
        def e2 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category2, location2))
        when: "controller is called with an id"
        def result = rentalController.getCars(1,25,"id",true)
        then: "result has elements"
        result.size() >0
        result.get(0).id > 0
        then: "result contains both inserted elements"
        result.get(0).id == e1.id
        result.get(1).id == e2.id

    }

    def "Test Get Cars REST"() {
        given: "entities"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def category2 = Car.Category.van
        def location = locationService.add("USA")
        def location2 = locationService.add("IE")

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category, location))
        def e2 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category2, location2))
        when: "controller is called with an id"
        def e3 = mockMvc.perform(get("/cars")).andReturn().response.contentAsString
        def result = new JsonSlurper().parseText(e3)
        then: "result has elements"
        result.size() >0
        result.get(0).id > 0
        then: "result contains both inserted elements"
        result.get(0).id == e1.id
        result.get(1).id == e2.id
    }

    def "Test Add Car"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id
        when: "controller is called with a name"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)
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
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id
        when: "REST is called with a name"
        def result = mockMvc.perform(post(
                "/car?name=$name&registration=$registration&manufacturer=$manufacturer&model=$model&" +
                        "transmission=$transmission&category=$category&locationID=$location_id")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(result)

        then: "result is an entity with an id"
        json.id > 0
        def id = json.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id
        then: "name matches name inserted"
        repoReturned.name == name

    }

    def "Test Get Car"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category, location))
        when: "controller is called with an id"
        def result = rentalController.getCarById(e1.id)
        then: "result is an entity with an id"
        result.id == e1.id
        result.id > 0
        then: "result's name is John Smith"
        result.name == name
        result.name == e1.name
        when: "findAll is called on the repository"
        def resultList = carRepository.findAll()
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
        def transmission = Car.Transmission.manual
        def category = Car.Category.car

        def location = locationService.add("USA")

        def e1 = carRepository.save(new Car(name, registration, manufacturer, model, transmission, category, location))
        when: "controller is called with an id"
        def result = mockMvc.perform(get("/car/$e1.id")).andReturn().response.contentAsString
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
        def transmission = Car.Transmission.manual
        def category = Car.Category.car

        def location = locationService.add("USA")
        def location_id = location.id
        when: "controller is called with a country"

        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        result.name="Joe Smyth"
        rentalController.editCar(id, result.name, result.manufacturer, result.model, transmission, category)
        then: "original value has been changed"
        carService.findById(id).name == "Joe Smyth"

    }

    def "Test Edit Car REST"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id
        when: "controller is called with a country"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        when: "values changed, and put back in repo"
        result.name = "Joe Smyth"
        mockMvc.perform(post(String.format("/car/$result.id/edit?&name=$result.name")))
        then: "original value has changed"
        carService.findById(id).name == "Joe Smyth"

    }

    def "Test Delete Car"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id

        when: "controller is called with a car"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)
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
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id
        when: "controller is called with a country"
        def result = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)
        then: "result is an entity with an id"
        result.id > 0
        def id = result.id
        then: "result can be found in repository"
        def repoReturned = carService.findById(id)
        repoReturned.id == id

        then: "result can be deleted in repository"
        def returned = mockMvc.perform(post("/car/$id/delete")).andReturn().response.contentAsString
        def json = new JsonSlurper().parseText(returned)
        then: "result should not be in the repository"
        json != carService.findById(id)
    }

    ////////////////////////////////////////////////
    ////// Car Info
    ////////////////////////////////////////////////

    def "Test Get Bookings"(){
        given: "several entities"
        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")
        def startDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-05")
        def endDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-07")

        def e1 = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        rentalController.createBooking(car_id, customer_id, startDate2, endDate2)
        when: "controller is called"
        def res = rentalController.getBookings(1,25,"id",true)

        then: "result contains elements"
        res.size() == 2
        then: "result contains first element"
        res.get(0).id == e1.id

    }


    def "Test Get Bookings REST"(){
        given: "several entities"
        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")
        def startDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-05")
        def endDate2 = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-07")

        def e1 = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        rentalController.createBooking(car_id, customer_id, startDate2, endDate2)
        when: "controller is called"
        def e2 = mockMvc.perform(get("/bookings")).andReturn().response.contentAsString
        def res = new JsonSlurper().parseText(e2)
        then: "result contains elements"
        res.size() == 2
        then: "result contains first element"
        res.get(0).id == e1.id

    }

    def "Test Add Booking"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
        then: "result can be found in repository"
        def repoReturned = bookingService.findById(id)
        repoReturned.id == id

    }

    def "Test Add Booking REST"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def location_id = location.id

        def customer = customerService.add("John Smith")
        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = "2019/02/01"
        def endDate = "2019/02/04"

        when: "controller is called with a name"
        def json = new JsonSlurper().parseText(mockMvc.perform(post
                ("/booking?startDate=$startDate&endDate=$endDate&car_id=$car_id&customer_id=$customer_id")).
                andReturn().response.contentAsString)
        then: "result is an entity with an id"
        json.id > 0
        def id = json.id
        then: "result can be found in repository"
        def repoReturned = bookingService.findById(id)
        repoReturned.id == id

    }

    def "Test Get Booking"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
        then: "result can be found in repository"
        def repoReturned = bookingService.findById(id)
        repoReturned.id == id

        when: "findAll is called on the repository"
        def resultList = bookingRepository.findAll()
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        id in resultList*.id

    }

    def "Test Get Booking REST"() {
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
        then: "result can be found in repository"
        def repoReturned =  new JsonSlurper().parseText(
                mockMvc.perform(get("/booking/$id")).andReturn().response.contentAsString)
        repoReturned.id == id

        when: "findAll is called on the repository"
        def resultList = bookingRepository.findAll()
        then: "some results"
        !resultList.isEmpty()
        and: "including our earlier id"
        id in resultList*.id
    }

    def "Test Edit Booking"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
        then: "result can be found in repository"
        def repoReturned = bookingService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        repoReturned.startDate=new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-03")
        rentalController.editBooking(id,car_id,customer_id,repoReturned.startDate,repoReturned.endDate)
        then: "original value has been changed"
        bookingService.findById(id).startDate.time == repoReturned.startDate.time

    }

    def "Test Edit Booking REST"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
        then: "result can be found in repository"
        def repoReturned = bookingService.findById(id)
        repoReturned.id == id

        when: "values can be changed, and put back in repo"
        def newDate ="2019-02-03"
        mockMvc.perform(post("/booking/$id/edit?startDate=$newDate"))
        then: "original value has been changed"
        bookingService.findById(id).startDate.time == repoReturned.startDate.time

    }

    def "Test Delete Booking"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)

        then: "result can be deleted in repository"
        def returned = rentalController.deleteBooking(booking.id)
        returned!= rentalController.getBooking(booking.id)
    }

    def "Test Delete Booking REST"(){
        given: "an entity"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)

        then: "result can be deleted in repository"
        def json = mockMvc.perform(post("/booking/$booking.id/delete")).andReturn().response.contentAsString
        def returned = new JsonSlurper().parseText(json)
        returned!= rentalController.getBooking(booking.id)
    }

    def "Test Get Location Cars"(){
        given: "entities"

        def location = locationService.add("USA")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)
        def car2 = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.automatic, Car.Category.van,location.id)

        when: "controller is called"
        def result = rentalController.getLocationCars(location.id)
        then: "result has elements"
        result.size() > 0
        then: "result contains inserted cars"
        car.id in result*.id
        car2.id in result*.id
    }

    def "Test Get Location Cars REST"(){
        given: "entities"

        def location = locationService.add("USA")
        def car = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.manual, Car.Category.car,location.id)
        def car2 = rentalController.putCar("Test","Test","Test","Test" ,Car.Transmission.automatic, Car.Category.van,location.id)

        when: "controller is called"
        def json = mockMvc.perform(get("/location/$location.id/cars")).andReturn().response.contentAsString
        def result = new JsonSlurper().parseText(json)
        then: "result has elements"
        result.size() > 0
        then: "result contains inserted cars"
        result*.id.contains((int)car.id)
        result*.id.contains((int)car2.id)

    }

    def "Test No Location Car"(){
        given: "entities"
        when: "car is added without location"
        def car = rentalController.getLocationCars(0)
        then: "result should be null"
        car == null
    }

    def "Test Bad Bookings"(){
        given: "entities"

        def name = "John Smith"
        def registration = "ABC123"
        def manufacturer = "Ford"
        def model = "Type"
        def transmission = Car.Transmission.manual
        def category = Car.Category.car
        def location = locationService.add("USA")
        def customer = customerService.add("John Smith")
        def location_id = location.id

        def car = rentalController.putCar(name, registration, manufacturer, model, transmission, category, location_id)

        def customer_id = customer.id
        def car_id = car.id

        def startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-01")
        def endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-04")

        when: "controller is called with a name"
        def booking = rentalController.createBooking(car_id, customer_id, startDate, endDate)
        then: "result is an entity with an id"
        booking.id > 0
        def id = booking.id
    }
}