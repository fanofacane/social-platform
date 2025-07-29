--商品id
local productId = ARGV[1]
--用户id
local userId = ARGV[2]
--订单id
local orderId = ARGV[3]
--商品数量
local numbers = ARGV[4]

--库存key
local stockKey ='productCache:stock::' ..productId

--判断库存是否充足
if(tonumber(redis.call('get',stockKey)) <=0) then
    return 1;
end
--扣库存
redis.call('incrby',stockKey,-1)
--发送到消息队列
redis.call('xadd','stream.orders','*','userId',userId,'productId',productId,'id',orderId,'numbers',numbers)
return 0;