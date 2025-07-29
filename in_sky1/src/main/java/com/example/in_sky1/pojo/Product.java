package com.example.in_sky1.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private String title;
    private Integer categoriesId;
    private String picture;
    private Integer price;
    private Integer sale;
    private Integer stock;
    private LocalDateTime createTime;
}
