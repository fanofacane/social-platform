<template>
  <div class="shop-container">
    <!-- 顶部搜索栏 -->
    <div class="search-bar">
      <div class="search-wrapper">

          <input type="text" placeholder="搜索商品" v-model="searchQuery" class="search-input">
          <router-link to="/shop/orders" class="orders-link">
          我的订单
          </router-link>
          </div>
    </div>

    <!-- 分类导航 -->
    <div class="category-nav">
      <div class="category-wrapper">
        <div 
          v-for="category in categories" 
          :key="category.id"
          class="category-item"
          :class="{ active: currentCategory == category.id }"
          @click="selectCategory(category.id)"
        >
          {{ category.name }}
        </div>
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="product-list">
      <div class="product-grid">
        <div 
          v-for="product in productById" 
          :key="product.id"
          class="product-card"
          @click="viewProduct(product)"
        >
          <div class="product-image">
            <img :src="product.picture">
          </div>
          <div class="product-info">
            <div class="product-name">{{ product.title }}</div>
            <div class="product-price">¥{{ product.price }}</div>
            <div class="product-sales">已售{{ product.sale }}件</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {getAllProduct} from'@/api/shop'
import { RouterLink } from 'vue-router';
const router = useRouter()
onMounted(() => {
  // TODO: 初始化加载数据
  selectAllProduct()
})
// 搜索关键词
const searchQuery = ref('')

// 当前选中的分类
const currentCategory = ref(0)
//当前选择的商品
const currentProduct = ref()
// 分类数据
const categories = ref([
  { id: 0, name: '全部' },
  { id: 1, name: '推荐' },
  { id: 2, name: '新品' },
  { id: 3, name: '热卖' },
  { id: 4, name: '服装' },
  { id: 5, name: '美妆' },
  { id: 6, name: '数码' },
  { id: 7, name: '食品' },
  { id: 8, name: '家居' }
])

// 商品数据
const products = ref([])

const productById=ref([])
// 选择分类
const selectCategory = (categoryId) => {
  currentCategory.value=categoryId
  if(categoryId==0){
    productById.value=products.value
  }else{
        // TODO: 根据分类加载商品
  productById.value=products.value.filter(f=>f.categoriesId==categoryId)
  }
}


// 查看商品详情
const viewProduct = (product) => {
  // router.push(`/shop/product/${id}`)
  router.push({path:`/shop/product/${product.id}`,query:product})
}
const selectAllProduct=async()=>{
  const res=await getAllProduct()
  products.value=res.data
  productById.value=res.data
}

</script>

<style scoped>
.shop-container {
    min-height: 100vh;
  background-color: white;
  width: 1200px;
  margin: 0 auto;
}

.search-bar {
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 20px 0;
  background-color: #fff;
}

.search-wrapper {
    width: 100%;
    margin-left: 370px;
}

.search-input {
  width: 450px;
  background-color: #f5f5f5;
  border-radius: 24px;
  padding: 12px 20px;
  transition: all 0.3s ease;
}

.search-input:hover {
  background-color: #e8e8e8;
}

.search-input input {
  flex: 1;
  border: none;
  background: none;
  margin-left: 12px;
  font-size: 16px;
  outline: none;
}
.orders-link {
  margin-left: 80px;
  padding: 10px 15px;
  text-decoration: none;
  border-radius: 15px;
  transition: all 0.3s ease;
  font-weight: bold;
  display: inline-block;
  color: #ff2c55;
    box-shadow: 0 6px 8px rgba(0, 0, 0, 0.05);
}

.orders-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 8px rgba(0, 0, 0, 0.15);
}

.category-nav {
  background-color: #fff;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.category-wrapper {
    width: 100%;
    margin: 0 auto;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 15px;
}

.category-item {
  padding: 8px 24px;
  font-size: 16px;
  color: #666;
  border-radius: 20px;
  background-color: #f5f5f5;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-item:hover {
  background-color: #e8e8e8;
}

.category-item.active {
  color: #fff;
  background-color: #ff2c55;
}

.product-list {
  border-radius: 12px;
  width: 80%;
  background-color: white;
  padding: 30px 140px;

}

.product-grid {

  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.product-card {
  background-color: #fff;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  padding-top: 100%;
  position: relative;
}

.product-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
}

.product-price {
  font-size: 20px;
  color: #ff2c55;
  font-weight: bold;
  margin-bottom: 6px;
}

.product-sales {
  font-size: 14px;
  color: #999;
}

@media (max-width: 1200px) {
  .product-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .product-grid {
    grid-template-columns: repeat(1, 1fr);
  }
  
  .category-wrapper {
    justify-content: flex-start;
    overflow-x: auto;
    padding: 0 20px;
  }
  
  .category-item {
    flex-shrink: 0;
  }
}
</style>
