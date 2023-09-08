package com.green.babymeal.admin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AdminShipmentDto {
    private List<Long> orderCode; // 주문번호
    private byte shipment;
}
