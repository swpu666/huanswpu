import axios from '~/axios.js'

export function login(username,password){
  // 拓展域名:服务器,已经有基础域名
  return axios.post("/admin/login",{
    username,
    password
  })
}

// 接口方法
export function getInfo(){
  // post型请求方式 :本项目只需要在头传入token
  // 且已经在axios的请求拦截中写了
  return axios.post("/admin/getinfo")
}
// 退出登录
export function logOut(){
  // post型请求方式 :本项目只需要在头传入token
  // 且已经在axios的请求拦截中写了
  return axios.post("/admin/logout")
}

// 修改密码
export function updatepassword(data){
  // post型请求方式 :本项目只需要在头传入token
  // 且已经在axios的请求拦截中写了,这个请求body必须传三个参数
  return axios.post("/admin/updatepassword",data)
}