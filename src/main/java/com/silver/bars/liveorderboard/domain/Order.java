package com.silver.bars.liveorderboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
