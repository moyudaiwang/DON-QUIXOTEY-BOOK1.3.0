<template>
  <el-container class="app-container">
    <el-aside width="auto" class="app-aside">
      <el-menu default-active="1-4-1" class="el-menu-vertical-demo" :unique-opened="true" router  @open="handleOpen" @close="handleClose" :collapse="isCollapse">
        <el-radio-group v-model="isCollapse" style="cursor: pointer">
          <i v-if="!isCollapse" style=" font-size:20px" class="el-icon-s-fold" @click="isCollapse=true"></i>
          <i v-if="isCollapse" style=" font-size:20px" class="el-icon-s-unfold" @click="isCollapse=false"></i>
        </el-radio-group>

        <template v-for="menu in menuList">
          <!-- 目录类型菜单 -->
          <el-submenu v-if="menu.type === '0' && menu.status === '0'" :key="menu.index" :index="menu.index">
            <template slot="title">
              <i :class="menu.icon"></i>
              <span slot="title">{{ menu.title }}</span>
            </template>
            <!-- 子菜单递归 -->
            <template v-if="menu.children && menu.children.length > 0">
              <template v-for="child in menu.children">
                <!-- 菜单类型 -->
                <el-menu-item v-if="child.type === '1' && child.status === '0'" :key="child.index" :index="child.index" @click="goTo(child.path)">
                  <i :class="child.icon"></i>
                  <span slot="title">{{ child.title }}</span>
                </el-menu-item>
                <!-- 按钮类型不显示 -->
              </template>
            </template>
          </el-submenu>
          <!-- 一级菜单 -->
          <el-menu-item v-if="menu.type === '1' && menu.status === '0'" :key="menu.index" :index="menu.index" @click="goTo(menu.path)">
            <i :class="menu.icon"></i>
            <span slot="title">{{ menu.title }}</span>
          </el-menu-item>
        </template>

      </el-menu>
    </el-aside>
    <el-main class="app-main">
      <router-view></router-view>
    </el-main>
  </el-container>
</template>
<script>
import { queryMenuTree } from '@/api/authInfo'

export default {
  data () {
    return {
      isCollapse: false,
      activeIndex: '1',
      activeIndex2: '1',
      input: '',
      drawer: false,
      activeName: 'first',
      menuList: []
    }
  },
  mounted () {
    this.getMenuData()
  },
  beforeDestroy() {
  },
  methods: {
    getMenuData() {
      let param = {
      };
      queryMenuTree(param).then(res => {
          if (res.data) {
            this.menuList = this.formatMenuData(res.data)
          }
        })
        .catch(error => {
          console.error('获取菜单数据失败:', error)
        })
    },
    formatMenuData(data) {
      return data.map(item => ({
        index: item.menuId,
        title: item.menuName,
        icon: item.menuIcon,
        path: item.menuUrl,
        type: item.menuType,
        status: item.menuStatus,
        children: item.children ? this.formatMenuData(item.children) : null
      }))
    },
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    goTo (path) {
      this.$router.replace(path)
    },
    handleCommand (path) {
      this.$router.replace(path)
    },
    handleClick () {
    }
  },
  watch: {
  },
  components: {
  }
}
</script>
<style scoped>
  .el-main {
    text-align: center;
    line-height: 20px;
  }

  body > .el-container {
    margin-bottom: 40px;
  }

  .el-container:nth-child(5) ,
  .el-container:nth-child(6) {
    line-height: 260px;
  }

  .el-container:nth-child(7) {
    line-height: 320px;
  }
  .el-menu-vertical-demo:not(.el-menu--collapse) {
      width: 200px;
      min-height: 100%;
  }
  .el-menu--collapse {
    width: 60px;
    min-height: 100%;
  }

  .el-row {
    margin-bottom: 0px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
  }

  .el-radio-group {
    font-size: 20px;
    margin-top: 20px;
    margin-left: 5px;
    .shrinkBtn {
      position: fixed;
      left: -18px;
      top: 38px;
      margin-left: 10px;
      transform: rotate(180deg);
    }
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }

/* 移除之前的固定高度设置，改用vh单位 */
.app-container {
  min-height: 100vh; /* 确保最小高度占满视口 */
  height: 100vh;
  display: flex;
  overflow: hidden; /* 防止出现双滚动条 */
}

.app-aside {
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止侧边栏溢出 */
}

.app-main {
  height: 100vh;
  overflow-y: auto; /* 允许内容区域滚动 */
  padding: 20px;
  box-sizing: border-box;
}

/* 优化菜单滚动 */
.el-menu {
  height: 100%;
  border-right: none;
  overflow-y: auto;
  overflow-x: hidden;
}

/* 优化滚动条样式 */
.el-menu::-webkit-scrollbar {
  width: 60px;
}

.el-menu::-webkit-scrollbar-thumb {
  background-color: rgba(144, 147, 153, 0.3);
  border-radius: 3px;
}

/* 所有菜单项基础样式 */
.el-menu-item,
.el-submenu__title {
  text-align: left;
  padding-left: 20px !important;
}

/* 子菜单向右缩进 */
.el-submenu .el-menu-item {
  padding-left: 40px !important;
}

/* 二级子菜单更多缩进 */
.el-submenu .el-submenu .el-menu-item {
  padding-left: 60px !important;
}

/* 三级子菜单的缩进 */
.el-submenu .el-submenu .el-submenu .el-menu-item {
  padding-left: 80px !important;
}

/* 图标和文字的间距 */
.el-menu-item i,
.el-submenu__title i {
  margin-right: 5px;
}

/* 收起状态的样式 */
.el-menu--collapse {
  width: 60px;
  min-height: 100%;
}

/* 收起状态下的图标居中 */
.el-menu--collapse .el-menu-item i,
.el-menu--collapse .el-submenu__title i {
  margin: 0;
}
</style>
