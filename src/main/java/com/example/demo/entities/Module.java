package com.example.demo.entities;


import javax.persistence.*;

import java.util.Set;

@javax.persistence.Entity
public class Module {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long moduleID;
    public String name = "";

    @OneToOne(optional = true)
    public Person teacher;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Person> students;

    public Module(){}

    public Module(String name){
        this.name = name;
    }

}
