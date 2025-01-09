<script setup>
import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net";

const form = reactive({
  username:'',
  password:'',
  password_repeat:'',
  email:'',
  code:''
})

const validateUsername=( rule,value,callback)=>{
  if(value===''){
    callback(new Error('请输入用户名'))
  }else if(!/^[\u4e00-\u9fa5a-zA-Z0-9]+$/.test(value)){
    callback(new Error('用户名不包含特殊字符，只能是中文/英文'))
  }else {
    callback()
  }
}

const validatePassword=( rule,value,callback)=>{
  if(value===''){
    callback(new Error('请再次输入相同的密码'))
  }else if(value !==form.password){
    callback(new Error('两次输入的密码不一致'))
  }else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur','change'] },
    { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: ['blur','change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['blur','change'] },
    { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: ['blur','change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur','change'] }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: ['blur','change'] },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur','change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: ['blur','change'] },
    { min: 6, max: 6, message: '验证码长度为6位', trigger: ['blur','change'] }
  ]
}

const formRef=ref()
const isEmailValid=ref(false)
const coldDown=ref(0)

const onValidate=(prop,isValid)=>{
  if (prop==='email')
    isEmailValid.value=isValid
}

const register=()=>{
  formRef.value.validate((isValid)=>{
    if (isValid){
      post('api/auth/register',{
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      },(message)=>{
        ElMessage.success(message)
        router.push('/')
      })
    }else {
      ElMessage.warning('请完整填完相关信息')
    }
  })
}

const validateEmail=()=>{
  post('api/auth/valid_register_email',{
    email:form.email
  },(message)=>{
    ElMessage.success(message)
    coldDown.value=60
    setInterval(()=>{
      coldDown.value--
    },1000)
  })
}
</script>

<template>
<div style="text-align: center;margin:0 20px">
  <div style="margin-top: 100px">
    <div style="font-size: 25px;font-weight: bold">注册新用户</div>
    <div style="font-size: 14px;color: gray">欢迎使用学习平台，在下方填写信息</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
      <el-form-item prop="username">
        <el-input v-model="form.username" :maxlength="8" type="text" placeholder="用户名">
          <template #prefix>
            <el-icon><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" :maxlength="18" type="password" placeholder="密码">
          <template #prefix>
            <el-icon><Lock/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password_repeat">
        <el-input v-model="form.password_repeat" :maxlength="18" type="password" placeholder="重复密码">
          <template #prefix>
            <el-icon><Lock/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input v-model="form.email" type="text" placeholder="电子邮件地址">
          <template #prefix>
            <el-icon><Message/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row :gutter="10">
          <el-col :span="17" >
            <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
              <template #prefix>
                <el-icon><EditPen/></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="5">
            <el-button @click="validateEmail" type="success" :disabled="!isEmailValid || coldDown>0">
              {{ coldDown>0?'冷却中...'+coldDown+'秒':'获取验证码' }}
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
  <div style="margin-top: 80px">
    <el-button @click="register" style="width: 270px" type="warning">立即注册</el-button>
  </div>
  <div style="font-size: 14px;line-height: 15px;margin-top: 20px">
    <span style="color: gray">已经注册？</span>
    <el-link @click="router.push('/')" type="primary" style="translate: 0 -1px">立即登录</el-link>
  </div>
</div>
</template>

<style scoped>

</style>