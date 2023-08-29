package com.wandoo.homework.utils;

import com.wandoo.homework.dto.RegistrationRequestDTO;
import com.wandoo.homework.dto.TransactionRequestDTO;
import com.wandoo.homework.dto.UpdatePersonalDataRequestDTO;
import com.wandoo.homework.model.CurrentTransaction;
import com.wandoo.homework.model.CurrentUser;
import com.wandoo.homework.model.TransactionType;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

public class TestFixtures {


    public static RegistrationRequestDTO prepareRegistrationRequest() {
        val email = String.format("email-%s@email.com", UUID.randomUUID().toString());
        val password = UUID.randomUUID().toString();
        return new RegistrationRequestDTO(email, password);
    }

    public static UpdatePersonalDataRequestDTO preparePersonalDataRequest(Long currentUserId) {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String surname = RandomStringUtils.randomAlphabetic(10);
        return UpdatePersonalDataRequestDTO.builder()
                .firstName(firstName)
                .surname(surname)
                .personalId(currentUserId)
                .build();
    }

    public static TransactionRequestDTO prepareTransactionRequest(CurrentUser currentUser, String amountValue) {
        val amount = new TransactionRequestDTO.AmountDTO();
        amount.setCurrency("EUR");
        amount.setAmount(new BigDecimal(amountValue).setScale(2, RoundingMode.UP));
        return TransactionRequestDTO.builder()
                .accountHolderFullName(currentUser.getFullName())
                .accountHolderPersonalId(currentUser.getPersonalId().toString())
                .transactionType(TransactionType.FUNDING)
                .investorId(UUID.randomUUID().toString())
                .bookingDate(LocalDate.now())
                .accountNumber(UUID.randomUUID().toString())
                .amount(amount)
                .build();
    }

    public static CurrentTransaction prepareCurrentTransaction(TransactionRequestDTO request) {
        return CurrentTransaction.builder()
                .transactionType(request.getTransactionType())
                .accountHolderFullName(request.getAccountHolderFullName())
                .accountHolderPersonalId(request.getAccountHolderPersonalId())
                .accountNumber(request.getAccountNumber())
                .amount(prepareAmount(request.getAmount()))
                .investorId(request.getInvestorId())
                .bookingDate(request.getBookingDate())
                .rawResponse(JsonConverter.toJsonString(request))
                .build();
    }

    private static CurrentTransaction.AmountDTO prepareAmount(TransactionRequestDTO.AmountDTO from) {
        val result = new CurrentTransaction.AmountDTO();
        result.setAmount(from.getAmount());
        result.setCurrency(from.getCurrency());
        return result;
    }

    public static RegistrationRequestDTO prepareInvalidRegistrationRequest() {
        val password = UUID.randomUUID().toString();
        return new RegistrationRequestDTO(null, password);
    }
}
