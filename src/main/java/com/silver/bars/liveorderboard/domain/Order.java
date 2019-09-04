package com.silver.bars.liveorderboard.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Order {

    @NotNull
    private String userId;
    @NotNull
    private Double quantity;
    @NotNull
    private Long price;
    @NotNull
    private OrderType orderType;

}
