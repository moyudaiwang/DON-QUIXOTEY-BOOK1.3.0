<template>
  <div class="about-container">
    <!-- 愿景部分 -->
    <section class="vision-section">
      <h2>网站愿景</h2>
      <div class="vision-content">
        <p>DonQuixotey致力于打造一个开放、共享的文化交流平台。我们希望：</p>
        <ul>
          <li>促进知识的自由传播与交流</li>
          <li>建立人与文化的深度连接</li>
          <li>以全新的视角观察我们的文明</li>
        </ul>
      </div>
    </section>

    <!-- 留言反馈部分 -->
    <section class="feedback-section">
      <h2>留言反馈</h2>
      <div class="feedback-content">
        <el-form ref="feedbackForm" :model="feedbackForm" :rules="feedbackRules" label-width="80px">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="feedbackForm.email" placeholder="请输入您的邮箱（选填）"></el-input>
          </el-form-item>
          <el-form-item label="类型" prop="type">
            <el-select v-model="feedbackForm.type" placeholder="请选择留言类型">
              <el-option label="功能建议" value="suggestion"></el-option>
              <el-option label="问题反馈" value="problem"></el-option>
              <el-option label="内容纠错" value="correction"></el-option>
              <el-option label="其他" value="other"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="留言内容" prop="content">
            <el-input type="textarea" v-model="feedbackForm.content" :rows="4" placeholder="请输入您的留言内容"></el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="feedbackForm.isAnonymous">匿名留言</el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitFeedback">提交留言</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </section>

    <!-- 联系我们部分 -->
    <section class="contact-section">
      <h2>联系我们</h2>
      <div class="contact-content">
        <div class="contact-info">
          <!--TODO 1.2.0上线前隐藏 <div class="info-item">
            <i class="el-icon-location"></i>
            <span>地址：额...不重要</span>
          </div>
          <div class="info-item">
            <i class="el-icon-phone"></i>
            <span>电话：社恐...不提供</span>
          </div>-->
          <div class="info-item">
            <i class="el-icon-message"></i>
            <span>邮箱：sisyphus1219@gmail.com</span>
          </div>
        </div>

        <!-- 社交媒体图标 -->
        <div class="social-media">
          <div v-for="(icon, index) in socialIcons" :key="index" class="social-icon">
            <el-popover trigger="hover" placement="top">
              <el-image class="qr-code" :src="icon.qrCode"></el-image>
              <el-avatar slot="reference" :src="icon.icon" :size="40"></el-avatar>
            </el-popover>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'AboutUs',
  data() {
    return {
      socialIcons: [
        {
          icon: require('@/assets/image/icon/weChat.png'),
          qrCode: require('@/assets/image/code/weChatCode.jpg')
        },
        {
          icon: require('@/assets/image/icon/douBan.png'),
          qrCode: require('@/assets/image/code/douBanCode.jpg')
        },
        {
          icon: require('@/assets/image/icon/xhs.png'),
          qrCode: require('@/assets/image/code/xhsCode.jpg')
        }
      ],
      feedbackForm: {
        name: '',
        email: '',
        type: '',
        content: '',
        isAnonymous: false
      },
      feedbackRules: {
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择留言类型', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入留言内容', trigger: 'blur' },
          { min: 5, message: '留言内容至少5个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitFeedback() {
      this.$refs.feedbackForm.validate((valid) => {
        if (valid) {
          // TODO: 调用后端API提交留言
          this.$message({
            message: '感谢您的留言！',
            type: 'success'
          });
          this.resetForm();
        }
      });
    },
    resetForm() {
      this.$refs.feedbackForm.resetFields();
      this.feedbackForm.isAnonymous = false;
    }
  }
}
</script>

<style scoped>
.about-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

section {
  margin-bottom: 60px;
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

h2 {
  color: #333;
  font-size: 24px;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 2px solid #669999;
}

.vision-content {
  color: #666;
  line-height: 1.8;
}

.vision-content ul {
  padding-left: 20px;
  margin-top: 20px;
}

.vision-content li {
  margin-bottom: 15px;
  position: relative;
  padding-left: 20px;
}

.vision-content li::before {
  content: "•";
  color: #669999;
  position: absolute;
  left: 0;
  font-size: 20px;
}

.contact-content {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 30px;
}

.contact-info {
  flex: 1;
  min-width: 300px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  color: #666;
}

.info-item i {
  font-size: 20px;
  color: #669999;
  margin-right: 15px;
  width: 24px;
  text-align: center;
}

.social-media {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.social-icon {
  cursor: pointer;
  transition: transform 0.3s;
}

.social-icon:hover {
  transform: translateY(-3px);
}

.qr-code {
  width: 120px;
  height: 120px;
}

.feedback-section {
  margin-bottom: 60px;
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.feedback-content {
  max-width: 600px;
  margin: 0 auto;
}

.el-select {
  width: 100%;
}

.el-checkbox {
  margin-left: 0;
}

@media screen and (max-width: 768px) {
  .about-container {
    padding: 20px 10px;
  }

  section {
    padding: 20px;
  }

  .contact-content {
    flex-direction: column;
  }

  .social-media {
    justify-content: center;
  }

  .feedback-content {
    padding: 0 10px;
  }
}
</style>
