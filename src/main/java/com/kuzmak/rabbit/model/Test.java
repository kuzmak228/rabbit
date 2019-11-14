package com.kuzmak.rabbit.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Type;

@Data
@AllArgsConstructor
@ToString
public class Test implements Type {

    private String firstName;
    private String lastName;


    @Override
    public String getTypeName() {
        return getClass().getSimpleName();
    }
}
