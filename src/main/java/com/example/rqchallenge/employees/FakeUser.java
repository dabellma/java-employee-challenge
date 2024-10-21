package com.example.rqchallenge.employees;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FakeUser {

    //this is used as the other object on the other dummy api call I was using that proved okhttpclient was used correctly
    private int id;
    private String userId;
    private String title;
    private String body;

}
