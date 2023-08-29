package com.wandoo.homework.model;

import lombok.Data;

@Data
public class CurrentUser {
    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String surname;

    private Long personalId;

    private String fullName;
}
