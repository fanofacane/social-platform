package com.example.in_sky1.controller;

import com.example.in_sky1.pojo.Order;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrdersController {
//    @Autowired
//    private OrderService orderService;
//    @PostMapping
//    public Result postOrder(@RequestBody Order order){
//        return orderService.createOrder(order);
//    }
//    @GetMapping("/getOrderInfo")
//    public Result getOrders(@RequestParam Integer userId){
//        return orderService.getOrders(userId);
//    }
//    @PostMapping("/confirmReceive/{orderId}")
//    public Result confirmReceive(@PathVariable Long orderId){
//        return orderService.confirmReceive(orderId);
//    }
}
