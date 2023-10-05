import axios from '~/axios.js'
export function getStatistics1(){
  // get型请求方式 
  // 且已经在axios的请求拦截中写了
  return axios.get("/admin/statistics1")
}
export function getStatistics3(type){
  // get型请求方式 
  // 且已经在axios的请求拦截中写了
  return axios.get("/admin/statistics3?type="+type)
}
export function getStatistics2(){
  // get型请求方式 
  // 且已经在axios的请求拦截中写了
  return axios.get("/admin/statistics2")
}