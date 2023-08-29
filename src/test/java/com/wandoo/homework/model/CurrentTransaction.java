package com.wandoo.homework.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentTransaction {


    private String accountNumber;

    private String accountHolderFullName;

    private String accountHolderPersonalId;

    private TransactionType transactionType;

    private String investorId;

    private AmountDTO amount;

    private LocalDate bookingDate;

    private String rawResponse;

    @Getter
    @Setter
    public static class AmountDTO {

        private String currency;

        private BigDecimal amount;
    }
}


