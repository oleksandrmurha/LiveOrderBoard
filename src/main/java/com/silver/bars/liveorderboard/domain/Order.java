package com.silver.bars.liveorderboard.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class Order {

    @NotNull
    private String userId;
    @NotNull
    private BigDecimal quantity;
    @NotNull
    private Long price;
    @NotNull
    private OrderType orderType;

}
