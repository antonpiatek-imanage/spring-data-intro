package com.example.demo.repositories;


import com.example.demo.entities.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface ModuleRepository extends CrudRepository<Module,Long> {


    @Query("FROM Module m WHERE m.name =:name")
    Module findByName(@Param("name") String name);

//    @Query("SELECT module FROM module m INNER JOIN m.teacher teacher WHERE teacher.name=:name")
//    Module findByTeacherName(@Param("name") String name);
}
