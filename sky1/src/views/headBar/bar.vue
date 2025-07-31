<template>
    <nav class="tiktok-navbar">
      <!-- 左侧占位 -->
      <div class="nav-left"></div>
  
      <!-- 右侧导航区 -->
      <div class="nav-right">
        <!-- 常规导航项 -->
        
  <!-- 通知按钮 -->
  <!-- 私信按钮 -->

  <!-- <div class="nav-item notification-container"
            @mouseenter="activeDropdown = 'notification'"
            @mouseleave="closeDropdowns">
        
        <el-icon class="icon-notification" :size="25" color="#000000"><Bell /></el-icon>
        <div class="notification-badge">3</div>
        <transition name="slide-fade">
          <div v-show="activeDropdown === 'notification'" class="dropdown-menu notification-dropdown">
            <div class="menu-header">互动消息</div>
            <div class="notification-list">
              <div v-for="(item, index) in notifications" :key="index" class="notification-item">
                <img :src="item.avatar" class="notify-avatar">
                <div class="notify-content">
                  <div class="notify-text">
                    <span class="username">{{ item.username }}</span>
                    {{ item.action }}
                    <span class="extra">{{ item.extra }}</span>
                  </div>
                  <div class="notify-time">{{ item.time }}</div>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
  
      
      <div class="nav-item message-container"
            @mouseenter="activeDropdown = 'message'"
            @mouseleave="closeDropdowns">
            <el-icon :size="25" color="#000000"><ChatSquare /></el-icon>
        <div class="message-badge">2</div>
        <transition name="slide-fade">
          <div v-show="activeDropdown === 'message'" class="dropdown-menu message-dropdown">
            <div class="menu-header">私信</div>
            <div class="message-list">
              <div v-for="(msg, index) in messages" :key="index" class="message-item">
                <img :src="msg.avatar" class="msg-avatar">
                <div class="msg-content">
                  <div class="msg-header">
                    <span class="username">{{ msg.username }}</span>
                    <span class="msg-time">{{ msg.time }}</span>
                  </div>
                  <div class="msg-preview">
                    {{ msg.content }}
                    <span v-if="msg.unread" class="unread-dot"></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div> -->
  
        <!-- 投稿按钮 -->
        <div class="upload-btn">
          <el-icon :size="25" color="#000000" @click="upload"><CirclePlus /></el-icon>
        </div>
  
        <!-- 用户头像及下拉菜单 -->
        <div class="avatar-container"
              @mouseenter="handleAvatarEnter"
              @mouseleave="handleAvatarLeave">
                <div v-if="!store.state.isLogin" @click="dialogVisible=true">
                        <img class="user-avatar"  src="http://47.96.156.182:9000/origin/sky/3e9ce3813b7199ea9588eeb920f41208_512_512.jpg" />
                </div>
                <div v-else>
                        <img :src="user.picture"   class="user-avatar" >
                </div>
          <!-- 用户菜单 -->
          <transition name="menu-fade">
            <div v-show="showUserMenu"   class="user-menu"  @mouseenter="cancelClose"  @mouseleave="triggerClose">
              <!-- 用户信息 -->
              <div class="user-profile" v-if="!store.state.isLogin">
                  <img src="http://47.96.156.182:9000/origin/sky/3e9ce3813b7199ea9588eeb920f41208_512_512.jpg" class="profile-avatar">
                  <div class="profile-info">
                  <div class="username">{{'请先登录哦！！'}}
                  <div class="stats">
                    <span>{{ '好久没见,快和你的朋友聊聊天吧！！' }}</span>
                  </div>
                </div>
              </div>
              </div>
              <div class="user-profile" v-else>
                <img :src="user.picture"   class="profile-avatar">
              <div class="profile-info">
                  <div class="username">{{user.username}}
                  <div class="stats">
                    <span>关注:</span>{{ userDetail.followingCount }}
                    <span>粉丝:</span>{{ userDetail.followerCount }}
                    <span>获赞:</span>{{ userDetail.likeCount }}
                  </div>
                </div>
              </div>
              </div>
              <!-- 菜单项 -->
              <div class="menu-list">
              <div class="menu-item"
                    v-for="item in menuItems"
                    :key="item.id"
                    @mouseenter="handleSubmenuEnter(item)"
                    @mouseleave="handleSubmenuLeave">
  
                  <div class="menu-extra">
                <i :class="item.icon"></i>
                <span class="menu-laber">{{ item.label }}</span>
                  <em v-if="item.count" class="menu-count">{{ item.count }}</em>
                  <i v-if="item.hasSubmenu" class="arrow"></i>
                </div>
  
                <!-- 二级作品菜单 -->
                <transition name="submenu-fade">
                  <div v-show="activeSubmenu === item.id" 
                        class="works-submenu">
                    <div class="works-grid">
                      <div v-for="(work, index) in userData[item.id]" 
                            :key="work.id"
                            class="work-item">
                            
                        <div class="work-cover">
                          <video :src="work.pictureUrl"></video>
                          <div class="work-info">
                            <div class="title">{{ work.title }}</div>
                            <div class="stats">
                              <i class="icon-like-small"></i>
                              <span>点赞数:</span>
                              {{ work.likesCount }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </transition>
              </div>
              </div>
  
              <!-- 退出登录 -->
              <!-- <div class="menu-item logout">
                <i class="icon-logout"></i>
                <span>退出登录</span>
              </div> -->
            </div>
          </transition>
        </div>
      </div>
    </nav>
    <el-main>
        <el-dialog class="custom"  v-model="dialogVisible" :close-on-click-modal="false" destroy-on-close>
          <index @loginSuccess="dialogVisible = false;"></index>
        </el-dialog>
        <el-dialog v-model="dialogShow" class="custom-dialog" align-center>
                <publicPostForm @publicSuccess="dialogShow = false;"></publicPostForm>
        </el-dialog>
    </el-main>
  
    
  </template>
  
  <script setup>
  import { useStore } from 'vuex'
  import { ref ,watch,computed ,onUnmounted,onMounted} from 'vue';
  import {queryUserApi} from '@/api/user'
  import {querLikedCardByIdApi,querCollectionCardByIdApi} from '@/api/user';
  import index from '@/views/login/index.vue';
  import publicPostForm from '@/views/public/publicPost.vue';

  const dialogShow=ref(false);
  const dialogVisible=ref(false);
  const store = useStore();
  const showDropdown = ref(false);
  const activeDropdown = ref(null);
 const user = computed(() => store.state.user)
 const isLogin = computed(() => store.state.isLogin)
 const userDetail=computed(()=>store.state.userDetail)
 import { ElNotification ,ElMessage} from 'element-plus'
  const likeList = ref([]);
  const collectionList=ref([]);
  const closeDropdowns = () => {
    activeDropdown.value = null;
  };
  // 交互逻辑
  const showUserMenu = ref(false);
  const activeSubmenu = ref(null);
  let menuTimer = null;
  const firstOpenTimer = ref(null);
  const ANIMATION_DELAY = 300; // Animation delay in milliseconds
  const HOVER_DELAY = 500; // Delay before opening submenu (500ms = 0.5s)
  
  watch(showUserMenu, (newVal) => {
    if (newVal) {
      // 显示菜单时自动展开第一个项
      firstOpenTimer.value = setTimeout(() => {
        activeSubmenu.value = menuItems.value[0].id;
      }, 1);
    } else {
      clearTimeout(firstOpenTimer.value);
    }
  });
// 监听登录状态的变化
watch(isLogin, (status) => {
  if(status){
    searchLikeCard();
    searchCollectionCard();
  }else{
    likeList.value=[]
    collectionList.value=[]
  }
});
const upload=()=>{
  if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
  dialogShow.value=true;
}
  // 菜单配置
  const menuItems = ref([
    { id: 'likeList', label: '我的喜欢', count: "", icon: 'icon-like', hasSubmenu: true },
    { id: 'collectionList', label: '我的收藏', count: "", icon: 'icon-collect', hasSubmenu: true },
    { id: 'history', label: '观看历史', extra: '30天内', icon: 'icon-history' ,hasSubmenu: true},
    { id: 'later', label: '稍后再看', count: "", icon: 'icon-later' ,hasSubmenu: true},
    { id: 'works', label: '我的作品', count: "", icon: 'icon-works',hasSubmenu: true }
  ]);
  // 静态通知数据
  const notifications = ref([
    {
      avatar: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg',
      username: '抖音小助手',
      action: '点赞了你的作品',
      extra: '#挑战赛',
      time: '2小时前'
    },
    {
      avatar: 'https://dummyimage.com/40x40/00d2ff/fff',
      username: '用户9527',
      action: '关注了你',
      extra: '',
      time: '昨天'
    },
    {
      avatar: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg',
      username: '系统通知',
      action: '你的作品获得新曝光',
      extra: '查看详情 >',
      time: '3分钟前'
    }
  ]);
  
  // 静态消息数据
  const messages = ref([
    {
      avatar: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg',
      username: '电商直播小助手',
      content: '您预约的直播即将开始：冬季服饰专场...',
      time: '09:30',
      unread: true
    },
    {
      avatar: 'https://dummyimage.com/40x40/ffd700/fff',
      username: '电商直播小助手',
      content: '您预约的直播即将开始：冬季服饰专场...',
      time: '09:30',
      unread: true
    },
    {
      avatar: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg',
      username: '美食探店王',
      content: '你推荐的餐厅太棒了！下次一起...',
      time: '昨天',
      unread: false
    },
    {
      avatar: 'https://dummyimage.com/40x40/7d6bff/fff',
      username: '旅行日记',
      content: '您关注的旅行日记更新了新作品',
      time: '周一',
      unread: true
    }
  ]);

  const searchLikeCard = async () => {
    if(!isLogin.value) return
    const res = await querLikedCardByIdApi();
    likeList.value = res.data.filter((item, index) => index < 3);
}
const searchCollectionCard = async () => {
  if(!isLogin.value) return
  const res = await querCollectionCardByIdApi();
  collectionList.value = res.data.filter((item, index) => index < 3)
}
const userData = ref({likeList,collectionList})
  // 作品数据
  const worksData = ref({
    likes: [
      { id: 1, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '爱完束状纹理...', likes: '12.3w' },
      { id: 2, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '今天好不好天...', likes: '8.2w' },
      { id: 3, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '夏日穿搭指南', likes: '5.6w' }
    ],
    collect: [
      { id: 1, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '发型教程合集', likes: '3.2w' },
      { id: 2, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '美食日记', likes: '2.8w' },
      { id: 3, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '旅行日记', likes: '4.1w' }
    ],
    history: [
      { id: 1, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '发型教程合集', likes: '3.2w' },
      { id: 2, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '美食日记', likes: '2.8w' },
      { id: 3, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '旅行日记', likes: '4.1w' }
    ],
    later: [
      { id: 1, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '发型教程合集', likes: '3.2w' },
      { id: 2, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '美食日记', likes: '2.8w' },
      { id: 3, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '旅行日记', likes: '4.1w' }
    ],
    works: [
      { id: 1, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '发型教程合集', likes: '3.2w' },
      { id: 2, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '美食日记', likes: '2.8w' },
      { id: 3, cover: 'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/07fd3f3d-057a-4262-b402-24e37ff21a2e.jpg', title: '旅行日记', likes: '4.1w' }
    ]
  });
  
  const handleAvatarEnter = () => {
    clearTimeout(menuTimer);
    menuTimer = setTimeout(() => showUserMenu.value = true, 200);
  };
  
  const handleAvatarLeave = () => {
    clearTimeout(menuTimer);
    menuTimer = setTimeout(() => {
      showUserMenu.value = false;
      activeSubmenu.value = null;
    }, 200);
  };
  
  const handleSubmenuEnter = (item) => {
    if (!item.hasSubmenu) return;
    
    // Clear any existing timers
    clearTimeout(menuTimer);
    
    // If there's an active submenu and it's different from the new one
    if (activeSubmenu.value && activeSubmenu.value !== item.id) {
      // First close the current submenu
      activeSubmenu.value = null;
      
      // Then open the new submenu after animation delay
      menuTimer = setTimeout(() => {
        // Add additional delay before opening new submenu
        setTimeout(() => {
          activeSubmenu.value = item.id;
        }, HOVER_DELAY);
      }, ANIMATION_DELAY);
    } else {
      // If no submenu is active or it's the same one, add delay before opening
      menuTimer = setTimeout(() => {
        activeSubmenu.value = item.id;
      }, HOVER_DELAY);
    }
  };
  
  const handleSubmenuLeave = () => {
    // Only clear the timer if we're not hovering over the menu area
    if (!showUserMenu.value) {
      clearTimeout(menuTimer);
      menuTimer = setTimeout(() => activeSubmenu.value = null, ANIMATION_DELAY);
    }
  };
  
  const cancelClose = () => {
    clearTimeout(menuTimer);
  };
  
  const triggerClose = () => {
    // Only close if we're not hovering over the menu area
    if (!showUserMenu.value) {
      handleAvatarLeave();
    }
  };
  
  </script>
  
  <style scoped>
    .custom {
      width:30%!important;
      min-width:"100px" !important;
    }
  /* 基础导航栏样式 */
  .tiktok-navbar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 56px;
    /* background: #363741; */
   
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    z-index: 1000;
    background-color: white !important;
    box-shadow: 0 2px 6px rgba(0,0,0,0.08);
  }
  
  .nav-right {
    display: flex;
    align-items: center;
    gap: 24px;
  }
  
  /* 导航项通用样式 */
  .nav-item {
    color: rgba(255,255,255,0.9);
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 14px;
    cursor: pointer;
    position: relative;
    transition: color 0.2s;
  }
  
  .nav-item:hover {
    color: #fff;
  }
  
  /* 图标基础样式 */
  [class^="icon-"] {
    display: inline-block;
    width: 20px;
    height: 20px;
    background-size: contain;
  }
  
  
  .avatar-container {
    position: relative;
    cursor: pointer;
    margin-right: 20px;
  
  }
  .user-avatar {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    border: 1px solid rgba(255,255,255,0.1);
  }
  /* 通知和私信容器定位 */
  .notification-container,
  .message-container {
    position: relative;
  }
  
  /* 下拉菜单统一样式调整 */
  .dropdown-menu {
    position: absolute;
    right: 0;
    top: 42px;
    width: 360px;
    background: #252836;
    border-radius: 4px;
    box-shadow: 0 8px 24px rgba(0,0,0,0.2);
    padding: 12px 0;
    z-index: 1001;
  }
  
  .notification-dropdown {
    width: 280px;
  }
  
  .menu-header {
    padding: 8px 16px;
    font-size: 14px;
    color: rgba(255,255,255,0.9);
    border-bottom: 1px solid rgba(255,255,255,0.08);
  }
  
  .empty-tip {
    padding: 24px 16px;
    text-align: center;
    color: rgba(255,255,255,0.6);
    font-size: 14px;
  }
  
  /* 消息菜单特殊样式 */
  .message-dropdown {
    width: 380px;
    padding-bottom: 8px;
  }
  
  /* 悬停时图标颜色变化 */
  .nav-item:hover .icon-notification,
  .nav-item:hover .icon-message {
    filter: brightness(1.2);
  }
  
  /* 新增样式 */
  .notification-badge,
  .message-badge {
    position: absolute;
    top: 7px;
    right: -6px;
    background: #fe2c55;
    color: rgb(255, 255, 255);
    font-size: 10px;
    min-width: 7px;
    height: 15px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 4px;
  }
  
  .notification-item {
    display: flex;
    padding: 12px 16px;
    gap: 12px;
    transition: background 0.2s;
  }
  
  .notification-item:hover {
    background: rgba(255,255,255,0.03);
  }
  
  .notify-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }
  
  .notify-content {
    flex: 1;
  }
  
  .notify-text {
    color: rgba(255,255,255,0.9);
    font-size: 14px;
    line-height: 1.4;
  }
  
  .username {
    color: #fe2c55;
    font-weight: 500;
  }
  
  .extra {
    color: rgba(255,255,255,0.6);
  }
  
  .notify-time {
    color: rgba(255,255,255,0.4);
    font-size: 12px;
    margin-top: 4px;
  }
  
  /* 私信消息样式 */
  .message-item {
    display: flex;
    padding: 12px 16px;
    gap: 12px;
  }
  
  .msg-avatar {
    width: 40px;
    height: 40px;
    border-radius: 8px;
  }
  
  .msg-content {
    flex: 1;
  }
  
  .msg-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 4px;
  }
  
  .username {
    color: rgba(255,255,255,0.9);
    font-weight: 500;
  }
  
  .msg-time {
    color: rgba(255,255,255,0.4);
    font-size: 12px;
  }
  
  .msg-preview {
    color: rgba(255,255,255,0.6);
    font-size: 13px;
    position: relative;
    padding-right: 20px;
  }
  
  .unread-dot {
    position: absolute;
    right: 0;
    top: 30px;
    transform: translateY(-50%);
    width: 8px;
    height: 8px;
    background: #fe2c55;
    border-radius: 50%;
  }
  /* 添加滚动条 */
  .dropdown-menu {
    max-height: 60vh;
    overflow-y: auto;
  }
  
  /* 自定义滚动条 */
  .dropdown-menu::-webkit-scrollbar {
    width: 6px;
  }
  .dropdown-menu::-webkit-scrollbar-thumb {
    background: rgba(255,255,255,0.2);
    border-radius: 3px;
  }
  
  
  
  /* 用户菜单样式 */
  .user-menu {
    position: absolute;
    right: 0;
    top: 42px;
    margin-top: -2px;
    /* width: 300px; */
    min-width: 240px;
    background: #252836;
    border-radius: 3%;
    box-shadow: 0 8px 24px rgba(0,0,0,0.2);
    padding: 12px 0;
    z-index: 1001;
    transition: all 0.3s ease; /* 添加过渡效果 */
  }
  .profile-avatar-if{
    padding: 0 16px 12px;
    border-bottom: 1px solid rgba(255,255,255,0.08);
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 12px;
  }
  .user-profile {
    display: flex;
    padding: 0 16px 12px;
    border-bottom: 1px solid rgba(255,255,255,0.08);
  }
  
  .profile-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 12px;
  }
  
  .profile-info {
    flex: 1;
    min-width: 0;
  }
  
  .username {
    font-size: 14px;
    color: #fff;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .stats {
    font-size: 12px;
    color: rgba(255,255,255,0.6);
    display: flex;
    gap: 12px;
    margin-top: 4px;
  }
  
  /* 菜单项样式 */
  
  .menu-item {
    position: static;
    padding: 8px 16px;
    margin: 8px 5px;
    border-radius: 15px;
    color: rgba(255,255,255,0.9);
    display: block;
    align-items: center;
    gap: 8px;
    background: rgba(255,255,255,0.10); 
    transition: all 0.00001s ease;
    width: 300px;
  } 
  
  .menu-item:hover {
    background: rgba(255,255,255,0.05);
  }
  
  .menu-extra {
    justify-content: space-between; /* 两端对齐 */
    font-size: 12px;
    font-style: normal;
    display: flex;
    gap: 8px;
    color: rgba(255,255,255,0.6);
  }
  .menu-laber{
    margin-right: 180px;
    width: 100px;
  }
  .menu-count{
  }
  .count {
    font-style: normal;
    font-size: 12px;
    color: rgba(255,255,255,0.6);
  }
  .works-submenu {
    position: relative;
    left: 0;
    top: 100%; 
    width: 100%;
    background: rgba(255,255,255,0.10);
    border-radius: 10%;
    box-shadow: 0 8px 24px rgba(255,255,255,0.10);
    z-index: 1002;
  } 
  
  .works-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 8px;
    
  }
  
  .work-cover video {
    width: 100%;
    height: 120px;
    object-fit: cover;
    border-radius: 2px;
    border-radius: 10%;
  }
  /* 隐藏第4个及以后的图片 */
.work-cover img:nth-child(n+4) {
  display: none;
}
  
  .work-info {
    padding: 6px 0;
  }
  
  .title {
    font-size: 12px;
    margin-left: 15px;
    color: white;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .stats {
    font-size: 10px;
    color: rgba(255,255,255,0.6);
    display: flex;
    align-items: center;
    gap: 4px;
  }
  
  /* 动画效果 */
  .menu-fade-enter-active,
  .submenu-fade-enter-active {
    transition: all 0.5s ease; /* Increased from 0.3s to 0.5s */
  }
  
  .menu-fade-leave-active,
  .submenu-fade-leave-active {
    transition: all 0.15s ease; /* Increased from 0.15s to 0.4s */
  }
  
  .menu-fade-enter-from,
  .menu-fade-leave-to,
  .submenu-fade-enter-from,
  .submenu-fade-leave-to {
    opacity: 0;
    transform: translateY(-5px);
  }
  
  /* 响应式处理 */
  @media (max-width: 768px) {
    .nav-item span {
      display: none;
    }
    
    
    .works-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
  /* 新增下边距防止折叠 */
  .user-menu {
    margin-bottom: 8px;
  }
  
  /* 调整作品网格间距 */
  .works-grid {
    gap: 6px;
  }
  
  /* 优化定位层级 */
  .user-menu {
    position: absolute;
    right: 0;
    top: calc(100% + 8px); /* 与头像保持间距 */
  }
  
  /* 响应式调整 */
  @media (max-width: 768px) {
    .menu-item {
      padding: 8px 12px;
    }
  }
  
  </style>