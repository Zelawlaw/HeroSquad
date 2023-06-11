package org.heroesunlimited.com.models;

import lombok.Data;

@Data
public class Weakness {
    private  String name;
    private  String description;
    private int id;

    public Weakness(String name, String description) {
        this.name = name;
        this.description = description;
    }



}


