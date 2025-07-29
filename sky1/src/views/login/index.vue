<script setup>
import { ref } from 'vue';
import { loginApi ,registerApi} from '@/api/login';
import { queryUserApi } from '@/api/user';
import { ElMessage } from 'element-plus';
import { useStore } from 'vuex'; 
import {  useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
const router=useRouter();
let loginForm = ref({username:'', password:''})
const emit = defineEmits(['loginSuccess'])
const registerForm=ref({username:'',password:'',confirmedPassword:''})

const store = useStore();
const activeTabName = ref('login')
const login = async () => {
    const res=await loginApi(loginForm.value);
    console.log(res.data)
    if(res.code){
    //存储用户当前信息
    localStorage.setItem('loginUser',JSON.stringify(res.data));
    const result = await queryUserApi(res.data.id)
    store.commit('updateUserDetail',result.data)
    store.commit('updateToken', res.data.token)
    store.commit("updateUser", res.data);    // 更新vuex中当前用户信息
    store.commit("updateIsLogin", true);    // 更新vuex中当前用户信息
    emit('loginSuccess')  // 触发父组件关闭登录框的回调
    open1()
    router.push('/index')
    }else{
        ElMessage.error(res.msg)
    }
}
const open1 = () => {
    ElNotification({
    title: '登录成功',
    message: '欢迎'+store.state.user.username,
    type: 'success',
    })
}

const submitRegister=async()=>{
    const res=await registerApi(registerForm.value)
    if(res.code){
    ElMessage.success('注册成功！！');
    activeTabName.value='login'
    }else{
        ElMessage.error(res.msg)
    }
}
const clear = async () => {
loginForm.value = {username:'', password:''}
}


</script>

<template>
<div class="login-register">
        <div class="login-register-container">
            <el-tabs stretch class="login-tabs" @tab-click="handleClick" v-model="activeTabName">
                <el-tab-pane name="login" label="登录" lazy>
                    <div class="login-box">
                        <el-input type="text" class="input" v-model="loginForm.username" placeholder="请输入账号" />
                        <el-input type="password" show-password class="input" v-model="loginForm.password" placeholder="请输入密码" />
                        <div class="submit" @click="login">登&nbsp;录</div>
                    </div>
                </el-tab-pane>
                <el-tab-pane name="register" label="注册" lazy>
                    <div class="register-box">
                        <el-input type="text" class="input" v-model="registerForm.username" placeholder="请输入账号" maxlength="50" />
                        <el-input type="password" show-password class="input" v-model="registerForm.password" placeholder="请输入密码" />
                        <el-input type="password" show-password class="input" v-model="registerForm.confirmedPassword" placeholder="再次确认密码" />
                        <div class="submit" @click="submitRegister">注&nbsp;册</div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </div>
        
    </div>
    
</template>

<style scoped>
.login-register {
    position: relative;
    /* display: flex; */
    width: 100%;
    height: 100%;
}


.login-register-container {
    display: block;
    width: 300px;
    height: 300px;
    padding: 60px;
}


.login-box, .register-box {
    display: flex;
    flex-direction: column;
}

.login-box .input, .login-box .tips {
    margin-top: 30px;
    width: 100%;
}

.register-box .input, .register-box .tips {
    margin-top: 20px;
    width: 100%;
}

.submit {
    color: #151515;
    border-radius: 4px;
    text-align: center;
    padding: 10px 15px;
    margin-top: 50px;
    background-color: #56eee4;
    width: 100px;
    margin-left: 80px;
}

.tips {
    color: var(--text2);
    font-size: 12px;
    text-align: center;
}

.tips .agreement {
    color: var(--brand_blue);
    margin-left: 4px;
    cursor: pointer;
}

/* element 元素 */
.el-input {
    --el-input-focus-border: #ccc;
    --el-input-focus-border-color: #ccc;
    --el-input-border-radius: 10px;
    --el-input-height: 40px;
}
</style>