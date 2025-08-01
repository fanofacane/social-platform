package com.yupi.mcp.mcpserver.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.msg = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

}
