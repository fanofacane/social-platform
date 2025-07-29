package com.example.in_sky1;

import com.example.in_sky1.mapper.OrderMapper;
import com.example.in_sky1.mapper.ProductMapper;
import com.example.in_sky1.pojo.Product;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InSky1ApplicationTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Test
    void statat() {
        List<Product> productList = productMapper.getAllProduct();
        System.out.println(productList);
        productList.stream().forEach(product ->
                stringRedisTemplate.opsForValue().set("StockCache::" + product.getId(), String.valueOf(product.getStock())));
        }
    @Test
    void cacheStock(){
        List<Product> productList = productMapper.getAllProduct();
        productList.stream().forEach(product ->
                stringRedisTemplate.opsForValue().set("productCache:stock::" + product.getId(), String.valueOf(product.getStock())));
    }
}
