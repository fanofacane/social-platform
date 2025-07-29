package com.example.in_sky1.mapper;
import java.util.List;
import com.example.in_sky1.pojo.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {
    @Select("select * from products")
    List<Product> getAllProduct();
    @Select("select stock from products where id=#{productId}")
    Integer getStockById(Integer productId);
    @Update("update products set stock= stock - #{numbers} , sale= sale + #{numbers} where id=#{productId} and stock - #{numbers} >0 ")
    boolean reduceStock(Integer productId,Integer numbers);
}
