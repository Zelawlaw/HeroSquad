package org.justiceleague.com.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Hero {

    private int id;
    private final String name;
    private final int age;
    private int squadId;
    private int powerId;
    private int weaknessId;
}

