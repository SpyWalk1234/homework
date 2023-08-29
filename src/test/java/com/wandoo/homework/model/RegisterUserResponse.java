package com.wandoo.homework.model;


import com.wandoo.homework.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {
    private UserDTO user;
    private Message message;
}
