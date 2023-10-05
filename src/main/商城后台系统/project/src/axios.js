import axios from 'axios'
//error通知框  封装工具库后简化
// import { ElNotification } from 'element-plus'
import { toast } from '~/composables/util'
// 通过封装工具库后
// import { useCookies } from '@vueuse/integrations/useCookies'
import {getCookies} from '~/composables/auth'
import store from '~/store'
const service=axios.create({
  // 基础域名 /api是已经改过的名
  baseURL:"/api"
})

// axios改成service 原因看上

// 添加请求拦截器
service.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么

  // 往head头加入token  :封装工具库后简化
  // const cookie=useCookies()
  // const token = cookie.get("admin-token")
  const token =getCookies()
  if(token){
    config.headers["token"]=token
  }
  return config;
}, function (error) {
  // 对请求错误做些什么

  return Promise.reject(error);
});

// 添加响应拦截器
service.interceptors.response.use(function (response) {
  // 2xx 范围内的状态码都会触发该函数。
  // 对响应数据做点什么
  // 相当于简化了：以后res.data.data直接写成res
  return response.data.data;
}, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么

    // 封装工具库后简化
    //   ElNotification({
    //     // title: 'Error',
    //     message: error.response.data.msg || "请求失败",
    //     type: 'error',
    //     //存在时间
    //     duration:3000
    // })
  const msg=error.response.data.msg || '请求失败'
  if(msg == '非法token，请先登录！'){
    store.dispatch("logout").finally(()=>location.reload())
  }
    toast(msg,"error")

  return Promise.reject(error);
});

export default service