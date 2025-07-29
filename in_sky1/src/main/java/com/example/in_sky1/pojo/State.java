package com.example.in_sky1.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class State implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Boolean isLike;
    private Boolean isCollection;
    private Boolean isFollow;
}
