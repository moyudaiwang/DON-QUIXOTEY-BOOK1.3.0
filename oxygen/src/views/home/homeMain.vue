<template>
  <div class="carousel-container">
    <!-- 加载提示 -->
    <div v-if="loading" class="loading-mask">
      <div class="loading-content">
        <i class="el-icon-loading"></i>
        <p>加载中...</p>
      </div>
    </div>

    <el-carousel :interval="6000" height="850px" @change="handleSlideChange">
      <el-carousel-item v-for="(item, index) in imagesBox" :key="item.id">
        <img
          :src="item.idView"
          :alt="'banner' + (index + 1)"
          class="carousel-image"
          @load="handleImageLoad(index)"
          loading="lazy">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
      loadedImages: 0,
      imagesBox: [
        {id: 0, idView: require('../../assets/image/home/banner1.jpg')},
        {id: 4, idView: require('../../assets/image/home/banner5.jpg')},
        {id: 5, idView: require('../../assets/image/home/banner6.jpg')}
      ]
    }
  },
  methods: {
    handleImageLoad(index) {
      this.loadedImages++
      if (this.loadedImages === this.imagesBox.length) {
        this.loading = false
      }
    },
    handleSlideChange(index) {
      // 预加载下一张图片
      if (index < this.imagesBox.length - 1) {
        const nextImage = new Image()
        nextImage.src = this.imagesBox[index + 1].idView
      }
    }
  }
}
</script>

<style>
.carousel-container {
  position: relative;
  width: 100%;
  height: 850px;
}

.loading-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 100;
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

.el-carousel__item {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 10px;
}

.item {
  margin-bottom: 10px;
}

.box-card {
  width: 575px;
}
</style>
