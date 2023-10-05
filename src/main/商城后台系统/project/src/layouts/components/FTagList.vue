<template>
<div>
  <div class="f-tag-list" :style="{left:$store.state.asideWidth}">
    <el-tabs  v-model="activeTab"
             type="card"
             class="flex-1 "
             style="min-width:100px"
             @tab-remove="removeTab"
             @tab-change="changeTab">
      <el-tab-pane v-for="item in tabList"
                   :closable="item.path != '/'"
                   :key="item.path"
                   :label="item.title"
                   :name="item.path">
      </el-tab-pane>
    </el-tabs>

    <span class="tag-btn">
      <!-- command可以得到下面的command -->
      <el-dropdown @command="handleClose">
        <span class="el-dropdown-link">
          <el-icon>
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="clearOther">关闭其他</el-dropdown-item>
            <el-dropdown-item command="clearAll">全部关闭</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </span>
  </div>
  <div style="height: 44px;"></div>
</div>
</template>

<script setup>
import {
  useTabList
} from "~/composables/useTabList.js"
const { 
  activeTab,
  tabList,
  changeTab,
  removeTab,
  handleClose}=useTabList()
</script>

<style scoped>
.f-tag-list{
  @apply fixed  bg-gray-100 flex items-center;
  z-index: 0;
  top:64px;
  right:0;
  height: 44px;

}
.tag-btn{
  @apply bg-white rounded ml-auto flex items-center justify-center px-2;
  height:32px;
}
:deep(.el-tabs__header){
  @apply mb-0 ;
  border:0 !important;
  /* vertical-align:middle; */
  display:inline;
  /* el-tabs__item不居中，修改为inline后解决 */
}
:deep(.el-tabs__nav){
  border: 0 !important;
}
:deep(.el-tabs__item){
  border: 0 !important;
  height: 32px;
  line-height: 32px;
  vertical-align: center;
  @apply bg-white mx-1 rounded;
}
:deep(.el-tabs__nav-next),:deep(.el-tabs__nav-prev){
  line-height: 32px;
  height: 32px;
}
:deep(.el-icon){
  line-height: 32px;
  height: 32px;
}
:deep(.is-disabled){
  cursor: not-allowed;
  @apply text-gray-300;
}
.el-dropdown-link:focus {
      outline: none;
}
/* :deep(.el-tabs__nav:nth-child(1) i){
     display: none;
} */
</style>