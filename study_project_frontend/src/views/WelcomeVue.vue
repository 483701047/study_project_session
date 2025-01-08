<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex:1">
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="https://i0.hdslb.com/bfs/archive/bece658ecf536eaa4d5e5e71f7ef9168256e5a0c.jpg"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold">欢迎来到学习平台</div>
      <div style="margin-top: 10px">来这里就开始学吧</div>
      <div style="margin-top: 5px">在这里可以学更多</div>
    </div>
    <div style="width: 400px;background-color: white;z-index: 1">
      <router-view v-slot="{Component}">
        <transition mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>

    </div>
  </div>
</template>

<style scoped>
.welcome-title{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>