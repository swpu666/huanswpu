import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import WindiCSS from 'vite-plugin-windicss'
//引入别名
import path from "path"
// https://vitejs.dev/config/
export default defineConfig({
  // 配置别名
  resolve:{
    alias:{
      "~":path.resolve(__dirname,'src')
    }
  },
  
  server:{
    proxy:{
      // 带选项写法：http://localhost:5173/api/bar -> http://jsonplaceholder.typicode.com/bar
      '/api': {
        target: 'http://ceshi13.dishait.cn',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    }
  },

  plugins: [vue(),WindiCSS()],
})

