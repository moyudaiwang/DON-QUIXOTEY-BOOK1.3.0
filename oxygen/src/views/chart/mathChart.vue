<template>
  <div class="chart-container">
    <div id="nebula-chart" class="chart"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'MathChart',
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
    if (this.chart) {
      this.chart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    isPrime(num) {
      // 处理负数：将负数转为正数判断
      num = Math.abs(num)
      // 处理特殊情况
      if (num <= 1) return false
      if (num <= 3) return true
      if (num % 2 === 0) return false
      
      // 只需要检查到平方根
      for (let i = 3; i <= Math.sqrt(num); i += 2) {
        if (num % i === 0) return false
      }
      return true
    },
    generateData() {
      const data = []
      // 生成500000以内的质数点
      for (let num = 1; num <= 500000; num++) {
        if (this.isPrime(num)) {
          // num作为到原点的距离，同时也作为弧度
          const r = num  // 距离
          const theta = num  // 弧度
          // 转换为直角坐标
          const x = r * Math.cos(theta)
          const y = r * Math.sin(theta)
          data.push([x, y])
        }
      }
      return data
    },
    initChart() {
      const chartDom = document.getElementById('nebula-chart')
      this.chart = echarts.init(chartDom, null, {
        renderer: 'canvas',
        useDirtyRect: true  // 启用脏矩形渲染优化
      })
      
      const data = this.generateData()
      
      const option = {
        title: {
          left: 'left',
          top: 10,
          padding: [0, 0, 0, 60],
          text: '500000以内的质数螺旋分布',
          subtext: `共计 ${data.length} 个质数点，每个点表示一个质数，距离=质数值，弧度=质数值`
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            const point = params[0]
            const x = point.data[0]
            const y = point.data[1]
            const r = Math.sqrt(x * x + y * y)
            const theta = Math.atan2(y, x)
            return `质数: ${Math.round(r)}<br/>距离: ${Math.round(r)}<br/>弧度: ${theta.toFixed(2)}`
          },
          enterable: false  // 禁止鼠标进入提示框
        },
        toolbox: {
          right: 20,
          feature: {
            dataZoom: {},
            saveAsImage: {
              pixelRatio: 2  // 提高导出图片质量
            }
          }
        },
        grid: {
          top: 60,
          bottom: 60,
          left: 60,
          right: 60,
          containLabel: true
        },
        xAxis: {
          type: 'value',
          name: 'x轴',
          scale: true,
          axisLine: {
            onZero: true,
            lineStyle: {
              color: '#333'
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed',
              color: '#ccc'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: 'y轴',
          scale: true,
          axisLine: {
            onZero: true,
            lineStyle: {
              color: '#333'
            }
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed',
              color: '#ccc'
            }
          }
        },
        dataZoom: [
          {
            type: 'inside',
            xAxisIndex: [0],
            yAxisIndex: [0]
          },
          {
            type: 'slider',
            xAxisIndex: [0],
            showDataShadow: false
          },
          {
            type: 'slider',
            yAxisIndex: [0],
            showDataShadow: false
          }
        ],
        animation: false,
        series: [
          {
            type: 'scatter',
            data: data,
            symbolSize: 1.5,  // 进一步减小点的大小
            itemStyle: {
              color: function(params) {
                const x = params.data[0]
                const y = params.data[1]
                const r = Math.sqrt(x * x + y * y)
                return `rgba(64, 158, 255, ${1 - r/500000})`
              }
            },
            emphasis: {
              symbolSize: 3,  // 减小悬停时的大小
              itemStyle: {
                color: '#F56C6C',
                borderColor: '#fff',
                borderWidth: 1
              }
            },
            progressive: 2000,  // 增加渐进式渲染的数量
            progressiveThreshold: 8000,  // 增加阈值
            large: true,
            largeThreshold: 8000
          }
        ]
      }
      
      // 确保x轴和y轴的单位长度相等
      const updateAxisScale = () => {
        const width = chartDom.clientWidth - option.grid.left - option.grid.right
        const height = chartDom.clientHeight - option.grid.top - option.grid.bottom
        const aspect = width / height
        
        if (aspect > 1) {
          // 宽度大于高度，调整网格以适应高度
          const targetWidth = height
          const padding = (width - targetWidth) / 2
          option.grid.left = padding + 60
          option.grid.right = padding + 60
        } else {
          // 高度大于宽度，调整网格以适应宽度
          const targetHeight = width
          const padding = (height - targetHeight) / 2
          option.grid.top = padding + 60
          option.grid.bottom = padding + 60
        }
      }
      
      updateAxisScale()
      this.chart.setOption(option)
      
      // 监听容器大小变化
      this.chart.on('resize', updateAxisScale)
    },
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    }
  }
}
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 100vh;  /* 使用视口高度 */
  padding: 10px;  /* 减小内边距 */
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chart {
  width: 100%;
  height: 100%;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
</style> 