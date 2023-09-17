package com.itswpu.huanswpu.dto;

import com.itswpu.huanswpu.entity.Orders;
import com.itswpu.huanswpu.entity.SetmealDish;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
public class OrderReceiveDto implements Serializable {
    private Long orderId;
    private String userAddress;

    private String userName;

    private String employeeAddress;

    private LocalDateTime orderTime;

    private BigDecimal amount;
}
