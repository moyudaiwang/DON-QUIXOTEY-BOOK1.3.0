<template>
  <div class="timeline-container">
    <div class="timeline-header">
      <h2>版本更新日志</h2>
    </div>

    <el-timeline>
      <el-timeline-item
        v-for="(activity, index) in activities"
        :key="index"
        :icon="activity.icon || getIcon(activity.type)"
        :type="activity.type"
        :color="activity.color || getColor(activity.type)"
        :size="activity.size || 'large'"
        :timestamp="activity.timestamp">

        <el-card class="timeline-card" :class="activity.type">
          <h3>{{ activity.title }}</h3>
          <div class="version-tag" v-if="activity.version">
            <el-tag size="small" :type="getTagType(activity.type)">
              Version {{ activity.version }}
            </el-tag>
          </div>

          <div class="content-wrapper">
            <div class="main-content">{{ activity.content }}</div>

            <div class="feature-list" v-if="activity.features && activity.features.length">
              <div class="feature-item" v-for="(feature, idx) in activity.features" :key="idx">
                <i :class="feature.icon || 'el-icon-check'"></i>
                <span>{{ feature.text }}</span>
              </div>
            </div>

            <div class="additional-info" v-if="activity.additionalInfo">
              {{ activity.additionalInfo }}
            </div>
          </div>
        </el-card>

      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
export default {
  name: 'TimeLine',
  data() {
    return {
      activities: [
        {
          title: '功能优化与问题修复',
          version: '1.2.2',
          content: '系统功能优化',
          timestamp: '2025-02-15',
          type: 'warning',
          features: [
            { text: '修复已知登录问题', icon: 'el-icon-user' },
            { text: '红楼梦头像压缩，提升加载速度', icon: 'el-icon-user' },
            { text: '优化个人中心更新信息导致头像数据异常问题', icon: 'el-icon-user' },
            { text: '新增50万以内质数螺旋分布图', icon: 'el-icon-user' },
            { text: '新增健康管理及资料下载页面', icon: 'el-icon-user' },
            { text: '优化书式地图定位坐标弹窗', icon: 'el-icon-user' },
            { text: '取消书式地图坐标聚合', icon: 'el-icon-menu' },
            { text: '新增书式地图数据：书院寻踪、觅文记', icon: 'el-icon-menu' },
            { text: '优化系统其他问题', icon: 'el-icon-setting' }
          ]
        },
        {
          title: '功能优化与问题修复',
          version: '1.2.1',
          content: '系统功能优化',
          timestamp: '2025-01-18',
          type: 'warning',
          features: [
            { text: '修复已知登录问题', icon: 'el-icon-user' },
            { text: '优化红楼梦人物头像定位坐标', icon: 'el-icon-user' },
            { text: '优化红楼梦人物图谱加载提示', icon: 'el-icon-search' },
            { text: '优化书式地图坐标初始化', icon: 'el-icon-menu' },
            { text: '新增书式地图数据：书肆寻踪', icon: 'el-icon-menu' },
            { text: '优化图书列表搜索栏错位问题', icon: 'el-icon-setting' },
            { text: '优化图书列表日期格式', icon: 'el-icon-setting' },
            { text: '优化系统其他问题', icon: 'el-icon-setting' }
          ]
        },
        {
          title: '重大更新：功能优化与问题修复',
          version: '1.2.0',
          content: '系统稳定性提升与功能优化',
          timestamp: '2025-01-01',
          type: 'warning',
          features: [
            { text: '修复已知登录问题', icon: 'el-icon-user' },
            { text: '优化红楼梦人物关系及筛选功能', icon: 'el-icon-search' },
            { text: '优化书式地图筛选功能', icon: 'el-icon-menu' },
            { text: '优化页面及交互', icon: 'el-icon-menu' },
            { text: '新增个人中心功能', icon: 'el-icon-setting' }
          ]
        },
        {
          title: '系统上线',
          version: '1.1.0',
          content: '系统正式上线运行',
          timestamp: '2024-11-01',
          type: 'info',
          features: [
            { text: '基础功能上线', icon: 'el-icon-success' },
          ]
        }
      ]
    }
  },
  methods: {
    getIcon(type) {
      const icons = {
        success: 'el-icon-check',
        warning: 'el-icon-warning',
        danger: 'el-icon-close',
        info: 'el-icon-info',
        primary: 'el-icon-star-on'
      }
      return icons[type] || 'el-icon-info'
    },
    getColor(type) {
      const colors = {
        success: '#67C23A',
        warning: '#E6A23C',
        danger: '#F56C6C',
        info: '#909399',
        primary: '#409EFF'
      }
      return colors[type]
    },
    getTagType(type) {
      const types = {
        success: 'success',
        warning: 'warning',
        danger: 'danger',
        info: '',
        primary: 'primary'
      }
      return types[type]
    }
  }
}
</script>

<style scoped>
.timeline-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
}

.timeline-header {
  text-align: center;
  margin-bottom: 40px;
}

.timeline-header h2 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.subtitle {
  color: #909399;
  font-size: 16px;
}

.timeline-card {
  margin-bottom: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.timeline-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.timeline-card h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #303133;
}

.version-tag {
  margin-bottom: 15px;
}

.content-wrapper {
  color: #606266;
}

.main-content {
  margin-bottom: 15px;
  line-height: 1.6;
}

.feature-list {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 15px;
  margin: 15px 0;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.feature-item:last-child {
  margin-bottom: 0;
}

.feature-item i {
  margin-right: 8px;
  color: #67C23A;
}

.additional-info {
  font-size: 14px;
  color: #909399;
  padding-top: 15px;
  border-top: 1px solid #EBEEF5;
}

/* 时间线样式优化 */
::v-deep .el-timeline-item__node--large {
  width: 16px;
  height: 16px;
}

::v-deep .el-timeline-item__tail {
  border-left: 2px solid #E4E7ED;
}

::v-deep .el-timeline-item__timestamp {
  color: #909399;
  font-size: 14px;
}

/* 响应式优化 */
@media screen and (max-width: 768px) {
  .timeline-container {
    padding: 0 15px;
  }

  .timeline-header h2 {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }

  .timeline-card h3 {
    font-size: 16px;
  }

  .feature-list {
    padding: 10px;
  }
}

/* 主题色适配 */
.timeline-card.success {
  border-left: 4px solid #67C23A;
}

.timeline-card.warning {
  border-left: 4px solid #E6A23C;
}

.timeline-card.danger {
  border-left: 4px solid #F56C6C;
}

.timeline-card.info {
  border-left: 4px solid #909399;
}

.timeline-card.primary {
  border-left: 4px solid #409EFF;
}
</style>
