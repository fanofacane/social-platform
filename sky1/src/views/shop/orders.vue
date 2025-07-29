<template>
  <div class="orders-container">
          <!-- 返回按钮 -->
      <div class="back-button" @click="goBack">
        <i class="iconfont icon-back"></i>
        返回
      </div>
    <h1 class="page-title">我的订单</h1>
    
    <!-- 订单状态筛选 -->
    <div class="order-filter">
      <div 
        v-for="status in orderStatuses" 
        class="filter-item"
        :class="{ active: currentStatus == status.value }"
        @click="selectStatus(status.value)"
      >
        {{ status.label }}
      </div>
    </div>

    <!-- 订单列表 -->
    <div class="order-list">
      <div v-for="order in filteredOrders"  class="order-card">
        <div class="order-header">
          <div class="order-info">
            <span class="order-id">订单号：{{ order.id }}</span>
            <span class="order-date">{{ order.createTime }}</span>
          </div>
        </div>
        
        <div class="order-content">
          <div class="product-info">
            <img :src="order.picture">
            <div class="product-details">
              <div class="product-name">{{ order.title }}</div>
              <div class="product-price">¥{{ order.price }}</div>
              <div class="product-quantity">x{{ order.numbers }}</div>
            </div>
          </div>
          <div class="order-total">
            <div class="total-amount">
              实付款：<span class="price">¥{{ order.price*order.numbers }}</span>
            </div>
            <div class="order-actions">
              <button 
                v-if="order.state == '0'" 
                class="btn-primary"
                @click="payOrder(order)"
              >
                立即付款
              </button>
              <button 
                v-if="order.state == '2'" 
                class="btn-primary"
                @click="confirmReceive(order)"
              >
                确认收货
              </button>
              <button 
                v-if="order.state == '3'" 
                class="btn-secondary"
                @click="writeReview(order)"
              >
                评价
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed ,onMounted} from 'vue'
import { useStore } from 'vuex';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router'
import {getOrderInfo,confirmrOrders} from '@/api/shop'
const router = useRouter()
const store = useStore();
const user = computed(() => store.state.user)
const isLogin = computed(() => store.state.isLogin);
const orders = ref([])
onMounted(()=>{
  getOrders();
})
// 订单状态
const orderStatuses = [
  { label: '全部', value: 'all' },
  { label: '待付款', value: '0' },
  { label: '待发货', value: '1' },
  { label: '待收货', value: '2' },
  { label: '已完成', value: '3' }
]

const currentStatus = ref('all')

const getOrders=async()=>{
  if(!isLogin) return 
  const res= await getOrderInfo(user.value.id);
  if(res){
    orders.value=res.data
  }
}

// 根据状态筛选订单
const filteredOrders = computed(() => {
  if (currentStatus.value == 'all') {
    return orders.value
  }
  return orders.value.filter(order => order.state == currentStatus.value) 
})


// 选择状态
const selectStatus = (status) => {
  currentStatus.value = status
}

// 支付订单
const payOrder = (orderId) => {
  console.log('支付订单:', orderId)
  // TODO: 实现支付逻辑
}

// 确认收货
const confirmReceive = async(order) => {
  console.log(order.id)
  const res=await confirmrOrders(order.id);
  if(res){
    order.state=3;
    ElMessage.success("收货成功！！")
  }
  // TODO: 实现确认收货逻辑
}

// 写评价
const writeReview = (orderId) => {
  console.log('写评价:', orderId)
  // TODO: 实现评价逻辑
}
// 返回上一页
const goBack = () => {
  router.back()
}
</script>

<style scoped>
.orders-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  min-height: calc(100vh - 60px);
  background-color: #f5f5f5;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
  text-align: center;
}

.order-filter {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  justify-content: center;
}

.filter-item {
  padding: 10px 24px;
  cursor: pointer;
  border-radius: 24px;
  transition: all 0.3s ease;
  font-size: 16px;
  min-width: 100px;
  text-align: center;
}

.filter-item:hover {
  background-color: #fff5f7;
  color: #ff2c55;
}

.filter-item.active {
  background-color: #ff2c55;
  color: #fff;
  font-weight: 500;
}

.order-list {
  max-width: 1000px;
  margin: 0 auto;
}

.order-card {
  background-color: #fff;
  border-radius: 16px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  border-bottom: 1px solid #eee;
  background-color: #fafafa;
}

.order-info {
  display: flex;
  gap: 30px;
  color: #666;
  font-size: 15px;
}

.order-status {
  font-weight: 600;
  font-size: 16px;
}

.order-status.pending {
  color: #ff2c55;
}

.order-status.shipped {
  color: #1890ff;
}

.order-status.completed {
  color: #52c41a;
}

.order-content {
  padding: 30px;
}

.product-info {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  align-items: center;
}

.product-info img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.product-details {
  flex: 1;
}

.product-name {
  font-size: 18px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 500;
}

.product-price {
  color: #ff2c55;
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 8px;
}

.product-quantity {
  color: #999;
  font-size: 15px;
}

.order-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.total-amount {
  font-size: 16px;
  color: #666;
}

.total-amount .price {
  color: #ff2c55;
  font-size: 24px;
  font-weight: bold;
  margin-left: 8px;
}

.order-actions {
  display: flex;
  gap: 16px;
}

.btn-primary {
  padding: 10px 28px;
  background-color: #ff2c55;
  color: #fff;
  border: none;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 15px;
  font-weight: 500;
}

.btn-primary:hover {
  background-color: #ff1a45;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(255, 44, 85, 0.3);
}

.btn-secondary {
  padding: 10px 28px;
  background-color: #fff;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 15px;
  font-weight: 500;
}

.btn-secondary:hover {
  background-color: #f5f5f5;
  color: #333;
  border-color: #ccc;
  transform: translateY(-1px);
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .orders-container {
    padding: 30px 15px;
  }
  
  .order-filter {
    padding: 15px 20px;
    gap: 20px;
  }
  
  .filter-item {
    padding: 8px 20px;
    min-width: 80px;
  }
}

@media (max-width: 768px) {
  .orders-container {
    padding: 20px 10px;
  }
  
  .page-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .order-filter {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .filter-item {
    padding: 6px 16px;
    font-size: 14px;
  }
  
  .order-header {
    padding: 15px 20px;
  }
  
  .order-content {
    padding: 20px;
  }
  
  .product-info {
    gap: 15px;
  }
  
  .product-info img {
    width: 100px;
    height: 100px;
  }
  
  .product-name {
    font-size: 16px;
  }
  
  .btn-primary,
  .btn-secondary {
    padding: 8px 20px;
    font-size: 14px;
  }
}
.back-button {
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  cursor: pointer;
  color: #666;
  margin-bottom: 20px;
}
</style> 