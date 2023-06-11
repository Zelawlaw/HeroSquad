package org.heroesunlimited.com.models;

import lombok.Data;

@Data
public class Hero {


    private String name;
    private  int age;
    private  int powerId;
    private  int weaknessId;
    private int id;
    private int squadId;

    public Hero(String name, int age, int powerId, int weaknessId) {
        this.name = name;
        this.age = age;
        this.powerId = powerId;
        this.weaknessId = weaknessId;
    }
}

