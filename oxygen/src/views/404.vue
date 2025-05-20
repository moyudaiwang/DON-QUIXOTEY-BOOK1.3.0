<template>
  <div class="error-container">
    <div class="clouds"></div>
    <div class="central-body">
      <div class="paper-plane">
        <div class="plane-body"></div>
        <div class="wing-right"></div>
        <div class="wing-left"></div>
        <div class="tail"></div>
      </div>
      <h1>4<span class="zero">0</span>4</h1>
      <h2>可恶，页面找不到啦...</h2>
      <p class="sub-text">别催了别催了，已经在开发了，呜呜呜T_T...</p>
      <router-link to="/home/homeMain" class="btn-go-home">返回首页</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: '404',
  mounted() {
    this.createClouds();
  },
  methods: {
    createClouds() {
      const cloudsContainer = document.querySelector('.clouds');
      for (let i = 0; i < 8; i++) {
        const cloud = document.createElement('div');
        cloud.className = 'cloud';
        cloud.style.left = `${Math.random() * 100}%`;
        cloud.style.top = `${Math.random() * 60}%`;
        cloud.style.animationDelay = `${Math.random() * 5}s`;
        cloudsContainer.appendChild(cloud);
      }
    }
  }
}
</script>

<style scoped>
:root {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.error-container {
  height: 100%;
  width: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  overflow: hidden;
  position: relative;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.clouds {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  pointer-events: none;
}

.cloud {
  position: absolute;
  width: 100px;
  height: 30px;
  background: #fff;
  border-radius: 15px;
  animation: float 15s linear infinite;
  opacity: 0.8;
}

.cloud::before,
.cloud::after {
  content: '';
  position: absolute;
  background: #fff;
  border-radius: 50%;
}

.cloud::before {
  width: 40px;
  height: 40px;
  top: -20px;
  left: 15px;
}

.cloud::after {
  width: 50px;
  height: 50px;
  top: -25px;
  left: 35px;
}

.central-body {
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  width: 100%;
  max-width: 600px;
}

.paper-plane {
  width: 100px;
  height: 70px;
  position: relative;
  margin: 0 auto 30px;
  animation: fly 6s ease-in-out infinite;
}

.plane-body {
  width: 100px;
  height: 25px;
  background: #5c86ff;
  position: absolute;
  bottom: 20px;
  left: 10px;
  transform: skew(-20deg);
}

.wing-right {
  width: 40px;
  height: 40px;
  background: #7c9fff;
  position: absolute;
  top: 0;
  right: 20px;
  transform: skew(30deg);
}

.wing-left {
  width: 60px;
  height: 20px;
  background: #4070ff;
  position: absolute;
  top: 20px;
  left: 20px;
  transform: skew(-15deg);
}

.tail {
  width: 20px;
  height: 20px;
  background: #7c9fff;
  position: absolute;
  bottom: 20px;
  left: 0;
  transform: skew(-40deg);
}

h1 {
  font-size: clamp(80px, 15vw, 120px);
  color: #2c3e50;
  margin: 0;
  font-weight: 700;
  letter-spacing: 8px;
  line-height: 1.2;
}

.zero {
  display: inline-block;
  animation: bounce 2s ease-in-out infinite;
  color: #5c86ff;
}

h2 {
  color: #34495e;
  font-size: clamp(18px, 4vw, 24px);
  margin: 10px 0;
  font-weight: 500;
}

.sub-text {
  color: #7f8c8d;
  font-size: clamp(14px, 3vw, 16px);
  margin: 10px 0 15px;
}

.btn-go-home {
  display: inline-block;
  padding: 12px 30px;
  background: #5c86ff;
  color: #fff;
  text-decoration: none;
  border-radius: 25px;
  transition: all 0.3s;
  font-weight: 500;
  box-shadow: 0 4px 15px rgba(92, 134, 255, 0.3);
}

.btn-go-home:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(92, 134, 255, 0.4);
}

@keyframes float {
  from { transform: translateX(100%); }
  to { transform: translateX(-100%); }
}

@keyframes fly {
  0%, 100% { transform: translateY(0) rotate(-5deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
</style>
