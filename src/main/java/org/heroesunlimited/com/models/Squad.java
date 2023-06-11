package org.heroesunlimited.com.models;

import lombok.Data;

@Data
public class Squad {
    private  String name;
    private  String cause;
    private int id;

    public Squad(String name, String cause) {
        this.name = name;
        this.cause = cause;
    }


}
