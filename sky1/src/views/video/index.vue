<template>
    <div class="app-container">
    
      <div
        class="video-container"
        :class="{ 'with-comments': drawer }"
        @touchstart="onTouchStart"
        @touchmove="onTouchMove"
        @touchend="onTouchEnd"
        @mousedown="onMouseDown"
        @mousemove="onMouseMove"
        @mouseup="onMouseUp"
        @mouseleave="onMouseUp"
        @wheel="onWheel"
      >
        <div class="video-wrapper" :style="wrapperStyle">
          <div v-for="(video, idx) in videoList" class="video-item">
            <video
              id="myvideo"
              object-fit="contain"
              :ref="el => setVideoRef(el, idx)"
              :src="video.pictureUrl"
              class="video-player"
              :autoplay="currentIndex === idx"
              :controls="false" 
              loop
              playsinline
              :muted.value
              :volume.value
              @play="onPlay(idx)"
              @pause="onPause(idx)"
              @timeupdate="onTimeUpdate(idx, $event)"
              @loadedmetadata="onLoadedMetadata(idx, $event)"
              @click="togglePlayPause(idx)"
            ></video>
            <div class="video-title">
              <span>@{{ video.author  }}</span>
              <span style="font-size: small; margin-left: 10px;">{{ LocalFormatTime(video.createTime) }}</span>
              <br/>
              <span style="font-size: small;font-weight: bold;">{{ video.title }}</span>  
            </div>
            <!-- 右侧操作栏 -->
            <div class="right-bar">
              <div>
                <img :src="video.picture" class="avatar" @click="pushUser(video.authorId)"/>
              </div>
              
              <div class="action-btn" @click="toggleLike(video)">
                  <svg viewBox="0 0 24 24"  xmlns="http://www.w3.org/2000/svg" width="2em" height="2em" focusable="false"><path :fill="video.isLike ? '#ff0000' : '#FFFFFF'" d="M2.00534 9C2.00179 8.91711 2 8.83376 2 8.75C2 5.57436 4.57436 3 7.75 3C9.43372 3 10.9484 3.72368 12 4.87696C13.0516 3.72368 14.5663 3 16.25 3C19.4256 3 22 5.57436 22 8.75C22 8.83376 21.9982 8.91711 21.9947 9H22C22 13.5738 14.6263 19.141 12.5425 20.6229C12.2133 20.8571 11.7867 20.8571 11.4575 20.6229C9.37369 19.141 2 13.5738 2 9H2.00534Z"></path></svg>
                  <span>{{ video.likesCount }}</span>
              </div>
              
              <div class="action-btn" @click="openComment(video)">
                  <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 99 99" width="3em" height="3em" preserveAspectRatio="xMidYMid meet" style="width: 100%; height: 100%; transform: translate3d(0px, 0px, 0px); content-visibility: visible;"><defs><clipPath id="__lottie_element_2"><rect width="99" height="99" x="0" y="0"></rect></clipPath></defs><g clip-path="url(#__lottie_element_2)"><g transform="matrix(0.6616870760917664,0,0,0.6616870760917664,76.31503295898438,52.850440979003906)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(3,0,0,3,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M-4.644999980926514,4.482999801635742 C-7.25,7.818999767303467 -13.482000350952148,8.300000190734863 -13.482000350952148,8.300000190734863 C-13.482000350952148,8.300000190734863 -14.413999557495117,11.48799991607666 -12.135000228881836,10.821000099182129 C-9.855999946594238,10.154000282287598 -7.25,8.152000427246094 -4.644999980926514,4.482999801635742z M-17.36,-1.04 C-17.36,-0.16 -18.07,0.56 -18.96,0.56 C-18.96,0.56 -18.96,0.56 -18.96,0.56 C-19.85,0.56 -20.57,-0.16 -20.57,-1.04 C-20.57,-1.92 -19.85,-2.64 -18.96,-2.64 C-18.07,-2.64 -17.36,-1.92 -17.36,-1.04z M-11.89,-1.04 C-11.89,-0.16 -12.61,0.56 -13.5,0.56 C-13.5,0.56 -13.5,0.56 -13.5,0.56 C-14.39,0.56 -15.11,-0.16 -15.11,-1.04 C-15.11,-1.92 -14.39,-2.64 -13.5,-2.64 C-12.61,-2.64 -11.89,-1.92 -11.89,-1.04z M-6.43,-1.04 C-6.43,-0.16 -7.15,0.56 -8.04,0.56 C-8.04,0.56 -8.04,0.56 -8.04,0.56 C-8.92,0.56 -9.64,-0.16 -9.64,-1.04 C-9.64,-1.92 -8.92,-2.64 -8.04,-2.64 C-7.15,-2.64 -6.43,-1.92 -6.43,-1.04z M-5.79,5.98 C-3.56,3.75 -2.25,1.42 -2.25,-1.29 C-2.25,-6.79 -7.29,-11.25 -13.5,-11.25 C-19.71,-11.25 -24.75,-6.79 -24.75,-1.29 C-24.75,4.21 -19.55,7.99 -13.34,7.99 C-13.34,7.99 -13.34,11.06 -13.34,11.06 C-13.34,11.06 -8.51,8.72 -5.79,5.98z"></path><g opacity="1" transform="matrix(1,0,0,1,-6.75,-7.456999778747559)"><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g><g opacity="1" transform="matrix(-1,0,0,1,0,0)"></g></g></g></g></g></svg>
                  <span>{{ video.commentsCount }}</span>
              </div>

              <div class="action-btn" @click="toggleCollection(video)">
                  <svg data-v-5f171eb7="" width="28" height="28" viewBox="0 0 28 28" xmlns="http://www.w3.org/2000/svg" class="video-fav-icon video-toolbar-item-icon"><path :fill="video.isCollection ? '#FACE15' : '#FFFFFF'" clip-rule="evenodd" d="M19.8071 9.26152C18.7438 9.09915 17.7624 8.36846 17.3534 7.39421L15.4723 3.4972C14.8998 2.1982 13.1004 2.1982 12.4461 3.4972L10.6468 7.39421C10.1561 8.36846 9.25639 9.09915 8.19315 9.26152L3.94016 9.91102C2.63155 10.0734 2.05904 11.6972 3.04049 12.6714L6.23023 15.9189C6.96632 16.6496 7.29348 17.705 7.1299 18.7605L6.39381 23.307C6.14844 24.6872 7.62063 25.6614 8.84745 25.0119L12.4461 23.0634C13.4276 22.4951 14.6544 22.4951 15.6359 23.0634L19.2345 25.0119C20.4614 25.6614 21.8518 24.6872 21.6882 23.307L20.8703 18.7605C20.7051 17.705 21.0339 16.6496 21.77 15.9189L24.9597 12.6714C25.9412 11.6972 25.3687 10.0734 24.06 9.91102L19.8071 9.26152Z"></path></svg>
                  <span>{{ video.collectionsCount }}</span>
              </div>

              <div class="action-btn">
                  <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 99 99" width="3em" height="3em" preserveAspectRatio="xMidYMid meet" style="width: 100%; height: 100%; transform: translate3d(0px, 0px, 0px); content-visibility: visible;"><defs><clipPath id="__lottie_element_182"><rect width="99" height="99" x="0" y="0"></rect></clipPath></defs><g clip-path="url(#__lottie_element_182)"><g transform="matrix(0.6400325298309326,0,0,0.6400325298309326,49.20100021362305,52.64942169189453)" opacity="1" style="display: block;"><g opacity="1" transform="matrix(3,0,0,3,0,0)"><path fill="rgb(255,255,255)" fill-opacity="1" d=" M0.8149999976158142,4.76200008392334 C0.8149999976158142,4.76200008392334 0.8149999976158142,-4.929999828338623 0.8149999976158142,-4.929999828338623 C0.8149999976158142,-4.929999828338623 -5.2179999351501465,-5.49399995803833 -9.244999885559082,-1.1749999523162842 C-13.272000312805176,3.1440000534057617 -13.517000198364258,11.817000389099121 -10.842000007629395,7.74399995803833 C-8.062000274658203,3.510999917984009 0.8149999976158142,4.76200008392334 0.8149999976158142,4.76200008392334z M10.514378547668457,2.995445728302002 C12.168766021728516,1.5337685346603394 12.21668815612793,-0.8918964862823486 10.621323585510254,-2.4177768230438232 C10.621323585510254,-2.4177768230438232 3.705676317214966,-9.032222747802734 3.705676317214966,-9.032222747802734 C2.110311985015869,-10.558103561401367 0.8149999976158142,-10.263825416564941 0.8149999976158142,-8.375499725341797 C0.8149999976158142,-8.375499725341797 0.8149999976158142,-4.953999996185303 0.8149999976158142,-4.953999996185303 C0.8240000009536743,1.8849999904632568 0.8149999976158142,1.0850000381469727 0.8149999976158142,1.0850000381469727 C0.8149999976158142,3.292600154876709 0.8149999976158142,6.536843776702881 0.8149999976158142,8.324999809265137 C0.8149999976158142,8.324999809265137 0.8149999976158142,8.324999809265137 0.8149999976158142,8.324999809265137 C0.8149999976158142,10.113155364990234 2.158234119415283,10.378231048583984 3.812621593475342,8.916553497314453 C3.812621593475342,8.916553497314453 10.514378547668457,2.995445728302002 10.514378547668457,2.995445728302002z"></path></g></g></g></svg>
              </div>

            </div>
            <!-- 自定义进度条和时间显示 -->
            <div class="custom-progress">
              <div class="progress-info">
                <span>{{ formatTime(video.currentTime) }}</span>
                <div class="progress-bar-bg">
                  <div class="progress-bar-fg" :style="{ width: video.progress + '%' }"></div>
                </div>
                <span>{{ formatTime(video.duration) }}</span>
              </div>
            </div>
            <!-- 音量控制组件 -->
            <div class="volume-control">
              <button class="mute-btn" @click="toggleMute(idx)">
                <svg v-if="muted" viewBox="0 0 24 24" width="24" height="24"><path fill="#fff" d="M16.5 12l4.5 4.5-1.5 1.5L15 13.5l-4.5 4.5H6v-6H2v-3h4V6h4.5l4.5 4.5 1.5-1.5L16.5 12z"/></svg>
                <svg v-else viewBox="0 0 24 24" width="24" height="24"><path fill="#fff" d="M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.06c1.48-.74 2.5-2.26 2.5-4.03z"/></svg>
              </button>
              <input type="range" min="0" max="1" step="0.01" v-model.number="volume" @input="setVolume(idx, volume)" />
            </div>
          </div>
        </div>
      </div>
      <!-- 评论区 -->
      <transition name="slide">
        <div v-if="drawer" class="comment-section">
          <div class="comment-header">
            <h3>评论 ({{ currentVideo?.commentsCount || 0 }})</h3>
            <button class="close-btn" @click="drawer = false">×</button>
        </div>
        <div class="comment-list">
            <CommentNode
                v-for="comment in comments"
                :key="comment.id"
                :comment="comment"
                :depth="0"
                @reply="handleReply"
            />
        </div>
          <div class="comment-input">
            <input 
              v-model="commentText"
              placeholder="说点什么..."
              @keyup.enter="submitComment()"
            />
            <button @click="submitComment()">发送</button>
          </div>
        </div>
      </transition>
    </div>
  </template>
  
<script setup>
import { ref, computed, nextTick, watch ,onMounted} from 'vue'
import { useStore } from 'vuex';
import CommentNode from '@/views/follow/CommentNode.vue'
import {queryAllCardApi,toggleLikeApi,toggleCollectionApi} from '@/api/index';
import {getPostCommentsApi,insertCommentApi} from '@/api/comment';
import {followUserApi,checkFollowerApi,unFollowUserApi} from '@/api/follow';
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
const router=useRouter();
const store = useStore();
const user = computed(() => store.state.user)
const isLogin = computed(() => store.state.isLogin);
const volume =ref(0.5)
const muted =ref(true)
const drawer=ref(false)
const commentText = ref('')
const selectedUser = ref(null)
const comments=ref([])
const expandedPostId = ref(null)
const currentVideo = ref(null)
const currentVideoId=ref()
const nextCursor = ref(null);    // 分页游标
const isLoading = ref(false);    // 加载状态
const hasMore = ref(true);       // 是否还有更多数据

  const videoList=ref([{            
            id:'',
            title:'',
            pictureUrl:'',
            content:'',
            author:'',
            picture:'',
            likesCount:'',
            createTime:'',
            updateTime:'',
            authorId:'',
            isFollow:'',
            isLike:'',
            isCollection:'',
            collectionsCount:'',
            progress: 0,
            duration: 0,
            currentTime: 0,
          }])

onMounted(()=>{
  getVideoList()
})
// 获取评论
const fetchComments = async (postId) => {
  try {
    const response = await getPostCommentsApi(postId,user.value.id);
    comments.value=response.data;
    expandedPostId.value = expandedPostId.value === postId ? null : postId;
  } catch (err) {
    console.error('评论加载失败')
  }

}
const getVideoList= async() =>{
    const res=await queryAllCardApi(user.value.id,'video')
    if(res.code){
      videoList.value=res.data.data
      nextCursor.value=res.data.nextCursor
    }
  }
  const currentIndex = ref(0)
  const startY = ref(0)
  const deltaY = ref(0)
  const isSwiping = ref(false)
  const videoRefs = ref([])
  const isMouseDown = ref(false)
  let wheelTimeout = null
  
  const wrapperStyle = computed(() => ({
    // transform: `translateY(${-currentIndex.value * 87 + (isSwiping.value ? deltaY.value / window.innerHeight * 100 : 0)}vh)`,
    transform: `translateY(${-currentIndex.value * 650 + (isSwiping.value ? deltaY.value / window.innerHeight * 100 : 0)}px)`,
    transition: isSwiping.value ? 'none' : 'transform 0.3s cubic-bezier(.4,0,.2,1)'
  }))
  
  const LocalFormatTime = (date) => {
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
  function formatTime(sec) {
    if (!sec || isNaN(sec)) return '00:00'
    const m = Math.floor(sec / 60)
    const s = Math.floor(sec % 60)
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
  }

  // 触摸事件
  function onTouchStart(e) {
    isSwiping.value = true
    startY.value = e.touches[0].clientY
  }
  function onTouchMove(e) {
    if (!isSwiping.value) return
    deltaY.value = e.touches[0].clientY - startY.value
  }
  function onTouchEnd() {
    finishSwipe()
  }
  
  // 鼠标事件
  function onMouseDown(e) {
    isMouseDown.value = true
    isSwiping.value = true
    startY.value = e.clientY
  }
  function onMouseMove(e) {
    if (!isMouseDown.value) return
    deltaY.value = e.clientY - startY.value
  }
  function onMouseUp() {
    if (!isMouseDown.value) return
    isMouseDown.value = false
    finishSwipe()
  }
  
  // 鼠标滚轮事件
  function onWheel(e) {
    if (wheelTimeout) return // 节流，防止滚动过快
    if (e.deltaY > 0 && currentIndex.value < videoList.value.length - 1) {
      currentIndex.value++
    } else if (e.deltaY < 0 && currentIndex.value > 0) {
      currentIndex.value--
    }
    wheelTimeout = setTimeout(() => {
      wheelTimeout = null
    }, 400)
  }
  
  function finishSwipe() {
    isSwiping.value = false
    if (Math.abs(deltaY.value) > 50) {
      if (deltaY.value < 0 && currentIndex.value < videos.value.length - 1) {
        currentIndex.value++
      } else if (deltaY.value > 0 && currentIndex.value > 0) {
        currentIndex.value--
      }
    }
    deltaY.value = 0
  }
  
  // 监听 currentIndex，切换时自动播放当前视频并暂停其他视频
  watch(currentIndex, (val) => {
    nextTick(() => {
      videoRefs.value.forEach((video, idx) => {
        if (video) {
          if (idx == val) {
            video.play();// 播放当前视频
            video.currentTime = 0;
            video.play().catch(() => {});
            currentVideoId.value=videoList.value[idx].id
            currentVideo.value=videoList.value[idx]
            if(drawer.value){
              fetchComments(videoList.value[idx].id)
            }
          } else {
            video.pause();// 暂停其他视频
            video.currentTime = 0;
          }
          video.volume = volume.value
          video.muted = muted.value
        }
      });
        // 预判下一个视频是否存在
  const nextIndex = val + 1;
  if (nextIndex >= videoList.value.length) {
    // 如果不存在，拉取新数据
    console.log(videoList.value)
    loadMore();
  }
    });
  });
const loadMore=async()=>{
  if (!hasMore.value || isLoading.value) return;
  isLoading.value = true;
  try {
    let cursor;
    let response;
    const newVideos=ref([{            
            id:'',
            title:'',
            pictureUrl:'',
            content:'',
            author:'',
            picture:'',
            likesCount:'',
            createTime:'',
            updateTime:'',
            authorId:'',
            isFollow:'',
            isLike:'',
            isCollection:'',
            collectionsCount:'',
            progress: 0,
            duration: 0,
            currentTime: 0,
          }])
    if (nextCursor.value) cursor = nextCursor.value;
    response = await queryAllCardApi(user.value.id,'video',cursor);
    newVideos.value = response.data.data;

    if (newVideos.value.length == 0) {
      hasMore.value = false;
      
    } else {
      videoList.value = [...videoList.value, ...newVideos.value];
      nextCursor.value = response.data.nextCursor;
    }
  } catch (error) {
    console.error('加载失败:', error);
  } finally {
    isLoading.value = false;
  }
}
  function onPlay(idx) {}
  function onPause(idx) {}
  
const toggleLike =async (video) => {
    if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
  const res=await toggleLikeApi(user.value.id,video.id)
  if(video.isLike){
    video.likesCount-=1
  }else{
    video.likesCount+=1
  }
  video.isLike=!video.isLike
}
const toggleCollection =async (video) => {
    if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
  await toggleCollectionApi(user.value.id,video.id)
    if(video.isCollection){
    video.collectionsCount-=1
  }else{
    video.collectionsCount+=1
  }
  video.isCollection=!video.isCollection
}

const openComment = (video) => {
  currentVideo.value = video
  drawer.value = true
  currentVideoId.value=video.id
  fetchComments(video.id)
}

const handleReply = (targetComment) => {
  replyTarget.value = {
    id: targetComment.id,
    user: targetComment.user
  }
}
  function onTimeUpdate(idx, e) {
    const video = e.target
    videoList.value[idx].progress = (video.currentTime / (video.duration || 1)) * 100
    videoList.value[idx].currentTime = video.currentTime
  }
  function onLoadedMetadata(idx, e) {
    const video = e.target
    videoList.value[idx].duration = video.duration
  }
  
  function toggleMute(idx) {
    const v = videoList.value[idx]
    v.muted = !v.muted
    if (videoRefs.value[idx]) {
      videoRefs.value[idx].muted = v.muted
      muted.value=v.muted;

    }
  }
  function setVolume(idx, value) {
    volume.value=value
    muted = value === 0
    if (videoRefs.value[idx]) {
      videoRefs.value[idx].volume = value
      videoRefs.value[idx].muted = v.muted
      
    }
  }
  
  function setVideoRef(el, idx) {
    if (el) {
      videoRefs.value[idx] = el
    }
  }
  function togglePlayPause(idx) {
    const video = videoRefs.value[idx];
    if (!video) return;
    if (video.paused) {
      video.play();
    } else {
      video.pause();
    }
  }
const submitComment = async() => {
    if(!isLogin.value){
        ElMessage({
        type: 'info',
        message: '请先登录',
      })
      return
  }
  if (!commentText.value.trim()) return
    const res = await insertCommentApi(currentVideoId.value,commentText.value)
    const response = await getPostCommentsApi(currentVideoId.value,user.value.id);
    comments.value=response.data;
    currentVideo.value.commentsCount+=1
    commentText.value = ''
}
const pushUser=(id)=>{
  if(user.value.id==id){
    router.push(`/user`)
  }else{
    router.push(`/visitor/${id}`)
  }
}
  </script>
  
  <style scoped>
  .app-container {
    display: flex;
    width: 100%;
    height: 650px;
    background: #000;
    border-radius: 1%;
    overflow: hidden;
  }

  .video-container {
    flex: 1;
    height: 100%;
    transition: all 0.3s ease;
  }

  .video-container.with-comments {
    width: calc(100% - 400px);
  }

  .video-container {
    width: 100%;
    height: 650px;
    overflow: hidden;
    position: flex;
    display: flex;
    background: #000;
    border-radius: 1%;
  }
  .video-wrapper {
    width: 100%;
    height: 100%;
    transition: transform 0.3s cubic-bezier(.4,0,.2,1);
  }
  .video-item {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
  }
  .video-player {
    width: 85%;
    height: 100%;
    object-fit: contain;
    background: #000;
  }
  .video-author{
    position: absolute;
    left: 20px;
  }
  .video-title {
    position: absolute;
    left: 20px;
    bottom: 70px;
    color: #fff;
    font-size: 20px;
    text-shadow: 0 2px 8px rgba(0,0,0,0.7);
  }
  .right-bar {
    position: absolute;
    right: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    z-index: 2;
  }
  .avatar {
    width: 45px;
    height: 45px;
    border-radius: 50%;
    overflow: hidden;
    border: 2px solid #fff;
    display: block;
    margin-bottom: 20px;
  }
  .avatar img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  .semi-icon{
    margin-left: 12px;
  }
  .action-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    color: #fff;
    font-size: 16px;
    cursor: pointer;
    user-select: none;
  }
  .action-btn svg {
    margin-bottom: 4px;
  }
  .custom-progress {
    position: absolute;
    left: 0;
    bottom: 36px;
    width: 100vw;
    z-index: 3;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .progress-info {
    width: 90vw;
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: #fff;
    font-size: 14px;
    margin-bottom: 2px;
  }
  .progress-bar-bg {
    flex: 1;
    height: 6px;
    background: rgba(255,255,255,0.2);
    border-radius: 3px;
    margin: 0 8px;
    overflow: hidden;
  }
  .progress-bar-fg {
    height: 100%;
    background: linear-gradient(90deg, #ff2c55, #ffb86c);
    transition: width 0.2s;
  }
  .volume-control {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 4;
    background: rgba(0,0,0,0.3);
  }
  .mute-btn {
    background: none;
    border: none;
    outline: none;
    cursor: pointer;
    margin-left: 800px;
    display: flex;
    align-items: center;
    
  }
  .volume-control input[type="range"] {
    width: 120px;
    accent-color: #ff2c55;
  }


/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}


.comment-list {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  /* 确保评论列表可以独立滚动 */
  overscroll-behavior: contain;
  -webkit-overflow-scrolling: touch;
}

.comment-input {
  padding: 16px;
  display: flex;
  gap: 8px;
}
.comment-section {
  width: 400px;
  height: 100%;
  background: white;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #eee;
  /* 确保评论区不会影响视频区域的滚动 */
  position: relative;
  z-index: 10;
}

.comment-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 0 8px;
}

.no-comments {
  text-align: center;
  color: #999;
  padding: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-user {
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.comment-text {
  color: #666;
  margin-bottom: 4px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-input input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

.comment-input button {
  padding: 8px 16px;
  background: #ff2c55;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.comment-input button:hover {
  background: #ff1a45;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}

/* 添加滚动条样式 */
.comment-list::-webkit-scrollbar {
  width: 6px;
}

.comment-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.comment-list::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.comment-list::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
