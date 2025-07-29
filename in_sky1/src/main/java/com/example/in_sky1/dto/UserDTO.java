package com.example.in_sky1.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String bio;
    private String ip;
    private int age;
    private String picture;
}
