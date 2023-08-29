package com.wandoo.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {


    private Long id;


    private String email;


    private String firstName;


    private String surname;


    private Long personalId;
}
