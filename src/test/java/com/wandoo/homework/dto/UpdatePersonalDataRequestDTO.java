package com.wandoo.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePersonalDataRequestDTO {

    private String firstName;

    @NotEmpty(message = "Surname cannot be null or blank")
    @Pattern(regexp = "^[^0-9]+$", message = "Surname cannot contain numbers")
    @JsonProperty("surname")
    private String surname;

    @NotEmpty(message = "Personal ID cannot be null")
    @Pattern(regexp = "^[0-9]{9}$", message = "Personal ID must be a 9-digit numeric value")
    @JsonProperty("personalId")
    private Long personalId;

}
