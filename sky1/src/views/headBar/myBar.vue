<script setup>
import{ref,onMounted,computed} from 'vue'
import { useStore } from 'vuex'
const store = useStore();
const user = computed(() => store.state.user)
</script>

<template>
<div class="profile-header">
    <!-- 用户信息区域 -->
    <div class="user-info-container">
        <!-- 头像 -->
        <div class="avatar-wrapper">
            <img :src="user.picture" class="user-avatar" alt="用户头像">
        </div>

        <!-- 文字信息 -->
        <div class="info-section">
            <!-- 昵称和操作按钮 -->
            <div class="name-row">
                <h1 class="nickname">{{user.username}}</h1>
                <div class="action-buttons">
                    <!-- <button class="follow-btn-myBar">关注</button> -->
                    <!-- <div class="more-actions">
                        <img  class="more-icon" alt="更多操作">
                    </div> -->
                </div>
            </div>

            <!-- 数据统计 -->
            <div class="stats-row">
                <span class="stat-item">关注 <em>37</em></span>
                <span class="stat-item">粉丝 <em>45</em></span>
                <span class="stat-item">获赞 <em>3393</em></span>
            </div>

            <!-- 账号信息 -->
            <div class="account-info">
                <span class="douyin-id">抖音号：tanzhicano</span>
                <span>10岁</span>
                <span>浙江 温州</span>
                
            </div>
            <div class="account-dio">
                <span class="source-info">来福来福来一口大福</span>
                <button class="edit-btn"><img src="edit-icon.svg" class="btn-icon">编辑资料</button>
            </div>
            
            <!-- 直播状态 -->

            <!-- <div class="live-status">
                <span class="live-tag">直播中</span>
                <span>1人正在直播</span>
            </div> -->

        </div>
    </div>    
</div>
<div class="route-nav-container">
    <nav class="route-nav" router>
        <div class="nav-item active">作品 <span class="count">1</span></div>
        <router-link to="/likedCard">
            <div class="nav-item">喜欢 <span class="count">4407</span></div>
        </router-link>
        <div class="nav-item">收藏</div>
        <div class="nav-item">观看历史</div>
        <div class="nav-item">稍后再看</div>
        <div class="nav-item">我的预约</div>
    </nav>
</div>
</template>

<style>
.profile-header {
    max-width: 1152px;
    height: 150px;
    margin-left: 10px;
    padding: 24px 32px;
    background: #fff;
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
.edit-btn{
    margin-left: auto;
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

</style>