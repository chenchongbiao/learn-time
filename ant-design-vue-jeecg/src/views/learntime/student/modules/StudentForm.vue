<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="登录账号(学生学号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
              <a-input v-model="model.username" placeholder="请输入登录账号(学生学号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="登录密码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="password">
              <a-input-password v-model="model.password" placeholder="请输入登录密码" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="学生姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realname">
              <a-input v-model="model.realname" placeholder="请输入学生姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="学院" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="college">
              <j-dict-select-tag type="list" v-model="model.college" dictCode="college,name,id" placeholder="请选择学院" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="专业" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="major">
              <j-dict-select-tag type="list" v-model="model.major" dictCode="major,name,id" placeholder="请选择专业" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="年级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="grade">
              <j-dict-select-tag type="list" v-model="model.grade" dictCode="grade,name,name" placeholder="请选择年级" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="班级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clbum">
              <a-input-number v-model="model.clbum" placeholder="请输入班级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'StudentForm',
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
          add: "/student/student/add",
          edit: "/student/student/edit",
          queryById: "/student/student/queryById"
        }
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
    },
    methods: {
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
    }
  }
</script>