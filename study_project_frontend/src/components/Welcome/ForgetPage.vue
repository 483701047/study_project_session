<script setup>
import {reactive,ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const active=ref(0)
const formRef=ref()
const isEmailValid=ref(false)
const coldDown=ref(0)

const form=reactive({
  email:'',
  code:'',
  password:'',
  password_repeat:''
})

const onValidate=(prop,isValid)=>{
  if (prop==='email')
    isEmailValid.value=isValid
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

const rules={
  email: [
    { required: true, message: '请输入邮箱地址', trigger: ['blur','change'] },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur','change'] }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: ['blur','change'] },
    { min: 6, max: 6, message: '验证码长度为6位', trigger: ['blur','change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['blur','change'] },
    { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: ['blur','change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur','change'] }
  ],
}

const validateEmail=()=>{
  post('api/auth/valid_reset_email',{
    email:form.email
  },(message)=>{
    ElMessage.success(message)
    coldDown.value=60
    setInterval(()=>{
      coldDown.value--
    },1000)
  })
}

const startReset=()=>{
  formRef.value.validate((isValid)=>{
    if (isValid){
      post('api/auth/start_reset',{
        email:form.email,
        code:form.code,
      },()=>{
        active.value++
      })
    }else {
      ElMessage.warning('请填写邮件地址和验证码')
    }
  })
}

const doReset=()=>{
  formRef.value.validate((isValid)=>{
    if (isValid){
      post('api/auth/do_reset',{
        password: form.password
      },(message)=>{
        ElMessage.success(message)
        router.push('/')
      })
    }else {
      ElMessage.warning('请填写密码')
    }
  })
}
</script>

<template>
  <div>
    <div style="margin: 30px 20px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证邮件地址"/>
        <el-step title="重新设置密码"/>
      </el-steps>
    </div>
    <transition name="el-fade-in-linear" mode="out-in">
      <div style="text-align: center;margin: 0 20px;height: 100%" v-if="active===0">
        <div style="margin-top: 100px">
          <div style="font-size: 25px;font-weight: bold;">重置密码</div>
          <div style="font-size: 14px;color: gray">请输入注册的电子邮件地址</div>
        </div>
        <div style="margin-top: 50px">
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
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
        <div style="margin-top: 70px">
          <el-button @click="startReset()" type="danger" style="width: 270px">确定开始下一步</el-button>
        </div>
      </div>
    </transition>
    <transition name="el-fade-in-linear" mode="out-in">
      <div style="text-align: center;margin: 0 20px;height: 100%" v-if="active===1">
        <div style="margin-top: 80px">
          <div style="font-size: 25px;font-weight: bold;">重置密码</div>
          <div style="font-size: 14px;color: gray">请输入你的新密码，并确认新密码</div>
        </div>
        <div style="margin-top: 50px">
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
            <el-form-item prop="password">
              <el-input v-model="form.password" :maxlength="18" type="password" placeholder="新密码">
                <template #prefix>
                  <el-icon><Lock/></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password_repeat">
              <el-input v-model="form.password_repeat" :maxlength="18" type="password" placeholder="重复新密码">
                <template #prefix>
                  <el-icon><Lock/></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-top: 70px">
          <el-button @click="doReset()" type="danger" style="width: 270px">立即重置密码</el-button>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>

</style>