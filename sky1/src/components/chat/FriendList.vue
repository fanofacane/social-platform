<template>
  <div class="friend-list">
    <div
      v-for="friend in friends"
      :key="friend.id"
      :class="['friend-item', { active: friend.id == activeId }]"
      @click="$emit('select', friend.id)"
    >
      <img :src="friend.picture" class="avatar" />
      <div class="info">
        <div class="name-unread">
          <span class="name">{{ friend.username }}</span>
          <span v-if="friend.unreadCount > 0" class="unread-dot">{{ friend.unreadCount }}</span>
        </div>
        <div class="last-message">
          <span>{{ friend.content }}</span>
          <span>{{ formatTime(friend.createTime) }}</span>
          </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
const props = defineProps({
  friends: Array,
  activeId: Number
})

const formatTime = (date) => {
    // 如果 date 不是 Date 对象，尝试转换
    if(date==null) return;
    if (!(date instanceof Date)) {
    date = new Date(date);
    // 检查转换后的日期是否有效
    if (isNaN(date.getTime())) {
      return "时间无效";
    }
  }
  const diff = Date.now() - date.getTime()
  const hours = Math.floor(diff / 3600000)
  if(Math.floor(diff/60000)<=1)return `刚刚`
  if (hours < 1) return `${Math.floor(diff/60000)}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (hours < 48) return `昨天`
  return date.toLocaleDateString()
}
</script>

<style scoped>
.friend-list {
  width: 260px;
  background: #f7f7f7;
  border-right: 1px solid #eee;
  height: 90vh;
  overflow-y: auto;
}
.friend-item {
  display: flex;
  align-items: center;
  padding: 16px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.2s;
}
.friend-item.active {
  background: #e6f7ff;
}
.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  margin-right: 12px;
  object-fit: cover;
  background: #fff;
  border: 1px solid #eee;
}
.info {
  flex: 1;
  min-width: 0;
}
.name-unread {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.name {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}
.unread-dot {
  background: #ff4d4f;
  color: #fff;
  border-radius: 12px;
  padding: 2px 8px;
  font-size: 12px;
  margin-left: 8px;
}
.last-message {
  display: flex;
  gap: 15px;
  color: #888;
  font-size: 13px;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style> 