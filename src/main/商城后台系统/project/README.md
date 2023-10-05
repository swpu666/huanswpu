<!-- 引入element-ui -->
pnpm install element-plus
 main.js:
  import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)
app.use(ElementPlus)
app.mount('#app')

<!-- 引入Windi css -->
npm i -D vite-plugin-windicss windicss

然后，在你的 Vite 配置中添加插件：

vite.config.js：
import WindiCSS from 'vite-plugin-windicss'

export default {
  plugins: [
    WindiCSS(),
  ],
}

main.js:
import 'virtual:windi.css'


<!-- 引入vue.router -->
...

<!-- 配置别名 -->

<!-- 使用element的图标 -->
pnpm install @element-plus/icons-vue
全局引入
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

<!-- 引入axios -->
npm install axios

<!-- vueuse 使用usecookie-->
依赖于@vueuse/integrations
npm i @vueuse/integrations
引入usecookie
import { useCookies } from '@vueuse/integrations/useCookies'

<!-- 拦截器:在请求或响应被 then 或 catch 处理前拦截它们。:axios官网 -->

<!-- 后端接口 -->
dishaxy.dishait.cn/shopadminapi


<!-- promise的then catch finally 差异-->
https://juejin.cn/post/7038104899057516557


<!-- 安装vuex -->
 npm install vuex@next --save
import { createStore } from 'vuex'
import { createStore } from 'vuex'


<!-- 安装nprogress :loading条-->
npm i nprogress
import "nprogress/nprogress.css"

<!-- element的布局容器 ：container -->
<el-header>：顶栏容器。

<el-aside>：侧边栏容器。

<el-main>：主要区域容器。

<el-footer>：底栏容器。

<!-- 全屏 -->
需要vueuse的工具：usefullscreen
npm i @vueuse/core
import {useFullscreen} from '@vueuse/core'
const {isFullscreen,enter,exit,toggle} = useFullscreen()

<!-- 引入eslint：修改vue3配置 -->
npm install --save-dev eslint eslint-plugin-vue
在项目的根目录下新增.eslintrc.js

<!-- vuejs:keep-alive 缓存组件 -->

<!-- vuejs:transition :全局动画-->

<!-- 开源动画库 animate.css -->

<!-- elementplus:Skeleton骨架屏 -->

<!-- gsap动画库：数字滚动动画 -->
npm i gsap
import gsap from "gsap"

<!-- Echarts -->
npm install echarts

<!-- vueuse:resize：监听容器适配 -->
import {useResizeObserver} from "@vueuse/core"

<!-- 自定义指令：用来通过用户权限判断是否显示某组件:v-permission -->



