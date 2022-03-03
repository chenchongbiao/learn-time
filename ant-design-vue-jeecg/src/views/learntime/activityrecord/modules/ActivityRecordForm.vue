<template>
  <a-spin :spinning='confirmLoading'>
    <j-form-container :disabled='formDisabled'>
      <a-form-model ref='form' :model='model' :rules='validatorRules' slot='detail'>
        <a-row>
          <a-col :span='24'>
            <a-form-model-item label='活动名称' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='activityName'>
              <a-input v-model='model.activityName' placeholder='请输入活动名称'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='主办方' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='sponsor'>
              <a-input v-model='model.sponsor' placeholder='请选择主办方'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='学号' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='uid'>
              <a-input v-model='model.uid' placeholder='请输入学号'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='姓名' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='name'>
              <a-input v-model='model.name' placeholder='请输入姓名'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='所在学院' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='academy'>
              <j-dict-select-tag type='list' v-model='model.academy' dictCode='sys_depart,depart_name,id'
                                 placeholder='请选择所在学院' />
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='班级' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='clazz'>
              <a-input v-model='model.clazz' placeholder='请输入班级'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='参加类型' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='joinType'>
              <j-dict-select-tag type='list' v-model='model.joinType' dictCode='join_type' placeholder='请选择参加类型' />
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='获奖情况' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='award'>
              <a-input v-model='model.award' placeholder='请输入获奖情况'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='学时类型' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='creditType'>
              <j-dict-select-tag type='list' v-model='model.creditType' dictCode='learn_time_type'
                                 placeholder='请选择学时类型' />
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='学时数量' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='credit'>
              <a-input-number v-model='model.credit' placeholder='请输入学时数量' style='width: 100%' />
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='填报人及联系方式' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='contact'>
              <a-input v-model='model.contact' placeholder='请输入填报人及联系方式'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='审核人' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='toName'>
              <a-input v-model='model.toName' placeholder='请输入审核人'></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span='24'>
            <a-form-model-item label='学年' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='year'>
              <j-dict-select-tag type='list' v-model='model.year' dictCode='year' placeholder='请选择学年' />
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
  name: 'ActivityRecordForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      validatorRules: {
        activityName: [{ required: true, message: '请输入姓名!' }],
        sponsor: [{ required: true, message: '请输入主办方' }]
      },
      url: {
        add: '/activityrecord/activityRecord/add',
        edit: '/activityrecord/activityRecord/edit',
        queryById: '/activityrecord/activityRecord/queryById'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    }
  }
}
</script>