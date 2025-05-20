<template>
  <div class="app">

<router-view/>
  </div>
</template>

<script>
import Menu from './views/menu.vue'
import Login from './views/login.vue'
import { clearCookie, getCookie } from '@/utils/login'
export default {
  data: function () {
    return {
      menuFlag: true,
      loginFlag: true,
      userInfo: {
        userId: '',
        userName: '',
        avatar: '',
      }

    }
  },

  methods: {
    check () {
      alert('Sdfsdfs')
      this.$router.push('/home')
      this.menuFlag = false
      this.loginFlag = true
    }
  },
  components: {
    Menu,
    Login
  },
  watch: {
    $route(to, from) {
      // 路由变化时检查登录状态
      const isLoggedIn = getCookie("DON_loginInfo");
      if (!isLoggedIn) {
        this.userInfo = {
          userId: '',
          userName: '',
          avatar: ''
        };
      }
    }
  },
  created () {
    setTimeout(() => {
    }, 1000)
  }
}
</script>
<style>
    /* 整体布局样式，让整个视图都铺满 */
    html, body, #app {
        height: 100%;
        width: 100%;
        margin: 0;
        padding: 0;
    }
</style>
