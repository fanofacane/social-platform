//package com.example.in_sky1.service.serviceImpl;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSON;
//import com.example.in_sky1.dto.OrderVO;
//import com.example.in_sky1.mapper.OrderMapper;
//import com.example.in_sky1.mapper.ProductMapper;
//import com.example.in_sky1.pojo.CacheTTL;
//import com.example.in_sky1.pojo.Order;
//import com.example.in_sky1.pojo.Result;
//import com.example.in_sky1.service.OrderService;
//import com.example.in_sky1.utils.CacheExpireUtils;
//import com.example.in_sky1.utils.RedisIdWorker;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.connection.stream.*;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Service
//public class OrderServiceImpl implements OrderService {
//    @Autowired
//    private OrderMapper orderMapper;
//    @Autowired
//    private ProductMapper productMapper;
//    @Autowired
//    private RedisIdWorker redisIdWorker;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private CacheExpireUtils cacheExpireUtils;
//    private static final DefaultRedisScript<Long> ORDER_LOCK_SCRIPT;
//    private static final ExecutorService ORDER_EXECUTOR= Executors.newSingleThreadExecutor();
//    @PostConstruct
//    public void init() {
//        ORDER_EXECUTOR.submit(new VoucherOrderHandler());
//    }
//    private class VoucherOrderHandler implements Runnable {
//        String queueName="stream.orders";
//        @Override
//        public void run() {
//            while (true){
//                try {
//                    //获取消息队列中的订单信息
//                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                            Consumer.from("g1", "c1"),
//                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
//                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
//                    );
//                    if (list.isEmpty()||list==null){
//                        continue;
//                    }
//                    //解析订单信息
//                    MapRecord<String, Object, Object> record = list.get(0);
//                    Map<Object, Object> value = record.getValue();
//                    Order order = BeanUtil.fillBeanWithMap(value, new Order(), true);
//                    //下单
//                    handleOrder(order);
//                    //ACK确认
//                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
//                }catch (Exception e){
//                    handlePendingList();
//                }
//            }
//        }
//    }
//    static {
//        ORDER_LOCK_SCRIPT = new DefaultRedisScript<>();
//        ORDER_LOCK_SCRIPT.setResultType(Long.class);
//        ORDER_LOCK_SCRIPT.setLocation(new ClassPathResource("orderLock.lua"));
//    }
//
//    @Override
//    public Result createOrder(Order order) {
//        Long orderId=redisIdWorker.nextId("order");
//        System.out.println("orderId:"+orderId);
//        Long result=stringRedisTemplate.execute(
//            ORDER_LOCK_SCRIPT, Collections.emptyList(),
//                order.getProductId().toString(),
//                order.getUserId().toString(),
//                Long.toString(orderId),
//                order.getNumbers().toString()
//        );
//        if(result.intValue()!=0){
//            return Result.error("库存不足");
//        }
//        return Result.success(orderId);
//    }
//
////    @Transactional
////    public Result createOrder(Order order) {
////        Integer stock = productMapper.getStockById(order.getProductId());
////        if(stock>0){
////            boolean success = productMapper.reduceStock(order.getProductId(),order.getNumbers());
////            if(!success) return Result.error("库存不足");
////            order.setCreateTime(LocalDateTime.now());
////            order.setState(2);
////            boolean insert = orderMapper.insert(order);
////            if(!insert) return Result.error("购买失败");
////        }
////        return Result.success("购买成功");
////    }
//
//    @Override
//    public Result getOrders(Integer userId) {
//        int ttl=cacheExpireUtils.randomExpire(CacheTTL.ORDER_CACHE_BASE);
//        String s = stringRedisTemplate.opsForValue().get("orderCache:info::" + userId);
//        if(s==null||s.isEmpty()){
//            List<OrderVO> orderList=orderMapper.getOrderByUserId(userId);
//            String jsonStr = JSONUtil.toJsonStr(orderList);
//            stringRedisTemplate.opsForValue().set("orderCache:info::" + userId,jsonStr,ttl, TimeUnit.SECONDS);
//            return Result.success(orderList);
//        }
//        List<OrderVO> list = JSONUtil.toList(s,OrderVO.class);
//        return Result.success(list);
//    }
//
//    @Override
//    public Result confirmReceive(Long orderId) {
//        return Result.success(orderMapper.confirmReceive(orderId));
//    }
//    private void handleOrder(Order order) {
//        order.setCreateTime(LocalDateTime.now());
//        order.setState(2);
//        System.out.println(order);
//        orderMapper.insert(order);
//        productMapper.reduceStock(order.getProductId(),order.getNumbers());
//        stringRedisTemplate.delete("orderCache:info::"+order.getUserId());
//    }
//    private void handlePendingList() {
//        String queueName="stream.orders";
//        while (true){
//            try {
//                //获取Pending-list中的订单信息
//                List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                        Consumer.from("g1", "c1"),
//                        StreamReadOptions.empty().count(1),
//                        StreamOffset.create(queueName, ReadOffset.from("0"))
//                );
//                if (list.isEmpty()||list==null){
//                    break;
//                }
//                //解析订单信息
//                MapRecord<String, Object, Object> record = list.get(0);
//                Map<Object, Object> value = record.getValue();
//                Order order = BeanUtil.fillBeanWithMap(value, new Order(), true);
//                //下单
//                handleOrder(order);
//                //ACK确认
//                stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
//            }catch (Exception e){
//                log.error("处理Pending-list异常",e);
//            }
//        }
//    }
//}
