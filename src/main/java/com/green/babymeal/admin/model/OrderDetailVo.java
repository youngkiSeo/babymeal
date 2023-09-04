package com.green.babymeal.admin.model;

import com.green.babymeal.common.entity.OrderlistEntity;
import com.green.babymeal.common.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Builder
@Getter
@Setter
public class OrderDetailVo {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private int count;
    private int totalPrice;
}
