package com.wandoo.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wandoo.homework.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;

    @JsonProperty("type")
    private TransactionType transactionType;

    private BigDecimal amount;

    private String rawResponse;
}


