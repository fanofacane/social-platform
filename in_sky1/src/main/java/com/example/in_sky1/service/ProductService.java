package com.example.in_sky1.service;

import com.example.in_sky1.mapper.ProductMapper;
import com.example.in_sky1.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;

public interface ProductService {

    Result getAllProduct();
}
