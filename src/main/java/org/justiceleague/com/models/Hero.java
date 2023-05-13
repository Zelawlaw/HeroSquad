package org.justiceleague.com.models;

import lombok.Data;

@Data
public class Hero {

    private final String name;
    private final int age;
    private int id;
    private int squadId;
    private int powerId;
    private int weaknessId;
}

