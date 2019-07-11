package com.example.demo.repositories;

import com.example.demo.entities.Entity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * See https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public interface EntityRepository extends CrudRepository<Entity,Long> {





    Set<Entity> findByName(String name);





    Set<Entity> findByNameLike(String name);







    List<Entity> findByNameLikeOrderByIdAsc(String name);
    List<Entity> findByNameLikeOrderByIdDesc(String name);




    Entity findFirstByNameLikeOrderByIdAsc(String name);



    @Query("SELECT e FROM Entity e INNER JOIN e.children child WHERE child.name = :childName")
    Set<Entity> findByChildName(@Param("childName") String childName);


    @Query(nativeQuery = true, value = "SELECT e.* FROM entity e INNER JOIN child_entity ce ON ce.parent_id=e.id WHERE ce.name = :childName")
    Set<Entity> findByChildNameNative(@Param("childName") String childName);
}
