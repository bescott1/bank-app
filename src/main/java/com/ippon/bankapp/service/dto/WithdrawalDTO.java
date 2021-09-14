package com.ippon.bankapp.service.dto;

import java.math.BigDecimal;

public class WithdrawalDTO {
    private BigDecimal amount;

    public WithdrawalDTO() {}

    public WithdrawalDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
