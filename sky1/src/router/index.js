import { createRouter, createWebHistory } from 'vue-router'

import IndexView from '@/views/index/index.vue'
import LayoutView from '@/views/layout/index.vue'
import UserChildrenView from '@/views/user/children/index.vue'
import likedCardView from '@/views/user/children/likedCard.vue'
import CollectionCardView from '@/views/user/children/collectionCard.vue'
import LoginView from '@/views/login/loginRegister.vue'
import FollowView from '@/views/follow/index.vue'
import VideoView from '@/views/video/index.vue'
import { useStore } from 'vuex'
import MyIndex from '@/views/user/myIndex.vue'
import PublicPostView from '@/views/public/publicPost.vue'
import ChatView from '@/views/chat/index.vue'
import visitorUserView from '@/components/visitorUser/visitorUser.vue'
import shopView from '@/views/shop/shop.vue'
import shopDetailView from '@/views/shop/product-detail.vue'
import ordersView from '@/views/shop/orders.vue'
import animeView from '@/views/anime/index.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: "layout",
      component: LayoutView,
      redirect: '/index', //重定向
      meta: { requestAuth: false },
      children: [
        { path: 'origin', name: 'origin', component: IndexView, meta: { requestAuth: false } },
        {
          path: 'user', name: 'user', component: MyIndex, redirect: '/user/userIndex', meta: { requestAuth: true },
          children: [
            { path: 'userIndex', name: 'userIndex', component: UserChildrenView, meta: { requestAuth: true } },
            { path: 'likedCard', name: 'likedCard', component: likedCardView, meta: { requestAuth: true } },
            { path: 'collectionCard', name: 'collectionCard', component: CollectionCardView, meta: { requestAuth: true } },
          ]
        },
        { path: 'follow', name: 'follow', component: FollowView },
        { path: 'index', name: 'index', component: VideoView },
        { path: 'chat/:id?', name: 'chat', component: ChatView },
        { path: 'visitor/:id', name: 'visitor', component: visitorUserView },
        { path: 'shop', name: 'shop', component: shopView },
        { path: 'shop/product/:id', name: 'product', component: shopDetailView },
        { path: 'shop/orders', name: 'order', component: ordersView },
        { path: 'anime', name: 'anime', component: animeView },
      ]
    },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/public', name: 'public', component: PublicPostView },
  ],

});
// 本地没有token就跳到登录界面
router.beforeEach((to, from, next) => {
  const store = useStore()
  if (to.meta.requestAuth && !store.state.user) {
    const localUser = JSON.parse(localStorage.getItem('loginUser'))
    localUser ? store.commit('updateUser', localUser) : next({ name: "layout" });
  } else {
    next();
  }
});

export default router
