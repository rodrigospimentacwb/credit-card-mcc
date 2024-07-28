package br.com.pepper.credit_card_mcc.controller.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record RequestCreateTransactionDto(@NotBlank String account,
                                          @DecimalMin(value = "0.01", inclusive = true) BigDecimal totalAmount,
                                          @NotBlank String mcc,
                                          @NotBlank String merchant) {
}
