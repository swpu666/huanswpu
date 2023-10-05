<script setup>
</script>

<template>
<div>
 <router-view></router-view>
</div>
</template>

<style>
body{
  @apply bg-gray-100;
}
#nprogress  .bar{
  background-color: #f4f4f4!important;
  /* 增加权重 */
  height: 4px!important;
}
/* 设置滚动条样式 */
::-webkit-scrollbar{
  width: 4px;
  height: 6px;
}
/* 滚动条边角部分 */
::-webkit-scrollbar-corner{
  display: block;
}
/* 滚动条 */
::-webkit-scrollbar-thumb{
  border-radius: 8px;
  background-color: rgba(0,0,0,0.2);
}
/* 滚动条轨道 */
::-webkit-scrollbar-thumb,
::-webkit-scrollbar-track{
  border-right-color: transparent;
  border-left-color: transparent;
  background-color:rgba(0,0,0,0.1) ;
}
/* 滚动条轨道 */
</style>
