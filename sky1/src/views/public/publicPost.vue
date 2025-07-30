<script setup>
import { ref ,reactive} from 'vue';
import {insertPost} from '@/api/user'
import { ElNotification } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
const loginUser = JSON.parse(localStorage.getItem('loginUser'));
const token=ref('')
const router = useRouter()
token.value=loginUser.token
const postForm = reactive({pictureUrl: '',title: '',content: '',fileType:''})
const emit = defineEmits(['publicSuccess'])

const publicPost= async()=>{
    const res=await insertPost(postForm)
    if(res.code){
        open1();
        cancel();
        router.push('/index')
    }else{
        ElMessage.error("发布失败，存在异常")
        cancel();
    }
}
const cancel=()=>{
    postForm.pictureUrl=''
    postForm.content=''
    postForm.title=''
    postForm.fileType=''
    emit('publicSuccess')
}
// 文件上传
// 图片上传成功后触发
const handleAvatarSuccess = (response) => {
  postForm.pictureUrl = response.data
    // postForm.pictureUrl = "http://syt87sfjl.hd-bkt.clouddn.com/"+response.data;
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png' &&rawFile.type!=='video/mp4' &&rawFile.type!=='video/quicktime') {
    ElMessage.error('只支持上传图片或者图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 100) {
    ElMessage.error('只能上传2GB以内')
    return false
  }
  postForm.fileType=rawFile.type;
  return true
}
const open1 = () => {
  ElNotification({
    title: 'Success',
    message: '发布成功',
    type: 'success',
  })
}
</script>

<template>
    <el-form :model="postForm" class="cco">
        <el-form-item class="uploadAvatar">
            <span>文件</span>
            <el-upload
                class="avatar-uploader"
                action="/api/upload"
                :headers="{'token': token}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
            >
            <img v-if="postForm.fileType?.startsWith('image/')" :src="postForm.pictureUrl" class="userFormPicture"/>
            <video v-if="postForm.fileType?.startsWith('video/')" :src="postForm.pictureUrl" class="userFormVideo" controls ></video>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
        </el-form-item>
    <el-form-item label="标题">
      <el-input type="text" class="input" v-model="postForm.title" placeholder="取什么标题呢？" />
    </el-form-item>
    <el-form-item label="内容">
      <el-input v-model="postForm.content" type="textarea" placeholder="想想有什么有趣的事情吧"/>
    </el-form-item>
    <el-form-item class="buten">
        <el-button @click="cancel" class="butenCancel">取消</el-button>
        <el-button type="primary" @click="publicPost" >发布</el-button>
    </el-form-item>
  </el-form>
</template>

<style>
.cco{
    padding: 0 30px;
}
.uploadAvatar{
    margin-bottom: 50px;
}
.buten{
    margin-left: 110px;
    margin-top: 30px;
}
.butenCancel{
    margin-right: 120px;
}
.userFormPicture {
    width: 200px;
    height: 200px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    margin-left: 30px;
}
.userFormVideo {
    width: 350px;
    height: 250px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    margin-left: 30px;
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  margin-left: 20px;
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  border-radius: 10px;
  /* 添加灰色的虚线边框 */
  border: 1px dashed var(--el-border-color);
}
</style>