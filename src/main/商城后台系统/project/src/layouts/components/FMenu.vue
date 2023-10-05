<template>
  <div class="f-menu" :style="{width:$store.state.asideWidth}">
    <!--default-activ默认激活：刷新后依旧处于激活状态 unique-opened只能展开一个子菜单；collapse；是否折叠，collapse-transition：是否动画 -->
    <el-menu :default-active="defaultActive" unique-opened :collapse="isCollapse"  @select="handleSelect"
             class="border-0" :collapse-transition="false">
      <template v-for="(item,index) in asideMenus" 
                :key="index">
                <!-- vue的key属性作用：https://blog.csdn.net/zyj362633491/article/details/86654014：在列表渲染时使用key属性
使用key属性强制替换元素  报错原因：自己搜索-->
        <el-sub-menu v-if="item.child"
                     :index="item.name">
          <template #title>
            <el-icon>
              <component :is="item.icon"></component>
            </el-icon>
            <span>{{item.name}}</span>
          </template>
          <el-menu-item v-for="(item2,index2) in item.child"
                        :key="index2"
                        :index="item2.frontpath">
            <el-icon><component :is="item2.icon"></component></el-icon>
            <span>{{item2.name}}</span>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item v-else
                      :index="item.frontpath">
          <el-icon><component :is="item.icon"></component></el-icon>
          <!-- is和component动态切换组件 :item.icon相当于'DeleteFilled' 来着element-->
          <span>{{item.name}}</span>
        </el-menu-item>
      </template>

    </el-menu>

  </div>
</template>

<script setup>
// 引入计算属性
import { computed ,ref} from 'vue';
import { useRouter,useRoute } from 'vue-router';
import {useStore} from 'vuex'
const router = useRouter()
const store = useStore()
const route=useRouter()
// 默认选中
const defaultActive = ref(route.path)
// 是否折叠
const isCollapse = computed(()=> !(store.state.asideWidth=='250px'))
// 侧边栏有后端传入的menus决定控件
const asideMenus = computed(()=>store.state.menus)
// [{
//   // "name": "后台面板",
//   // "icon": "help",
//   // "child": [{
//   //   "name": "主控台",
//   //   "icon": "home-filled",
//   //   "frontpath": "/",
//   // }]
// }, {
//   "name": "商城管理",
//   "icon": "shopping-bag",
//   "child": [{
//     "name": "商品管理",
//     "icon": "shopping-cart-full",
//     "frontpath": "/goods/list",
//   }]
// }]

const handleSelect=(e)=>{
  // e是他的path路径:他的唯一标识index=frontsize
  router.push(e)
}

</script>

<style>
.f-menu {
  /* 设置动画时长 */
  transition: all 0.5s;
  top: 64px;
  bottom: 0;
  left: 0;
  /* overflow: auto; */
  overflow-y: auto;
  /* 还是闪的原因 */
  overflow-x:hidden ;
  @apply shadow-md fixed bg-light-50;
}
/* 设置滚动条 */
.f-menu::-webkit-scrollbar{
  width: 0;
}
</style>