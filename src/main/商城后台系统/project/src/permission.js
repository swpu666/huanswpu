// 引入
import {router,addRoutes} from "~/router/index.js"
import {getCookies} from "~/composables/auth.js"
import { toast,showFullLoading,hideFullLoading } from '~/composables/util'
import store  from "./store"
// 全局前置守卫
let hasGetInfo = false
router.beforeEach(async (to,from,next)=>{
  //显示全屏loading
    showFullLoading()

    const token=getCookies()
    // if(to.path="/"){
    if(!token && to.path!="/login" ){
      toast("请先登录",'error')
      return next({path:"/login"})
    }
  // }
  // 如果已经登录
  if(token && to.path=="/login"){
    toast("请勿重复登录",'error')
    return next({path:from.path ? from.path:'/'})
  }

  // 如果用户登录了，知道获取用户信息，并且存储在vuex中
  let hasNewRoutes = false
  // hasGetInfo防止多次请求加载深度慢
  if(token && !hasGetInfo){
    // 使用await 必须在前面加上async
  let {menus}=  await store.dispatch("getinfo")
  hasGetInfo=true
    // 动态添加路由
   hasNewRoutes = addRoutes(menus)
    // 刷新后404问题，原因：只注册了一个路由，需要手动导航
  }
  // 设置页面标题
  let title = (to.meta.title? to.meta.title:"")+"-商城后台系统"
  document.title=title;
  // 有新路由手动导航，否则调用next
  hasNewRoutes ? next(to.fullPath) : next()
})

// 全局后置守卫
router.afterEach((to, from) => {
  // to and from are both route objects.
  // 渲染完关闭loading
  hideFullLoading()
})