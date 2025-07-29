package com.example.in_sky1.service.serviceImpl;

import com.example.in_sky1.mapper.ProductMapper;
import com.example.in_sky1.pojo.Product;
import com.example.in_sky1.pojo.Result;
import com.example.in_sky1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public Result getAllProduct() {
        List<Product> productList = productMapper.getAllProduct();
        return Result.success(productList);
    }
}
