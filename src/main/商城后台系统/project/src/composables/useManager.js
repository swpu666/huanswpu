import { ref, reactive } from 'vue'
import FormDrawer from '~/components/FormDrawer.vue'
import { showModal, toast } from "~/composables/util"
import { logOut, updatepassword } from "~/api/manager"
import { useRouter } from "vue-router";
import { useStore } from "vuex";
export function useRepassword () {
  const router = useRouter()
  const store = useStore()
  const formDrawerRef = ref(null)


  const form = reactive({
    oldpasswold: '',
    password: '',
    repassword: ''
  })
  // rules
  const rules = {
    oldpassword: [
      {
        required: true,//必填的意思
        message: '旧密码不能为空',// 提示信息
        trigger: 'blur' //触发时机--失去焦点
      },
      // { 
      //   // min: 3,//最小长度
      //    max: 5, //最大长度
      //    message: '用户名不能超过8个字符',
      //     trigger: 'blur'},
    ],
    password: [{
      required: true,//必填的意思
      message: '新密码不能为空',// 提示信息
      trigger: 'blur' //触发时机--失去焦点
    },],

    repassword: [{
      required: true,//必填的意思
      message: '确认密码不能为空',// 提示信息
      trigger: 'blur' //触发时机--失去焦点
    }]

  }
  //使得formRef变成响应式
  const formRef = ref(null)

  // 当用户单击了表单中的 Submit 按钮而提交一个表单时，就会调用这个事件句柄函数。注意，当调用方法Form.submit() 时，该处理器函数不会被调用。
  // 如果 onsubmit 句柄返回 fasle，表单的元素就不会提交。如果该函数返回其他值或什么都没有返回，则表单会被提交。
  const onSubmit = () => {
    // 等于this.$ref.formRef.value.validate
    // validate()校验表单，如果表单所有内容都合法，则返回valid，如果非valid则提示要填写完表单项，非valid则就说明表单并不是完全合法的，不是全部都符合内容的
    formRef.value.validate((valid) => {
      if (!valid) {
        return false;
      }
      formDrawerRef.value.showLoading()
      updatepassword(form)
        .then(res => {
          toast("修改密码成功,请重新登录")
          store.dispatch("logout")
          router.push("/login")
        })
        .finally(() => {
          formDrawerRef.value.hideLoading()
        })
    })
  }
  const openRePasswordForm = ()=>formDrawerRef.value.open()
  return {
    formDrawerRef,
    form,
    rules,
    formRef,
    onSubmit,
    openRePasswordForm
  }
}
export function useLogout(){
  const router = useRouter()
  const store = useStore()
  function handleLogout () {
    showModal("是否要退出登录").then(res => {
      logOut().finally(res => {
        // 移除cookie里的token
        // 清除当前用户状态 vuex
        store.dispatch("logout")
        // 跳转登录页
        router.push("/login")
        // 提示退出成功
        toast("退出成功", "success")
      })
    })
      .catch(() => {
  
      })
  }
  return {
    handleLogout
  }
}