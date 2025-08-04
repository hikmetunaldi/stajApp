package com.personalApp.shared_model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    ERKEK(0),
    KADIN(1);

    private final int value;

    Gender(int value){this.value = value;}

    public static Gender fromValue(int value){
        for(Gender g : Gender.values()){
            if(g.getValue() == value){
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid gender value:" + value);
    }

    @JsonCreator
    public static Gender fromString(String key){
        return Arrays.stream(Gender.values())
                .filter(g->g.name().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Invalid gender value: " + key));
    }
}
