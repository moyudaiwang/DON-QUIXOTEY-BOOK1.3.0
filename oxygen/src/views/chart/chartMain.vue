<template>
  <div class="chart-container">
    <el-card>
      <div ref="chartRef" class="chart"></div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'ChartMain',
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.chart.dispose()
  },
  methods: {
    initChart() {
      const chartRef = this.$refs.chartRef
      if (chartRef) {
        this.chart = echarts.init(chartRef, 'dark')
        this.chart.setOption({
          title: {
            text: '红楼梦各家族人口结构',
            subtext: '共计346人，非完整数据'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['男', '女']
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { show: true, readOnly: false },
              magicType: { show: true, type: ['line', 'bar'] },
              restore: { show: true },
              saveAsImage: { show: true }
            }
          },
          calculable: true,
          xAxis: [
            {
              type: 'category',
              data: ['甄家', '史家', '王家', '薛家', '贾家', '林家', '皇家', '太虚幻境']
            }
          ],
          yAxis: [
            { type: 'value' }
          ],
          series: [
            {
              name: '男',
              type: 'bar',
              data: [9, 7, 8, 7, 107, 4, 32, 2],
              markPoint: {
                data: [
                  { type: 'max', name: 'Max' },
                  { type: 'min', name: 'Min' }
                ]
              },
              markLine: {
                data: [{ type: 'average', name: 'Avg' }]
              }
            },
            {
              name: '女',
              type: 'bar',
              data: [6, 8, 5, 10, 126, 6, 3, 6],
              itemStyle: {
                  color: '#F56C6C'  // 设置为红色
                },
              markPoint: {
                data: [
                  { type: 'max', name: 'Max' },
                  { type: 'min', name: 'Min' }
                ]
              },
              markLine: {
                data: [{ type: 'average', name: 'Avg' }]
              }
            }
          ]
        })
      }
    },
    handleResize() {
      this.chart.resize()
    }
  }
}
</script>

<style scoped>
.chart-container {
  padding: 20px;
}
.chart {
  width: 100%;
  height: 500px;
}
</style>
