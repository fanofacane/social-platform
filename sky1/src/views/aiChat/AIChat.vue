<template>
  <div class="chat-container">
    <div class="messages" ref="messagesContainer">
      <div
        v-for="(message, index) in messages"
        :key="index"
        class="message"
        :class="{ 'user-message': message.role === 'user', 'ai-message': message.role === 'assistant' }"
      >
        <div class="message-content">
          {{ message.content }}
        </div>
        <div class="message-time">
          {{ formatTime(message.timestamp) }}
        </div>
      </div>
    </div>

    <div class="input-area">
      <textarea
        v-model="inputMessage"
        @keydown.enter.exact.prevent="sendMessage"
        placeholder="输入消息..."
        :disabled="isLoading"
      ></textarea>
      <button @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
        {{ isLoading ? '发送中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted ,computed} from 'vue'
import {sendAI} from '@/api/chat'
import { useStore } from 'vuex';
const store = useStore();
const user = computed(() => store.state.user)
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)


const formatTime = (date) => {
  return new Date(date).toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return

  const messageContent = inputMessage.value.trim()
  inputMessage.value = ''

  // 添加用户消息
  const userMessage = {
    role: 'user',
    content: messageContent,
    timestamp: new Date()
  }
  messages.value.push(userMessage)

  // 添加助手消息占位
  const assistantMessage = {
    role: 'assistant',
    content: '',
    timestamp: new Date()
  }
  messages.value.push(assistantMessage)

  scrollToBottom()

  isLoading.value = true

  try {
      const res=await sendAI(messageContent,user.value.id)
      assistantMessage.content=res
  } catch (error) {
    console.error('发送消息失败:', error)
    messages.value[messages.value.length - 1].content = '消息发送失败，请重试'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  // 初始化示例消息
  messages.value.push({
    role: 'assistant',
    content: '您好！有什么可以帮您的？',
    timestamp: new Date()
  })
})
</script>

<style scoped>
.chat-container {
  max-width: 800px;
  margin: 0 auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #f5f5f5;
}

.message {
  max-width: 70%;
  margin: 10px 0;
  padding: 12px 16px;
  border-radius: 15px;
  word-wrap: break-word;
  white-space: pre-wrap;
}

.user-message {
  background-color: #007bff;
  color: white;
  margin-left: auto;
}

.ai-message {
  background-color: #e9ecef;
  margin-right: auto;
}

.message-content {
  font-size: 16px;
  line-height: 1.5;
}

.message-time {
  font-size: 12px;
  opacity: 0.8;
  margin-top: 4px;
  text-align: right;
}

.input-area {
  padding: 20px;
  background-color: white;
  border-top: 1px solid #ddd;
  display: flex;
  gap: 10px;
}

textarea {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: none;
  min-height: 50px;
  max-height: 150px;
}

button {
  padding: 12px 24px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #0056b3;
}
</style>