package org.heroesunlimited.com.models;

import lombok.Data;

@Data
public class Power {



    private  String name;
    private  String description;
    private int id;

    public Power(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
