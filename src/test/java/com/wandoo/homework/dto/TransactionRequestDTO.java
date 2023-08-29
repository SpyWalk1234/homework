package com.wandoo.homework.dto;

import com.wandoo.homework.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    private String accountNumber;

    private String accountHolderFullName;

    private String accountHolderPersonalId;

    private TransactionType transactionType;

    private String investorId;

    private AmountDTO amount;

    private LocalDate bookingDate;

    @Data
    public static class AmountDTO {

        private String currency;

        private BigDecimal amount;
    }
}


