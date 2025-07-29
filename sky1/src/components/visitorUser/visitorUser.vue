<script setup>
import{ref,onMounted,computed} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import {queryUserApi} from '@/api/user'
import { ElNotification } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Waterfall } from "vue-waterfall-plugin-next";
import "vue-waterfall-plugin-next/dist/style.css";
import { Close} from "@element-plus/icons-vue";
import{querCardByIdApi,queryVisitorUserApi,querVisitorCardByIdApi} from '@/api/user';
import {queryAllCardDetailApi,toggleLikeApi,toggleCollectionApi} from '@/api/index';
import CommentNode from '@/views/follow/CommentNode.vue'
import {getPostCommentsApi,insertCommentApi} from '@/api/comment';
import {followUserApi,checkFollowerApi} from '@/api/follow';
const store = useStore();
const route = useRoute()
const router = useRouter()
const user = ref('') //这是一个用户信息
const list = ref([]);
const newComment=ref()
const selectedItem = ref(null);
const isModalVisible = ref(false);
const isFollowing = ref(false);
const currentUser = computed(() => store.state.user)
const isLogin = computed(() => store.state.isLogin);
onMounted(()=>{
    searchInfo();
    searchCard();
})
const searchInfo =async ()=>{
    const res=await queryVisitorUserApi(route.params.id);
    if(isLogin.value){
        const result=await checkFollowerApi(currentUser.value.id,route.params.id);
        isFollowing.value=result.data;
    }
    user.value=res.data;
}
// 导航项配置
const navItems = ref([
    { text: '作品', path: '', isActive: false },
    { text: '喜欢', path: '', isActive: false },
    { text: '收藏', path: '', isActive: false },
    { text: '观看历史', path: '', isActive: false },
    { text: '我的预约', path: '', isActive: false }
])
const open1 = () => {
    ElNotification({
    title: '抱歉',
    message: '作者隐私,暂时无法查看',
    type: 'error',
    })
}

// 自动匹配当前路由激活状态
// const isRouteActive = (path) => {
//     return route.path == path
// }

// 点击处理函数
const selectItem = (item) => {
        if(item.text!='作品'){
            open1();
        }
}

const searchCard = async () => {
  const res = await querVisitorCardByIdApi(route.params.id);
  if(res.code){
    list.value = res.data;
  }
}
// 点击卡片事件
const showDetail = async (item) => {
  const res = await queryAllCardDetailApi(item.id,currentUser.value.id);
  if(res.code){
    selectedItem.value = res.data;
  }
  if(store.state.isLogin==true){
    const result=await checkFollowerApi(currentUser.value.id,selectedItem.value.authorDetail.id);
    isFollowing.value=result.data
  }
  isModalVisible.value = true;
  document.body.style.overflow = 'hidden'; // 禁止滚动
}
const toggleLike =async (post) => {
  const res=await toggleLikeApi(currentUser.value.id,post.id)
  if(post.isLike){
    post.likesCount-=1
    user.value.likeCount-=1
  }else{
    post.likesCount+=1
    user.value.likeCount+=1
  }
  post.isLike=!post.isLike
}
const toggleCollection =async (selectedItem) => {
  if(selectedItem.isCollection){
    selectedItem.isCollection=false
    selectedItem.collectionsCount-=1
  }else{
    selectedItem.isCollection=true
    selectedItem.collectionsCount+=1
  }
  await toggleCollectionApi(user.value.id,selectedItem.id)
}
const sendMessage =(id)=>{
      if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
    router.push(`/chat/${id}`)
}
const closeModal = () => {  
      isModalVisible.value = false;  
      document.body.style.overflow = 'auto'; // 恢复滚动
    };
const postComment =async (post) => {
  if (!newComment.value.trim()) return;
    const res=await insertCommentApi(post.id,newComment.value)
    post.commentsList.push(res.data)
    newComment.value = ''
}
const toggleFollow = async () => {
  if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
  try {
    if(isFollowing.value==true){
      isFollowing.value=false;
      user.value.followerCount-=1
    }else{
      isFollowing.value=true;
      user.value.followerCount+=1
    }
    await followUserApi(currentUser.value.id,route.params.id);
  } catch (error) {
    console.error('操作失败:', error);
    // 这里可以添加错误提示逻辑
  } finally {
    console.log(isFollowing.value)
  }
};
const pushUser=(id)=>{
  if(user.value.id==id){
    router.push(`/user`)
  }else{
    router.push(`/visitor/${id}`)
  }
}
</script>

<template>
    <el-container>
        <el-header height="230px">
    <div class="profile-header">
    <!-- 用户信息区域 -->
    <div class="user-info-container">
        <!-- 头像 -->
        <div class="avatar-wrapper">
            <img v-if="user.picture" :src="user.picture" class="user-avatar" alt="用户头像">
            <img v-else src="https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/88ad0b02-dc41-4e55-88b6-ae5c00f0d18c.jpg" class="user-avatar" alt="用户头像">
        </div>
        <!-- 文字信息 -->
        <div class="info-section">
            <!-- 昵称和操作按钮 -->
            <div class="name-row">
                <h1 class="nickname">{{user.username}}</h1>
                <el-button type="danger" size="small" round  @click="toggleFollow" :class="{ 'following': isFollowing }" >
                      {{isFollowing?'取关':'关注'}}
                </el-button>
            </div>
            
            <!-- 数据统计 -->
            <div class="stats-row">
                <span class="stat-item">关注 <em>{{user.followingCount}}</em></span>
                <span class="stat-item">粉丝 <em>{{user.followerCount}}</em></span>
                <span class="stat-item">获赞 <em>{{user.likeCount}}</em></span>
            </div>
            
            <!-- 账号信息 -->
            <div class="account-info">
                <span class="source-info">{{user.bio}}</span>
                <span class="ageinfo">
                <svg width="12" height="12" fill="none" xmlns="http://www.w3.org/2000/svg" class="" 
                viewBox="0 0 12 12" style="margin-right: 4px;"><path fill-rule="evenodd" 
                clip-rule="evenodd" d="M8 1.25a.75.75 0 0 0 0 1.5h1.09L7.54 4.298a.757.757
                    0 0 0-.058.066 4 4 0 1 0 .968 1.112.752.752 0 0 0 .15-.117L10.25 3.71V5a.75.75
                    0 0 0 1.5 0V2a.75.75 0 0 0-.75-.75H8zM5 10a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 
                    5z" fill="#168EF9"></path></svg>
                {{user.age}}岁</span>
                <span class="ipinfo">IP:{{user.ip}}</span>
                
                
            </div>
            <div class="account-dio">
                <button class="modern-btn" @click="sendMessage(user.id)">私信</button>
            </div>

        </div>
    </div>    
</div>


<div class="nav-container">
    <div v-for="(item, index) in navItems" :key="index" class="nav-item" @click="selectItem(item)">
        <div class="nav-link">{{ item.text }}</div>
        <div class="indicator" ></div>
    </div>
</div>

            </el-header>
        <el-main>
                <div class="feed-container">
        <Waterfall :list="list" :width="300" :hasAroundGutter="false" max-height="300px">
        <template #item="{item}">
            <div class="card" >
                <img v-if="item.fileType?.startsWith('image/')" :src="item.pictureUrl" style="border-radius: 8px; width: 100%; height: 100%;" @click="showDetail(item)" />
                <video v-if="item.fileType?.startsWith('video/')" :src="item.pictureUrl" style="border-radius: 8px; width: 100%; height: 100%;" @click="showDetail(item)"></video>
            <div class="footer">
                <a class="title"><span>{{item.title}}</span></a>
                <div class="author-wrapper">
                <a class="author">
                    <img class="author-avatar" :src="item.picture" />
                    <span class="name">{{item.author}}</span>
                </a>
                <div :class="{ 'liked': item.isLike }" @click="toggleLike(item)" class="b-like-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 18 18" width="18" height="18" style="width: 18px; height: 18px;"><path d="M10.4511 2.2220125C10.218425 2.194885 10.002175 2.2953725 9.884175 2.433395C9.4264 2.9688525 9.321875 3.7501399999999996 8.978575 4.3581725C8.533574999999999 5.146395 8.1198 5.6213375 7.609775000000001 6.068507499999999C7.1751375 6.449565 6.738407499999999 6.697442499999999 6.3125 6.8050575L6.3125 14.854575C6.9198900000000005 
                    14.868174999999999 7.572900000000001 14.876875 8.25 14.876875C9.936425 14.876875 11.367025 
                    14.823325 12.33115 14.773699999999998C13.03235 14.737575 13.646025000000002 
                    14.390075 13.966750000000001 13.81945C14.401900000000001 13.04535 14.9387 11.909650000000001 15.264174999999998 10.571200000000001C15.56665 9.327275 15.704699999999999 8.304325 15.766675 7.582224999999999C15.7988 7.208262500000001 15.50165 6.875019999999999 15.059999999999999 6.875019999999999L11.323274999999999 6.875019999999999C11.156575 6.875019999999999 11.000800000000002 6.791952499999999 10.907975 6.653499999999999C10.783725 6.468192500000001 10.82855 6.2670175 10.9037 6.07485C11.059 5.675084999999999 11.29355 4.9974475 11.382425000000001 4.4018275C11.470875000000001 3.80917 11.450999999999999 3.32219 11.212050000000001 2.86913C10.9571 2.3857825 10.66065 2.2464475 10.4511 2.2220125zM12.034300000000002 5.87502L15.059999999999999 5.87502C16.02035 5.87502 16.850875 6.64489 16.763 7.667825C16.697100000000002 8.435525 16.55155 9.5092 16.235825000000002 10.807500000000001C15.882625 12.259950000000002 15.3035 13.482225 14.838450000000002 14.309474999999999C14.32695 15.2194 13.377475 15.721150000000002 12.38255 15.772375C11.405125 15.822725 9.956949999999999 15.876875000000002 8.25 15.876875000000002C6.5961925 15.876875000000002 5.0846825 15.826025000000001 4.0136674999999995 15.77715C2.8370825 15.723474999999999 1.8519999999999999 14.850000000000001 1.725645 13.654824999999999C1.6404649999999998 12.849274999999999 1.5625 11.80725 1.5625 10.689375C1.5625 9.665175000000001 1.6279400000000002 8.736175 1.7045524999999997 7.998975C1.8351224999999998 6.7427075 2.9137075 5.87502 4.130655 5.87502L5.8125 5.87502C6.072015 5.87502 6.457235 5.7490675 6.9505175 5.316582499999999C7.377705000000001 4.942045 7.7193000000000005 4.5546075 8.107775 3.8665374999999997C8.492075 3.18585 8.605825 2.389785 9.124075 1.783595C9.452975 1.3988800000000001 9.99475 1.162025 10.5669 1.228745C11.16225 1.29816 11.717425 1.683875 12.09655 2.4025825000000003C12.478275 3.1262375000000002 12.474075 3.8618225 12.371500000000001 4.54938C12.302149999999997 5.0139949999999995 12.155425000000001 5.510059999999999 12.034300000000002 5.87502zM5.3125 14.82705L5.3125 6.875019999999999L4.130655 6.875019999999999C3.3792199999999997 6.875019999999999 2.77211 7.400795 2.6991975000000004 8.10235C2.6253525 8.812875 2.5625 9.70665 2.5625 10.689375C2.5625 11.762875 2.6374975 12.768475 2.7200975 13.549700000000001C2.7919925 14.229675 3.3521950000000005 14.74595 4.05924 14.778224999999999C4.4278775 14.795 4.849985 14.812050000000001 5.3125 14.82705z" fill="currentColor">
                    </path></svg>
                    <span class="count">{{ item.likesCount }}</span>
                </div>
                </div>
            </div>
            </div>
        </template>
        </Waterfall>
    </div>

    <div v-if="isModalVisible" class="modal-overlay" @click.self="closeModal">
      <div class="modal" v-if="selectedItem" >
        <div class="note-detail-mask" style="transition: background-color 0.4s ease 0s;">
    <div class="note-container">
      <div class="media-container">
        <el-carousel height="90vh">
          <el-carousel-item>
            <img v-if="selectedItem.fileType?.startsWith('image/')" class="videoStyle" :src="selectedItem.pictureUrl" fit="cover" />
            <video v-if="selectedItem.fileType?.startsWith('video/')" class="videoStyle" :src="selectedItem.pictureUrl" fit="cover" controls></video>
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="interaction-container">
        <div class="author-container">
          <div class="author-me">
            <div class="info">
              <img class="avatar-item"  style="width: 40px; height: 40px"  :src="selectedItem.authorDetail.picture"/>
              <span class="name">{{selectedItem.authorDetail.username}}</span>
            </div>
            <div class="follow-btn">
              <el-button type="danger" size="large" round  @click="toggleFollow" :class="{ 'following': isFollowing }">
              {{isFollowing?'取关':'关注'}}
            </el-button>
            </div>

          </div>
            <div class="note-content">
              <div class="title">{{ selectedItem.title }}</div>
              <div class="bottom-container">
                <span class="date">{{selectedItem.createTime}}</span>
              </div>
            </div>
          <div class="note-scroller"> 

            <div class="divider interaction-divider"></div>

            <!-- 评论 -->
          
            <div class="comments-el">
                <div class="comments-container">
                <CommentNode
                v-for="comment in selectedItem.commentsList"
                :key="comment.id"
                :comment="comment"
                :depth="0"
                @reply="handleReply"
                />
                </div>
            </div>
            <!-- "interactions-footer" -->

        </div> 
        <div class="interactions-footer">
            <div class="buttons">
              <div class="left">
                <div class="collect-wrapper" @click="toggleCollection(selectedItem)" :class="{ 'collectioned': selectedItem.isCollection }">
                  <span class="like-lottie"><svg class="star-icon" :class="{ 'active': isFilled }"width="13px" height="13px" viewBox="0 0 24 24">
              <path  fill="currentColor" d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21L12 17.77L5.82 21L7 14.14L2 9.27L8.91 8.26L12 2Z" /></svg> </span>
                  <span class="count">{{ selectedItem.collectionsCount }}</span>
                </div>
                <span class="chat-wrapper">
                  <span class="like-lottie">
                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 18 18" width="18" height="18" style="width: 13px; height: 13px;"><path d="M9.789075 2.2956175C8.97235 1.6308450000000003 7.74999 2.212005 7.74999 3.26506L7.74999 5.3915500000000005C6.642015000000001 5.5780325 5.3073725 6.040405 4.141735000000001 7.11143C2.809155 8.335825 1.751515 10.3041 1.45716 13.404099999999998C1.409905 13.9018 1.7595399999999999 14.22505 2.105415 14.317499999999999C2.442215 14.40755 2.8807175 14.314625 3.127745 13.92915C3.9664525 12.620249999999999 4.89282 11.894575 5.765827499999999 11.50585C6.4628049999999995 11.19545 7.14528 11.093125 7.74999 11.0959L7.74999 13.235025C7.74999 14.2881 8.97235 14.869250000000001 9.789075 14.2045L15.556199999999999 9.510425000000001C16.355075 8.860149999999999 16.355075 7.640124999999999 15.556199999999999 6.989840000000001L9.789075 2.2956175zM9.165099999999999 3.0768275000000003L14.895025 7.739050000000001C15.227975 7.980475 15.235775 8.468875 14.943874999999998 8.7142L9.17615 13.416800000000002C8.979474999999999 13.562024999999998 8.75 13.4269 8.75 13.227375000000002L8.75 10.638175C8.75 10.326975000000001 8.542125 10.134725 8.2544 10.1118C7.186765 10.02955 6.1563175 10.2037 5.150895 10.69295C4.14982 11.186925 3.2102250000000003 12.096525 2.573625 13.00995C2.54981 13.046975 2.52013 13.046025 2.5211725 12.986C2.8971525 10.0573 3.9373475 8.652125 4.807025 7.85305C5.87747 6.8694775 7.213197500000001 6.444867500000001 8.2272 6.33056C8.606525 6.287802500000001 8.74805 6.0849325 8.74805 5.7032275L8.74805 3.2615475C8.74805 3.0764875000000007 8.993175 2.9321925 9.165099999999999 3.0768275000000003z" fill="currentColor"></path></svg>
                  </span></span>
              </div>
              <div class="share-wrapper"></div>
            </div>
            <div class="comment-wrapper active comment-comp">
              <div class="input-wrapper">
                <input class="comment-input" type="text" v-model="newComment" placeholder="想说点什么呢！！" />
                <div class="input-buttons">
                  <Close style="width: 1.2em; height: 1.2em" />
                </div>
              </div>
              <button class="submit" @click="postComment(selectedItem)">发送</button>
            </div>
          
        </div>
    </div>
    
    </div>


  </div>
      </div>
      </div>
</div>

        </el-main>
    </el-container>

</template>

<style>
.profile-header {
    max-width: 1152px;
    height: 150px;
    margin-left: 10px;
    padding: 24px 32px;
    background: #fff;
    overflow: hidden;
}

.user-info-container {
    display: flex;
    gap: 30px;
}

.avatar-wrapper {
    flex: 0 0 96px;
    height: 100px;
}

.user-avatar {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    border: 2px solid #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    margin-top: 10px;
}

.info-section {
    flex: 1;
    margin-top: 20px;
}

.name-row {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
}

.nickname {
    font-size: 20px;
    margin-right: 20px;
}

.action-buttons {
    display: flex;
    gap: 12px;
}

.follow-btn-myBar {
    background: #fe2c55;
    color: #fff;
    border: none;
    padding: 6px 24px;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
}

.more-actions {
    padding: 6px;
    cursor: pointer;
}

.stats-row {
    display: flex;
    gap: 20px;
    margin-bottom: 12px;
}

.stat-item {
    font-size: 14px;
    color: #666;
}

.stat-item em {
    font-style: normal;
    color: #000;
    font-weight: 500;
    margin-left: 4px;
}

.account-info {
    font-size: 14px;
    color: #666;
    margin-bottom: 12px;
    margin-top: 20px;
}

.douyin-id {
    margin-right: 16px;
}

.account-dio{
    display: flex;
    margin-bottom: 12px;
    font-size: 13px;
    color: #666;
}

.modern-btn {
  margin-left: auto;
  /* 基础样式 */
  padding: 1em 2em;
  font-size: 10px;
  font-weight: 600;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.2s ease-in-out;
  /* 渐变背景 */
  background: linear-gradient(135deg, #6366f1 0%, #a855f7 50%, #ec4899 100%);
  color: white;
  
  /* 3D 阴影效果 */
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1),
              0 2px 4px -1px rgba(0, 0, 0, 0.06),
              inset 0 2px 4px 0 rgba(255, 255, 255, 0.1);
}

/* 悬停效果 */
.modern-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1),
              0 4px 6px -2px rgba(0, 0, 0, 0.05);
  filter: brightness(110%);
}

/* 点击效果 */
.modern-btn:active {
  transform: translateY(1px);
  filter: brightness(90%);
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

/* 聚焦状态 */
.modern-btn:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
}

/* 可选 - 添加加载动画 */
.modern-btn.loading::after {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.4),
    transparent
  );
  animation: loading 1.5s infinite;
}

@keyframes loading {
  100% {
    left: 100%;
  }
}
.live-status {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #fe2c55;
    font-size: 14px;
}

.live-tag {
    background: #ffe7eb;
    padding: 2px 6px;
    border-radius: 2px;
}

.toolbar {
    display: flex;
    gap: 24px;
    margin-top: 20px;
}

.tool-item {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    color: #666;
    cursor: pointer;
}

.tool-icon {
    width: 18px;
    height: 18px;
}
.route-nav-container {
    max-width: 1152px;
    margin: 0 auto;
    border-bottom: 1px solid #eee;
}

.route-nav {
    display: flex;
    gap: 40px;
    padding: 0 32px;
}

.nav-item {
    position: relative;
    padding: 16px 0;
    font-size: 16px;
    color: #666;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 6px;
}

.nav-item.active {
    color: #161823;
    font-weight: 500;
}

.nav-item.active::after {
    content: "";
    position: absolute;
    bottom: -1px;
    left: 0;
    right: 0;
    height: 3px;
    background: #fe2c55;
}

.count {
    font-size: 14px;
    color: #999;
}

.nav-item.active .count {
    color: #666;
}

/* 悬停效果 */
.nav-item:hover {
    color: #161823;
}
.right-buttons{
    margin-left: 600px;
}
.custom-link{
    text-decoration: none;
} 

.nav-container {
  display: flex;
  gap: 20px;
}
.nav-item {
  position: relative;
  cursor: pointer;
  padding: 12px 16px;
}

.nav-link {
  transition: color 0.3s;
}

.indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 3px;
  background-color: #ff0000;
  transition: width 0.3s ease;
}

.indicator.active {
  width: calc(100% - 32px); /* 根据实际间距调整 */
}

/* 可选：添加hover效果 */
.nav-item:hover .nav-link {
  color: #ff4444;
}
.ageinfo{
    padding-right: 10px;
    padding-left: 10px;
}

.userForm-avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    margin-left: 85px;
}
.authorEdit{
    margin-left: 100px;
    font-size: 11px;
}

.feed-container {
    position: relative;
    transition: width 0.5s;

    .footer {
      padding: 12px;
      .title {
        margin-bottom: 8px;
        word-break: break-all;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2; 
        overflow: hidden;
        font-weight: 500;
        font-size: 14px;
        line-height: 140%;
        color: #333;
      }

      .author-wrapper {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 20px;
        color: rgba(51, 51, 51, 0.8);
        font-size: 12px;
        transition: color 1s;

        .author {
          display: flex;
          align-items: center;
          color: inherit;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-right: 12px;

          .author-avatar {
            margin-right: 6px;
            width: 20px;
            height: 20px;
            border-radius: 20px;
            border: 1px solid rgba(0, 0, 0, 0.08);
            flex-shrink: 0;
          }

          .name {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        .like-wrapper {
          position: relative;
          cursor: pointer;
          display: flex;
          align-items: center;

          .count {
            margin-left: 2px;
          }
        }
      }
    }
  }
.top {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 100vw;
    height: 72px;
    position: fixed;
    top: 1px;
    margin-top: 1px;
    /* z-index: 10; */
    align-items: center;
    background: #fff;

    header {
      position: relative;
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      max-width: 1728px;
      height: 72px;
      padding: 0 16px 0 24px;
      z-index: 10;

      .tool-box {
        width: 24px;
        height: 70px;
        position: absolute;
        left: 0;
        top: 0;
      }

      .input-box {
        margin-top: 50px;
        height: 40px;
        position: fixed;
        left: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-left: 85px;
        
        transform: translate(-50%);

        @media screen and (max-width: 695px) {
          display: none;
        }

        @media screen and (min-width: 960px) and (max-width: 1191px) {
          width: calc(-36px + 50vw);
        }

        @media screen and (min-width: 1192px) and (max-width: 1423px) {
          width: calc(-33.6px + 40vw);
        }

        @media screen and (min-width: 1424px) and (max-width: 1727px) {
          width: calc(-42.66667px + 33.33333vw);
        }

        @media screen and (min-width: 1728px) {
          width: 533.33333px;
        }
        .search-input {
          
          padding: 0 84px 0 16px;
          width: 80%;
          display: flex;
          height: 100%;
          font-size: 16px;
          line-height: 120%;
          color: #333;
          caret-color: #ff2442;
          background: rgba(0, 0, 0, 0.03);
          border-radius: 999px;
        }

        .input-button {
          position: absolute;
          right: 0;
          top: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
          color: rgba(51, 51, 51, 0.8);

          .close-icon .search-icon {
            width: 40px;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            color: rgba(51, 51, 51, 0.8);
          }
        }
      }
    }
  }
  .modal-overlay {  
  position: fixed;  
  top: 0;  
  left: 0;  
  width: 100%;  
  height: 100%;  
  background: rgba(0, 0, 0, 0.5);  
  display: flex;  
  justify-content: center;  
  align-items: center;  
}
.modal {  
  background: white;  
  padding: 20px;  
  border-radius: 8px;  
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);  
}  
/* 动画效果 */  
.fade-enter-active, .fade-leave-active {  
  transition: opacity 0.3s;  
}  
.fade-enter, .fade-leave-to {  
  opacity: 0;  
} 
  .note-detail-mask {
  position: fixed;
  left: 190px;
  top: 50px;
  display: flex;
  width: 100vw;
  height: 100vh;
  z-index: 20;
  overflow: auto;

  .close-cricle {
    left: 1.3vw;
    top: 1.3vw;
    position: fixed;
    display: flex;
    z-index: 100;
    cursor: pointer;

    .close-mask-white {
      box-shadow:
        0 2px 8px 0 rgba(0, 0, 0, 0.04),
        0 1px 2px 0 rgba(0, 0, 0, 0.02);
      border: 1px solid rgba(0, 0, 0, 0.08);
    }

    .close {
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 100%;
      width: 40px;
      height: 40px;
      border-radius: 40px;
      cursor: pointer;
      transition: all 0.3s;
    }
  }

  .note-container {
    width: 70%;

    height: 85%;
    transition:
      transform 0.4s ease 0s,
      width 0.4s ease 0s;
    
    transform: translate(88px, 28px) scale(0.88);
    overflow: visible;

    display: flex;
    box-shadow:
      0 8px 64px 0 rgba(0, 0, 0, 0.04),
      0 1px 4px 0 rgba(0, 0, 0, 0.02);
    border-radius: 20px;
    transform-origin: left top;

    .media-container {
      width: 65%!important;
      height: auto;
      min-width: 380px;
      position: relative;
      background: rgba(0, 0, 0, 0.03);
      flex-shrink: 0;
      flex-grow: 0;
      -webkit-user-select: none;
      user-select: none;
      overflow: hidden;
      border-radius: 20px 0 0 20px;
      transform: translateZ(0);
      height: 100%;
      object-fit: contain;
    }

    .interaction-container {
      width: 32%;
      flex-shrink: 0;
      border-radius: 0 20px 20px 0;
      position: relative;
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      height: 100%;
      background-color: #fff;
      overflow: hidden; 
      border-left: 1px solid rgba(0, 0, 0, 0.08);

      .author-me {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 40%;
        padding: 24px;
        border-bottom: 1px solid transparent;
        .follow-btn{
          display: flex;
          
        }
        .info {
          display: flex;
          align-items: center;

          .avatar-item {
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            border-radius: 100%;
            border: 1px solid rgba(0, 0, 0, 0.08);
            object-fit: cover;
          }

          .name {
            padding-left: 12px;
            height: 40px;
            display: flex;
            align-items: center;
            font-size: 16px;
            color: rgba(51, 51, 51, 0.8);
          }
        }
      }

      .note-scroller::-webkit-scrollbar {
        display: none;
      }

      .note-content {
	          /* padding: 0 24px 24px; */
          color: var(--color-primary-label);
          position: sticky;  /* 关键属性 */
          top: 0;            /* 触发粘性的阈值位置 */
            background: rgb(255, 255, 255);
          padding: 10px;
            z-index: 1000;
          .title {
            margin-bottom: 8px;
            font-weight: 600;
            font-size: 18px;
            line-height: 140%;
	}
  .desc {
            margin: 0;
            font-weight: 400;
            font-size: 16px;
            line-height: 150%;
            color: #000508;
            white-space: pre-wrap;
            overflow-wrap: break-word;

            .tag-bubble {
             display: inline-block;
             padding: 5px 5px;
             margin: 0px;
             /* background: #31b1f7;
             border-radius: 20px; */
             color: #0e29f1;
             position: relative;
             transition: transform 0.2s ease;
  }

            .tag {
              margin-right: 2px;
              color: #13386c;
            }
          }
}

        .note-scroller {
	        transition: scroll 0.4s;
        overflow-y: scroll;
        flex-grow: 1;
        height: 40vh;
          


          .bottom-container {
            display: flex;
            justify-content: space-between;
            margin-left: 300px;
            margin-top: 12px;

            .date {
              font-size: 14px;
              line-height: 120%;
              color: rgba(51, 51, 51, 0.6);
            }
          }
        }

        .interaction-divider {
          margin: 0 24px;
        }
        .divider {
          margin: 4px 8px;
          list-style: none;
          height: 0;
          border: solid rgba(0, 0, 0, 0.08);
          border-width: 1px 0 0;
        }
        .comments-el {
          position: relative;

          .comments-container {
            padding: 10px;

            .total {
              font-size: 14px;
              color: rgba(51, 51, 51, 0.6);
              margin-left: 8px;
              margin-bottom: 30px;
            }

            .list-container {
              position: relative;

              .parent-comment {
                margin-bottom: 16px;

                .comment-item {
                  position: relative;
                  display: flex;
                  padding: 8px;

                  .comment-inner-container {
                    position: relative;
                    display: flex;
                    z-index: 1;
                    width: 100%;
                    flex-shrink: 0;

                    .avatar {
                      flex: 0 0 auto;

                      .avatar-item {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        border-radius: 100%;
                        border: 1px solid rgba(0, 0, 0, 0.08);
                        object-fit: cover;
                        width: 40px;
                        height: 40px;
                      }
                    }

                    .right {
                      margin-left: 12px;
                      display: flex;
                      flex-direction: column;
                      font-size: 14px;
                      flex-grow: 1;

                      .author-wrapper {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;

                        .author {
                          display: flex;
                          align-items: center;
                          .name {
                            color: rgba(51, 51, 51, 0.6);
                            line-height: 18px;
                          }
                        }
                      }

                      .content {
                        margin-top: 4px;
                        line-height: 10%;
                        color: #333;
                      }

                      .info {
                        display: flex;
                        flex-direction: column;
                        justify-content: space-between;
                        font-size: 12px;
                        line-height: 16px;
                        color: rgba(51, 51, 51, 0.6);

                        .date {
                          margin: 8px 0;
                        }
                        .interactions {
                          display: flex;
                          margin-left: -2px;

                          .like-wrapper {
                            padding: 0 4px;
                            color: rgba(51, 51, 51, 0.8);
                            font-weight: 500;

                            position: relative;
                            cursor: pointer;
                            display: flex;
                            align-items: center;

                            .like-lottie {
                              width: 16px;
                              height: 16px;
                              left: 4px;
                            }

                            .count {
                              margin-left: 2px;
                              font-weight: 500;
                            }
                          }
                        }
                      }
                    }
                  }
                }

                .reply-container {
                  margin-left: 52px;

                  .show-more {
                    margin-left: 44px;
                    height: 32px;
                    line-height: 31px;
                    color: #13386c;
                    cursor: pointer;
                    font-weight: 500;
                    font-size: 14px;
                  }
                }
              }
            }
          }
        }

        .interactions-footer {
          margin-top: 0px;

        .buttons {
          display: flex;
          justify-content: space-between;
          margin-left: 275px;
          .count {
            margin-left: 5px;
            margin-right: 12px;
            font-weight: 500;
            font-size: 12px;
          }

          .left {
            display: flex;
            margin-left: 35px;
            .like-wrapper {
              position: relative;
              cursor: pointer;
              display: flex;
              justify-content: left;
              color: rgba(51, 51, 51, 0.6);
              margin-right: 5px;
              align-items: center;
              .like-lottie {
                transform: scale(1.7);
              }
            }

            .collect-wrapper {
              position: relative;
              cursor: pointer;
              display: flex;
              color: rgba(51, 51, 51, 0.6);
              margin-right: 5px;
              align-items: center;
              .like-lottie {
                transform: scale(1.7);
              }
            }
            .collectioned{
                color: #0881f3;
              }

            .chat-wrapper {
              cursor: pointer;
              color: rgba(51, 51, 51, 0.6);
              display: flex;
              align-items: center;
              .like-lottie {
                transform: scale(1.7);
              }
            }
          }
        }

        .comment-wrapper {
          &.active {
            .input-wrapper {
              flex-shrink: 1;
            }
          }
        }

        .comment-wrapper {
          display: flex;
          font-size: 16px;
          overflow: hidden;

          .input-wrapper {
            display: flex;
            position: relative;
            width: 80%;
            flex-shrink: 0;
            transition: flex 0.3s;

            .comment-input:placeholder-shown {
              background-image: none;
              padding: 12px 92px 12px 30px;
              
              background-repeat: no-repeat;
              background-size: 16px 16px;
              background-position: 16px 12px;
              color: rgba(51, 51, 51, 0.3);
            }

            .comment-input {
              padding: 12px 92px 12px 16px;
              width: 220px;
              height: 20px;
              line-height: 16px;
              background: rgba(0, 0, 0, 0.03);
              caret-color: rgba(51, 51, 51, 0.3);
              border-radius: 22px;  
              border: none;
              outline: none;
              resize: none;
              color: #333;
            }

            .input-buttons {
              position: absolute;
              right: -28px;
              top: 0;
              height: 40px;
              display: flex;
              align-items: center;
              justify-content: center;
              width: 92px;
              color: rgba(51, 51, 51, 0.3);
            }
          }

          .submit {
            margin-left: 8px;
            width: 60px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            flex-shrink: 0;
            background: #3d8af5;
            border-radius: 44px;
            font-size: 16px;
          }
        }

        .comment-comp {
          margin-top: 20px;
        }
      }
      }
    }
  }

  .b-like-icon {
  /* 基础样式 */
  cursor: pointer;
  transition: color 0.2s ease-in-out;
}

/* 点赞状态样式 */
.b-like-icon.liked {
  color: #00B5E5; /* B站主题蓝色 */
}


/* 悬停效果 */
.b-like-icon:hover:not(.liked) {
  color: #555;
}

/* 点击动画 */
.b-like-icon:active {
  transform: scale(0.95);
}

/* 在全局样式中添加 */
.card {
  width: 300px !important;; /* 与Waterfall组件配置的width一致 */
  margin-bottom: 70px; /* 留出间距 */
  height: 300px !important; /* 强制固定高度 */
}
.card img {
  aspect-ratio: 1/1; /* 正方形比例 */
  object-fit: cover; /* 裁剪适配 */
}
.videoStyle{
  width: 100%;
  height: 100%;
  background-color: #000508;
}
</style>