<template>
  <!-- 封装Fheader的各个方法，简化 -->
  <!-- destory-on-close:控制是否在关闭drawer后销毁子元素 -->
   <el-drawer v-model="showDrawer"
               :title="title"
               :size="size"
               :destroy-on-close="destroyOnClose"
               :close-on-click-modal="false">
               <!-- label-width="80px"  限制label宽度 -->
      <div class="formDrawer">
        <div class="body">
          <slot></slot>
        </div>
        <div class="actions">
          <el-button   type="primary"
            @click="submit"    :loading="loading"     >{{confirmText}}</el-button>
           <el-button   type="default"
                     >取消</el-button>
        </div>
      </div>
    </el-drawer>
</template>

<script setup>
import {ref} from "vue"
const showDrawer = ref(false)
// 对外暴露属性
const props=defineProps({
  title:String,
  size:{
    type:String,
    default:"45%"
  },
  destroyOnClose:{
    type:Boolean,
    default:false
  },
  confirmText:{
    type:String,
    default:"提交"
  }
})
const loading = ref(false)
const showLoading=()=>loading.value=true
const hideLoading=()=>loading.value=false

// 打开
const open=()=>showDrawer.value=true;
// 关闭
const close=()=>showDrawer.value=false
// 提交
  // 对外暴露事件
  const emit = defineEmits("submit")
  // 手动触发submit事件
  const submit = ()=>emit("submit")
// 向父组件暴露以下方法
defineExpose({
  open,
  close,
  showLoading,
  hideLoading
})
// https://juejin.cn/post/7126852961245855775
// defineExpose - 子组件暴露自己的属性或方法
// defineEmits - 子组件向父组件事件传递
// defineProps - 组件之间传值
</script>

<style>
  .formDrawer{
    width: 100%;
    height: 100%;
    position: relative;
    @apply flex flex-col
  }
  .formDrawer .body{
    flex: 1;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 50px;
    overflow-y:auto ;
  }
  .formDrawer .actions{
    height: 50px;
    @apply mt-auto flex items-center
  }
</style>