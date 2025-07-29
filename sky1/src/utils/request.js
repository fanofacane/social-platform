import axios from 'axios'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

//axios的响应 request 拦截器
request.interceptors.request.use(
  (config) => { //成功回调
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if (loginUser && loginUser.token) { //如果有登录用户信息，就设置请求头
      config.headers.token = loginUser.token;
    }
    return config
  },
  (error) => { //失败回调
    return Promise.reject(error)
  }
)
//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    if (error.response.status === 401) { //token过期
      localStorage.removeItem('loginUser') //删除登录用户信息
      //  router.push('/login') //跳转到登录页
    }
    return Promise.reject(error)
  }
)

export default request