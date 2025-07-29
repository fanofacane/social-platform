package com.example.in_sky1.pojo;

import lombok.Data;
import org.apache.ibatis.mapping.ResultMap;

import java.io.Serializable;
import java.util.Map;

/**
 * 后端统一返回结果
 */
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer code; //编码：1成功，0为失败
    private String msg; //错误信息
    private Object data; //数据

    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = 1;
        result.msg = "success";
        return result;
    }
    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
