<template>
  <!-- 图表 -->
  <el-card  shadow="never">
    <template #header>
    <div class="flex justify-between">
      <span class="text-sm">订单统计</span>
      <div>
        <el-check-tag v-for="(item, index) in options" :key="index" :checked="current==item.value" style="margin-right:8px"
        @click="handleChoose(item.value)">
          {{ item.text }}
        </el-check-tag>
      </div>
    </div>
    </template>
    <!-- card body -->
    <div ref="el" id="chart" style="width: 100%;height: 300px;"></div>
  </el-card>
  
</template>

<script setup>
import {getStatistics3} from "~/api/index.js"
import * as echarts from 'echarts';
// 引入vueuse的屏幕适配
import {useResizeObserver} from "@vueuse/core"
import { ref,onMounted,onBeforeMount} from "vue"
const current=ref("week")
const options = [{
  text:"近一个月",
  value:"month"
},
{
  text:"近一周",
  value:"week"
},
{
  text:"近24小时",
  value:"hour"
},
]
const handleChoose=(type)=>{
  current.value = type
  getData()
}
var myChart =null
onMounted(()=>{
var chartDom = document.getElementById('chart');
// 判断结点是否还存在
if(chartDom){
  myChart = echarts.init(chartDom);
  getData()
}
})
function getData() {

let option = {
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      showBackground: true,
      backgroundStyle: {
        color: 'rgba(180, 180, 180, 0.2)'
      }
    }
  ]
};
// 添加图片加载时的loading
myChart.showLoading();
// 自己为图表添加数据
getStatistics3(current.value).then(res=>{
  option.xAxis.data=res.x
  option.series[0].data = res.y
  myChart.setOption(option);
}).finally(()=>{
    myChart.hideLoading();
})

// 切换页面不缓存,销毁
// 用echarts记住这一步一定不能漏掉
onBeforeMount(()=>{
  if(myChart){
    echarts.dispose;
  }
})
}

// reisze
const el =ref(null)
useResizeObserver(el, (entries) => {
    // 逆天，调用echarts的resize
      myChart.resize()
    })
</script>

<style>

</style>