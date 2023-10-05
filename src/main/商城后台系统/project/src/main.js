import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
// 引入element-plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 引入windi css
import 'virtual:windi.css'
// 引入router
import {router} from './router/index.js'
// 全局引入elment图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 引入vuex：store
import store from './store'
// 路由守卫
import "./permission.js"
// 引入nprogress
import "nprogress/nprogress.css"
import permission from './directives/permission.js'
const app = createApp(App)
// use图标elment图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(store)
app.use(router)
app.use(ElementPlus)
app.use(permission)
app.mount('#app')


