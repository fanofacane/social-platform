import axios from "axios"
//创建axios实例对象
const aiRequest = axios.create({
  baseURL: '/ai',
  timeout: 600000
})
export default aiRequest;