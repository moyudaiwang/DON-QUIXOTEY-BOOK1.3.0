<template>
  <el-container class="user-profile">
    <el-main>
      <el-form :model="userForm" :label-position="labelPosition" ref="userForm" :rules="userRules" label-width="80px" @submit.prevent="updateInfo">

        <!-- 头像 -->
        <el-form-item prop="avatar">
          <div class="avatar-wrapper">
            <el-avatar :size="100" :src="userForm.avatar" class="avatar" >  </el-avatar>
          </div>
        </el-form-item>

        <!-- 用户ID -->
        <el-form-item label="用户ID" prop="userId">
          <el-col :span="18">
            <el-input v-model="userForm.userId" :disabled="true" />
          </el-col>
        </el-form-item>

        <!-- 用户名 -->
        <el-form-item label="用户名" prop="userName">
          <el-col :span="18">
            <el-input v-model="userForm.userName" placeholder="请输入用户名" />
          </el-col>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="pwd">
          <el-col :span="18">
            <el-input v-model="userForm.pwd" show-password placeholder="请输入密码" />
          </el-col>
          <el-button @click="updatePwd">修改密码</el-button>
        </el-form-item>

        <!-- 性别 -->
        <el-form-item label="性别" prop="sex">
          <el-col :span="8">
            <el-radio-group v-model="userForm.sex">
              <el-radio label="M">男</el-radio>
              <el-radio label="F">女</el-radio>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <!-- 出生日期 -->
        <el-form-item label="出生日期" prop="birthday">
          <el-col :span="18">
            <el-date-picker
              v-model="userForm.birthday"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              style="width: 328px;"
            >
            </el-date-picker>
          </el-col>
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="updateInfo">保存</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
import { setCookie,getCookie ,clearCookie } from '@/utils/login'
import {updatePwd } from '@/api/authInfo'
import {queryByUserId,updateInfo,updateAvatar } from '@/api/userInfo'
export default {
  name: 'setting',
  data () {
    return {
        labelPosition: 'right',
        userForm: {
          userId: '',
          userName: '',
          sex: '',
          birthday: '',
          pwd: '',
          avatar: '',

        },
        userRules:{
          userName: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 2, max: 24, message: '请输入[2~24]位中文、字母或数字', trigger: 'blur' }
          ],
          pwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 32, message: '请输入[6~32]位字母或数字', trigger: 'blur' }
          ],
          sex: [
            {required: true, message: '请选择性别', trigger: 'change' }
          ],
          birthday: [
            { type: 'date', required: true, message: '请选择出生日期', trigger: 'change' }
          ],

        },
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.userForm.userId = getCookie("DON_loginInfo");
      this.queryByUserId();
    });
  },
  methods: {
    //查询
    queryByUserId() {
      let param = {
        userId: this.userForm.userId
      }
      queryByUserId(param).then(res => {
        if (res.data.code == '100200') {
          this.userForm = res.data.object;
          if (this.userForm.avatar != '' && this.userForm.avatar != null){
            this.userForm.avatar = require('../../../static/images/avatar/'+this.userForm.avatar);
          }else {
            //默认头像
            this.userForm.avatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png';
          }
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    //更新个人信息
    updateInfo() {
      updateInfo(this.userForm).then(res => {
        if (res.data.code == '100200') {
          this.$message({message: res.data.msg, type: 'success'})
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    //更新头像
    updateAvatar() {
      let pwd = this.userForm.pwd.replace(/\s/g,'');
      let userId = this.userForm.userId.replace(/\s/g,'');
      let userName = this.userForm.userName.replace(/\s/g,'');
      if(pwd == null || pwd ==''){
          this.$message({message: '密码不能为空', type: 'error'})
          return
      }
      if(userId == pwd || userName == pwd){
          this.$message({message: '密码不能与(用户ID或用户名)相同', type: 'error'})
          return
      }
      let param = {
        userId: this.userForm.userId,
        pwd: this.userForm.pwd
      }
      updatePwd(param).then(res => {
        if (res.data.code == '100200') {
          this.$message({message: res.data.msg, type: 'success'})
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    //更新密码
    updatePwd() {
      let pwd = this.userForm.pwd.replace(/\s/g,'');
      let userId = this.userForm.userId.replace(/\s/g,'');
      let userName = this.userForm.userName.replace(/\s/g,'');
      if(pwd == null || pwd ==''){
          this.$message({message: '密码不能为空', type: 'error'})
          return
      }
      if(userId == pwd || userName == pwd){
          this.$message({message: '密码不能与(用户ID或用户名)相同', type: 'error'})
          return
      }
      let param = {
        userId: this.userForm.userId,
        pwd: this.userForm.pwd
      }
      updatePwd(param).then(res => {
        if (res.data.code == '100200') {
          this.$message({message: res.data.msg, type: 'success'})
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
  }
}
</script>

<style scoped>
.user-profile {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background: #f9f9f9;
  border: 1px solid #ccc;
  border-radius: 10px;
  text-align: center; /* 居中对齐 */
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
}
.avatar {
  position: relative;
  transition: opacity 0.3s;
}
.avatar-wrapper:hover .avatar {
  opacity: 0.5; /* 鼠标悬停时头像变半透明 */
}

</style>
