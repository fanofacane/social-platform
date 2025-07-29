package com.example.in_sky1.controller;

import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public Result getAll() {
        return productService.getAllProduct();
    }
}
