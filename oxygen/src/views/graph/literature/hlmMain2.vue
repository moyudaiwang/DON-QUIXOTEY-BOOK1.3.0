
<template>
  <div ref="echartsContainer" style="width: 600px; height: 400px;"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'EChartsComponent',
  data () {
    return {
    testt:["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
    }

  },
  mounted() {
    var myChart = echarts.init(this.$refs.echartsContainer);

    myChart.setOption({
      // ECharts 配置项
      title: {
        text: 'ECharts 示例'
      },
      tooltip: {},
      xAxis: {
        data: this.testt
      },
      yAxis: {},
      series: [{
        name: '销量',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20]
      }]
    });
  }
}
</script>
