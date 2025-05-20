import Vue from 'vue'
import VueRouter from 'vue-router'
import { getCookie } from '@/utils/login'
import { Message } from 'element-ui'

import ArticleMain from '@/views/article/articleMain'

import HomeMain from '@/views/home/homeMain'
import BookMain from '@/views/book/bookMain'
import BookFlowMain from '@/views/book/bookFlowMain'
import ChartMain from '@/views/chart/chartMain'
import MathChart from '@/views/chart/mathChart'
import CalendarMain from '@/views/life/calendarMain'
import LifeInfo from '@/views/life/lifeInfo'
import DownloadMain from '@/views/life/downloadMain'
import HealthMain from '@/views/life/healthMain'
import AccountInfoMain from '@/views/auth/accountInfoMain'
import RoleInfoMain from '@/views/auth/roleInfoMain'
import MenuInfoMain from '@/views/auth/menuInfoMain'
import UserInfoMain from '@/views/user/userInfoMain'
import SettingMain from '@/views/setting/settingMain'
import ToolMain from '@/views/setting/toolMain'
import TimeLineMain from '@/views/setting/timeLineMain'
import ContactMain from '@/views/setting/contactMain'
import PersonalMain from '@/views/setting/personalMain'
import LogMain from '@/views/monitor/logMain'
import MapMain from '@/views/map/mapMain'
import WeiliMap from '@/views/map/weiliMap'
import HlmMain from '@/views/graph/literature/hlmMain'
import HlmMain2 from '@/views/graph/literature/hlmMain2'
import Login from '@/views/login'
import Home from '@/views/home'
import Menu from '@/views/menu'
import NotFound from '@/views/404'
Vue.use(VueRouter)

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  { path: '/', name: 'login', component: Login}, // 重定向，用来指向一打开网页就跳转到哪个路由
  { path: '/home', name: 'Home', component: Home, // 首页
    children: [ // 开始嵌套路由，这下面的所有路由都是Main路由的子路由
      { path: '/home/homeMain', name: 'HomeMain', component: HomeMain},
      { path: '/article/articleMain', name: 'ArticleMain', component: ArticleMain, meta: { requiresAuth: false }},
        { path: '/menu', name: 'Menu', component: Menu, // 数据管理
          children: [
            { path: '/user/userInfoMain', name: 'UserInfoMain', component: UserInfoMain, meta: { requiresAuth: true }},
            { path: '/auth/accountInfoMain', name: 'AccountInfoMain', component: AccountInfoMain, meta: { requiresAuth: true }},
            { path: '/auth/roleInfoMain', name: 'RoleInfoMain', component: RoleInfoMain, meta: { requiresAuth: true }},
            { path: '/auth/menuInfoMain', name: 'MenuInfoMain', component: MenuInfoMain, meta: { requiresAuth: true }},
            { path: '/book/bookMain', name: 'BookMain', component: BookMain, meta: { requiresAuth: true }},
            { path: '/book/bookFlowMain', name: 'BookFlowMain', component: BookFlowMain, meta: { requiresAuth: true }},
            { path: '/chart/chartMain', name: 'ChartMain', component: ChartMain, meta: { requiresAuth: true }},
            { path: '/chart/mathChart', name: 'MathChart', component: MathChart, meta: { requiresAuth: true }},
            { path: '/life/calendarMain', name: 'CalendarMain', component: CalendarMain, meta: { requiresAuth: true }},
            { path: '/life/lifeInfo', name: 'LifeInfo', component: LifeInfo, meta: { requiresAuth: true }},
            { path: '/life/downloadMain', name: 'downloadMain', component: DownloadMain, meta: { requiresAuth: true }},
            { path: '/life/healthMain', name: 'healthMain', component: HealthMain, meta: { requiresAuth: true }},
            { path: '/setting/settingMain', name: 'SettingMain', component: SettingMain, meta: { requiresAuth: true }},
            { path: '/setting/toolMain', name: 'ToolMain', component: ToolMain, meta: { requiresAuth: true }},
            { path: '/setting/timeLineMain', name: 'TimeLineMain', component: TimeLineMain, meta: { requiresAuth: true }},
            { path: '/setting/contactMain', name: 'ContactMain', component: ContactMain, meta: { requiresAuth: true }},
            { path: '/setting/personalMain', name: 'PersonalMain', component: PersonalMain, meta: { requiresAuth: true }},
            { path: '/monitor/logMain', name: 'LogMain', component: LogMain},
            { path: '/graph/literature/hlmMain', name: 'HlmMain', component: HlmMain, meta: { requiresAuth: true }},
            { path: '/graph/literature/hlmMain2', name: 'HlmMain2', component: HlmMain2, meta: { requiresAuth: true }},
            { path: '/map/mapMain', name: 'MapMain', component: MapMain, meta: { requiresAuth: true }},
            { path: '/map/weiliMap', name: 'WeiliMap', component: WeiliMap, meta: { requiresAuth: true }},
            { path: '/*', name: '404', component: NotFound} // 注意，这里不是嵌套路由了，这是为了设置404页面，一定要放在最后面，这样当服务器找不到页面的时候就会全部跳转到404
          ]
        },

    ]
  },
  { path: '/*', name: '404', component: NotFound} // 注意，这里不是嵌套路由了，这是为了设置404页面，一定要放在最后面，这样当服务器找不到页面的时候就会全部跳转到404
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  // 检查路由是否需要登录权限
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查是否已登录
    if (!getCookie("DON_loginInfo")) {
      // 未登录，显示提示信息
      Message.warning('请先登录');
      next({
        path: '/home/homeMain',
        // 保存原本要去的路径，登录后可以直接跳转
        query: { redirect: to.fullPath }
      });
    } else {
      next(); // 已登录，放行
    }
  } else {
    next(); // 不需要登录权限的页面直接放行
  }
});
export default router
