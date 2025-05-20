<template>
  <div>
    <!-- 筛选悬浮窗 -->
    <div v-show="showFilter" class="filter-panel">
      <el-radio-group v-model="checked_sex" size="mini" @change="doFilter">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="男">男</el-radio-button>
        <el-radio-button label="女">女</el-radio-button>
      </el-radio-group>
      <el-radio-group v-model="checked_clan" size="mini" style="margin-left:10px;" @change="doFilter">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="贾">贾</el-radio-button>
        <el-radio-button label="史">史</el-radio-button>
        <el-radio-button label="王">王</el-radio-button>
        <el-radio-button label="薛">薛</el-radio-button>
      </el-radio-group>
    </div>
    <!-- 筛选悬浮球按钮 -->
    <div class="float-btn" @click="toggleFilter" @mouseenter="showFilterPanel">
      <i :class="['el-icon-s-operation', {'is-active': showFilter}]"></i>
    </div>

    <div ref="myPage" style="margin: 0; width: 100%; height: calc(100vh - 100px); position: relative;">
      <!-- 加载遮罩层 -->
      <div v-if="isLoading" class="loading-mask">
        <div class="loading-content">
          <i class="el-icon-loading"></i>
          <p>正在加载，请稍候...</p>
        </div>
      </div>

      <SeeksRelationGraph ref="seeksRelationGraph" :options="graphOptions" :on-node-click="onNodeClick" @mouseout.native="hideNodeTips" :on-line-click="onLineClick" >
      </SeeksRelationGraph>
    </div>
    <!--节点详情悬浮框-->
    <div v-if="isShowNodeTipsPanel" :style="{left: nodeMenuPanelPosition.x + 'px', top: nodeMenuPanelPosition.y + 'px' }" style="z-index: 999;padding:10px;background-color: #ffffff;border:#eeeeee solid 1px;box-shadow: 0px 0px 8px #cccccc;position: absolute;">
      <div class="c-node-menu-item">名称：{{currentNode.text}}</div>
      <div class="c-node-menu-item">昵称:{{currentNode.data.nickname}}</div>
      <div class="c-node-menu-item">性别:{{currentNode.data.sexType}}</div>
      <div class="c-node-menu-item">年龄:{{currentNode.data.age}}</div>
      <div class="c-node-menu-item">家族:{{currentNode.data.clan}}</div>
      <div class="c-node-menu-item">首次出场回目:{{currentNode.data.debut}}</div>
      <div class="c-node-menu-item">住址:{{currentNode.data.address}}</div>
      <div class="c-node-menu-item">简介:{{currentNode.data.profile}}</div>
    </div>



  </div>
</template>

<script>
import SeeksRelationGraph from 'relation-graph'
import { queryHlmInfo, queryHlmRel } from '@/api/graph'
export default {
  name: 'SeeksRelationGraphDemo',
  components: { SeeksRelationGraph },
  data () {
    return {
      param: {
        queryName: '',
        pageNum: 1, // 初始页
        pageSize: 100 // 每页的数据
      },
      hlmInfoData: [],
      hlmRelData: [],
      hlmInfoTotal: 0,
      hlmRelTotal: 0,
      // -------------------------------初始化动作参数---------------------------------------------------
      baseHigh: 0,
      rootNode: {},
      showFilter: false, // 控制筛选悬浮窗显示隐藏
      // ----------------------------节点详情悬浮框------------------------------------------
      isShowNodeTipsPanel: false, // 默认隐藏
      nodeMenuPanelPosition: { x: 0, y: 0 }, // 初始坐标
      currentNode: {}, // 悬浮框内容对象
      // ----------------------------------------------------------------------------------

      g_loading: true,
      demoname: '---',
      checked_sex: '',
      checked_clan: '',
      rel_checkList: ['家族', '主仆', '亲戚', '朋友', '夫妻'],
      all_rel_type: ['家族', '主仆', '亲戚', '朋友', '夫妻'],
      graphOptions: {
        layoutName: 'tree',
        from: 'top',
        layoutClassName: 'seeks-layout-center',
        defaultNodeBorderWidth: 0,
        defaultNodeColor: 'rgba(238, 178, 94, 1)',
        allowSwitchLineShape: true,
        allowSwitchJunctionPoint: true,
        defaultJunctionPoint: 'tb', // 默认的连线与节点接触的方式（border:边缘/ltrb:上下左右/tb:上下/lr:左右）当布局为树状布局时应使用tb或者lr，这样才会好看
        defaultLineShape: 4, // 4：折线

        'layouts': [
          {
            'label': '自动',
            'layoutName': 'tree',
            'layoutClassName': 'seeks-layout-center',
            'min_per_width': 90, // 调解节点间最小宽度
            'max_per_width': 170, // 调解节点间最大宽度
            'min_per_height': 180, // 调解节点间最小高度
            'max_per_height': 190 // 调解节点间最大高度
            // 'levelDistance': 1100
          },
          {
            'label': '树状',
            'layoutName': 'tree',
            'layoutClassName': 'seeks-layout-center',
            'useLayoutStyleOptions': true,
            'from': 'top',
            'defaultNodeBorderWidth': 0,
            'defaultNodeColor': 'rgba(238, 178, 94, 1)',
            'allowSwitchLineShape': true,
            'allowSwitchJunctionPoint': true,
            'defaultJunctionPoint': 'lr', // 默认的连线与节点接触的方式（border:边缘/ltrb:上下左右/tb:上下/lr:左右）当布局为树状布局时应使用tb或者lr，这样才会好看
            'defaultLineShape': 4,

            'min_per_width': 140,
            'max_per_width': 200,
            'min_per_height': 200,
            'max_per_height': 500
          }
        ]

        // 这里可以参考"Graph 图谱"中的参数进行设置
      },
      isLoading: true,  // 添加加载状态
    }
  },
  created () {
  },
  mounted () {
    this.demoname = this.$route.params.demoname
    this.initInfo()
  },
  methods: {
    // 初始化人物数据
    initInfo () {
      var _this = this
      let param = {
        queryName: this.param.queryName
      }
      queryHlmInfo(param).then(res => {
        if (res.data.code == '100200') {
          this.hlmInfoData = res.data.object.list
          this.hlmInfoTotal = res.data.object.total
          this.initRel()
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // 初始化人物关系
    initRel () {
      var _this = this
      let param = {
        queryName: this.param.queryName
      }
      queryHlmRel(param).then(res => {
        if (res.data.code == '100200') {
          this.hlmRelData = res.data.object.list
          this.hlmRelTotal = res.data.object.total
          this.setGraphData()
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // -------------------------------------------------------------------------------------------
    // 给头像拼接相对路径转换方法
  getAvatarPath(user) {
    let sexF = user.sex;
    try {
      return require(`@/assets/image/hlm/${user.userName}.jpg`)
    } catch (e) {
      // 如果找不到对应的图片，返回默认头像
      if(sexF == 'M' ){
        return require('@/assets/image/hlm/男.jpg')
      }else {
        return require('@/assets/image/hlm/女.jpg')
      }
    }
  },
    setGraphData () {
      var newData = { rootId: 'J001', links: [], nodes: [] }
      // 人物数据处理
      for (var i = 0; i < this.hlmInfoData.length; i++) {
        if (this.hlmInfoData[i].sex == 'M') {
          var objM = {
            id: this.hlmInfoData[i].userId,
            text: this.hlmInfoData[i].userName,
            color: 'rgba(0, 206, 209, 1)',
            borderColor: '#6cc0ff',
            innerHTML: `<div class="c-my-node" style="background-image: url(${this.getAvatarPath(this.hlmInfoData[i])});border:#6cc0ff solid 3px;"><div class="c-node-name" style="color:#6cc0ff">${this.hlmInfoData[i].userName}</div></div>`,

            data: {
              isGoodMan: true,
              sexType: '男',
              sex: this.hlmInfoData[i].sex,
              nickname: this.hlmInfoData[i].nickname,
              age: this.hlmInfoData[i].age,
              clan: this.hlmInfoData[i].clan,
              debut: this.hlmInfoData[i].debut,
              address: this.hlmInfoData[i].address,
              profile: this.hlmInfoData[i].profile,
              userLevel: this.hlmInfoData[i].userLevel
            }
          }
          newData.nodes.push(objM)
        } else {
          var objF = {
            id: this.hlmInfoData[i].userId,
            text: this.hlmInfoData[i].userName,
            color: '#ec6941',
            borderColor: '#ff875e',
            innerHTML: `<div class="c-my-node" style="background-image: url(${this.getAvatarPath(this.hlmInfoData[i])});border:#ff875e solid 3px;"><div class="c-node-name" style="color:#ff875e">${this.hlmInfoData[i].userName}</div></div>`,
            data: {
              isGoodMan: true,
              sexType: '女',
              sex: this.hlmInfoData[i].sex,
              nickname: this.hlmInfoData[i].nickname,
              age: this.hlmInfoData[i].age,
              clan: this.hlmInfoData[i].clan,
              debut: this.hlmInfoData[i].debut,
              address: this.hlmInfoData[i].address,
              profile: this.hlmInfoData[i].profile,
              userLevel: this.hlmInfoData[i].userLevel
            }
          }
          newData.nodes.push(objF)
        }
      }
      // 人物关系数据处理
      for (var j = 0; j < this.hlmRelData.length; j++) {
        var objRel = {
          from: this.hlmRelData[j].fromId,
          to: this.hlmRelData[j].toId,
          text: this.hlmRelData[j].relName,
          color: '#d2c0a5',
          fontColor: '#d2c0a5',
          data: {
            relName: this.hlmRelData[j].relName,
            relType: this.hlmRelData[j].relType,
            relLevel: this.hlmRelData[j].relLevel,
            relStatus: this.hlmRelData[j].relStatus,
            relRemark: this.hlmRelData[j].relRemark
          }
        }
        newData.links.push(objRel)
      }

      this.$refs.seeksRelationGraph.setJsonData(newData, (seeksRGGraph) => {
        // 这些写上当图谱初始化完成后需要执行的代码
        this.initAction()
        this.baseAction()
        this.isLoading = false  // 图谱加载完成后隐藏遮罩
      })
    },
    // 鼠标点击节点触发
    onNodeClick (nodeObject, $event) {
      if (this.isShowNodeTipsPanel == true) {
        this.isShowNodeTipsPanel = false
      } else {
        this.currentNode = nodeObject
        var _base_position = this.$refs.myPage.getBoundingClientRect()
        this.isShowNodeTipsPanel = true
        this.nodeMenuPanelPosition.x = $event.clientX - _base_position.x + 280
        this.nodeMenuPanelPosition.y = $event.clientY - _base_position.y + 180
      }
    },
    // 鼠标点击关系触发
    onLineClick (lineObject, $event) {
    },
    // 鼠标悬浮节点触发
    showNodeTips (nodeObject, $event) {
      this.currentNode = nodeObject
      var _base_position = this.$refs.myPage.getBoundingClientRect()
      this.isShowNodeTipsPanel = true
      this.nodeMenuPanelPosition.x = $event.clientX - _base_position.x + 10
      this.nodeMenuPanelPosition.y = $event.clientY - _base_position.y + 10
    },
    // 鼠标悬浮节点离开隐藏
    hideNodeTips (nodeObject, $event) {
      this.isShowNodeTipsPanel = false
    },
    //筛选悬浮窗
    toggleFilter() {
      this.showFilter = !this.showFilter
    },
    showFilterPanel() {
      this.showFilter = true
    },

    // 初始化动作
    initAction () {
      /// /-------------------------计算基准节点高度间距（以红楼梦人物关系图谱-贾家间距为准）----------------------------------------
      var graph = this.$refs.seeksRelationGraph
      for (var i = 0; i < this.hlmInfoData.length; i++) {
        if (this.hlmInfoData[i].userName == '红楼梦人物关系图谱') {
          this.rootNode = graph.getNodeById(this.hlmInfoData[i].userId)
          var nodeROOT = graph.getNodeById(this.hlmInfoData[i].userId)
        }
        if (this.hlmInfoData[i].userName == '贾家') {
          var nodeJJ = graph.getNodeById(this.hlmInfoData[i].userId)
        }
      }
      this.baseHigh = nodeJJ.y - nodeROOT.y
    },

    // 等级转换Y轴间距
    userLevelTran (level) {
      if (level == 'A') {
        return this.baseHigh * 2
      }
      if (level == 'B') {
        return this.baseHigh * 3
      }
      if (level == 'C') {
        return this.baseHigh * 4
      }
      if (level == 'D') {
        return this.baseHigh * 5
      }
      if (level == 'E') {
        return this.baseHigh * 6
      }
      if (level == 'F') {
        return this.baseHigh * 7
      }
      if (level == 'G') {
        return this.baseHigh * 8
      }
      if (level == 'H') {
        return this.baseHigh * 9
      }
      if (level == 'I') {
        return this.baseHigh * 10
      }
    },
    // 基础动作
    baseAction () {
      var graph = this.$refs.seeksRelationGraph
      for (var k = 0; k < this.hlmInfoData.length; k++) {
      // --------------------------获取指定节点坐标----------------------------------------
        // 获取根节点坐标y轴
        // 循环所有非0的人物类型，按等级计算y轴
        if (this.hlmInfoData[k].userLevel != '0') {
          var s = this.userLevelTran(this.hlmInfoData[k].userLevel)
          graph.getNodeById(this.hlmInfoData[k].userId).y = this.rootNode.y + s
        }
      }

      for (var i = 0; i < this.hlmInfoData.length; i++) {
        // --------------------------夫妻头像自动靠近及夫妻线隐藏----------------------------------------
        if (this.hlmInfoData[i].sex == 'F') {
          var nodeQ = graph.getNodeById(this.hlmInfoData[i].userId)
          // 根据关系，找到丈夫节点
          var nodeFid = ''
          var linkLevel = ''
          for (var j = 0; j < this.hlmRelData.length; j++) {
            if (this.hlmRelData[j].toId == nodeQ.id && this.hlmRelData[j].relType == 'FQ') {
              nodeFid = this.hlmRelData[j].fromId
              linkLevel = this.hlmRelData[j].relLevel
              break
            }
          }
          // 根据丈夫坐标、结合妻子等级、设置妻子坐标
          if (nodeFid != '') {
            var nodeF = graph.getNodeById(nodeFid)
            // 直接改变位置
            nodeQ.x = nodeF.x + linkLevel * 70
            nodeQ.y = nodeF.y
          }
        }
        if (this.hlmInfoData[i].sex == 'M') {
          var nodeF = graph.getNodeById(this.hlmInfoData[i].userId)
          // 根据关系，找到丈夫节点
          var nodeQid = ''
          var linkLevel = ''
          for (var j = 0; j < this.hlmRelData.length; j++) {
            if (this.hlmRelData[j].toId == nodeF.id && this.hlmRelData[j].relType == 'FQ') {
              nodeQid = this.hlmRelData[j].fromId
              linkLevel = this.hlmRelData[j].relLevel
              break
            }
          }
          // 根据丈夫坐标、结合妻子等级、设置妻子坐标
          if (nodeQid != '') {
            var nodeQ = graph.getNodeById(nodeQid)
            // 直接改变位置
            nodeF.x = nodeQ.x + linkLevel * 70
            nodeF.y = nodeQ.y
          }
        }

      }
      // 隐藏夫妻关系线
      var allLines = this.$refs.seeksRelationGraph.getLines()
      allLines.forEach(thisLine => {
        thisLine.relations.forEach(thisLink => {
          if (thisLink.data['relType'] === 'FQ') {
            thisLink.isHide = true
          } else {
            thisLink.isHide = false
          }
        })
      })
    },

    doFilter () {
      var allNodes = this.$refs.seeksRelationGraph.getNodes()
      var allLines = this.$refs.seeksRelationGraph.getLines()
      allNodes.forEach(thisNode => {
        var _isHideThisLine = false
        if (this.checked_sex !== '') {
          if (thisNode.data['sexType'] !== this.checked_sex) {
            _isHideThisLine = true
          }
        }
        if (this.checked_clan !== '') {
          if (thisNode.data['clan'] !== this.checked_clan) {
            _isHideThisLine = true
          }
        }
        thisNode.opacity = _isHideThisLine ? 0.1 : 1
      })
      allLines.forEach(thisLine => {
        // 注意这里的line和json数据中link不一样，一条线（line）上可以附着多条关系(link),可以通过line.relations获取到这条线上所有的关系数据(link)
        var _isHideThisLine = true
        thisLine.relations.forEach(thisLink => {
          if (this.rel_checkList.indexOf(thisLink.data['type']) === -1) {
            //thisLink.isHide = true
          } else {
            _isHideThisLine = false
            //thisLink.isHide = false
          }
        })
        // thisNode.opacity = _isShowThisNode ? 1 : 0.1
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  .c-my-node{
    background-position: center center;
    background-size: 100%;
    border:#ff8c00 solid 2px;
    height:80px;/*圆高*/
    width:80px;/*圆宽*/
    border-radius: 40px;
  }
  .c-node-name{
    width:160px;/*姓名宽度*/
    margin-left:-40px;/*姓名左右偏移*/
    text-align:center;
    margin-top:75px; /*姓名上下偏移*/
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  /*人物简介悬浮框css*/
  .c-node-menu-item{
    text-align: left;
    line-height: 25px;
    padding-left: 10px;
    cursor: pointer;
    color: #888888;
    font-size: 12px;
    border-top:#efefef solid 1px;
  }
  .c-node-menu-item:hover{
    background-color: rgba(66,187,66,0.2);
  }

/*鼠标变小手*/
.c-my-node {
  cursor: pointer;
  position: relative;
}

/* 筛选悬浮窗 */
.filter-panel {
  position: fixed;
  right: 80px;
  bottom: top;
  background: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  z-index: 99999;
}

/* 悬浮球按钮 */
.float-btn {
  position: fixed;
  right: 25px;
  bottom: top;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #409EFF, #67C23A);  /* 渐变背景 */
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);  /* 柔和的阴影 */
  z-index: 100000;
  transition: all 0.3s ease;
}
.float-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);  /* 悬浮时阴影加深 */
}
.float-btn i {
  color: #fff;
  font-size: 20px;  /* 稍微调小图标 */
  transition: all 0.3s ease;
}
.float-btn .is-active {
  transform: rotate(180deg);
}

/* 加载遮罩层样式 */
.loading-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-content {
  text-align: center;
}

.loading-content i {
  font-size: 40px;
  color: #409EFF;
  margin-bottom: 10px;
}

.loading-content p {
  color: #606266;
  font-size: 16px;
  margin-top: 10px;
}
</style>
