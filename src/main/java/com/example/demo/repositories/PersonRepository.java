package com.example.demo.repositories;

import com.example.demo.entities.Module;
import com.example.demo.entities.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface PersonRepository extends CrudRepository<Person,Long> {


    Person findById(long id);


    @Query("SELECT p FROM Person p WHERE p.name =:name")
    Person findByName(@Param("name") String name);

//    @Query(nativeQuery = true, value= "SELECT m.* FROM Module m " +
//            "INNER JOIN PERSON_MODULES pm ON m.moduleID = pm.MODULES_MODULEID " +
//            "INNER JOIN PERSON p ON p.personID = pm.PERSON_PERSONID" +
//            " WHERE p.name=:name")
    @Query(" FROM Module m "
    )
    Set<Module> findAllModulesByName(@Param("name") String name);

//    @Query("SELECT p FROM Person p WHERE p.name =:name")
//    Set<Module> findAllModulesByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE Person p SET p.name=:name WHERE p.personID=:id")
    void editPersonName(@Param("id") long id, @Param("name") String name);


    @Transactional
    @Modifying
    @Query("UPDATE Person p SET p.phoneNum=:phoneNum WHERE p.id=:id")
    void editPersonPhoneNum(@Param("id") long id, @Param("phoneNum") String phoneNum);
}
