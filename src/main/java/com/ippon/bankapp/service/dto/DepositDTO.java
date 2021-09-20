package com.ippon.bankapp.service.dto;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class DepositDTO {
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    public DepositDTO() {}

    public DepositDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
