<template>
    <el-container  class="home-container">
      <!-- 头部导航 -->
      <el-header >
        <el-row>
          <el-col :span="24" style="background:#669999">
            <div style="float:left; cursor: pointer">
              <h4 style="color: #555555; padding-top: 10px" class="menuTitle radius" @click="goTo('/home/homeMain')">DonQuixotey</h4>
            </div>
            <div class="button-login-left">
              <el-menu :default-active="activeIndex" class="el-menu-demo" style="background:#669999" mode="horizontal" @select="handleSelect" >
                <el-menu-item index="1" @click="goTo('/book/bookMain')" style="color: #555555;" >图书管理</el-menu-item>
                <!--TODO 发版前注释<el-submenu index="2">
                  <template slot="title" @click="goTo('/book/bookMain')">图书交流</template>
                  <el-menu-item index="2-1">购书</el-menu-item>
                  <el-menu-item index="2-2">出售</el-menu-item>
                </el-submenu>-->
                <!-- TODO 发版前注释 <el-menu-item index="3" @click="goTo('/article/articleMain')" style="color: #555555;" >论坛</el-menu-item>-->
              </el-menu>
            </div>
            <div class="button-login-right">
              <el-badge class="button-login-right-badge" v-if="!logged">
                <el-button type="primary" @click="loginTo()">登录</el-button>
                <el-button type="primary" @click="registerTo()">注册</el-button>
              </el-badge>
              <el-badge class="button-login-right-photo" v-if="logged">
                <el-dropdown @command="handleCommand" style="padding-top: 0px;right: 10px;">
                  <div command="/setting/personalMain">
                    <el-avatar  :src="userInfo.avatar"></el-avatar>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item >用户名:{{userInfo.userName}}</el-dropdown-item>
                    <el-dropdown-item command="/setting/personalMain">个人中心</el-dropdown-item>
                    <el-dropdown-item @click.native="handleHomeReturn()">退出</el-dropdown-item> <!--command="/"-->
                  </el-dropdown-menu>
                </el-dropdown>
              </el-badge>
            </div>
          </el-col>
        </el-row>
      </el-header>

      <!-- 主要内容区域 -->
      <el-main style="padding: 0; ">
        <router-view></router-view>
        <!--  登录弹出框 class="login-container" -->
        <el-dialog title="登录" :visible.sync="loginVisible" width="20%" :before-close="loginClose" >
            <el-form :model="loginForm" ref="loginForm" :rules="loginRules" label-position="left">
              <el-form-item prop="alien">
                <el-input v-model="loginForm.alien" placeholder="用户ID/用户名"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input type="password" show-password v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" style="width: 100% " @click="login()">登录</el-button>
            </div>
        </el-dialog>
        <!--  注册弹窗 -->
        <el-dialog title="注册" :visible.sync="registerVisible" width="20%" :before-close="registerClose" >
          <el-form :model="registerForm" ref="registerForm" :rules="registerRules" label-position="left">
            <el-form-item prop="userId">
              <el-input type="text" v-model="registerForm.userId" placeholder="用户ID"></el-input>
            </el-form-item>
            <el-form-item prop="userName">
              <el-input type="text" v-model="registerForm.userName" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" show-password v-model="registerForm.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item prop="isPassword">
              <el-input type="password" show-password v-model="registerForm.isPassword" placeholder="请确认密码"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" >
              <el-button type="primary" style="width: 100%" @click="register()">注册</el-button>
          </div>
        </el-dialog>
      </el-main>

    <!-- 页脚 -->
    <el-footer style="height: auto; padding: 0;background: #669999">
        <el-col :span="24"  class="footer-links">
          <el-link v-for="link in links" :key="link.url" :underline="false" :href="link.url" target="_blank">
            {{link.text}}
          </el-link>
        </el-col>
        <el-col :span="24" class="footer-copyright">
          <div class="footer-info">
            <span>豫ICP备2020035146号 | Copyright ©2020-2025 www.donQuixotey.com All Rights Reserved</span>
            <div class="social-icons">
              <el-popover v-for="(icon, index) in socialIcons" :key="index" trigger="hover" placement="top">
                <el-image class="qr-code" :src="icon.qrCode"></el-image>
                <el-avatar slot="reference" :src="icon.icon" size="small" style="cursor: pointer"></el-avatar>
              </el-popover>
            </div>
          </div>
        </el-col>
    </el-footer>
  </el-container>

</template>
<script>
import { setCookie,getCookie ,clearCookie } from '@/utils/login'
import {  isEmail,isPhone,isNum,replaceSpace } from '@/utils/validate'
import {login,register} from '@/api/common'
export default {
  data () {
    return {
      isCollapse: false,
      activeIndex: '1',
      logged: false, //是否已登录
      loginVisible:false,
      registerVisible:false,
      loginForm: {
        alien: '',
        password: '',
      },
      registerForm: {
        userId: '',
        userName: '',
        password: '',
        isPassword: '',
        verifyCode: ''
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
      registerRules:{
        userId: [
          { required: true, message: '请输入用户ID', trigger: 'blur' },
          { min: 6, max: 20, message: '请输入[6~20]位字母/数字', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '请输入[2~20]位字母/数字/中文', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 32, message: '请输入[6~30]位字母/数字', trigger: 'blur' }
        ],
        isPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 32, message: '请输入[6~30]位字母/数字', trigger: 'blur' }
        ]
      },
      alienType:'1',
      userInfo: {
        userId: '',
        userName: '',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      },
      socialIcons: [
        {
          icon: require('../assets/image/icon/weChat.png'),
          qrCode: require('../assets/image/code/weChatCode.jpg')
        },
        {
          icon: require('../assets/image/icon/douBan.png'),
          qrCode: require('../assets/image/code/douBanCode.jpg')
        },
        {
          icon: require('../assets/image/icon/xhs.png'),
          qrCode: require('../assets/image/code/xhsCode.jpg')
        }
      ],
      links: [
        { text: '中国国家图书馆', url: 'http://www.nlc.cn' },
        { text: '国家典籍博物馆', url: 'http://www.nlc.cn/nmcb/' },
        { text: '国家古籍数字化资源总平台', url: 'https://www.guji.cn/' },
        { text: '国家哲学社会科学文献中心', url: 'https://www.ncpssd.cn/' },
        { text: '国家数据', url: 'https://data.stats.gov.cn' },
        { text: '国家博物馆', url: 'https://www.chnmuseum.cn/' },
        { text: '中国古籍保护协会', url: 'http://www.chinaabp.cn/chinaabp/' },
        { text: '敦煌研究院', url: 'https://www.dha.ac.cn' },
        { text: '故宫博物院', url: 'https://www.dpm.org.cn' },
        { text: '南京博物院', url: 'http://www.njmuseum.com/zh' },
        { text: '陕西历史博物馆', url: 'http://www.sxhm.com' },
        { text: '天一阁博物院', url: 'https://www.tianyige.com.cn/' },
        { text: '大英博物馆', url: 'https://www.britishmuseum.org/' },
        { text: '大都会博物馆', url: 'https://www.metmuseum.org/' },
        { text: '奈良国立博物馆', url: 'https://www.narahaku.go.jp/' },
        { text: '静嘉堂文库美术馆', url: 'https://www.seikado.or.jp/' },
        { text: '卢浮宫博物馆', url: 'https://www.louvre.fr/zh/' },
        { text: '美国国会图书馆', url: 'https://www.loc.gov/' },
        { text: '荣宝斋', url: 'http://www.rongbaozhai.cn/' },
        { text: '西泠印社', url: 'http://www.xlys.org.cn/' },
        { text: '豆瓣读书', url: 'https://book.douban.com' },
        { text: '凤凰读书', url: 'https://culture.ifeng.com' },
        { text: '孔夫子旧书网', url: 'https://www.kongfz.com' },
        { text: '爱思想网', url: 'http://www.aisixiang.com' },
        { text: '书格', url: 'https://new.shuge.org' },
        { text: '观沧海', url: 'https://www.ageeye.cn' },
        { text: '地图书', url: 'https://www.ditushu.com/' },
        { text: '学术地图发布平台', url: 'http://amap.zju.edu.cn/' },
        { text: '中国哲学书电子化计划', url: 'https://ctext.org' },
        { text: '漢川草廬', url: 'http://www.xn--5rtnx620bw5s.tw/index.php' },
        { text: '中国古典文献资源导航系统', url: 'https://www.wenxianxue.cn/' },
        { text: '古腾堡计划', url: 'https://www.gutenberg.org/' },
        { text: '教科文组织', url: 'https://www.unesco.org/zh' },
        { text: '北京国际图书博览会', url: 'https://www.bibf.net/' },
        { text: '生活·读书·新知三联书店', url: 'http://www.sdxjpc.com/main/' },
        { text: 'Penguin', url: 'https://www.penguin.com/' },
        { text: '中国色', url: 'http://zhongguose.com#tianlan' },
        { text: '发音词典', url: 'https://zh.forvo.com/' },
        { text: '默沙东诊疗手册', url: 'https://www.msdmanuals.cn' },
        { text: 'GeoGebra', url: 'https://www.geogebra.org' },
        { text: 'WhoWasAlive', url: 'https://neal.fun/who-was-alive/' },
        { text: 'DeepSea', url: 'https://neal.fun/deep-sea/' }
      ]
    }
  },
  mounted () {
    if(getCookie("DON_loginInfo")){
       this.logged = true;
    }
    // 从 localStorage 获取用户信息
    const userInfo = JSON.parse(localStorage.getItem('userInfo'));
    if (userInfo) {
      this.userInfo = userInfo; // 假设 home 组件中有 userInfo 数据
    }
  },
  beforeDestroy() {
    document.body.style.overflow = '';
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    handleCommand(path) {
      this.$router.replace(path);
    },
    goTo (path) {
      this.$router.replace(path)
    },
    //登录TO
    loginTo(){
      this.loginVisible = true;
    },
    //注册TO
    registerTo(){
      this.registerVisible = true;
    },

    loginClose () {
      this.loginVisible = false
      this.$refs.loginForm.resetFields()
    },
    registerClose () {
      this.registerVisible = false
      this.$refs.registerForm.resetFields()
    },
    //登录
    login() {
      let param = {
        alien: this.loginForm.alien,
        pwd: this.loginForm.password
      };
      login(param).then(res => {
        if (res.data.code == '100200') {
          this.userInfo = res.data.object;
          setCookie("DON_loginInfo", this.userInfo.userId, 1);
          this.logged = true;
          this.loginVisible = false;
          if (this.userInfo.avatar != '' && this.userInfo.avatar != null){
            this.userInfo.avatar = require('../../static/images/avatar/'+this.userInfo.avatar);
          }else {
            //默认头像
            this.userInfo.avatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png';
          }
          // 获取重定向地址
            const redirect = this.$route.query.redirect;
            // 如果有重定向地址，则跳转到该地址，否则跳转到首页
            this.$router.replace(redirect || '/home/homeMain');
        } else {
          this.$message({message: res.data.msg, type: 'error'});
        }
      }).catch(error => {
        console.log(error);
      });
    },
    register() {
      if(this.registerForm.password !== this.registerForm.isPassword) {
        this.$message({message: '两次密码不一致', type: 'error'});
        return;
      }
      let param = {
        userId: this.registerForm.userId,
        userName: this.registerForm.userName,
        pwd: this.registerForm.password
      };
      register(param).then(res => {
        if (res.data.code == '100200') {
          this.$message({message: '注册成功', type: 'success'});
          this.registerVisible = false;
        } else {
          this.$message({message: res.data.msg, type: 'error'});
        }
      }).catch(error => {
        console.log(error);
      });
    },
    //退出
    handleHomeReturn() {
      // 先执行登出操作
      this.loginOut();
      // 然后跳转到首页
      this.$router.replace('/home/homeMain');
    },
    loginOut() {
      // 清除 localStorage
      localStorage.removeItem('userInfo');
      clearCookie("DON_loginInfo");
      this.logged = false;
      //this.$message({message: '退出成功', type: 'success'});
    },



  },
  watch: {
  },
  components: {
  }
}
</script>
<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 0;
}

.el-header {
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.button-login-left {
  float: left;
  margin-left: 20px;
}

.button-login-right {
  float: right;
  margin-right: 20px;
  margin-top: 10px;
}

.el-main {
  flex: 1;
  padding: 0;
  // overflow: hidden;
}


/* 页脚整体样式 */
.el-footer {
  width: 100%;
  text-align: center;
  color: #555555;
  padding: 20px 0;
}

/* 链接列表样式 */
.footer-links {
  padding: 15px 20px;
  text-align: center;
  line-height: 2;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

/* 链接样式 */
.el-link {
  color: #555555;
  font-size: 13px;
  white-space: nowrap;
}

/* 版权信息区域样式 */
.footer-copyright {
  padding: 10px 20px;
  text-align: center;
}

/* 页脚信息样式 */
.footer-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  color: #555555;
  flex-wrap: wrap;
  font-size: 13px;
}

/* 社交图标容器样式 */
.social-icons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

/* 二维码图片样式 */
.qr-code {
  width: 120px;
  height: 120px;
  display: block;
  margin: 0 auto;
}

/* 响应式调整 */
@media screen and (max-width: 1200px) {
  .footer-links {
    padding: 10px;
  }

  .el-link {
    font-size: 12px;
  }

  .footer-info {
    font-size: 12px;
  }
}

</style>
