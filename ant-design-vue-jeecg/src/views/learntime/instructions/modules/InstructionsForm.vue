<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row v-if='username !== "admin"'>
          <a-col :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <div style='width: auto' v-html="model.content"></div>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row v-else>
          <a-col :span="24">
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" placeholder="请输入标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
<!--            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">-->
<!--              <j-editor v-model="model.content" />-->
<!--            </a-form-model-item>-->
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <div style='width: auto' v-html="model.content"></div>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="可查看用户" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group  v-model="model.userIdentity"  @change="identityChange">
                <a-radio :value="1">普通用户</a-radio>
                <a-radio :value="2">上级</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import Vue from 'vue'
  import { USER_INFO } from '@/store/mutation-types'
  // import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'InstructionsForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/learntime/instructions/add",
          edit: "/learntime/instructions/edit",
          queryById: "/learntime/instructions/queryById"
        },
        username: ''
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
      this.initData();
    },
    methods: {
      initData() {
        this.getUsername();
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      getUsername(){
        this.username = Vue.ls.get(USER_INFO).username
      },
      identityChange(e){
        if(e.target.value===1){
          this.departIdShow=false;
        }else{
          this.departIdShow=true;
        }
      }
    }
  }
</script>
<style scoped>

</style>