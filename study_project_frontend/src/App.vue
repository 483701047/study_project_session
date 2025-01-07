<script setup>
import {Lock, User} from '@element-plus/icons-vue'
</script>

<template>
<div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
  <div style="flex:1">
    <el-image style="width: 100%;height: 100%" fit="cover"
        src="https://i0.hdslb.com/bfs/archive/bece658ecf536eaa4d5e5e71f7ef9168256e5a0c.jpg"/>
  </div>
  <div style="width: 400px;background-color: white">
    <div style="text-align: center;margin: 0 20px">
      <div style="text-align: center;margin-top: 150px">
        <div style="font-size: 25px;font-weight: bold">登录</div>
        <div style="font-size: 14px;color: gray">请输入用户名和密码</div>
      </div>
      <div style="margin-top: 50px">
        <el-input type="text" placeholder="用户名或邮箱" >
          <template #prefix>
            <el-icon ><User /></el-icon>
          </template>
        </el-input>
        <el-input type="password" style="margin-top: 10px" placeholder="密码" >
          <template #prefix>
            <el-icon ><lock /></el-icon>
          </template>
        </el-input>
      </div>

        <el-row style="margin-top: 5px">
          <el-col :span="12" style="text-align: left">
            <el-checkbox v-model="checkd1" label="记住我" size="large"/>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link>忘记密码？</el-link>
          </el-col>
        </el-row>

      <div style="margin-top: 40px">
        <el-button type="success" plain style="width: 270px">立即登录</el-button>
      </div>
      <el-divider>
        <span style="color: gray;font-size: 13px">没注册？</span>
      </el-divider>
      <div>
        <el-button type="warning" plain style="width: 270px">登录账号</el-button>
      </div>
    </div>

  </div>
</div>
</template>

<style scoped>

</style>
