<template>
  <div class="chat-window">
    <div class="chat-header">
      <span>{{ friend?.username || '请选择好友' }}</span>
    </div>
    <div class="chat-body" ref="chatBody">
      <div v-for="msg in messages"  :class="['chat-message', msg.from == currentUser.id ? 'from-me' : 'from-other']">
        <template v-if="msg.from != currentUser.id">
          <img :src="friend?.picture" class="avatar left-avatar" @click="pushUser(friend.id)"/>
          <div class="bubble" v-if="friend.id!=10">{{ msg.content }}</div>
          <div>
              <div v-if="friend.id==10" >{{ AItext(msg.content) }}</div>
              <div v-if="friend.id==10" v-html="filterVideoTags(msg.content)"  class="cardshow"></div>
              <div v-if="friend.id==10" v-html="extractLinks(msg.content)" ></div>
          </div>
        </template>
        <template v-else>
          <img :src="currentUser.picture" class="avatar right-avatar" />
          <div class="bubble">{{ msg.content }}</div>
        </template>
      </div>
    </div>
    <div class="chat-input">
      <button class="emoji-btn" @click="showEmoji = !showEmoji">😀</button>
      <div v-if="showEmoji" class="emoji-panel">
        <span v-for="emoji in emojis" :key="emoji" class="emoji-item" @click="addEmoji(emoji)">{{ emoji }}</span>
      </div>
      <input v-model="input" @keyup.enter="send" placeholder="请输入消息..." />
      <button @click="send">发送</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, defineProps, defineEmits, inject ,computed} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
const router=useRouter();
const store = useStore();
const AIrouter = ref();
const user = computed(() => store.state.user)
const props = defineProps({
  messages: Array,
  friend: Object,
  currentUser: Object
})
const emit = defineEmits(['send'])
const input = ref('')
const chatBody = ref(null)
const showEmoji = ref(false)
const emojis = [
  '😀', '😁', '😂', '🤣', '😃', '😄', '😅', '😆', '😉', '😊', '😍', '😘', '😗', '😙', '😚', '😋', '😜', '😝', '😛', '🤑', '🤗', '🤔', '😎', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣', '😖', '😫', '😩', '😤', '😠', '😡', '😶', '😐', '😑', '😯', '😦', '😧', '😮', '😲', '😵', '😳', '😱', '😨', '😰', '😢', '😭', '😤', '😠', '😡', '🤬', '🤯', '😳', '🥺', '😬', '🤥', '😌', '😔', '😪', '🤤', '😴', '😷', '🤒', '🤕', '🤢', '🤮', '🥵', '🥶', '🥴', '😵', '🤯', '🤠', '🥳', '😎', '🤓', '🧐', '😕', '😟', '🙁', '☹️', '😮', '😯', '😲', '😳', '🥺', '😦', '😧', '😨', '😰', '😥', '😢', '😭', '😱', '😖', '😣', '😞', '😓', '😩', '😫', '🥱', '😤', '😡', '😠', '🤬', '😈', '👿', '💀', '☠️', '🤡', '👹', '👺', '👻', '👽', '👾', '🤖', '😺', '😸', '😹', '😻', '😼', '😽', '🙀', '😿', '😾'
]
function send() {
  if (input.value.trim()) {
    emit('send', input.value)
    input.value = ''
    showEmoji.value = false
    console.log(props.messages)
  }
}
function addEmoji(emoji) {
  input.value += emoji
}

watch( props.messages,(val)=>{
    async () => {
    await nextTick()
    if (chatBody.value) {
      chatBody.value.scrollTop = chatBody.value.scrollHeight
    }
  },
  { deep: true }
}
)

const pushUser=(id)=>{
  if(user.value.id==id){
    router.push(`/user`)
  }else{
    router.push(`/visitor/${id}`)
  }
}
const extractLinks=(htmlStr)=> {
    const regex = /<a [^>]*>.*?<\/a>/g;
    const matches = htmlStr.match(regex);
    const link=matches ? matches.join(' ') : '';
    return matches ? matches.join(' ') : ''; 
  }
const filterImgTags=(htmlStr)=> {
  const parser = new DOMParser();
  const doc = parser.parseFromString(htmlStr, 'text/html');
  const imgs = Array.from(doc.querySelectorAll('img'));
  // 将所有的img标签拼接成HTML字符串
  return imgs.map(img => img.outerHTML).join(' ');
}

const filterVideoTags=(htmlStr)=> {
  const parser = new DOMParser();
  const doc = parser.parseFromString(htmlStr, 'text/html');
  const imgs = Array.from(doc.querySelectorAll('video'));
  // 将所有的video标签拼接成HTML字符串
  return imgs.map(img => img.outerHTML).join(' ');
}
const AItext=(htmlStr)=>{
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlStr, 'text/html');

    // 删除所有a标签
    const links = Array.from(doc.querySelectorAll('a'));
    links.forEach(link => link.remove());

    const lik = Array.from(doc.querySelectorAll('img'));
    lik.forEach(link => link.remove());
    // 获取纯文本
  return doc.body.textContent || '';
}
</script>

<style scoped>
.chat-window {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 90vh;
  background: #fff;
  width: calc(60vw - 260px + 300px);
  min-width: 600px;
  overflow: hidden;
}

.chat-header {
  height: 56px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  padding: 0 24px;
  font-weight: bold;
  font-size: 18px;
  background: #f7f7f7;
}

.chat-body {
  flex: 1;
  overflow-y: auto;
  padding: 24px 16px 12px 16px;
  background: #f9f9f9;
}

.chat-message {
  display: flex;
  align-items: flex-end;
  margin-bottom: 18px;
}

.from-me {
  flex-direction: row-reverse;
}

.from-other {
  flex-direction: row;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  background: #fff;
  border: 1px solid #eee;
}

.left-avatar {
  margin-right: 10px;
  margin-left: 0;
}

.right-avatar {
  margin-left: 10px;
  margin-right: 0;
}

.bubble {
  max-width: 60%;
  background: #e6f7ff;
  border-radius: 16px;
  padding: 10px 16px;
  font-size: 15px;
  color: #222;
  position: relative;
  word-break: break-all;
}

.from-me .bubble {
  background: #95ec69;
  color: #222;
}

.chat-input {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #eee;
  background: #fafafa;
  position: relative;
}

.chat-input input {
  flex: 1;
  height: 36px;
  border: 1px solid #ddd;
  border-radius: 18px;
  padding: 0 16px;
  font-size: 15px;
  outline: none;
  margin-right: 10px;
}

.chat-input button {
  height: 36px;
  padding: 0 18px;
  border: none;
  border-radius: 18px;
  background: #2d8cf0;
  color: #fff;
  font-size: 15px;
  cursor: pointer;
  transition: background 0.2s;
}

.chat-input button:hover {
  background: #1976d2;
}

.emoji-btn {
  background: none;
  border: none;
  font-size: 22px;
  margin-right: 8px;
  cursor: pointer;
}

.emoji-panel {
  position: absolute;
  bottom: 48px;
  left: 16px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 8px 12px;
  display: flex;
  flex-wrap: wrap;
  max-width: 320px;
  max-height: 180px;
  overflow-y: auto;
  z-index: 10;
}

.emoji-item {
  font-size: 22px;
  margin: 4px;
  cursor: pointer;
  transition: transform 0.1s;
}

.emoji-item:hover {
  transform: scale(1.2);
}
.cardshow{
  margin-bottom: 5px; /* 留出间距 */
  display: flex;
  flex-direction: row; /* 横向排列 */
  flex-wrap: wrap;     /* 内容多了换行 */
  gap: 10px;           
}
</style>