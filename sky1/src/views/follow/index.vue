<template>
  <div class="container">
    <!-- 关注用户导航栏 -->

      <div class="nav-container">
      <div class="followed-list">
        <div 
          class="followed-item all-dynamic"
          :class="{ active: selectedUser === null }"
          @click="selectUser(null)"
        >
          <div class="nav-label" @click="reset()">全部动态</div>
        </div>
        <div  v-for="user in followedUsers"  :key="user.id"  class="followed-item"  :class="{ active: selectedUser === user.id }"  @click="selectUser(user.id)">
          <img :src="user.picture"  class="nav-avatar" >
          <div class="nav-username">{{ user.username }}</div>
        </div>
      </div>
    </div>

    <!-- 动态列表 -->
    <div class="dynamic-list">
      <div v-for="post in filteredPosts" :key="post.id" class="dynamic-card" >
        <div class="user-info">
          <img :src="post.user.picture" class="avatar" @click="pushUser(post.user.id)">
          <div>
            <div class="username">{{ post.user.username }}</div>
            <div class="post-time">{{ formatTime(post.createTime) }}</div>
            <div class="content" v-html="parseContent(post.content)"></div>
            <img v-if="post.fileType?.startsWith('image/')" :src="post.pictureUrl" class="dynamicImgPost" />
            <video v-if="post.fileType?.startsWith('video/')" :src="post.pictureUrl" class="dynamicVideoPost" controls></video>
            <!-- <img :src="post.pictureUrl" class="dynamicPost"> -->
          </div>
        </div>
        <div class="interaction">
          <div class="interaction-item">
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 18 18" width="30" height="30" style="width: 18px; height: 18px;"><path d="M9.789075 2.2956175C8.97235 1.6308450000000003 7.74999 2.212005 7.74999 3.26506L7.74999 5.3915500000000005C6.642015000000001 5.5780325 5.3073725 6.040405 4.141735000000001 7.11143C2.809155 8.335825 1.751515 10.3041 1.45716 13.404099999999998C1.409905 13.9018 1.7595399999999999 14.22505 2.105415 14.317499999999999C2.442215 14.40755 2.8807175 14.314625 3.127745 13.92915C3.9664525 12.620249999999999 4.89282 11.894575 5.765827499999999 11.50585C6.4628049999999995 11.19545 7.14528 11.093125 7.74999 11.0959L7.74999 13.235025C7.74999 14.2881 8.97235 14.869250000000001 9.789075 14.2045L15.556199999999999 9.510425000000001C16.355075 8.860149999999999 16.355075 7.640124999999999 15.556199999999999 6.989840000000001L9.789075 2.2956175zM9.165099999999999 3.0768275000000003L14.895025 7.739050000000001C15.227975 7.980475 15.235775 8.468875 14.943874999999998 8.7142L9.17615 13.416800000000002C8.979474999999999 13.562024999999998 8.75 13.4269 8.75 13.227375000000002L8.75 10.638175C8.75 10.326975000000001 8.542125 10.134725 8.2544 10.1118C7.186765 10.02955 6.1563175 10.2037 5.150895 10.69295C4.14982 11.186925 3.2102250000000003 12.096525 2.573625 13.00995C2.54981 13.046975 2.52013 13.046025 2.5211725 12.986C2.8971525 10.0573 3.9373475 8.652125 4.807025 7.85305C5.87747 6.8694775 7.213197500000001 6.444867500000001 8.2272 6.33056C8.606525 6.287802500000001 8.74805 6.0849325 8.74805 5.7032275L8.74805 3.2615475C8.74805 3.0764875000000007 8.993175 2.9321925 9.165099999999999 3.0768275000000003z"></path></svg>
          {{ post.collectionsCount }}
          </div>
          <div class="interaction-item" @click="fetchComments(post.id)">💬 {{ post.commentsCount }}</div>
          <div :class="{ 'liked': post.state.isLike }" @click="toggleLike(post)" class="b-like-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 18 18" width="18" height="18" style="width: 18px; height: 18px;"><path d="M10.4511 2.2220125C10.218425 2.194885 10.002175 2.2953725 9.884175 2.433395C9.4264 2.9688525 9.321875 3.7501399999999996 8.978575 4.3581725C8.533574999999999 5.146395 8.1198 5.6213375 7.609775000000001 6.068507499999999C7.1751375 6.449565 6.738407499999999 6.697442499999999 6.3125 6.8050575L6.3125 14.854575C6.9198900000000005 
                    14.868174999999999 7.572900000000001 14.876875 8.25 14.876875C9.936425 14.876875 11.367025 
                    14.823325 12.33115 14.773699999999998C13.03235 14.737575 13.646025000000002 
                    14.390075 13.966750000000001 13.81945C14.401900000000001 13.04535 14.9387 11.909650000000001 15.264174999999998 10.571200000000001C15.56665 9.327275 15.704699999999999 8.304325 15.766675 7.582224999999999C15.7988 7.208262500000001 15.50165 6.875019999999999 15.059999999999999 6.875019999999999L11.323274999999999 6.875019999999999C11.156575 6.875019999999999 11.000800000000002 6.791952499999999 10.907975 6.653499999999999C10.783725 6.468192500000001 10.82855 6.2670175 10.9037 6.07485C11.059 5.675084999999999 11.29355 4.9974475 11.382425000000001 4.4018275C11.470875000000001 3.80917 11.450999999999999 3.32219 11.212050000000001 2.86913C10.9571 2.3857825 10.66065 2.2464475 10.4511 2.2220125zM12.034300000000002 5.87502L15.059999999999999 5.87502C16.02035 5.87502 16.850875 6.64489 16.763 7.667825C16.697100000000002 8.435525 16.55155 9.5092 16.235825000000002 10.807500000000001C15.882625 12.259950000000002 15.3035 13.482225 14.838450000000002 14.309474999999999C14.32695 15.2194 13.377475 15.721150000000002 12.38255 15.772375C11.405125 15.822725 9.956949999999999 15.876875000000002 8.25 15.876875000000002C6.5961925 15.876875000000002 5.0846825 15.826025000000001 4.0136674999999995 15.77715C2.8370825 15.723474999999999 1.8519999999999999 14.850000000000001 1.725645 13.654824999999999C1.6404649999999998 12.849274999999999 1.5625 11.80725 1.5625 10.689375C1.5625 9.665175000000001 1.6279400000000002 8.736175 1.7045524999999997 7.998975C1.8351224999999998 6.7427075 2.9137075 5.87502 4.130655 5.87502L5.8125 5.87502C6.072015 5.87502 6.457235 5.7490675 6.9505175 5.316582499999999C7.377705000000001 4.942045 7.7193000000000005 4.5546075 8.107775 3.8665374999999997C8.492075 3.18585 8.605825 2.389785 9.124075 1.783595C9.452975 1.3988800000000001 9.99475 1.162025 10.5669 1.228745C11.16225 1.29816 11.717425 1.683875 12.09655 2.4025825000000003C12.478275 3.1262375000000002 12.474075 3.8618225 12.371500000000001 4.54938C12.302149999999997 5.0139949999999995 12.155425000000001 5.510059999999999 12.034300000000002 5.87502zM5.3125 14.82705L5.3125 6.875019999999999L4.130655 6.875019999999999C3.3792199999999997 6.875019999999999 2.77211 7.400795 2.6991975000000004 8.10235C2.6253525 8.812875 2.5625 9.70665 2.5625 10.689375C2.5625 11.762875 2.6374975 12.768475 2.7200975 13.549700000000001C2.7919925 14.229675 3.3521950000000005 14.74595 4.05924 14.778224999999999C4.4278775 14.795 4.849985 14.812050000000001 5.3125 14.82705z" fill="currentColor">
              </path></svg>
            {{ post.likesCount }}
          </div>
        </div>

        
        <div v-if="expandedPostId === post.id" class="comments-section">
          <div class="comment-stats">
            <span class="comment-count">评论 {{ post.commentsCount }}</span>
      <div class="comment-tabs">
        <span  class="tab"  :class="{ active: post.sortType === 'hot' }">
          最热
        </span>
        <span  class="tab"  :class="{ active: post.sortType === 'new' }">
          最新
        </span>
      </div>
    </div>
          <div class="comment-input">
            <img :src="user.picture" class="avatar" >
            <div class="input-container">
              <div v-click-outside="resetReplyTarget">
              <input
                v-model="newComment"
                :placeholder= "'说点什么....'"
                @keyup.enter="postComment(post)"
              >
              <button @click="postComment(post)">发布</button>
            </div>
            </div>
          </div>

          
          <div class="comment-tree">
            <CommentNode
                v-for="comment in comments"
                :key="comment.id"
                :comment="comment"
                :depth="0"
                @reply="handleReply"
            />
          </div>
        </div>
      </div>
    </div> 
  </div>
</template>

<script setup>
import { ref, reactive, computed ,onMounted} from 'vue'
import CommentNode from '@/views/follow/CommentNode.vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import {getFollowerApi,getPostsByUsersApi} from '@/api/follow';
import {toggleLikeApi} from '@/api/index';
import {getPostCommentsApi,insertCommentApi} from '@/api/comment';
const store = useStore();
const router=useRouter();
const user = computed(() => store.state.user)
const followedUsers = ref([])
const dynamicPosts = ref([])
const error = ref(null)
const selectedUser = ref(null)
const comments=ref([])
const expandedPostId = ref(null)
const newComment = ref('')

onMounted(()=>{
  fetchFollowedUsers();
  fetchPosts();
})

const fetchFollowedUsers = async () => {
    const response = await getFollowerApi();
    if(response.data){
        followedUsers.value = response.data.rows;
    }
}
// 获取动态数据方法
const fetchPosts = async () => {
    const response = await getPostsByUsersApi();
    if(response.data){
        dynamicPosts.value = response.data;
    }
}
// 获取评论
const fetchComments = async (postId) => {
    const response = await getPostCommentsApi(postId,user.value.id);
    if(response.data){
        comments.value=response.data;
    }
    expandedPostId.value = expandedPostId.value === postId ? null : postId;
}


// 计算属性
const filteredPosts = computed(() => {
  return selectedUser.value ? dynamicPosts.value.filter(post => post.user.id === selectedUser.value) : dynamicPosts.value;
})

const reset=()=>{
  selectedUser.value=null
}
// 方法
const selectUser = (userId) => {
  selectedUser.value = userId
}

const toggleComments = (postId) => {
  expandedPostId.value = expandedPostId.value === postId ? null : postId
}

const formatTime = (date) => {
    // 如果 date 不是 Date 对象，尝试转换
    if (!(date instanceof Date)) {
    date = new Date(date);
    // 检查转换后的日期是否有效
    if (isNaN(date.getTime())) {
      return "时间无效";
    }
  }
  const diff = Date.now() - date.getTime()
  const hours = Math.floor(diff / 3600000)
  if (hours < 1) return `${Math.floor(diff/60000)}分钟前`
  if (hours < 24) return `${hours}小时前`
  return date.toLocaleDateString()
}

const parseContent = (content) => {
  return content.replace(/#(\S+)/g, '<span class="hashtag">#$1</span>')
}

const postComment =async (post) => {
  if (!newComment.value.trim()) return;
    const res = await insertCommentApi(post.id,newComment.value)
    const response = await getPostCommentsApi(post.id,user.value.id);
    comments.value=response.data;
    // comments.value.push(res.data)
    newComment.value = ''
}

const findComment = (comments, targetId) => {
  for (const comment of comments) {
    if (comment.id === targetId) return comment
    if (comment.replies) {
      const found = findComment(comment.replies, targetId)
      if (found) return found
    }
  }
  return null
}

const handleReply = (targetComment) => {
  replyTarget.value = {
    id: targetComment.id,
    user: targetComment.user
  }
}
const pushUser=(id)=>{
  if(user.value.id==id){
    router.push(`/user`)
  }else{
    router.push(`/visitor/${id}`)
  }
}

const toggleLike =async (post) => {
  const res=await toggleLikeApi(user.value.id,post.id)
  if(post.state.isLike){
    post.likesCount-=1
  }else{
    post.likesCount+=1
  }
  post.state.isLike=!post.state.isLike
}
</script>

<style scoped>
.interaction {
  display: flex;
  align-items: center;
  gap: 250px; /* 控制两个按钮间距 */
  padding: 8px 0;
  margin-left: 48px;
}

.interaction-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #9499a0;
  cursor: pointer;
  transition: all 0.2s;
}

.interaction-item:hover {
  color: #00a1d6;
}

.icon {
  font-size: 1.1em;
  line-height: 1;
}
.container {
  width: 90%;
  
  padding: 20px;

}

/* 导航栏样式 */
.nav-container {
  position: sticky;
  top: 0;
  background: white;
  margin-left: 280px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
  z-index: 100;
  width: 60%;
  margin-bottom: 20px;
  border-radius: 10px;
}

.followed-list {
  display: flex;
  overflow-x: auto;
  padding: 0 16px;
  gap: 24px;
  -webkit-overflow-scrolling: touch;
}

.followed-item {
  flex: 0 0 auto;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}
.nav-label{
  margin-top: 30px;
  font-weight: 300px;
}

.nav-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
}

.nav-username {
  font-size: 12px;
  color: #61666d;
  max-width: 64px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.dynamic-list{
  position: sticky;
  top: 0;
  background: white;
  margin-left: 280px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
  z-index: 100;
  width: 60%;
  margin-bottom: 20px;
  border-radius: 10px;
}
/* 动态卡片样式 */
.dynamic-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 30px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.15);
}

.user-info {
  display: flex;
  margin-bottom: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}

.post-time {
  color: #9499a0;
  font-size: 0.9em;
}
.dynamicImgPost{
  width: 300px !important;; /* 与Waterfall组件配置的width一致 */
  height: 300px !important; /* 强制固定高度 */
  aspect-ratio: 1/1; /* 正方形比例 */
  object-fit: contain; /* 裁剪适配 */
  border-radius: 10px;
}
.dynamicVideoPost{
  width: 400px !important;; /* 与Waterfall组件配置的width一致 */
  height: 300px !important; /* 强制固定高度 */
  aspect-ratio: 1/1; /* 正方形比例 */
  object-fit: contain; /* 裁剪适配 */
  border-radius: 10px;
  background-color: black;
}

/* 评论区样式 */
/* .comments-section {
  margin-top: 16px;
  border-top: 1px solid #eee;
  padding-top: 4px;
} */

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.input-container {
  flex: 1;
  display: flex;
  gap: 8px;
}

input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
}

button {
  background: #00a1d6;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
}

.hashtag {
  color: #00a1d6;
  margin-right: 8px;
}
/* 新增评论统计样式 */
.comment-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 12px 0;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.comment-count {
  color: #18191c;
  font-weight: 500;
  font-size: 14px;
}

.comment-tabs {
  display: flex;
  gap: 12px;
}

.tab {
  color: #9499a0;
  font-size: 13px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.tab.active {
  color: #00a1d6;
  background: #e7f7ff;
}

/* 调整原有互动栏上边距 */
.interaction {
  margin-top: -4px;
}
/* 增加排序切换动画 */
.comment-tree {
  transition: all 0.3s ease;
}

.tab {
  transition: all 0.2s ease;
  position: relative;
}

.tab.active::after {
  content: "";
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #00a1d6;
  animation: slide 0.3s ease;
}

@keyframes slide {
  from { transform: scaleX(0); }
  to { transform: scaleX(1); }
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
</style>