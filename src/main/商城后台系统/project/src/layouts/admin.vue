<template>
  <el-container>
    <el-header>
      <f-header></f-header>
    </el-header>
    <el-container>
      <el-aside :width="$store.state.asideWidth">
        <f-menu></f-menu>
      </el-aside>
      <el-main>
        <f-tag-list></f-tag-list>
        <!--浅析＜router-view＞ v-slot事例: https://blog.csdn.net/m13012606980/article/details/126028170 
        通过这种方法获得调换的组件名-->
        <router-view v-slot="{Component}">
          <transition name="fade">
            <!-- 最多不能超过十个、 -->
            <keep-alive :max="10">
              <component :is="Component"></component>
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import FHeader from './components/FHeader.vue';
import FMenu from './components/FMenu.vue';
import FTagList from './components/FTagList.vue';
</script>

<style >
.el-aside {
  transition: all 0.2;
}
/* 进入之前 */
.fade-enter-from{
  opacity: 0;
}
/* 进入之后 */
.fade-enter-to{
  opacity: 1;
}
/* 离开之前 */
.fade-leave-from{
  opacity: 1;
}
/* 离开之后 */
.fade-leave-to{
  opacity: 0;
}
.fade-enter-active,
.fade-leave-active{
  transition: all 0.3s;
}
.fade-enter-active{
  transition-delay: 0.3s;
}
</style>