<template>
  <div class="map-container">

    <!-- 筛选悬浮窗 -->
    <div v-show="showFilter" class="filter-panel">
      <el-row :gutter="0">
        <el-col :span="24">
          <el-input placeholder="请输入地址" v-model="input3" class="input-with-select" style="margin-bottom: 15px;">
            <el-button slot="append" icon="el-icon-search" @click="queryMapPoints"></el-button>
          </el-input>
        </el-col>
        <el-col :span="24">
          <el-cascader placeholder="请选择书目" :options="options" v-model="optValue" @change="queryMapPoints" clearable></el-cascader>
        </el-col>
      </el-row>
    </div>
    <!-- 悬浮球按钮 -->
    <div class="float-btn" @click="clickFilterPanel" @mouseenter="showFilterPanel">
      <i :class="['el-icon-s-operation', {'is-active': showFilter}]"></i>
    </div>

    <!--
  scroll-wheel-zoom是是否可以缩放
  @ready是图加载完后触发事件
  center是位置定位
  zoom是缩放大小限制
  inertial-dragging是允许惯性拖拽
 -->
    <baidu-map ref="map" class="bm-view" :zoom="11" :center="center" inertial-dragging @ready="mapReady" @click="clickEvent" :scroll-wheel-zoom="true">
      <!-- 比例尺   anchor="BMAP_ANCHOR_BOTTOM_RIGHT"代表放在右下角 -->
      <bm-scale anchor="BMAP_ANCHOR_BOTTOM_RIGHT"></bm-scale>
      <!-- 定位控件   anchor="BMAP_ANCHOR_BOTTOM_RIGHT"代表放在右下角 -->
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <!-- 地区检索  keyword：关键字搜索   @searchcomplete：检索完成后的回调函数   auto-viewport：检索结束后是否自动调整地图事业  -->
      <bm-local-search :keyword="keyword" @searchcomplete="search" :auto-viewport="true" class="search"></bm-local-search>
      <!-- 缩放控件   anchor代表控件停靠位置   anchor="BMAP_ANCHOR_TOP_RIGHT"代表放在右上角-->
      <bm-navigation anchor="BMAP_ANCHOR_TOP_LEFT"></bm-navigation>

      <!-- 标记点渲染 -->
      <template>
        <bm-marker v-for="(item, index) in markers"
                   :key="item.id || index"
                   :position="{lng: item.lng, lat: item.lat}"
                   :title="item.name"
                   @click="markerCli(item)">
        </bm-marker>
      </template>

      <!--信息窗体-->
      <bm-info-window :position="infoPoint" :show="infoWindow.show" @close="infoWindowClose" @open="infoWindowOpen">
        <el-descriptions :title="pointInfo.landmark" :column="3" border>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-reading"></i> 书名 </template> {{pointInfo.bookName}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-user"></i> 人物 </template> {{pointInfo.hero}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-files"></i> 相关图书 </template> {{pointInfo.relBook}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-sunrise"></i> 年代 </template> {{pointInfo.times}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-s-custom"></i> 陪访人 </template> {{pointInfo.companion}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-magic-stick"></i> 轶事 </template> {{pointInfo.anecdote}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-s-ticket"></i> 门票 </template> {{pointInfo.tickets}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-date"></i> 寻访时间 </template> {{pointInfo.searchTime}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-location-outline"></i> 地址 </template> {{pointInfo.address}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label"> <i class="el-icon-notebook-2"></i> 备注 </template> {{pointInfo.remark}}
          </el-descriptions-item>
        </el-descriptions>
      </bm-info-window>
      <!--海量点-->
      <!--
              <bm-point-collection :points="points" shape="BMAP_POINT_SHAPE_STAR" color="red" size="BMAP_POINT_SIZE_SMALL" @click="clickPoint"></bm-point-collection>
      -->
      <!--全景地图控件-->
      <!--<bm-panorama></bm-panorama>-->


    </baidu-map>
  </div>
</template>

<script>
//引入组件
import {
  BaiduMap,
  BmControl,
  BmView,
  BmAutoComplete,
  BmLocalSearch,
  BmMarker,
  BmGeolocation
} from "vue-baidu-map";
import { initMapMenu, queryMapPoints } from '@/api/map'
import { queryByModule } from '@/api/treeRel'
export default {
  data() {
    return {
      center: {lng: 116.404, lat: 39.915},
      keyword: "",
      input3: "",
      startPoint: { lng: 116, lat: 40},
      points: [],
      markers: [], // 当前显示的标记点数组
      markerMap: new Map(), // 用于存储标记点ID和数据的映射关系
      infoWindow: {
        show: false,
        currentMarkerId: null // 当前打开的标记点ID
      },
      infoPoint: {lng: 116, lat: 40},
      pointInfo: {},
      options: [],
      optValue: [],
      showFilter: false,
      currentMarker: null, // 当前选中的标记点
    };
  },
  //需要引入的组件
  components: {
    BaiduMap,
    BmControl,
    BmView,
    BmAutoComplete,
    BmLocalSearch,
    BmMarker,
    BmGeolocation
  },
  mounted () {
    // this.addPoints();
    //this.initMarkers();
    this.queryByModule();
    this.queryMapPoints('');
  },
  methods: {
    //根据模块查询级联下拉框
    queryByModule(){
      let param = {
        module: 'WLXF_MAP'
      }
      queryByModule(param).then(res => {
        this.options = res.data.object
      })
    },
    // 初始化标记点数据
    initializeMarkerData(list) {
      this.markers = [];
      this.markerMap.clear();
      this.pointInfo = {};
      this.infoWindow.show = false;
      this.currentMarker = null;

      list.forEach((item, index) => {
        if (item.x && item.y) {
          const markerId = item.id || `marker-${index}`;
          const markerData = {
            id: markerId,
            lng: item.x,
            lat: item.y,
            landmark: item.landmark,
            bookName: item.bookName,
            hero: item.hero,
            relBook: item.relBook,
            times: item.times,
            companion: item.companion,
            anecdote: item.anecdote,
            tickets: item.tickets,
            searchTime: item.searchTime,
            address: item.address,
            remark: item.remark
          };

          this.markers.push(markerData);
          this.markerMap.set(markerId, markerData);
        }
      });
    },

    // 查询地图坐标点
    queryMapPoints(value) {
      let idVal = '';
      if (value && Array.isArray(value) && value.length > 0) {
        idVal = value[value.length - 1];
      }

      const param = {
        module: 'WLXF_MAP',
        id: idVal
      };

      queryMapPoints(param).then(res => {
        if (res.data.code === '200') {
          this.initializeMarkerData(res.data.object);
        } else {
          this.initializeMarkerData([]);
          this.$message({message: res.data.msg, type: 'error'});
        }
      }).catch(error => {
        console.error('加载地图点位失败:', error);
        this.initializeMarkerData([]);
        this.$message({message: '加载地图点位失败', type: 'error'});
      });
    },

    // 处理标记点点击
    markerCli(marker) {
      if (!marker || !marker.id) {
        console.error('无效的标记点数据:', marker);
        return;
      }

      const markerData = this.markerMap.get(marker.id);
      if (!markerData) {
        console.error('未找到对应的标记点数据:', marker.id);
        return;
      }

      // 如果点击的是同一个标记点，且信息窗口已经显示，则关闭它
      if (this.infoWindow.currentMarkerId === marker.id && this.infoWindow.show) {
        this.infoWindow.show = false;
        this.infoWindow.currentMarkerId = null;
        this.currentMarker = null;
        return;
      }

      // 更新当前选中的标记点
      this.currentMarker = marker;

      // 设置信息窗口位置
      this.infoPoint = {
        lng: markerData.lng,
        lat: markerData.lat
      };

      // 更新点位信息
      this.pointInfo = { ...markerData };

      // 记录当前打开的标记点ID并显示信息窗口
      this.infoWindow.currentMarkerId = marker.id;
      this.infoWindow.show = true;
    },

    // 信息窗口关闭处理
    infoWindowClose() {
      this.infoWindow.show = false;
      this.infoWindow.currentMarkerId = null;
      this.currentMarker = null;
    },

    // 信息窗口打开处理
    infoWindowOpen() {
      // 不需要额外的判断，因为我们已经在 markerCli 中处理了显示逻辑
    },

    clickEvent(){
    },
    search(){
    },
    //地图加载后的回调
    mapReady({ BMap, map }) {
      //保存this指向，因为在百度的回调中this不指向vue
      const _this = this;
      // 获取自动定位方法
      var geolocation = new BMap.Geolocation();
      // 获取自动定位获取的坐标信息
      geolocation.getCurrentPosition(
        function (r) {
          //可以console.log看一下这个r，他里面包含了检索到的位置信息。下面就把两个维度信息赋值给center来定位
          //_this.center = {
          //  lng: r.point.lng,
          //  lat: r.point.lat,
          //};
        },
        //启用高精度
        { enableHighAccuracy: true }
      );
    },
    clickPoint (e) {
      alert(`单击点的坐标为：${e.point.lng}, ${e.point.lat}, ${e.point.tx}`);
    },
    addPoints () {
      const points = [];
      for (var i = 0; i < 1000; i++) {
        const position = {lng: Math.random() * 40 + 85, lat: Math.random() * 30 + 21, tx:Math.random() * 2}
        points.push(position)
      }
      this.points = points
    },
    //随机初始化点位
    initMarkers(){
      const markers = []
      for (let i = 0; i < 10; i++) {
        const position = {lng: Math.random() * 40 + 85, lat: Math.random() * 30 + 21}
        this.markers.push(position)
      }
    },
    //筛选悬浮球弹窗
    clickFilterPanel() {
      this.showFilter = !this.showFilter
    },
    showFilterPanel() {
      this.showFilter = true
    },


  },
  // 添加watch来监听数据变化
  watch: {
    markers: {
      handler(newMarkers) {
        console.log('标记点数据已更新:', newMarkers.length);
      },
      deep: true
    },

    'infoWindow.show': function(newVal) {
      console.log('信息窗口状态变化:', newVal);
    }
  }
};
</script>

<style scoped>
/* 地图容器 */
.map-container {
  width: 100%;
  height: calc(100vh - 100px);  /* 减小高度 */
  position: relative;
  margin: 0;
  padding: 0;
  overflow: hidden;  /* 防止出现滚动条 */
}
/* 地图视图 */
.bm-view {
  width: 100%;
  height: calc(100vh - 100px);  /* 与容器保持一致 */
  margin: 0;
  padding: 0;
}
/* 确保页面本身也不会出现滚动条 */
body {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.input-with-select {
  width: 100%;
}
.search {
  height: 300px;
  overflow: auto;
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
  z-index: 1;
  width: 250px;
}
/* 悬浮球按钮 */
.float-btn {
  position: fixed;
  right: 20px;
  bottom: top;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  z-index: 100000;
  transition: all 0.3s ease;
}
.float-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}
.float-btn i {
  color: #fff;
  font-size: 20px;
  transition: all 0.3s ease;
}
.float-btn .is-active {
  transform: rotate(180deg);
}

</style>
