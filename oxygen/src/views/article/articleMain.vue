<template>
  <div class="forum-container">
    <!-- 顶部操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="showPostDialog">发帖</el-button>
      <el-select v-model="selectedCategory" placeholder="选择分类" class="category-select">
        <el-option
          v-for="item in categories"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-input
        placeholder="搜索帖子"
        v-model="searchText"
        class="search-input">
        <el-button slot="append" icon="el-icon-search" @click="searchPosts"></el-button>
      </el-input>
    </div>

    <!-- 帖子列表 -->
    <div class="post-list">
      <el-card v-for="post in posts" :key="post.id" class="post-card">
        <div class="post-header">
          <el-avatar :src="post.authorAvatar" size="small"></el-avatar>
          <span class="author-name">{{ post.author }}</span>
          <span class="post-time">{{ post.createTime }}</span>
        </div>
        <div class="post-title" @click="viewPost(post)">{{ post.title }}</div>
        <div class="post-preview">{{ post.preview }}</div>
        <div class="post-footer">
          <span class="post-category">{{ getCategoryLabel(post.category) }}</span>
          <div class="post-stats">
            <span><i class="el-icon-view"></i> {{ post.views }}</span>
            <span><i class="el-icon-chat-line-square"></i> {{ post.comments }}</span>
            <span><i class="el-icon-star-off"></i> {{ post.likes }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <!-- 发帖对话框 -->
    <el-dialog title="发布新帖" :visible.sync="dialogVisible" width="50%">
      <el-form :model="newPost" :rules="rules" ref="postForm">
        <el-form-item label="标题" prop="title">
          <el-input v-model="newPost.title"></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="newPost.category" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="newPost.content" :rows="6"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPost">发布</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ForumMain',
  data() {
    return {
      searchText: '',
      selectedCategory: '',
      currentPage: 1,
      pageSize: 10,
      total: 100,
      dialogVisible: false,
      categories: [
        { value: 'all', label: '全部' },
        { value: 'tech', label: '技术讨论' },
        { value: 'share', label: '经验分享' },
        { value: 'question', label: '问题求助' },
        { value: 'news', label: '行业新闻' }
      ],
      posts: [
        {
          id: 1,
          title: '如何学习Vue.js？',
          author: '张三',
          authorAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createTime: '2024-03-20 10:30',
          preview: 'Vue.js是一个流行的前端框架，本文将分享学习Vue.js的经验和方法...',
          category: 'tech',
          views: 1234,
          comments: 58,
          likes: 99
        },
        // 可以添加更多测试数据
      ],
      newPost: {
        title: '',
        category: '',
        content: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入内容', trigger: 'blur' },
          { min: 10, max: 5000, message: '长度在 10 到 5000 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    searchPosts() {
      // 实现搜索逻辑
      console.log('搜索:', this.searchText)
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchPosts()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchPosts()
    },
    showPostDialog() {
      this.dialogVisible = true
    },
    submitPost() {
      this.$refs.postForm.validate((valid) => {
        if (valid) {
          // 实现发帖逻辑
          console.log('发布帖子:', this.newPost)
          this.dialogVisible = false
        }
      })
    },
    viewPost(post) {
      // 实现查看帖子详情逻辑
      console.log('查看帖子:', post)
    },
    getCategoryLabel(value) {
      const category = this.categories.find(c => c.value === value)
      return category ? category.label : ''
    },
    fetchPosts() {
      // 实现获取帖子列表的逻辑
      console.log('获取帖子列表')
    }
  },
  mounted() {
    this.fetchPosts()
  }
}
</script>

<style scoped>
.forum-container {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
}

.category-select {
  width: 150px;
}

.search-input {
  width: 300px;
}

.post-list {
  margin-bottom: 20px;
}

.post-card {
  margin-bottom: 15px;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.author-name {
  margin-left: 10px;
  font-weight: bold;
}

.post-time {
  margin-left: 15px;
  color: #999;
  font-size: 12px;
}

.post-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  cursor: pointer;
}

.post-title:hover {
  color: #409EFF;
}

.post-preview {
  color: #666;
  margin-bottom: 10px;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #999;
}

.post-category {
  background-color: #f0f2f5;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.post-stats {
  display: flex;
  gap: 15px;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>
