package com.itswpu.huanswpu.dto;

import com.itswpu.huanswpu.entity.OrderDetail;
import lombok.Data;

import java.util.List;
@Data
public class OrderDetailDto {
    private List<OrderDetail> orderDetails;

    private String phone;


}
