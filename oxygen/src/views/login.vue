<template>
<div>
  <body id="poster">

    <div class="background">
     <img :src="imgSrc" width="100%" height="100%" alt="" />
    </div>
    <div class="login">
    <el-form class="login-container" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left" label-width="0px">
      <h3 class="login_title">LOGIN</h3>
      <el-form-item prop="alien">
        <el-input v-model="loginForm.alien" placeholder="用户ID/用户名"></el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
      </el-form-item>

      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;border: none" @click="login()">登 录</el-button>
      </el-form-item>
    </el-form>
    </div>
  </body>
  </div>
</template>

<script>
import { setCookie, getCookie, clearCookie } from '@/utils/login'
import {login} from '@/api/common'
export default {
  name: 'Login',
  data () {
    return {
      loginForm: {
        alien: '',
        password: ''
      },
      loginRules:{
        alien: [
          { required: true, message: '请输入用户ID/用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '请输入[2~20]位字母/数字/中文', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 32, message: '请输入[6~30]位字母/数字', trigger: 'blur' }
        ]
      },
      userInfo: {
        userId: '',
        userName: '',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      },
      responseResult: [],
      imgSrc: require('../assets/grild001.jpg')
    }
  },
  mounted () {
    // 判断页面是否有对应Cookie
    if (getCookie('pas') && getCookie('name')) {
    // 有则写入
      this.loginForm.username = getCookie('name');
      this.loginForm.password = getCookie("pas");
    } else {
        // 无则写空
        this.loginForm.username = ''
        this.loginForm.password = ''
    }
  },
  methods: {

    //登录
    login() {
      let param = {
        alien: this.loginForm.alien,
        pwd: this.loginForm.password
      };
      login(param).then(res => {
        if (res.data.code == '100200') {
          this.userInfo = res.data.object;
          // 保存用户信息到 localStorage
          localStorage.setItem('userInfo', JSON.stringify({
            userId: this.userInfo.userId,
            userName: this.userInfo.userName,
            avatar: this.userInfo.avatar != '' && this.userInfo.avatar != null
              ? require('../../static/images/avatar/'+this.userInfo.avatar)
              : 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          }));
          setCookie("DON_loginInfo", this.userInfo.userId, 1);
          this.$message({message: res.data.msg, type: 'success'});
          this.$router.replace('/home/homeMain');
        } else {
          this.$message({message: res.data.msg, type: 'error'});
        }
      }).catch(error => {
        console.log(error);
      });
    },



  }
}
</script>

<style>
  #poster {
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0px;
    padding: 0;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    /*登录框光环*/
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
  }

.background{
    width:100%;
    height:100%;  /**宽高100%是为了图片铺满屏幕 */
    z-index:-1;
    position: absolute;
}

</style>
