<script setup>
import {get} from "@/net"
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {useStore} from "@/stores/index.js";
import {Document, Location, Setting, Menu as IconMenu, Search} from "@element-plus/icons-vue";

const store=useStore()

const logout=()=>{
  get('api/auth/logout',(message)=>{
    ElMessage.success(message)
    store.auth.user=null
    router.push('/')
  })
}
</script>

<template>
  <div style="height: 100vh;">
    <el-container style="height: 100%;">
      <el-aside style="border-right: 1px solid #a39f9f" width="250px">
        <div style="text-align: center;padding: 15px" >
          <el-image  src="https://element-plus.org/images/element-plus-logo.svg"
                     style="width: 150px;"/>
        </div>

        <el-menu
            style="border: none"
            default-active="2">
          <el-sub-menu index="1">
            <template #title>
              <el-icon><location /></el-icon>
              <span>Navigator One</span>
            </template>
            <el-menu-item-group title="Group One">
              <el-menu-item index="1-1">item one</el-menu-item>
              <el-menu-item index="1-2">item two</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="Group Two">
              <el-menu-item index="1-3">item three</el-menu-item>
            </el-menu-item-group>
            <el-sub-menu index="1-4">
              <template #title>item four</template>
              <el-menu-item index="1-4-1">item one</el-menu-item>
            </el-sub-menu>
          </el-sub-menu>
          <el-menu-item index="2">
            <el-icon><icon-menu /></el-icon>
            <span>Navigator Two</span>
          </el-menu-item>
          <el-menu-item index="3" disabled>
            <el-icon><document /></el-icon>
            <span>Navigator Three</span>
          </el-menu-item>
          <el-menu-item index="4">
            <el-icon><setting /></el-icon>
            <span>Navigator Four</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="border-bottom: 1px solid #a39f9f">
          <div style="display: flex;padding: 5px 0">
            <div style="flex: 1">
              <el-input
                  style="width: 400px;margin-top: 6px"
                  placeholder="搜索论坛内容...">
                <template #prefix>
                  <el-icon><Search/></el-icon>
                </template>
              </el-input>
            </div>
            <div style="display: flex">
              <div style="text-align: right;margin: 10px 10px 0 0;line-height: 15px">
                <div style="font-weight: bold">{{store.auth.user.username}}</div>
                <div style="font-size: 12px">{{store.auth.user.email}}</div>
              </div>
              <el-dropdown trigger="click">
                <el-avatar class="avatar" :size="45" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item>Action 1</el-dropdown-item>
                    <el-dropdown-item>Action 2</el-dropdown-item>
                    <el-dropdown-item>Action 3</el-dropdown-item>
                    <el-dropdown-item disabled>Action 4</el-dropdown-item>
                    <el-dropdown-item divided>Action 5</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        <el-main>Main</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.avatar:hover{
  cursor: pointer;
}
</style>