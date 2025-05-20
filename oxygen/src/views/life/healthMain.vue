<template>
  <div class="health-container">
    <div class="health-header">
      <h2>健康管理</h2>
      <p class="subtitle">关注健康，科学管理</p>
    </div>

    <el-row :gutter="20">
      <!-- BMI计算器 -->
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="health-card">
          <div slot="header">
            <span><i class="el-icon-data-analysis"></i> BMI计算器</span>
          </div>
          <el-form :model="bmiForm" label-position="top">
            <div class="form-row">
              <el-form-item label="身高(cm)" class="el-form-item">
                <el-input-number v-model="bmiForm.height" :min="50" :max="250" :step="1"></el-input-number>
              </el-form-item>
              <el-form-item label="体重(kg)" class="el-form-item">
                <el-input-number v-model="bmiForm.weight" :min="1" :max="200" :step="0.1"></el-input-number>
              </el-form-item>
            </div>
            <div class="button-row">
              <el-button type="primary" @click="calculateBMI">计算BMI</el-button>
            </div>
            <div v-if="bmiResult.show" class="result-box">
              <div class="bmi-gauge" ref="bmiGauge" style="height: 200px;"></div>
              <p>身体状况：<span :class="bmiResult.statusClass">{{ bmiResult.status }}</span></p>
              <p>建议：{{ bmiResult.advice }}</p>
            </div>
          </el-form>
        </el-card>
      </el-col>

      <!-- 身高预测 -->
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="health-card">
          <div slot="header">
            <span><i class="el-icon-user"></i> 身高预测</span>
          </div>
          <el-form :model="heightForm" label-position="top">
            <div class="form-row">
              <el-form-item label="父亲身高(cm)" class="el-form-item">
                <el-input-number v-model="heightForm.fatherHeight" :min="50" :max="250" :step="1"></el-input-number>
              </el-form-item>
              <el-form-item label="母亲身高(cm)" class="el-form-item">
                <el-input-number v-model="heightForm.motherHeight" :min="50" :max="250" :step="1"></el-input-number>
              </el-form-item>
            </div>
            <div class="form-row">
              <el-form-item label="性别" class="el-form-item">
                <el-radio-group v-model="heightForm.gender">
                  <el-radio label="male">男</el-radio>
                  <el-radio label="female">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            <div class="button-row">
              <el-button type="primary" @click="predictHeight">预测身高</el-button>
            </div>
            <div v-if="heightResult.show" class="result-box">
              <div class="height-chart" ref="heightChart" style="height: 200px;"></div>
              <p class="note">注：此预测仅供参考，实际身高受多种因素影响</p>
            </div>
          </el-form>
        </el-card>
      </el-col>

      <!-- 基础代谢率 -->
      <el-col :xs="24" :sm="12" :md="8">
        <el-card class="health-card">
          <div slot="header">
            <span><i class="el-icon-pie-chart"></i> 基础代谢率</span>
          </div>
          <el-form :model="bmrForm" label-position="top">
            <div class="form-row">
              <el-form-item label="年龄" class="el-form-item">
                <el-input-number v-model="bmrForm.age" :min="1" :max="120" :step="1"></el-input-number>
              </el-form-item>
              <el-form-item label="性别" class="el-form-item">
                <el-radio-group v-model="bmrForm.gender">
                  <el-radio label="male">男</el-radio>
                  <el-radio label="female">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
            <div class="form-row">
              <el-form-item label="身高(cm)" class="el-form-item">
                <el-input-number v-model="bmrForm.height" :min="50" :max="250" :step="1"></el-input-number>
              </el-form-item>
              <el-form-item label="体重(kg)" class="el-form-item">
                <el-input-number v-model="bmrForm.weight" :min="1" :max="200" :step="0.1"></el-input-number>
              </el-form-item>
            </div>
            <div class="button-row">
              <el-button type="primary" @click="calculateBMR">计算BMR</el-button>
            </div>
            <div v-if="bmrResult.show" class="result-box">
              <div class="bmr-chart" ref="bmrChart" style="height: 200px;"></div>
              <p>活动建议：{{ bmrResult.advice }}</p>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'health-main',
  data() {
    return {
      bmiForm: {
        height: 170,
        weight: 60
      },
      bmiResult: {
        show: false,
        value: '',
        status: '',
        statusClass: '',
        advice: ''
      },
      heightForm: {
        fatherHeight: 175,
        motherHeight: 160,
        gender: 'male'
      },
      heightResult: {
        show: false,
        value: '',
        range: ''
      },
      bmrForm: {
        age: 25,
        gender: 'male',
        height: 170,
        weight: 60
      },
      bmrResult: {
        show: false,
        value: '',
        advice: ''
      },
      charts: {
        bmiGauge: null,
        heightChart: null,
        bmrChart: null
      }
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    calculateBMI() {
      const height = this.bmiForm.height / 100
      const weight = this.bmiForm.weight
      const bmi = (weight / (height * height)).toFixed(1)
      
      this.bmiResult.value = bmi
      this.bmiResult.show = true

      if (bmi < 18.5) {
        this.bmiResult.status = '体重过轻'
        this.bmiResult.statusClass = 'status-warning'
        this.bmiResult.advice = '建议适当增加营养摄入，注意均衡饮食'
      } else if (bmi >= 18.5 && bmi < 24) {
        this.bmiResult.status = '体重正常'
        this.bmiResult.statusClass = 'status-success'
        this.bmiResult.advice = '请继续保持健康的生活方式'
      } else if (bmi >= 24 && bmi < 28) {
        this.bmiResult.status = '超重'
        this.bmiResult.statusClass = 'status-warning'
        this.bmiResult.advice = '建议适当控制饮食，增加运动量'
      } else {
        this.bmiResult.status = '肥胖'
        this.bmiResult.statusClass = 'status-danger'
        this.bmiResult.advice = '建议咨询医生，制定科学的减重计划'
      }

      this.$nextTick(() => {
        this.initBMIGauge(bmi)
      })
    },
    predictHeight() {
      const father = this.heightForm.fatherHeight
      const mother = this.heightForm.motherHeight
      let predictedHeight
      let range

      if (this.heightForm.gender === 'male') {
        predictedHeight = ((father + mother) * 0.54).toFixed(1)
        range = 5
      } else {
        predictedHeight = ((father * 0.923 + mother) / 2).toFixed(1)
        range = 4
      }

      this.heightResult.show = true
      this.heightResult.value = predictedHeight
      this.heightResult.range = `${predictedHeight - range} ~ ${Number(predictedHeight) + range}`

      this.$nextTick(() => {
        this.initHeightChart(predictedHeight, range)
      })
    },
    calculateBMR() {
      let bmr
      const { age, gender, height, weight } = this.bmrForm

      if (gender === 'male') {
        bmr = 66 + (13.7 * weight) + (5 * height) - (6.8 * age)
      } else {
        bmr = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age)
      }

      this.bmrResult.show = true
      this.bmrResult.value = Math.round(bmr)
      this.bmrResult.advice = `每日摄入${Math.round(bmr * 1.2)}~${Math.round(bmr * 1.4)}卡路里可维持当前体重`

      this.$nextTick(() => {
        this.initBMRChart(bmr)
      })
    },
    initBMIGauge(bmi) {
      if (this.charts.bmiGauge) {
        this.charts.bmiGauge.dispose()
      }
      
      const chartDom = this.$refs.bmiGauge
      this.charts.bmiGauge = echarts.init(chartDom)
      
      const option = {
        series: [{
          type: 'gauge',
          startAngle: 180,
          endAngle: 0,
          min: 0,
          max: 40,
          splitNumber: 8,
          radius: '100%',
          center: ['50%', '60%'],
          axisLine: {
            lineStyle: {
              width: 6,
              color: [
                [0.4625, '#67C23A'],  // 0-18.5 偏瘦
                [0.6, '#409EFF'],     // 18.5-24 正常
                [0.7, '#E6A23C'],     // 24-28 超重
                [1, '#F56C6C']        // 28+ 肥胖
              ]
            }
          },
          pointer: {
            icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
            length: '60%',
            width: 8,
            offsetCenter: [0, 0],
            itemStyle: {
              color: 'auto'
            }
          },
          axisTick: {
            length: 8,
            lineStyle: {
              color: 'auto',
              width: 2
            }
          },
          splitLine: {
            length: 15,
            lineStyle: {
              color: 'auto',
              width: 3
            }
          },
          axisLabel: {
            color: '#464646',
            fontSize: 12,
            distance: -25,
            formatter: function (value) {
              if (value === 18.5 || value === 24 || value === 28) {
                return value.toString()
              }
              return ''
            }
          },
          title: {
            offsetCenter: [0, '20%'],
            fontSize: 16,
            color: '#464646'
          },
          detail: {
            fontSize: 24,
            offsetCenter: [0, '50%'],
            valueAnimation: true,
            formatter: function (value) {
              return value.toFixed(1)
            },
            color: 'auto'
          },
          data: [{
            value: bmi,
            name: 'BMI'
          }]
        }]
      }

      this.charts.bmiGauge.setOption(option)
    },
    initHeightChart(predictedHeight, range) {
      if (this.charts.heightChart) {
        this.charts.heightChart.dispose()
      }

      const chartDom = this.$refs.heightChart
      this.charts.heightChart = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: '15%',
          left: '10%',
          right: '10%',
          bottom: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          min: Number(predictedHeight) - range - 5,
          max: Number(predictedHeight) + range + 5,
          axisLabel: {
            formatter: '{value}cm'
          }
        },
        yAxis: {
          type: 'category',
          data: ['预测身高'],
          axisLine: { show: false },
          axisTick: { show: false }
        },
        series: [
          {
            name: '身高范围',
            type: 'bar',
            data: [{
              value: range * 2,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                  { offset: 0, color: '#91cc75' },
                  { offset: 0.5, color: '#5470c6' },
                  { offset: 1, color: '#91cc75' }
                ])
              }
            }],
            barWidth: '40%',
            label: {
              show: true,
              position: 'top',
              formatter: `${predictedHeight}±${range}cm`,
              fontSize: 14,
              color: '#303133'
            }
          }
        ]
      }

      this.charts.heightChart.setOption(option)
    },
    initBMRChart(bmr) {
      if (this.charts.bmrChart) {
        this.charts.bmrChart.dispose()
      }

      const chartDom = this.$refs.bmrChart
      this.charts.bmrChart = echarts.init(chartDom)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: '10%',
          left: '15%',
          right: '15%',
          bottom: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          name: '卡路里/天',
          nameLocation: 'middle',
          nameGap: 30,
          axisLabel: {
            formatter: '{value} kcal'
          }
        },
        yAxis: {
          type: 'category',
          data: ['基础代谢', '轻度活动', '中度活动'],
          axisLine: { show: false },
          axisTick: { show: false }
        },
        series: [
          {
            name: '每日消耗',
            type: 'bar',
            data: [
              {
                value: bmr,
                itemStyle: { color: '#409EFF' }
              },
              {
                value: bmr * 1.2,
                itemStyle: { color: '#67C23A' }
              },
              {
                value: bmr * 1.4,
                itemStyle: { color: '#E6A23C' }
              }
            ],
            barWidth: '40%',
            label: {
              show: true,
              position: 'right',
              formatter: '{c} kcal',
              fontSize: 12,
              color: '#303133'
            }
          }
        ]
      }

      this.charts.bmrChart.setOption(option)
    },
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) {
          chart.resize()
        }
      })
    }
  }
}
</script>

<style scoped>
.health-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.health-header {
  text-align: center;
  margin-bottom: 30px;
}

.health-header h2 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 10px;
}

.subtitle {
  color: #909399;
  font-size: 16px;
}

.health-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
  height: 100%;  /* 确保卡片高度一致 */
}

.health-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.el-card__header {
  padding: 15px 20px;
  font-size: 16px;
  font-weight: bold;
}

.el-card__header i {
  margin-right: 8px;
  color: #409EFF;
}

.form-row {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.form-row .el-form-item {
  margin-bottom: 0;
  flex: 1;
}

.button-row {
  display: flex;
  justify-content: center;
  margin: 15px 0;
}

.result-box {
  margin-top: 10px;
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  min-height: 260px;
}

.bmi-gauge {
  height: 220px !important;
  margin-bottom: 15px;
}

.height-chart, .bmr-chart {
  height: 220px !important;
  margin-bottom: 15px;
}

.highlight {
  color: #409EFF;
  font-weight: bold;
  font-size: 18px;
}

.status-success {
  color: #67C23A;
}

.status-warning {
  color: #E6A23C;
}

.status-danger {
  color: #F56C6C;
}

.note {
  font-size: 12px;
  color: #909399;
  margin-top: 10px;
}

.el-input-number {
  width: 100%;
}

.el-form-item {
  margin-bottom: 15px;
}

.el-form-item__label {
  padding-bottom: 4px;
}

.el-radio-group {
  display: flex;
  justify-content: center;
  gap: 20px;
}

@media screen and (max-width: 768px) {
  .health-container {
    padding: 10px;
  }

  .health-header h2 {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }

  .el-card__header {
    padding: 12px 15px;
  }
}
</style> 