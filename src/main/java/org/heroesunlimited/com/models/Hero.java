package org.heroesunlimited.com.models;

import lombok.Data;

@Data
public class Hero {


    private final String name;
    private final int age;
    private final int powerId;
    private final int weaknessId;
    private int id;
    private int squadId;
}

