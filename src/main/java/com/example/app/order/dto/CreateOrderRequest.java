package com.example.app.order.dto;
import java.util.List;
public class CreateOrderRequest {
    public Long userId;
    public String address;
    public List<CreateOrderItemRequest> items;
}
