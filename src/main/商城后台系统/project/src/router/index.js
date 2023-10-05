import{
  createRouter,
  createWebHashHistory
} from 'vue-router'

// 通过别名找到位置
import Admin from "~/layouts/admin.vue"
// import{Admin} from "~/layouts/admin.vue"
// 报错：没有发现export
import Index from '~/pages/index.vue'
import Login from '~/pages/login.vue'
import NotFound from '~/pages/404.vue'
import GoodsList from '~/pages/goods/list.vue'
import CategoryList from '~/pages/category/list.vue'
import UseList from '~/pages/user/list.vue'
import OrderList from '~/pages/order/list.vue'
import CommentList from '~/pages/comment/list.vue'
import ImageList from '~/pages/image/list.vue'
import NoticeList from '~/pages/notice/list.vue'
import SettingBase from '~/pages/setting/base.vue'
import CouponList from '~/pages/coupon/list.vue'
import ManagerList from '~/pages/manager/list.vue'
// 配置路由:默认路由，所有用户共享
const routers=[{
  path:'/',
  name:"admin",
  component:Admin,
  // // 子路由
  // children:[{
  //   path:'/',
  //   component:Index,
  //   meta:{
  //     title:"后台首页"
  //   }
  // },{
  //   path:'/goods/list',
  //   component:GoodsList,
  //   meta:{
  //     title:"商品管理"
  //   }
  // }]
},
// {
//   path:'/category/list',
//   component:CategoryList,
//   meta:{
//     title:"分类列表"
//   }
// },
{
  path:'/login',
  component:Login,
  meta:{
    title:"登录页"
  }
},
// 发生错误跳转的网页
{
  path: '/:pathMatch(.*)*',
  name:NotFound,
  component:NotFound
},
]

// 动态路由：动态匹配菜单
const asyncRoutes = [
  {
    path:'/',
    name:"/",
    component:Index,
    meta:{
      title:"后台首页"
    }
  },
  {
    path:'/goods/list',
    name:"/goods/list",
    component:GoodsList,
    meta:{
      title:"商品管理"
    }
  },
  {
    path:'/category/list',
    name:"/category/list",
    component:CategoryList,
    meta:{
      title:"分类列表"
  }
},
{
  path:'/user/list',
  name:"/user/list",
  component:UseList,
  meta:{
    title:"用户列表"
}
},
{
  path:'/order/list',
  name:"/order/list",
  component:OrderList,
  meta:{
    title:"订单列表"
}
},
{
  path:'/comment/list',
  name:"/comment/list",
  component:CommentList,
  meta:{
    title:"评价列表"
}
},
{
  path:'/image/list',
  name:"/image/list",
  component:ImageList,
  meta:{
    title:"图库列表"
}
},
{
  path:'/notice/list',
  name:"/notice/list",
  component:NoticeList,
  meta:{
    title:"公告列表"
}
},
{
  path:'/setting/base',
  name:"/setting/base",
  component:SettingBase,
  meta:{
    title:"配置"
}
},
{
  path:'/coupon/list',
  name:"/coupon/list",
  component:CouponList,
  meta:{
    title:"优惠券列表"
}
},
{
  path:'/manager/list',
  name:"/manager/list",
  component:ManagerList,
  meta:{
    title:"管理员管理"
}
},
]
export const router =  createRouter({
  history:createWebHashHistory(),
  routes: routers,
})
// 动态添加路由的方法
export function addRoutes(menus){
  // 是否有新的路由
  let hasNewRoutes = false
  const findAndAddRouterByMenus = (arr)=>{
    arr.forEach(e=> {
      // 当数组中的元素在测试条件时返回 true 时, find() 返回符合条件的元素，之后的值不会再调用执行函数。
// 如果没有符合条件的元素返回 undefined
     let item =  asyncRoutes.find(o=>o.path == e.frontpath)
      // router.hasRoute()检查该路由是否存在
     
      if(item && !router.hasRoute(item.path)){
        router.addRoute("admin",item)
        hasNewRoutes = true
      }
      // 递归查找子路由
      if(e.child && e.child.length>0){
        findAndAddRouterByMenus(e.child)
      }
    })
  }
  findAndAddRouterByMenus(menus)
  return hasNewRoutes
}