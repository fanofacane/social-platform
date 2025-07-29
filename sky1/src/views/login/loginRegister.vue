<template>
    <div class="login-container">
      <div class="chiikawa-fall-group">
        <div
          v-for="(img, idx) in chiikawaImgs"
          :key="img"
          class="chiikawa-fall-outer"
          :class="[
            isFalling ? 'falling' : '',
            isRolling ? 'rolling' : '',
            isFadeOut ? 'fade-out' : ''
          ]"
          :style="{
            left: `${30 + idx * 90}px`,
            animationDelay: isRolling ? `${idx * 0.15}s` : `${idx * 0.1}s`
          }"
        >
          <img
            :src="img"
            class="chiikawa-fall-inner"
            :class="isRolling ? 'spinning' : ''"
            alt="chiikawa"
          />
        </div>
      </div>

      <form class="login-form" @submit.prevent="onLogin" ref="loginFormRef">
      <h2>欢迎</h2>
      <div class="input-group">
        <input v-model="username" type="text" placeholder="用户名" required />
      </div>
      <div class="input-group">
        <input v-model="password" type="password" placeholder="密码" required />
      </div>
      <button type="submit">登录</button>
    </form>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, nextTick } from 'vue'
  
  const username = ref('')
  const password = ref('')
  const isFalling = ref(false)
  const isRolling = ref(false)
  const isFadeOut = ref(false)
  
    // 你的图片路径
  const chiikawaImgs = [
    'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/97da95de01e22b042a5ea45dbae6a8e.jpg',
    'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/81204beaf42793334815f8470bc208c.jpg',
    'https://w71one-sp.oss-cn-hangzhou.aliyuncs.com/0d99a2224c9c311045de889fb41f13b.jpg'
  ]

    const onLogin = () => {
    alert(`欢迎, ${username.value}！`)
  }
  
  onMounted(async () => {
    await nextTick()
    isFalling.value = true
  
    // 掉落动画时长与CSS一致
    setTimeout(() => {
      isRolling.value = true
      // 4秒后淡出
      setTimeout(() => {
        isFadeOut.value = true
      }, 5000)
    }, 1200)
  })
  </script>
  
  <style scoped>
  .login-container {
    height: 400px;
    width: 500px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;
  }
  
  .chiikawa-fall-group {
    position: absolute;
    top: 0;
    left: 50%;
    width: 300px;
    height: 120px;
    transform: translateX(-50%);
    z-index: 3;
    pointer-events: none;
  }
  
  .chiikawa-fall-outer {
    position: absolute;
    top: -120px;
    width: 80px;
    height: 80px;
    opacity: 0;
  }
  
  .chiikawa-fall-inner {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    box-shadow: 0 4px 16px rgba(255, 200, 220, 0.18);
    background: #fff;
    object-fit: cover;
    display: block;
  }
  
  /* 掉落动画 */
  .chiikawa-fall-outer.falling {
    animation: chiikawaDrop 1.2s cubic-bezier(.68,-0.55,.27,1.55) forwards;
    opacity: 1;
  }
  
  @keyframes chiikawaDrop {
    0% {
      top: -120px;
      opacity: 0;
    }
    20% {
      opacity: 1;
    }
    100% {
      top: 1px; /* h2上方 */
      opacity: 1;
    }
  }
  
  /* 移动动画 */
  .chiikawa-fall-outer.rolling {
    animation:
      chiikawaDrop 1.2s cubic-bezier(.68,-0.55,.27,1.55) forwards,
      rollingMove 4s 1.2s forwards cubic-bezier(.68,-0.55,.27,1.55);
  }
  
  @keyframes rollingMove {
    0%   { transform: translateX(0);}
    20%  { transform: translateX(-100px);}
    60%  { transform: translateX(100px);}
    90%  { transform: translateX(0);}
    100% { transform: translateX(0);}
  }
  
  /* 旋转动画 */
  .chiikawa-fall-inner.spinning {
    animation: rollingSpin 1s linear infinite;
  }
  
  @keyframes rollingSpin {
    0%   { transform: rotate(0deg);}
    100% { transform: rotate(360deg);}
  }
  
  /* 淡出动画 */
  .chiikawa-fall-outer.fade-out {
    animation: fadeOut 0.8s forwards;
  }
  @keyframes fadeOut {
    to { opacity: 0; }
  }
  
  .login-form {
    background: #fff;
    border-radius: 24px;
    box-shadow: 0 8px 32px rgba(255, 200, 220, 0.18);
    display: flex;
    flex-direction: column;
    align-items: center;
    min-width: 320px;
    position: relative;
    z-index: 2;
    margin-bottom: 170px;
  }
  
  .login-form h2 {
    font-family: 'Comic Sans MS', 'Chalkboard SE', 'Comic Neue', cursive;
    color: #ff8bb0;
    margin-bottom: 32px;
    font-size: 1.6rem;
    letter-spacing: 2px;
  }
  
  .input-group {
    width: 100%;
    margin-bottom: 20px;
  }
  
  input[type="text"], input[type="password"] {
    width: 80%;
    padding: 12px 16px;
    border: 2px solid #ffe4f0;
    border-radius: 12px;
    font-size: 1rem;
    background: #fffbe9;
    transition: border 0.2s;
    outline: none;
    margin-left: 10px;
  }
  input:focus {
    border: 2px solid #ff8bb0;
  }
  
  button[type="submit"] {
    background: linear-gradient(90deg, #ffb6d5 0%, #ffe4f0 100%);
    color: #fff;
    font-size: 1.1rem;
    border: none;
    border-radius: 16px;
    padding: 12px 48px;
    cursor: pointer;
    box-shadow: 0 2px 8px rgba(255, 200, 220, 0.12);
    transition: background 0.2s, transform 0.1s;
    font-family: inherit;
  }
  button[type="submit"]:active {
    background: #ff8bb0;
    transform: scale(0.97);
  }
  
.login-form {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(255, 200, 220, 0.18);
  padding: 48px 32px 32px 32px;
  margin-top: 180px;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 320px;
  position: relative;
  z-index: 2;
  overflow: visible;
}

/* 左上角四分之一圆chiikawa */
.corner-chiikawa {
  position: absolute;
  top: 0;
  left: 0;
  width: 50%;
  height: 50%;
  border-radius: 0 0 100% 0;
  overflow: hidden;
  z-index: 3;
  /* 可选：加点阴影让头像更立体 */
  box-shadow: 0 4px 16px rgba(255, 200, 220, 0.18);
  pointer-events: none;
}
.corner-chiikawa img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  /* 让chiikawa脸部更居中显示，可微调 */
  transform: translate(-15%, -15%);
}
  </style>