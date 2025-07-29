<script setup>
import{ref,computed,onMounted} from 'vue'
import { RouterLink } from 'vue-router';
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import {queryUserApi,updateUserinfoApi} from '@/api/user'
import { ElMessage } from 'element-plus';
import { ElNotification } from 'element-plus'
const store = useStore();
const userinfo = computed(() => store.state.user)
const route = useRoute()
const router = useRouter()
const user = ref('') //这是一个用户信息
const userForm = ref('') //复制一份用户信息
const dialogFormVisible=ref(false)
onMounted(()=>{
    searchInfo();
})
const searchInfo =async ()=>{
    const res=await queryUserApi();
    user.value=res.data;
    userForm.value={ ...user.value }
}
// 导航项配置
const navItems = ref([
    { text: '作品', path: '/user/userIndex', isActive: false },
    { text: '喜欢', path: '/user/likedCard', isActive: false },
    { text: '收藏', path: '/user/collectionCard', isActive: false },
    { text: '观看历史', path: '', isActive: false },
    { text: '我的预约', path: '', isActive: false }
])
const open1 = () => {
    ElNotification({
    title: '维护',
    message: '该功能还在开发中',
    type: 'error',
    })
}

// 自动匹配当前路由激活状态
const isRouteActive = (path) => {
    return route.path === path
}

// 点击处理函数
const selectItem = (item) => {
    if(item.path!==''&&item.path!==null){
        // 跳转路由
        router.push(item.path)
        // 更新激活状态
        navItems.value.forEach(navItem => {
        navItem.isActive = navItem.path === item.path
        })
    }else if(store.state.isLogin){
        open1();
    }
}
const edit=()=>{
    dialogFormVisible.value=true;
}
const save=async() => {
    const result=await updateUserinfoApi(userForm.value)
    if(result.code){
        ElMessage.success('保存成功');
        userinfo.value.picture=userForm.value.picture
        userinfo.value.username=userForm.value.username
        userinfo.value.bio=userForm.value.bio
        dialogFormVisible.value = false;
        searchInfo();
    }else{
        ElMessage.error('保存失败')
    }
}

const handleCancel = () => {
    Object.assign(userForm, user)
    dialogFormVisible.value=false
}
// 文件上传
// 图片上传成功后触发
const handleAvatarSuccess = (response) => {
    userForm.value.picture = response.data;
    // userForm.value.picture = "http://syt87sfjl.hd-bkt.clouddn.com/"+response.data;
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}
</script>

<template>
    <el-container>
        <el-header height="230px">
    <div class="profile-header">
    <!-- 用户信息区域 -->
    <div class="user-info-container">
        <!-- 头像 -->
        <div class="avatar-wrapper" v-if="store.state.isLogin">
            <img v-if="user.picture" :src="user.picture" class="user-avatar" alt="用户头像">
            <img v-else src="https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/88ad0b02-dc41-4e55-88b6-ae5c00f0d18c.jpg" class="user-avatar" alt="用户头像">
        </div>
        <div class="avatar-wrapper" v-else>
            <img src="https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/88ad0b02-dc41-4e55-88b6-ae5c00f0d18c.jpg" class="user-avatar" alt="用户头像">
        </div>
        <!-- 文字信息 -->
        <div class="info-section">
            <!-- 昵称和操作按钮 -->
            <div class="name-row">
                <h1 class="nickname" v-if="store.state.isLogin">{{user.username}}</h1>
                <h1 class="nickname" v-else>你还没有登录</h1>
                <div class="action-buttons">
                </div>
            </div>
            
            <!-- 数据统计 -->
            <div class="stats-row">
                <span class="stat-item">关注 <em>{{user.followingCount}}</em></span>
                <span class="stat-item">粉丝 <em>{{user.followerCount}}</em></span>
                <span class="stat-item">获赞 <em>{{user.likeCount}}</em></span>
            </div>
            
            <!-- 账号信息 -->
            <div class="account-info"  v-if="store.state.isLogin">
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

                <button class="modern-btn" @click="edit">编辑资料</button>
            </div>

        </div>
    </div>    
</div>


<div class="nav-container">
    <div 
        v-for="(item, index) in navItems" 
        :key="index" 
        class="nav-item"
        @click="selectItem(item)"
    >
        <div class="nav-link">{{ item.text }}</div>
        <div 
        class="indicator" 
        :class="{ active: item.isActive || isRouteActive(item.path) }"
        ></div>
    </div>
</div>
<!-- 对话框容器 -->
<!-- Form -->
    <el-dialog v-model="dialogFormVisible" title="编辑资料" width="20%" >
    <el-form :model="userForm" class="editForm">
        <el-form-item>
            <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :headers="{'token': store.state.token}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
            <img v-if="userForm.picture" :src="userForm.picture" class="userForm-avatar"/>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <br/>
            <span class="authorEdit">点击修改头像</span>
        </el-form-item>
        
    <el-form-item label="名字" :label-width="formLabelWidth">
        <el-input v-model="userForm.username" autocomplete="off" />
    </el-form-item>
    <el-form-item label="简介" :label-width="formLabelWidth">
        <el-input v-model="userForm.bio" type="textarea" rows="1"/>
    </el-form-item>

    </el-form>
    <template #footer>
        <span class="dialog-footer">
            <el-button @click="handleCancel">取消</el-button>
            <el-button type="primary" @click="save">保存</el-button>
        </span>
    </template>
    </el-dialog>
            </el-header>
        <el-main><router-view /></el-main>
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

</style>