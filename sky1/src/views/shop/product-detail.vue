<template>
  <div class="product-detail">
    <div class="detail-container">
      <!-- 返回按钮 -->
      <div class="back-button" @click="goBack">
        <i class="iconfont icon-back"></i>
        返回
      </div>

      <!-- 商品信息区域 -->
      <div class="product-content">
        <div class="product-gallery">
          <div class="main-image">
            <img :src="product.picture">
          </div>
          <!-- <div class="thumbnail-list">
            <div 
              v-for="(img, index) in product.picture" 
              :key="index"
              class="thumbnail"
              :class="{ active: currentImage == index }"
              @click="currentImage = index"
            >
              <img :src="img" :alt="product.title">
            </div>
          </div> -->
        </div>

        <!-- 商品信息 -->
        <div class="product-info">
          <h1 class="product-name">{{ product.title }}</h1>
          <div class="product-price">¥{{ product.price }}</div>
          <div class="product-sales">已售 {{ product.sale }} 件</div>
          
          <!-- 商品规格 -->
          <!-- <div class="product-specs">
            <div class="spec-title">规格</div>
            <div class="spec-options">
              <div 
                v-for="spec in product.specs" 
                :key="spec.id"
                class="spec-item"
                :class="{ active: selectedSpec === spec.id }"
                @click="selectSpec(spec.id)"
              >
                {{ spec.name }}
              </div>
            </div>
          </div> -->

          <!-- 购买数量 -->
          <div class="product-quantity">
            <div class="quantity-title">数量</div>
            <div class="quantity-control">
              <button @click="decreaseQuantity">-</button>
              <input type="number" v-model="quantity" min="1">
              <button @click="increaseQuantity">+</button>
            </div>
          </div>

          <!-- 购买按钮 -->
          <div class="action-buttons">
            <button class="add-to-cart" @click="addToCart">加入购物车</button>
            <button class="buy-now" @click="buyNow">立即购买</button>
          </div>
        </div>
      </div>

      <!-- 商品详情 -->
      <!-- <div class="product-description">
        <div class="description-title">商品详情</div>
        <div class="description-content" v-html="product.description"></div>
      </div> -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted ,computed} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {buyProduct} from '@/api/shop'
import { useStore } from 'vuex';
const isLogin = computed(() => store.state.isLogin);
const store = useStore();
const route = useRoute()
const router = useRouter()
const user = computed(() => store.state.user)
// 商品数据
const product = ref(route.query)

const currentImage = ref(0)
const selectedSpec = ref(null)
const quantity = ref(1)

// 选择规格
const selectSpec = (specId) => {
  selectedSpec.value = specId
}

// 增加数量
const increaseQuantity = () => {
  quantity.value++
}

// 减少数量
const decreaseQuantity = () => {
  if (quantity.value > 1) {
    quantity.value--
  }
}

// 加入购物车
const addToCart = () => {
  if (!selectedSpec.value) {
    alert('请选择规格')
    return
  }
  // TODO: 实现加入购物车逻辑
  console.log('加入购物车', {
    productId: product.value.id,
    specId: selectedSpec.value,
    quantity: quantity.value
  })
}

// 立即购买
const buyNow = () => {
  if(!isLogin.value){
          ElMessage({
        type: 'info',
        message: '请先登录！！',
      })
      return
  }
  // TODO: 实现立即购买逻辑
  console.log('立即购买', {
    productId: product.value.id,
    specId: selectedSpec.value,
    quantity: quantity.value
  })
    ElMessageBox.confirm(
    '确认购买?',
    '创建订单',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      buySuccess();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '创建订单失败',
      })
    })
}
const buySuccess=async()=>{
    try{
      const res=await buyProduct(user.value.id,product.value.id,quantity.value)
      if(res.code==0){
        ElMessage({
        type: 'error',
        message: '库存不足',
      })
      return
      }
      product.value.sale= parseFloat(quantity.value)+parseFloat(product.value.sale)
        ElMessage({
        type: 'error',
        message: '下单成功',
      })
    }catch{
      ElMessage({
        type: 'error',
        message: '创建订单失败',
      })
    }
}
// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {

})
</script>

<style scoped>
.product-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px 0;
}

.detail-container {
  width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
}

.back-button {
  display: inline-flex;
  align-items: center;
  padding: 8px 16px;
  cursor: pointer;
  color: #666;
  margin-bottom: 20px;
}

.back-button:hover {
  color: #ff2c55;
}

.product-content {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
}

.product-gallery {
  width: 500px;
}

.main-image {
  width: 100%;
  height: 500px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.main-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
}

.thumbnail.active {
  border-color: #ff2c55;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.product-price {
  font-size: 28px;
  color: #ff2c55;
  font-weight: bold;
  margin-bottom: 10px;
}

.product-sales {
  font-size: 14px;
  color: #999;
  margin-bottom: 30px;
}

.product-specs {
  margin-bottom: 30px;
}

.spec-title {
  font-size: 16px;
  color: #333;
  margin-bottom: 15px;
}

.spec-options {
  display: flex;
  gap: 10px;
}

.spec-item {
  padding: 8px 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.spec-item:hover {
  border-color: #ff2c55;
}

.spec-item.active {
  border-color: #ff2c55;
  color: #ff2c55;
}

.product-quantity {
  margin-bottom: 30px;
}

.quantity-title {
  font-size: 16px;
  color: #333;
  margin-bottom: 15px;
}

.quantity-control {
  display: flex;
  align-items: center;
  width: fit-content;
}

.quantity-control button {
  width: 36px;
  height: 36px;
  border: 1px solid #ddd;
  background: #fff;
  font-size: 18px;
  cursor: pointer;
}

.quantity-control input {
  width: 60px;
  height: 36px;
  border: 1px solid #ddd;
  border-left: none;
  border-right: none;
  text-align: center;
  font-size: 16px;
}

.action-buttons {
  display: flex;
  gap: 20px;
}

.action-buttons button {
  flex: 1;
  height: 48px;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-to-cart {
  background-color: #fff;
  border: 1px solid #ff2c55 !important;
  color: #ff2c55;
}

.add-to-cart:hover {
  background-color: #fff5f7;
}

.buy-now {
  background-color: #ff2c55;
  color: #fff;
}

.buy-now:hover {
  background-color: #ff1a45;
}

.product-description {
  padding-top: 40px;
  border-top: 1px solid #eee;
}

.description-title {
  font-size: 20px;
  color: #333;
  margin-bottom: 20px;
}

.description-content {
  color: #666;
  line-height: 1.6;
}
</style> 