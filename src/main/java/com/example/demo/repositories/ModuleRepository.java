package com.example.demo.repositories;


import com.example.demo.entities.Module;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface ModuleRepository extends CrudRepository<Module,Long> {


    @Query("FROM Module m WHERE m.name =:name")
    Module findByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE Module m SET m.name=:new_name WHERE m.name=:curr_name")
    void editModuleName(@Param("curr_name") String curr_name, @Param("new_name") String new_name);

    //Add a query to find a module by the name of it's set teacher


}
