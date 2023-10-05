<template>
  <div>
    <div class="f-header">
      <span class="logo">
        <el-icon class="mr-1">
          <GoodsFilled />
        </el-icon>
        后台管理系统
      </span>
      <el-icon class="icon-btn" @click="$store.commit('handleAsideWidth')">
        <Fold v-if="$store.state.asideWidth=='250px'"/>
        <Expand v-else />
      </el-icon>
      <el-tooltip content="刷新"
                  placement="bottom"
                  effect="dark">
        <el-icon class="icon-btn"
                 @click="handleRefresh">
          <Refresh />
        </el-icon>
      </el-tooltip>
      <div class="ml-auto flex items-center">
        <el-tooltip content="全屏"
                    placement="bottom"
                    effect="dark">
          <!-- 直接调用全屏方法就完了 -->
          <el-icon class="icon-btn"
                   @click="toggle">
            <FullScreen v-if="!isFullscreen" />
            <Aim v-else />
          </el-icon>
        </el-tooltip>
        <el-dropdown class="dropdown"
                     @command="handleCommand">
          <span class="flex items-center text-light-50">
            <el-avatar class="mr-2"
                       :size="25"
                       :src="$store.state.user.avatar" />
            {{$store.state.user.username}}
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="rePassword">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!-- 抽屉组件 -->
    <!-- 通过formdrawer封装后简写 -->
    <!-- <el-drawer v-model="showdrawer"
               title="修改密码"
               size="45%"
               :close-on-click-modal="false">
               label-width="80px"  限制label宽度 
      <el-form ref="formRef"
               label-width="80px"
               size="small"
               :rules="rules"
               :model="form"
               class="w-[250px]">
        指定prop
        <el-form-item prop="oldpassword" label="旧密码">
          <el-input v-model="form.oldpassword"
                    placeholder="请输入旧密码">
          </el-input>
        </el-form-item>
        指定prop
        <el-form-item prop="password" label="新密码">
          <el-input type="password"
                    v-model="form.password"
                    placeholder="请输入新密码">
          </el-input>
        </el-form-item>
        <el-form-item prop="repassword" label="确认密码">
          <el-input type="password"
                    v-model="form.repassword"
                    placeholder="请输入新密码">
          </el-input>
        </el-form-item>
        <el-form-item>
          默认存在loading属性为true：正在加载状态 否则状态为正常
          <el-button   type="primary"
                     @click="onSubmit"
                     :loading="loading">提交</el-button>

        </el-form-item>
      </el-form>
    </el-drawer>-->

    <form-drawer ref="formDrawerRef"
                 title="修改密码"
                 destroyOnClose
                 @submit="onSubmit">
      <el-form ref="formRef"
               label-width="80px"
               size="small"
               :rules="rules"
               :model="form"
               class="w-[250px]">
        <!-- 指定prop -->
        <el-form-item prop="oldpassword"
                      label="旧密码">
          <el-input v-model="form.oldpassword"
                    placeholder="请输入旧密码">
          </el-input>
        </el-form-item>
        <!-- 指定prop -->
        <el-form-item prop="password"
                      label="新密码">
          <el-input type="password"
                    v-model="form.password"
                    placeholder="请输入新密码">
          </el-input>
        </el-form-item>
        <el-form-item prop="repassword"
                      label="确认密码">
          <el-input type="password"
                    v-model="form.repassword"
                    placeholder="请输入新密码">
          </el-input>
        </el-form-item>
      </el-form>
    </form-drawer>
  </div>

</template>

<script setup>
import FormDrawer from '~/components/FormDrawer.vue'
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import {useRepassword,useLogout} from "~/composables/useManager.js"
// 引入全屏
import { useFullscreen } from '@vueuse/core'
const {
  isFullscreen,//是否全屏状态
  enter,//进入全屏
  exit,//退出全屏
  toggle //进入或退出全屏
} = useFullscreen()
const {
  formDrawerRef,
    form,
    rules,
    formRef,
    onSubmit,
    openRePasswordForm
}=useRepassword()
const{
  handleLogout
}=useLogout()

// 修改密码 简化
// const formDrawerRef = ref(null)


// const form = reactive({
//   oldpasswold: '',
//   password: '',
//   repassword: ''
// })
// // rules
// const rules = {
//   oldpassword: [
//     {
//       required: true,//必填的意思
//       message: '旧密码不能为空',// 提示信息
//       trigger: 'blur' //触发时机--失去焦点
//     },
//     // { 
//     //   // min: 3,//最小长度
//     //    max: 5, //最大长度
//     //    message: '用户名不能超过8个字符',
//     //     trigger: 'blur'},
//   ],
//   password: [{
//     required: true,//必填的意思
//     message: '新密码不能为空',// 提示信息
//     trigger: 'blur' //触发时机--失去焦点
//   },],

//   repassword: [{
//     required: true,//必填的意思
//     message: '确认密码不能为空',// 提示信息
//     trigger: 'blur' //触发时机--失去焦点
//   }]

// }
// //使得formRef变成响应式
// const formRef = ref(null)

// // 当用户单击了表单中的 Submit 按钮而提交一个表单时，就会调用这个事件句柄函数。注意，当调用方法Form.submit() 时，该处理器函数不会被调用。
// // 如果 onsubmit 句柄返回 fasle，表单的元素就不会提交。如果该函数返回其他值或什么都没有返回，则表单会被提交。
// const onSubmit = () => {
//   // 等于this.$ref.formRef.value.validate
//   // validate()校验表单，如果表单所有内容都合法，则返回valid，如果非valid则提示要填写完表单项，非valid则就说明表单并不是完全合法的，不是全部都符合内容的
//   formRef.value.validate((valid) => {
//     if (!valid) {
//       return false;
//     }
//     formDrawerRef.value.showLoading()
//     updatepassword(form)
//       .then(res => {
//         toast("修改密码成功,请重新登录")
//         store.dispatch("logout")
//         router.push("/login")
//       })
//       .finally(() => {
//         formDrawerRef.value.hideLoading()
//       })
//   })
// }
// 通过command指令和handlecommand指令获得子组件的command值
const handleCommand = (value) => {
  switch (value) {
    case "logout":
      handleLogout()
      break;

    case "rePassword":
      // //showdrawer.value = true
      openRePasswordForm()
      break
  }
}

// 刷新
const handleRefresh = () => {
  location.reload()
}
// 退出登录 
// function handleLogout () {
//   showModal("是否要退出登录").then(res => {
//     logOut().finally(res => {
//       // 移除cookie里的token
//       // 清除当前用户状态 vuex
//       store.dispatch("logout")
//       // 跳转登录页
//       router.push("/login")
//       // 提示退出成功
//       toast("退出成功", "success")
//     })
//   })
//     .catch(() => {

//     })
// }


</script>

<style>
.f-header {
  @apply flex items-center bg-indigo-700 text-light-50 fixed top-0 left-0 right-0;
  height: 64px;
  z-index: 1000;
}
.logo {
  width: 250px;
  @apply flex justify-center items-center text-xl font-thin;
}
.icon-btn {
  @apply flex justify-center items-center;
  width: 42px;
  height: 64px;
  cursor: pointer;
}
.icon-btn:hover {
  @apply bg-indigo-600;
}
.f-header .dropdown {
  height: 64px;
  cursor: pointer;
  @apply flex justify-center items-center mx-5;
}
</style>