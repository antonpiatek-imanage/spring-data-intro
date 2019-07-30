package com.example.demo.repositories;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Set;

/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface PersonRepository extends CrudRepository<Person,Long> {


    @Query("SELECT p FROM Person p WHERE p.name =:name")
    Person findByName(@Param("name") String name);

    @Query("SELECT m FROM Module m INNER JOIN m.students students WHERE students.name=:name")
    Set<Module> findAllModulesByName(@Param("name") String name);

}
