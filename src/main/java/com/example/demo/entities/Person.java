package com.example.demo.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@javax.persistence.Entity
public class Person {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long personID;

    public String name;
    public String phoneNum;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Module> modules;

    public Person(){}

    public Person(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
        modules = new HashSet<Module>();
    }

    public void addModule(Module module){
        this.modules.add(module);
    }

}
