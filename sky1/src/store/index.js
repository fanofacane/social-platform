// src/store.js  
import { createStore } from 'vuex';
import { queryUserApi } from '@/api/user';
import { ElMessage } from 'element-plus';
import router from '@/router';


const store = createStore({
    state() {
        return {
            // 是否加载中
            isLoading: false,
            // 是否登录
            isLogin: false,
            // 是否外部触发打开登录框
            openLogin: false,
            // 当前用户
            user: {},
            //
            userDetail: {},
            //当前token值
            token: '',
            //当前ws状态
            ws: null,
            //通知消息
            notifications: [],
            //聊天消息
            chatMessages: []
        };
    },
    mutations: {
        // 退出登录或登录过期时初始化个别数据
        initData(state) {
            state.isLogin = false;
            state.user = {};
        },
        // 更新登录状态
        updateIsLogin(state, isLogin) {
            state.isLogin = isLogin;
        },
        // 更新当前用户
        updateUser(state, user) {
            state.user = user;
            // console.log("更新vuex中用户信息: ", state.user);
        },
        updateUserDetail(state, userDetail) {
            state.userDetail = userDetail;
        },
        //更新当前token
        updateToken(state, token) {
            state.token = token
        },
        //退出登录
        logout(state) {
            state.user = {};
            state.isLogin = false;
            localStorage.removeItem("loginUser");
            router.push("/index");
            ElMessage.success("退出成功");
        },
        //更新ws状态
        SET_WS(state, ws) {
            state.ws = ws
        },
        //接受通知消息
        ADD_NOTIFICATION(state, notification) {
            state.notifications = notification
        },
        //接受聊天消息
        ADD_CHAT_MESSAGE(state, message) {
            state.chatMessages = message
        }
    },
    actions: {
        initWebSocket({ commit, dispatch, state }) {
            if (state.isLogin) {
                const ws = new WebSocket(`ws://localhost:8082/order-tracking?token=${state.token}`)
                // 新增：监听错误事件
                ws.onerror = (error) => {
                    console.error('WebSocket 连接错误：', error);
                    // 打印错误细节（如错误码、消息）
                    console.error('错误信息：', error.message);
                };
                ws.onopen = () => {
                    console.log('WebSocket 连接已建立')
                }
                ws.onmessage = (event) => {
                    console.log('后端推送数据：' + event.data)
                    let msg = JSON.parse(event.data)
                    if (msg.order == 'logout') {
                        console.log(state.notifications)
                        commit('logout')
                        commit('ADD_NOTIFICATION', msg)
                        return;
                    }
                    if (msg.to == state.user.id) {
                        commit('ADD_CHAT_MESSAGE', msg)
                    }
                }
                ws.onclose = () => {
                    setTimeout(() => dispatch('initWebSocket'), 1000)
                }
                commit('SET_WS', ws)
            }
        },
        // 初始化用户信息  
        async initializeUser({ commit, state }) {
            const token = localStorage.getItem('loginUser');
            if (state.isLogin) {
                const result = await queryUserApi(state.user.id)
                commit('updateUserDetail', result.data)
            }
            if (token) {
                // 你可以选择在这里 dispatch API 获取详细信息  
                // 或者本地存储用户基本信息  
                // 例如：  
                const userInfo = JSON.parse(localStorage.getItem('loginUser'));
                const result = await queryUserApi(userInfo.id)
                commit('updateUserDetail', result.data)
                commit('updateToken', userInfo.token)
                commit('updateUser', userInfo);
                commit('updateIsLogin', true);
            } else {
                commit('initData')
            }
        },
        // // 获取当前用户信息
        // async getPersonalInfo({ state, commit }) {
        //     if (state.isLogin) {
        //         // 这里为了更方便捕捉到错误后做出反应，就不使用封装的函数了
        //         const result = await queryUserApi(state.user.id)
        //             .catch(() => {
        //                 localStorage.removeItem("loginUser");
        //                 ElMessage.error("请登录后查看");
        //             });
        //         if (result) {

        //             commit("updateUser", result.data);
        //             commit("updateIsLogin", true);
        //         }
        //     }
        // },
    },
});

export default store;  