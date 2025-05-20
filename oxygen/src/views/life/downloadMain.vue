<template>
  <div class="download-container">

    <el-card v-for="(category, index) in categories"
             :key="index"
             class="download-card"
             :class="{ 'hover-effect': true }">
      <div slot="header" class="card-header">
        <span><i :class="category.icon"></i> {{ category.name }}</span>
      </div>
      <div class="file-list">
        <div v-for="(file, fileIndex) in category.files"
             :key="fileIndex"
             class="file-item">
          <div class="file-info">
            <i :class="getFileIcon(file.type)"></i>
            <span class="file-name">{{ file.name }}</span>
            <span class="file-size">{{ file.size }}</span>
          </div>
          <el-button type="primary"
                     size="small"
                     icon="el-icon-download"
                     @click="downloadFile(file.path)">
            下载
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'DownloadMain',
  data() {
    return {
      categories: [
        {
          name: '数据',
          icon: 'el-icon-document',
          files: [
            {
              name: '世界范围内的自杀统计数据.csv',
              type: 'csv',
              size: '1.76MB',
              path: '/static/docs/info/世界范围内的自杀统计数据.csv'
            },
            {
              name: '全世界1908年以来空难数据.csv',
              type: 'csv',
              size: '1.52MB',
              path: '/static/docs/info/全世界1908年以来空难数据.csv'
            }
          ]
        }

      ]
    }
  },
  methods: {
    getFileIcon(type) {
      const iconMap = {
        pdf: 'el-icon-document',
        word: 'el-icon-document-copy',
        excel: 'el-icon-document',
        zip: 'el-icon-folder',
        default: 'el-icon-document'
      }
      return iconMap[type] || iconMap.default
    },
    downloadFile(path) {
      // 创建一个a标签
      const link = document.createElement('a')
      link.href = path
      link.download = path.split('/').pop()
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    }
  }
}
</script>

<style scoped>
.download-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
}

.download-header {
  text-align: center;
  margin-bottom: 40px;
}

.download-header h2 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.subtitle {
  color: #909399;
  font-size: 16px;
}

.download-card {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.hover-effect:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  font-size: 18px;
}

.card-header i {
  margin-right: 8px;
  color: #409EFF;
}

.file-list {
  padding: 5px 0;
}

.file-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #EBEEF5;
  gap: 10px;
}

.file-item:last-child {
  border-bottom: none;
}

.file-info {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.file-info i {
  font-size: 18px;
  margin-right: 8px;
  color: #909399;
  flex-shrink: 0;
}

.file-name {
  flex: 1;
  margin-right: 20px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
}

.file-size {
  color: #909399;
  font-size: 14px;
  margin-right: 20px;
  flex-shrink: 0;
  min-width: 60px;
  text-align: right;
}

@media screen and (max-width: 768px) {
  .download-container {
    padding: 0 15px;
  }

  .download-header h2 {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }

  .file-item {
    padding: 6px 10px;
  }

  .file-info {
    flex: 1;
    min-width: 0;
    margin-bottom: 0;
  }

  .el-button {
    width: auto;
    flex-shrink: 0;
  }
}
</style>
