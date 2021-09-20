package com.ippon.bankapp.service.dto;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class WithdrawalDTO {
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    public WithdrawalDTO() {}

    public WithdrawalDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
