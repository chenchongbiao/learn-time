<template>
  <div>
    <index-chart v-if="userIdentity==1"></index-chart>
    <index-bdc v-if="indexStyle==1 && userIdentity==2"></index-bdc>
    <index-task v-if="indexStyle==2 && userIdentity==2"></index-task>
    <div style="width: 100%;text-align: right;margin-top: 20px" v-if="userIdentity==2">
      请选择首页样式：
      <a-radio-group v-model="indexStyle">
<!--        <a-radio :value="1" v-if="userIdentity==1">统计图表</a-radio>-->
        <a-radio :value="1">统计图表</a-radio>
        <a-radio :value="2">任务表格</a-radio>
      </a-radio-group>
    </div>
  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'
  import Vue from 'vue'
  import { USER_INFO } from "@/store/mutation-types"
  import store from '@/store/'
  export default {
    name: "Analysis",
    components: {
      IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1,
        userIdentity: 1
      }
    },
    created() {
      this.getIndex()
    },
    methods: {
      // 根据不同用户选择显示不同的首页
      getIndex(){
        this.userIdentity = store.getters.userIdentity
        console.log(Vue.ls)
        console.log("userinfo",Vue.ls.get(USER_INFO))
        console.log("indexStyle",this.indexStyle)
      }
    }
  }
</script>