package com.example.in_sky1.service;

import com.example.in_sky1.pojo.Order;
import com.example.in_sky1.pojo.Result;

public interface OrderService {
    Result createOrder(Order order);

    Result getOrders(Integer userId);

    Result confirmReceive(Long orderId);
}
