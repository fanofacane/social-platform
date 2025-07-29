package com.example.in_sky1.mapper;

import com.example.in_sky1.dto.OrderVO;
import com.example.in_sky1.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO orders (id, user_id, product_id, state, numbers, create_time) " +
            "VALUES (#{id}, #{userId}, #{productId}, #{state}, #{numbers}, #{createTime})")
    boolean insert(Order order);

    List<OrderVO> getOrderByUserId(Integer userId);

    @Update("update orders set state = 3 where id=#{orderId}")
    boolean confirmReceive(Long orderId);
}
