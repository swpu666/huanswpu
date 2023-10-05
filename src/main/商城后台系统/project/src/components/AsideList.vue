<template>
  <div class="aside-list"
       :class="{'active':active}">
    <!-- 超出部分显示... -->
    <span class="truncate">
      <slot></slot>
    </span>
    <el-button class="ml-auto px-0.5"
               text
               type="primary"
               size="small"
               @click="$emit('edit')">
      <el-icon :size="12">
        <Edit />
      </el-icon>
    </el-button>
    <el-popconfirm title="是否要删除该分类？" confirmButtonText="确认"
    cancelButtonText="取消" @confirm="$emit('delete')"
    >
      <template #reference>
        <el-button class="ml-auto px-0.5"
                   text
                   type="primary"
                   size="small"
                   @click="$emit('delete')">
          <el-icon :size="12">
            <Close />
          </el-icon>
        </el-button>
      </template>
    </el-popconfirm>
  </div>
</template>

<script setup>
defineProps({
  active: {
    type: Boolean,
    default: false
  }
})
defineEmits(["edit", "delete"])
</script>

<style>
.aside-list {
  border-bottom: 1px solid #f4f4f4;
  cursor: pointer;
  @apply flex items-center p-3 text-sm text-gray-600;
}
.aside-list:hover,
.active {
  @apply bg-blue-50;
}
</style>