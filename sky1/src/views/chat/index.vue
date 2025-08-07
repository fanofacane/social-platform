<script setup>
import { ref, computed ,onMounted, watch} from 'vue'
import FriendList from '@/components/chat/FriendList.vue'
import ChatWindow from '@/components/chat/ChatWindow.vue'
import {queryChatUserList,queryChatList,clearUnread} from '@/api/chat'
import { useRoute, useRouter } from 'vue-router'
import {sendAI} from '@/api/chat'
import { useStore } from 'vuex';
const store = useStore();
const route=useRoute()
const user = computed(() => store.state.user)
const currentUser = ref(user)
const friends = ref([])
const messages = ref([])
const activeFriendId = ref(null)
const ws= computed(() => store.state.ws)
const chatMessages = computed(() => store.state.chatMessages)
const activeFriend = computed(() => friends.value.find(f => f.id == activeFriendId.value))
onMounted(()=>{
  queryUserList();
  getVistorUser();
})
// 监听消息状态的变化
watch(chatMessages, (val) => {
  console.log("chat收到")
    messages.value.push(val);
    console.log(val)
    const friend = friends.value.find(f => f.id == val.from)
    friend.content=val.content
    friend.unreadCount+=1
});
const queryUserList=async()=>{
  const res=await queryChatUserList(route.params.id);
  route.params.id?activeFriendId.value=route.params.id:activeFriendId.value=null;
  friends.value=res.data
}

const getChatList=async(id)=>{
  const res=await queryChatList(id);
  messages.value=res.data;
  console.log(messages.value)
}
const getVistorUser=()=>{
  if(route.params.id){
    getChatList(route.params.id)
  }
}

const filteredMessages = computed(() =>
  messages.value.filter(
  m =>
    (m.from == currentUser.value.id && m.to == activeFriendId.value) ||
    (m.from == activeFriendId.value && m.to == currentUser.value.id)
    )
  )

const selectFriend= async (id) => {

  activeFriendId.value = id
  getChatList(id);
  // 清除未读
  const friend = friends.value.find(f => f.id == id)
  if (friend.unreadCount > 0){
    friend.unreadCount = 0
    await clearUnread(id);
}
}

const handleSend=async(content)=> {
  const now = new Date()
  const createTime = `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,'0')}-${String(now.getDate()).padStart(2,'0')} ${String(now.getHours()).padStart(2,'0')}:${String(now.getMinutes()).padStart(2,'0')}`
  const msg = {
    // id: messages.value.length + 1,
    from: currentUser.value.id,
    to: activeFriendId.value,
    content,
    createTime,
    username:currentUser.value.username,
    picture:currentUser.value.picture
  }
  messages.value.push(msg)
  // 更新好友列表最后一条消息
  const friend = friends.value.find(f => f.id == activeFriendId.value)
  if (friend) friend.content = content
  if(friend.id==10){
    const res=await sendAI(content,currentUser.value.id)
  const Ai = {
    from: activeFriendId.value,
    to: currentUser.value.id,
    // content:res.data,
    content:res,
    createTime,
    username:friend.username,
    picture:friend.picture
  }
    console.log(Ai)
    messages.value.push(Ai);
  }else{
    // 通过 WebSocket 发送消息到后端
  if (ws.value && ws.value.readyState == 1) {
    console.log('前端发送的msg'+JSON.stringify(msg))
    ws.value.send(JSON.stringify(msg))
  }else{
    console.log('WebSocket 连接未建立，消息无法发送')
  }
  }
}
</script>
<template>
  <div class="app-container">
    <FriendList
      :friends="friends"
      :activeId="activeFriendId"
      @select="selectFriend"
    />
    <ChatWindow
      :messages="filteredMessages"
      :friend="activeFriend"
      :currentUser="currentUser"
      @send="handleSend"
    />
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  height: 100%;
  background: #f0f2f5;
}
</style>
