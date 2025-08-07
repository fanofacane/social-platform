<script setup>
import{ref,onMounted,computed,onBeforeUnmount,watch,h} from 'vue'
import index from '@/views/login/index.vue';
import headBar from '@/views/headBar/Bar.vue';
import {logout} from '@/api/login'
import { ElNotification ,ElMessage} from 'element-plus'
import { useStore } from 'vuex'
import { RouterLink ,useRouter} from 'vue-router';
const store = useStore();
const router = useRouter()
const dialogVisible = ref(false);
const user = computed(() => store.state.user)
const chatMessages = computed(() => store.state.chatMessages)
const notifications = computed(() => store.state.notifications)
const isLogin = computed(() => store.state.isLogin)
onMounted (() =>{
  store.dispatch('initWebSocket');
})
onBeforeUnmount(() => {
  const ws = store.state.ws
  if (ws) {
    ws.close()
    console.log('WebSocket已断开')
  }
})
// 监听消息状态的变化
watch(chatMessages, (val) => {
    chatMessage();
});
// 监听通知状态的变化
watch(notifications, (val) => {
    offlineNotice();
});
watch(isLogin,(val)=>{
  if(val){
    store.dispatch('initWebSocket');
  }
})

const handleClick = (r) => {

  // if(localStorage.getItem('loginUser')==null){
  //   e.preventDefault(); // 阻止默认导航
  //   dialogVisible.value = true;
  // }
      
      if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      // e.preventDefault();
      return
  }
  router.push('/'+r)
}
const handleLogout = async() => {
  await logout(user.value.id)
  store.commit('logout');
};
const open1 = () => {
    ElNotification({
    title: '维护',
    message: '该功能还在开发中',
    type: 'error',
    })
}
const offlineNotice = () => {
    ElNotification({
    title: '警告',
    message: notifications.value.content,
    type: 'error',
    })
}
const chatMessage = () => {
    ElNotification({
    title: '消息通知',
    message: h('div', { class: 'custom-notification' }, [
      h('img', {
        src: chatMessages.value.picture, // 替换为你的头像地址
        class: 'notification-avatar',
        style: { 
          width: '40px', 
          height: '40px', 
          borderRadius: '50%', 
          marginRight: '10px' 
        }
      }),
      h('span',{
        class: 'notification-text'
      }, chatMessages.value.username+':'),
      h('span', chatMessages.value.content)
    ]),
    duration: 3000,
    })
}

</script>

<template>

    <div class="main">
    <el-container hegiht="100%">
      <el-aside width="150px" style=" ">
        <div class="side-bar">
        <ul class="channel-list">
          <router-link to="/index" class="custom-link" >
          <li>
            <CirclePlus style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">精选</span >
          </li>
          </router-link>
          <router-link to="/origin" class="custom-link" >
            <li class="active-channel">
              <a class="link-wrapper"><House style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">推荐</span></a>
            </li>
          </router-link>
          <!-- <router-link to="/follow" class="custom-link" @click.native="handleClick"> -->
          <li @click="handleClick('follow')">
            <Bell style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">动态</span >
          </li> 
        <!-- </router-link> -->
          <li @click="open1">
            <Star style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel" >漫画</span >
          </li>
          <!-- <RouterLink to="/shop" class="custom-link" >
          <li>
            <User style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">商城</span >  
          </li>
          </RouterLink> -->
          <RouterLink to="/anime" class="custom-link">
            <li >
              <CirclePlus style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">番剧</span >
            </li>
          </RouterLink>
          <!-- <router-link to="/chat" class="custom-link" @click.native="handleClick"> -->
          <li @click="handleClick('chat')">
            <CirclePlus style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">消息</span >
          </li>
          <!-- </router-link> -->
          <!-- <router-link to="/user" class="custom-link" @click.native="handleClick"> -->
          <li @click="handleClick('user')">
            <User style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel">我的</span>
          </li>
        <!-- </router-link> -->
          <li>
            <User style="width: 1em; height: 1em; margin-right: 8px" /><span class="channel" @click="handleLogout">退出</span>
          </li>
        </ul>

      </div>
      </el-aside>
    

      <el-container>
        <el-header>
          <headBar></headBar>
      </el-header>
      <el-main height="100%" class="mainclass">
        <div>
        <router-view />
      </div>
      <el-dialog class="custom"  v-model="dialogVisible" :close-on-click-modal="false" destroy-on-close>
        <index @loginSuccess="dialogVisible = false;"></index>  
      </el-dialog>
      </el-main>
    </el-container>
    </el-container>
  </div>
</template>

<style>
  .custom {
    width:30%!important;
    min-width:"100px" !important;
  }
  .main {
    display: flex;

    .side-bar {
      height: 100%;
      background-color: #fff;
      display: flex;
      flex-direction: column;
      flex-shrink: 0;
      margin-top: 80px;
      position: fixed;
      overflow: visible;
      .channel-list {
        height: 100%;
        width: 100px;
        .custom-link{
          text-decoration: none;
        } 
        li {
          
          min-height: 48px;
          display: flex;
          align-items: center;
          cursor: pointer;
          margin-bottom: 8px;
          color: rgba(51, 51, 51, 0.6);
        }

        .channel {
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }
      }

      .information-container {
        display: inline-block;
        width: 100%;
        color: #333;
        font-size: 16px;
        position: absolute;
        bottom: 0;

        .information-pad {
          z-index: 16;
          margin-bottom: 4px;
          width: 100%;

          .container {
            width: 100%;
            background: #fff;
            box-shadow:
              0 4px 32px 0 rgba(0, 0, 0, 0.08),
              0 1px 4px 0 rgba(0, 0, 0, 0.04);
            border-radius: 12px;

            .divider {
              margin: 0px 12px;
              list-style: none;
              height: 0;
              border: 1px solid rgba(0, 0, 0, 0.08);
              border-width: 1px 0 0;
            }

            .group-wrapper {
              padding: 4px;

              .group-header {
                display: flex;
                align-items: center;
                padding: 0 12px;
                font-weight: 400;
                height: 32px;
                color: rgba(51, 51, 51, 0.6);
                font-size: 12px;
              }

              .menu-item {
                height: 40px;
                color: rgba(51, 51, 51, 0.8);
                font-size: 16px;
                border-radius: 8px;
                display: flex;
                align-items: center;
                padding: 0 12px;
                font-weight: 400;

                .icon {
                  color: rgba(51, 51, 51, 0.3);
                  margin-left: auto;
                }

                .component {
                  margin-left: auto;
                }

                .multistage-toggle {
                  position: relative;
                  background: rgba(0, 0, 0, 0.03);
                  display: flex();
                  padding: 2px;
                  border-radius: 999px;
                  cursor: pointer;

                  .active {
                    background: #fff;
                    box-shadow:
                      0 2px 8px 0 rgba(0, 0, 0, 0.04),
                      0 1px 2px 0 rgba(0, 0, 0, 0.02);
                    color: #333;
                  }

                  .toggle-item {
                    border-radius: 999px;
                    background: transparent;
                    color: rgba(51, 51, 51, 0.6);

                    .icon-wrapper {
                      width: 24px;
                      height: 24px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      cursor: pointer;
                    }
                  }
                }
              }
            }
          }
        }

        .information-wrapper {
          -webkit-user-select: none;
          user-select: none;
          cursor: pointer;
          position: relative;
          margin-bottom: 20px;
          height: 48px;
          width: 100%;
          display: flex;
          font-weight: 600;
          align-items: center;
          border-radius: 999px;
        }
      }
    }

    .main-content {
      width: 100%;
    }

    .main-content {
      @media screen and (min-width: 960px) and (max-width: 1191px) {
        padding-left: calc(-6px + 25vw);
      }

      @media screen and (min-width: 1192px) and (max-width: 1423px) {
        padding-left: calc(-4.8px + 20vw);
      }

      @media screen and (min-width: 1424px) and (max-width: 1727px) {
        padding-left: calc(-5.33333px + 16.66667vw);
      }

      @media screen and (min-width: 1728px) {
        padding-left: 282.66667px;
      }
    }
  }
.notification-text {
  color: #000000;
  font-size: 15px;
  margin-bottom: 100px;
  font-weight: bold;
}
</style>
