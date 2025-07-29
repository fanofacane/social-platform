<template>
    <div v-if="comment" class="comment-node" >
            <!-- 评论内容头部 -->
        <div class="comment-content">
          <div class="meta" @click="pushUser(comment.user.id)">
            <img :src="comment.user?.picture" class="picAuthor"> <!-- 使用可选链 -->
              <div class="username">{{ comment.user?.username }}</div>
          </div>
        <div class="actions">
            <span class="commentContent">{{ comment.content }}</span>
            <div class="gasComment">
            <span class="time">{{ formatTime(comment.createTime) }}</span>
            <span :class="{ 'liked': comment.isLike }" @click="toggleLike(comment)" class="b-like-icon">
                  <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 18 18" width="10" height="10" style="width: 14px; height: 14px;"><path d="M10.4511 2.2220125C10.218425 2.194885 10.002175 2.2953725 9.884175 2.433395C9.4264 2.9688525 9.321875 3.7501399999999996 8.978575 4.3581725C8.533574999999999 5.146395 8.1198 5.6213375 7.609775000000001 6.068507499999999C7.1751375 6.449565 6.738407499999999 6.697442499999999 6.3125 6.8050575L6.3125 14.854575C6.9198900000000005 
                    14.868174999999999 7.572900000000001 14.876875 8.25 14.876875C9.936425 14.876875 11.367025 
                    14.823325 12.33115 14.773699999999998C13.03235 14.737575 13.646025000000002 
                    14.390075 13.966750000000001 13.81945C14.401900000000001 13.04535 14.9387 11.909650000000001 15.264174999999998 10.571200000000001C15.56665 9.327275 15.704699999999999 8.304325 15.766675 7.582224999999999C15.7988 7.208262500000001 15.50165 6.875019999999999 15.059999999999999 6.875019999999999L11.323274999999999 6.875019999999999C11.156575 6.875019999999999 11.000800000000002 6.791952499999999 10.907975 6.653499999999999C10.783725 6.468192500000001 10.82855 6.2670175 10.9037 6.07485C11.059 5.675084999999999 11.29355 4.9974475 11.382425000000001 4.4018275C11.470875000000001 3.80917 11.450999999999999 3.32219 11.212050000000001 2.86913C10.9571 2.3857825 10.66065 2.2464475 10.4511 2.2220125zM12.034300000000002 5.87502L15.059999999999999 5.87502C16.02035 5.87502 16.850875 6.64489 16.763 7.667825C16.697100000000002 8.435525 16.55155 9.5092 16.235825000000002 10.807500000000001C15.882625 12.259950000000002 15.3035 13.482225 14.838450000000002 14.309474999999999C14.32695 15.2194 13.377475 15.721150000000002 12.38255 15.772375C11.405125 15.822725 9.956949999999999 15.876875000000002 8.25 15.876875000000002C6.5961925 15.876875000000002 5.0846825 15.826025000000001 4.0136674999999995 15.77715C2.8370825 15.723474999999999 1.8519999999999999 14.850000000000001 1.725645 13.654824999999999C1.6404649999999998 12.849274999999999 1.5625 11.80725 1.5625 10.689375C1.5625 9.665175000000001 1.6279400000000002 8.736175 1.7045524999999997 7.998975C1.8351224999999998 6.7427075 2.9137075 5.87502 4.130655 5.87502L5.8125 5.87502C6.072015 5.87502 6.457235 5.7490675 6.9505175 5.316582499999999C7.377705000000001 4.942045 7.7193000000000005 4.5546075 8.107775 3.8665374999999997C8.492075 3.18585 8.605825 2.389785 9.124075 1.783595C9.452975 1.3988800000000001 9.99475 1.162025 10.5669 1.228745C11.16225 1.29816 11.717425 1.683875 12.09655 2.4025825000000003C12.478275 3.1262375000000002 12.474075 3.8618225 12.371500000000001 4.54938C12.302149999999997 5.0139949999999995 12.155425000000001 5.510059999999999 12.034300000000002 5.87502zM5.3125 14.82705L5.3125 6.875019999999999L4.130655 6.875019999999999C3.3792199999999997 6.875019999999999 2.77211 7.400795 2.6991975000000004 8.10235C2.6253525 8.812875 2.5625 9.70665 2.5625 10.689375C2.5625 11.762875 2.6374975 12.768475 2.7200975 13.549700000000001C2.7919925 14.229675 3.3521950000000005 14.74595 4.05924 14.778224999999999C4.4278775 14.795 4.849985 14.812050000000001 5.3125 14.82705z" fill="currentColor">
                  </path></svg>
                  <span class="count">{{ comment.likesCount }}</span>
            </span>
            <button @click.stop="toggleReplyInput(comment)">回复</button>
            </div>
        </div>
        </div>
<!-- 回复输入框（动态显示在对应评论下方） -->
      <div v-if="showReply"  class="reply-input" ref="replyWindow">
        <input
          v-model="replyStates.get(comment.id).content"
          placeholder="说点什么吧！！"
          @keyup.enter="submitReply(comment)"
        >
        <button @click="submitReply(comment)">发布</button>
      </div>

        <template v-if="depth == 0">
          <div v-if="countDescendants(comment) > 1 && !showReplies">
            <div class="toggle-replies" @click="showReplies = true">
              展开{{ countDescendants(comment) }}条回复
            </div>
          </div>
          <template v-else>
            <div class="chilren">
            <CommentNode
              v-for="reply in comment.replies"
              :key="reply.id"
              :comment="reply"
              :depth="depth + 1"
              @reply="$emit('reply', $event)"
            />
            </div>
            <div v-if="countDescendants(comment) > 0" class="toggle-replies" @click="showReplies = false">
              收起
            </div>
          </template>
        </template>
        <template v-else>
          <CommentNode
            v-for="reply in comment.replies"
            :key="reply.id"
            :comment="reply"
            :depth="depth + 1"
            @reply="$emit('reply', $event)"
          />
        </template>
    </div>
</template>

<script setup>
import { defineProps ,reactive,ref, onMounted, onBeforeUnmount,nextTick ,computed} from 'vue'
import {toggleLikeApi} from '@/api/index';
import {insertCommentApi} from '@/api/comment';
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
const router=useRouter();
const store = useStore();
const user = computed(() => store.state.user)
const showReply = ref(false);
const replyWindow = ref(null); // 用于获取回复窗口的 DOM 元素
const showReplies = ref(false); // 控制主评论的子评论展开/收起

function countDescendants(comment) {
  if (!comment.replies || comment.replies.length == 0) return 0;
  return comment.replies.length + comment.replies.reduce((sum, c) => sum + countDescendants(c), 0);
}

  const props = defineProps({
    comment: {
      type: Object,
      required: true,
      validator: (val) => !!val.id && !!val.user
    },
    depth: {
      type: Number,
      default: 0
    }
  })
  // 点击外部关闭逻辑
const handleClickOutside = (event) => {
  if (replyWindow.value && !replyWindow.value.contains(event.target)) {
    showReply.value = false;
  }
};

// 添加全局点击监听
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

// 组件卸载时移除监听
onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
  const formatTime = (date) => {
        // 如果 date 不是 Date 对象，尝试转换
        if (!(date instanceof Date)) {
    date = new Date(date);
    // 检查转换后的日期是否有效
    if (isNaN(date.getTime())) {
      return "时间无效";
    }
  }
    // 与父组件相同的格式化逻辑
    const diff = Date.now() - date.getTime()
    const hours = Math.floor(diff / 3600000)
    if (hours < 1) return `${Math.floor(diff/60000)}分钟前`
    if (hours < 24) return `${hours}小时前`
    return date.toLocaleDateString()
  }
// 存储每条评论的回复输入框状态
const replyStates = reactive(new Map())

// 处理回复按钮点击
const toggleReplyInput = (comment) => {
  const state = replyStates.get(comment.id) || { content: '',targetUser: null }
  // 设置被回复的用户信息
  state.targetUser = comment.user
  // 自动填充 @用户名
  state.content = `@${comment.user.username} ` 
  showReply.value=!showReply.value
  console.log(showReply.value)
  replyStates.set(comment.id, state)

  // 下一帧聚焦输入框
  nextTick(() => {
    const input = document.querySelector(`#reply-input-${comment.id}`)
    input?.focus()
    // 将光标定位到 @ 后面
    input?.setSelectionRange(state.content.length, state.content.length)
  })
}

// 提交回复
const submitReply =async (parentComment) => {
  const state = replyStates.get(parentComment.id)
  if (!state.content.trim()) return

  // 将回复添加到父评论的 replies 数组
  const res=await insertCommentApi(parentComment.postId,state.content,parentComment.id)
  parentComment.replies.push(res.data);
  // 重置输入框状态
  state.content = ''
  showReply.value=false
}
const pushUser=(id)=>{
  if(user.value.id==id){
    router.push(`/user`)
  }else{
    router.push(`/visitor/${id}`)
  }
}
const toggleLike =async (comment) => {
  const res=await toggleLikeApi(user.value.id,null,comment.id)
  if(comment.isLike){
    comment.likesCount-=1
  }else{
    comment.likesCount+=1
  }
  comment.isLike=!comment.isLike
}
  </script>
  
  <style scoped>
  .comment-node {
    border-left: 2px solid #eee;
  }
  .chilren{
    margin-left:30px;
    font-size: 0.9em;
  }
  .meta {
    display: flex;
    font-size: 0.9em;
    height: 20px;
  }
  .username{
    color: #000000;
  }
  .commentContent{
    color: #666;
    font-size: 0.9em;
  }
  
  .mention {
    color: #00a1d6;
    margin-right: 4px;
  }
  
  .actions {
    align-items: center;
    margin-left: 52px;
  }

  .time{
    font-size: 10px;
    color: #9499a0;
    margin-top: 4px;
  }
  
  button {
    background: none;
    border: none;
    color: #00a1d6;
    cursor: pointer;

  }
.picAuthor{
  width: 40px;  
  height: 40px;
  border-radius: 50%;
  margin-right: 12px;
}
.reply-input {
  margin-left: 15px;
  margin-top: 8px;
}
.toggle-replies {
  color: #00a1d6;
  cursor: pointer;
  margin-left: 15px;
  font-size: 13px;
  margin-top: 4px;
}
/* 点赞状态样式 */
.b-like-icon.liked {
  color: #00B5E5; /* B站主题蓝色 */
}
.gasComment{
  display: flex;
  gap: 10px;
}
  </style>