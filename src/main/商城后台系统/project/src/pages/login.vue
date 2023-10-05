<template>
  <!-- 为什么会有自带的样式？删了那个css文件 -->
  <el-row class="login-container">
    <el-col :lg="16" :md="12"
            class="left">
      <div>
        <div>欢迎光临</div>
        <div>点击立即学习。</div>
      </div>
    </el-col>
    <!-- 没加flex-col 分成两行 -->
    <el-col :lg="8" :md="12"
            class="right">
      <h2 class="title">欢迎回来</h2>
      <div>
        <span class="line"></span>
        <span >账号密码登录</span>
        <span class="line"></span>
      </div>
      <!-- form上:rules  定义ref-->
      <el-form ref="formRef" :rules="rules" :model="form"
            class="w-[250px]">
            <!-- 指定prop -->
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
        </el-form-item>
        <!-- 指定prop -->
        <el-form-item prop="password">
          <el-input type="password" show-password v-model="form.password" placeholder="请输入密码">
            <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <!-- 默认存在loading属性为true：正在加载状态 否则状态为正常 -->
          <el-button round color="#626aef" type="primary"  class="w-[250px]"
                     @click="onSubmit" :loading="loading">登录</el-button>

        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script  setup>
import { ref,reactive,onMounted,onBeforeMount, onBeforeUnmount } from 'vue'
// 通过vuex简化
// import{login} from '~/api/manager.js'
//error通知框  封装工具库后简化
// import { ElNotification } from 'element-plus'
import { toast } from '~/composables/util'
import { useRouter } from 'vue-router';
//// 引入cookie
// import { useCookies } from '@vueuse/integrations/useCookies'
// 改为从自定义的工具类中引入cookie
import {setCookies} from "~/composables/auth"
// 引入vuex
import {useStore} from 'vuex'
const store=useStore()

const router=useRouter();

// import { User,Lock } from '@element-plus/icons-vue'
// do not use same name with ref
const form = reactive({
  password:'',
  username:''
})
// rules
const rules = {
  username:[
    { 
      required: true,//必填的意思
       message: '用户名不能为空',// 提示信息
       trigger: 'blur' //触发时机--失去焦点
      },
    // { 
    //   // min: 3,//最小长度
    //    max: 5, //最大长度
    //    message: '用户名不能超过8个字符',
    //     trigger: 'blur'},
  ],
  password:[{
       required: true,//必填的意思
       message: '密码不能为空',// 提示信息
       trigger: 'blur' //触发时机--失去焦点
  }]

}
//使得formRef变成响应式
const formRef=ref(null)

const loading=ref(false)

// 当用户单击了表单中的 Submit 按钮而提交一个表单时，就会调用这个事件句柄函数。注意，当调用方法Form.submit() 时，该处理器函数不会被调用。
// 如果 onsubmit 句柄返回 fasle，表单的元素就不会提交。如果该函数返回其他值或什么都没有返回，则表单会被提交。
const onSubmit=()=>{
  // 等于this.$ref.formRef.value.validate
  // validate()校验表单，如果表单所有内容都合法，则返回valid，如果非valid则提示要填写完表单项，非valid则就说明表单并不是完全合法的，不是全部都符合内容的
  formRef.value.validate((valid)=>{
    if(!valid){
      return false;
    }
  
    // 按钮状态设置为正在加载中，防止多次点击申请
    loading.value=true
  // 通过vuex简化登录
  store.dispatch("login",form).then(res=>{
    toast("登录成功","success")
    router.push("./")
  }).finally(()=>{
    // 把loading设置回正常状态
    loading.value=false
  })

  // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      // // console.log("验证通过");
      //   login(form.username,form.password)
      //   .then(res=>{
      //     // console.log(res);
      //     // 提示成功
      //     // 封装工具库后简化
      //     // ElNotification({
      //     //     message: "登录成功",
      //     //     type: 'success',
      //     //     //存在时间
      //     //     duration:3000
      //     // })
      //     toast("登录成功","success")
      //     // 一-------存储token：cookie  ：

      //     // token：Token是服务端生成的一串字符串,以作客户端进行请求的一个令牌,存储在res
      //     // cookie.set("admin-token",res.data.data.token)
          
      //     // const cookie = useCookies()//实例化
      //     // {{{{{
      //       // 通过封装的工具库进一步简化
      //     setCookies(res.token)
      //     // 拦截器简化后    封装工具库进一步简化后直接没了
      //     // cookie.set("admin-token",res.token)
      //     // }}}}}
      //     // 什么是cookie：Cookie，有时也用其复数形式 Cookies。类型为“小型文本文件”，是某些网站为了辨别用户身份，进行Session跟踪而储存在用户本地终端上的数据（通常经过加密），
      //     // 由用户客户端计算机暂时或永久保存的信息。
      //     //修改的token名和值：
      //     // cookie.set("admin-token","1232456")
      //     // cookie.get("admin-token") 得到值
      //     // cookie.remove("admin-token")删除

      //     // 二------存储用户相关信息：
      //     // 需要调用接口方法 引入:mannager中的getinfo
      //     // 避免重复使用res这一个变量名
      //     // then ? promise的一个方法即执行完前面的之后才执行then中的
      //     // 放入全局前置守卫中，本处简化
      //     // getInfo().then(res2=>{
      //     //   store.commit("SET_USERINFO",res2)
      //     //   console.log(res2);
      //     // })

      //     //跳转到后台首页
      //     router.push("/")

    //    Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'data')
    // at login.vue:115:33
    // 原因：上方中断，直接跳到catch，而catch没有err.response.data.msg  ？？
    // 引用：把userouter学成了useroute
      //   }).finally(()=>{
      //   // 把loading设置回正常状态
      //   loading.value=false
      // })

    // 通过响应拦截器统一解决error（axios）：放在每个接口写一个太繁琐
    // .catch(err=>{
    //   ElNotification({
    //       // title: 'Error',
    //       message: err.response.data.msg || "请求失败",
    //       type: 'error',
    //       //存在时间
    //       duration:3000
    //   })
    // })
  })
}
// 监听回车事件
function onKeyUp(e){
  if(e.key=="Enter") onSubmit()
}
//  监听键盘
onMounted(()=>{
  document.addEventListener("keyup",onKeyUp)
})
// 删除事件
onBeforeUnmount(()=>{
  document.removeEventListener("keyup",onKeyUp)
})
</script>

<style>
.login-container{
  @apply  bg-indigo-500 min-h-screen;
}
.login-container .left,.login-container .right{
  @apply flex items-center justify-center flex-col;
}
.login-container .right{
  @apply bg-light-50  flex-col;
}
.left>div>div:first-child{
  @apply font-bold text-5xl text-light-50 mb-5;
}
.left>div>div:nth-child(2){
 @apply text-gray-200 text-sm;
}
.right .title{
  @apply font-bold text-3xl text-gray-800;
}
.right>div{
  @apply flex items-center justify-center my-5 text-gray-300 space-x-2;
}
.right .line{
  @apply h-[1px] w-16 bg-gray-200;
}
</style>