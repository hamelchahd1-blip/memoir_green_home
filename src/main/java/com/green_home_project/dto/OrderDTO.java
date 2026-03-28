package com.green_home_project.dto;

import com.green_home_project.model.OrderStatus;
import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    private String plantName;
    private String buyerName;
    private String sellerName;
    private OrderStatus status;
}
