<template>
  <div class='account-settings-info-view'>
    <a-row :gutter='16'>
      <a-col :md='24' :lg='16'>

<!--        <a-card hoverable style='width: 300px'>-->
<!--          <a-card-meta title='学号' :description="uid"></a-card-meta>-->
<!--          <a-card-meta title='姓名' description='测试'></a-card-meta>-->
<!--          <a-card-meta title='年级' description='测试'></a-card-meta>-->
<!--          <a-card-meta title='班级' description='测试'></a-card-meta>-->
<!--          <a-card-meta title='学院' description='测试'></a-card-meta>-->
<!--        </a-card>-->
        <a-card title="">
          <a-card-grid style="width:20%;text-align:center">
            学号
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center" :hoverable="false">
            姓名
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            年级
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            班级
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            学院
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            {{ uid }}
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            {{ realname }}
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            {{ grade }}
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            {{ clazz }}
          </a-card-grid>
          <a-card-grid style="width:20%;text-align:center">
            {{ academy }}
          </a-card-grid>
        </a-card>
      </a-col>

      <!--      <a-col :md='24' :lg='8' :style="{ minHeight: '180px' }">-->
      <!--                <div class="ant-upload-preview" @click="$refs.modal.edit(1)" >-->
      <!--                  <a-icon type="cloud-upload-o" class="upload-icon"/>-->
      <!--                  <div class="mask">-->
      <!--                    <a-icon type="plus" />-->
      <!--                  </div>-->
      <!--                  <img :src="option.img"/>-->
      <!--                </div>-->
      <!--      </a-col>-->

    </a-row>

    <avatar-modal ref='modal'>

    </avatar-modal>
  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import store from '@/store/'

export default {
  components: {
    AvatarModal
  },
  data() {
    return {
      // cropper
      preview: {},
      option: {
        img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },
      uid: '',
      grade: '',
      clazz: '',
      academy: '',
      realname: ''
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.uid = store.getters.userInfo.username
      this.grade = store.getters.userInfo.grade
      this.clazz = store.getters.userInfo.clazz
      this.academy = store.getters.userInfo.orgCodeTxt
      this.realname = store.getters.userInfo.realname
      console.log(store.getters.userInfo)
    }
  }
}
</script>

<style lang='less' scoped>

.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }

  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img, .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>