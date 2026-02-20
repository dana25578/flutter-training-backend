package com.example.app.order.dto;
import java.time.LocalDateTime;
import java.util.List;
public class OrderResponse {
    public Long id;
    public Long userId;
    public String address;
    public double total;
    public LocalDateTime createdAt;
    public List<OrderItemResponse> items;
}
