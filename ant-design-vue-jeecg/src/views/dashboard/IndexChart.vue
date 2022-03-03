<template>
  <div class='page-header-index-wide'>
    <a-table :columns='creditColumns' :data-source='creditData' size='default'>
      <!--      <a slot="name" slot-scope="text">{{ text }}</a>-->
      <!--      <span slot="customTitle"><a-icon type="smile-o" /> Name</span>-->

    </a-table>

    <!-- 查询区域 -->
    <div class='table-page-search-wrapper'>
      <a-form layout='inline' @keyup.enter.native='searchQuery'>
        <a-row :gutter='24'>
          <a-col :md='6' :sm='8'>
            <a-form-item label='活动名称'>
              <j-input placeholder='输入活动名称模糊查询' v-model='queryParam.activityName'></j-input>
            </a-form-item>
          </a-col>

          <a-col :md='6' :sm='8'>
            <a-form-item label='学年'>
              <a-select v-model='queryParam.year' placeholder='请选择'>
                <a-select-option value=''>请选择</a-select-option>
                <a-select-option v-for='year in yearList' :key='year.id' :value='year.itemValue'>
                  {{ year.itemText }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md='6' :sm='8'>
            <a-form-item label='学时类型'>
              <a-select v-model='queryParam.creditType' placeholder='请选择学时类型'>
                <a-select-option value=''>请选择</a-select-option>
                <a-select-option value='0'>思想品德素质</a-select-option>
                <a-select-option value='1'>法律素养</a-select-option>
                <a-select-option value='2'>身心素质</a-select-option>
                <a-select-option value='3'>创新创业素质</a-select-option>
                <a-select-option value='4'>文体素质</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!--          <a-col :md="6" :sm="8">-->
          <!--            <a-form-item label="性别">-->
          <!--              <a-select v-model="queryParam.sex" placeholder="请选择性别">-->
          <!--                <a-select-option value="">请选择</a-select-option>-->
          <!--                <a-select-option value="1">男</a-select-option>-->
          <!--                <a-select-option value="2">女</a-select-option>-->
          <!--              </a-select>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :md='6' :sm='8'>
            <span style='float: left;overflow: hidden;' class='table-page-search-submitButtons'>
              <a-button type='primary' @click='searchQuery' icon='search'>查询</a-button>
              <a-button type='primary' @click='searchReset' icon='reload' style='margin-left: 8px'>重置</a-button>
<!--              <a-button type='primary' icon='download' style='margin-left: 8px'-->
<!--                        @click="handleExportXls('广州商学院“4+X”活动学时认定登记表')">导出</a-button>-->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <!--      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
      <!--        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项-->
      <!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
      <!--      </div>-->
      <!-- 操作按钮区域 -->

      <a-table
        ref='table'
        size='middle'
        bordered
        rowKey='id'
        :columns='columns'
        :dataSource='dataSource'
        :pagination='ipagination'
        :loading='activityLoading'
        :rowSelection='{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}'
        class='j-table-force-nowrap'
        @change='handleTableChange'>
      </a-table>
    </div>



    <a-card :loading='loading' :bordered='false' :body-style="{padding: '0'}">
      <div class='salesCard'>
        <a-tabs default-active-key='1' size='large' :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
<!--          <div class='extra-wrapper' slot='tabBarExtraContent'>-->
<!--            <div class='extra-item'>-->
<!--              <a>今日</a>-->
<!--              <a>本周</a>-->
<!--              <a>本月</a>-->
<!--              <a>本年</a>-->
<!--            </div>-->
<!--            <a-range-picker :style="{width: '256px'}" />-->
<!--          </div>-->
          <a-tab-pane loading='true' tab='学时统计' key='1'>
            <a-row>
              <a-col :xl='16' :lg='12' :md='12' :sm='24' :xs='24'>
                <bar title='学时数量排行' :dataSource='barData' />
              </a-col>
<!--              <a-col :xl='8' :lg='12' :md='12' :sm='24' :xs='24'>-->
<!--                <rank-list title='学时数量排行榜' :list='rankList' />-->
<!--              </a-col>-->
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <!--    <a-row>-->
    <!--      <a-col :span="24">-->
    <!--        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计" :style="{ marginTop: '24px' }">-->
    <!--          <a-row>-->
    <!--            <a-col :span="6">-->
    <!--              <head-info title="今日IP" :content="loginfo.todayIp"></head-info>-->
    <!--            </a-col>-->
    <!--            <a-col :span="2">-->
    <!--              <a-spin class='circle-cust'>-->
    <!--                <a-icon slot="indicator" type="environment" style="font-size: 24px"  />-->
    <!--              </a-spin>-->
    <!--            </a-col>-->
    <!--            <a-col :span="6">-->
    <!--              <head-info title="今日访问" :content="loginfo.todayVisitCount"></head-info>-->
    <!--            </a-col>-->
    <!--            <a-col :span="2">-->
    <!--              <a-spin class='circle-cust'>-->
    <!--                <a-icon slot="indicator" type="team" style="font-size: 24px"  />-->
    <!--              </a-spin>-->
    <!--            </a-col>-->
    <!--            <a-col :span="6">-->
    <!--              <head-info title="总访问量" :content="loginfo.totalVisitCount"></head-info>-->
    <!--            </a-col>-->
    <!--            <a-col :span="2">-->
    <!--              <a-spin class='circle-cust'>-->
    <!--                <a-icon slot="indicator" type="rise" style="font-size: 24px"  />-->
    <!--              </a-spin>-->
    <!--            </a-col>-->
    <!--          </a-row>-->
    <!--          <line-chart-multid :fields="visitFields" :dataSource="visitInfo"></line-chart-multid>-->
    <!--        </a-card>-->
    <!--      </a-col>-->
    <!--    </a-row>-->
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import RankList from '@/components/chart/RankList'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'
import Trend from '@/components/Trend'
// import { getLoginfo, getVisitInfo } from '@/api/api'
import store from '@/store/'
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ActivityRecordModal from '../learntime/activityrecord/modules/ActivityRecordModal'
import { getAction } from '@api/manage'
// import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
// import Vue from 'vue'
// import { USER_INFO } from '@/store/mutation-types'


// const rankList = []
// for (let i = 0; i < 7; i++) {
//   rankList.push({
//     name: '吴同学 ' + (i + 1),
//     total: 123.56 - i * 10
//   })
// }
// 文体素质学时
// const barData = [
//   {x: '法律素养',y:creditData[0].law},
//   {x: '身心素质',y:creditData[0].bodyMind},
//   {x: '创新创业素质',y:creditData[0].innovation},
//   {x: '思想品德素质',y:creditData[0].thought},
//   {x: '文体素质',y:creditData[0].cultureSports}
// ]
// for (let i = 0; i < 12; i += 1) {
//   barData.push({
//     x: `吴同学${i + 1}`,
//     y: Math.floor(Math.random() * 1000) + 200
//   })
// }
// 首页学时的列数据
// const creditColumns = [
//   // {
//   //   dataIndex: 'name',
//   //   key: 'name',
//   //   slots: { title: 'customTitle' },
//   //   scopedSlots: { customRender: 'name' },
//   // },
//   {
//     title: '总学时',
//     dataIndex: 'total',
//     key: 'total'
//   },
//   {
//     title: '法律素养',
//     dataIndex: 'law',
//     key: 'law'
//   },
//   {
//     title: '身心素质',
//     dataIndex: 'bodyMind',
//     key: 'bodyMind'
//   },
//   {
//     title: '创新创业素质',
//     key: 'innovation',
//     dataIndex: 'innovation'
//   },
//   {
//     title: '思想品德素质',
//     key: 'thought',
//     dataIndex: 'thought'
//   },
//   {
//     title: '文体素质',
//     key: 'cultureSports',
//     dataIndex: 'cultureSports'
//   }
// ]


export default {
  name: 'IndexChart',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo,
    ActivityRecordModal
  },
  data() {
    return {
      loading: true,
      activityLoading: true,
      center: null,
      rankList: [],
      barData: [],
      loginfo: {},
      visitInfo: [],
      indicator: <a-icon type='loading' style='font-size: 24px' spin />,
      creditData: [],
      creditColumns: [
        {
          title: '总学时',
          dataIndex: 'total',
          key: 'total'
        },
        {
          title: '法律素养',
          dataIndex: 'law',
          key: 'law'
        },
        {
          title: '身心素质',
          dataIndex: 'bodyMind',
          key: 'bodyMind'
        },
        {
          title: '创新创业素质',
          key: 'innovation',
          dataIndex: 'innovation'
        },
        {
          title: '思想品德素质',
          key: 'thought',
          dataIndex: 'thought'
        },
        {
          title: '文体素质',
          key: 'cultureSports',
          dataIndex: 'cultureSports'
        }
      ],
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '活动名称',
          align: 'center',
          dataIndex: 'activityName'
        },
        {
          title: '主办方',
          align: 'center',
          dataIndex: 'sponsor'
        },
        {
          title: '学号',
          align: 'center',
          dataIndex: 'uid'
        },
        {
          title: '姓名',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '所在学院',
          align: 'center',
          dataIndex: 'academy_dictText'
        },
        {
          title: '班级',
          align: 'center',
          dataIndex: 'clazz'
        },
        {
          title: '参加类型',
          align: 'center',
          dataIndex: 'joinType_dictText'
        },
        {
          title: '获奖情况',
          align: 'center',
          dataIndex: 'award'
        },
        {
          title: '学时类型',
          align: 'center',
          dataIndex: 'creditType_dictText'
        },
        {
          title: '学时数量',
          align: 'center',
          dataIndex: 'credit'
        },
        {
          title: '填报人及联系方式',
          align: 'center',
          dataIndex: 'contact'
        },
        {
          title: '审核人',
          align: 'center',
          dataIndex: 'toName'
        },
        {
          title: '学年',
          align: 'center',
          dataIndex: 'year_dictText'
        }
      ],
      url: {
        list: '/activityrecord/activityRecord/list',
        yearList: '/sys/dictItem/list',
        exportXlsUrl: '/activityrecord/activityRecord/exportXls'
      },
      superFieldList: [],
      yearList: []
    }
  },
  created() {
    // 统计表加载
    // setTimeout(() => {
    //   this.loading = !this.loading
    // }, 1000)
    // this.initLogInfo()

    this.initData()
  },
  methods: {
    // initLogInfo() {
    //   getLoginfo(null).then((res) => {
    //     if (res.success) {
    //       Object.keys(res.result).forEach(key => {
    //         res.result[key] = res.result[key] + ''
    //       })
    //       this.loginfo = res.result
    //     }
    //   })
    //   getVisitInfo().then(res => {
    //     if (res.success) {
    //       this.visitInfo = res.result
    //     }
    //   })
    // },
    initDictConfig() {
    },
    initData(){
      // 活动记录加载
      setTimeout(() => {
        this.activityLoading = !this.activityLoading
      }, 1000)
      this.getCredit()
      this.getYearList()
      this.getBarData()
    },
    // 读取学时
    getCredit() {
      let creditData = [{
        key: '1',
        total: 0,
        law: 0,
        bodyMind: 0,
        innovation: 0,
        thought: 0,
        cultureSports: 0
      }]
      creditData[0].law = store.getters.law
      creditData[0].bodyMind = store.getters.bodyMind
      creditData[0].innovation = store.getters.innovation
      creditData[0].thought = store.getters.thought
      creditData[0].cultureSports = store.getters.cultureSports
      creditData[0].total = creditData[0].law + creditData[0].bodyMind + creditData[0].innovation + creditData[0].thought + creditData[0].cultureSports
      console.log('学时', creditData)
      this.creditData = creditData
    },
    getBarData() {
        let barData = []
        barData[0] = {x: '法律素养',y: this.creditData[0].law}
        barData[1] = {x: '身心素质',y: this.creditData[0].bodyMind}
        barData[2] = {x: '创新创业素质',y: this.creditData[0].innovation}
        barData[3] = {x: '思想品德素质',y: this.creditData[0].thought}
        barData[4] = {x: '文体素质',y: this.creditData[0].cultureSports}
        console.log(barData)
        this.barData = barData
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'id', text: '主键', dictCode: '' })
      fieldList.push({ type: 'string', value: 'activityName', text: '活动名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sponsor', text: '主办方学院', dictCode: 'sys_depart,depart_name,id' })
      fieldList.push({ type: 'string', value: 'uid', text: '学号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'name', text: '姓名', dictCode: '' })
      fieldList.push({ type: 'string', value: 'academy', text: '所在学院', dictCode: 'sys_depart,depart_name,id' })
      fieldList.push({ type: 'string', value: 'clazz', text: '班级', dictCode: '' })
      fieldList.push({ type: 'int', value: 'joinType', text: '参加类型', dictCode: 'join_type' })
      fieldList.push({ type: 'string', value: 'award', text: '获奖情况', dictCode: '' })
      fieldList.push({ type: 'int', value: 'creditType', text: '学时类型', dictCode: 'learn_time_type' })
      fieldList.push({ type: 'double', value: 'credit', text: '学时数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'contact', text: '填报人及联系方式', dictCode: '' })
      fieldList.push({ type: 'string', value: 'toName', text: '审核人', dictCode: '' })
      fieldList.push({ type: 'int', value: 'year', text: '学年', dictCode: 'year' })
      fieldList.push({ type: 'string', value: 'createBy', text: '创建人', dictCode: '' })
      fieldList.push({ type: 'Date', value: 'createTime', text: '创建日期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'updateBy', text: '更新人', dictCode: '' })
      fieldList.push({ type: 'Date', value: 'updateTime', text: '更新日期', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sysOrgCode', text: '所属部门', dictCode: '' })
      this.superFieldList = fieldList
    },
    getYearList() {
      let dictId = '1481940297742143489'
      getAction(`${this.url.yearList}?dictId=${dictId}`).then(res => {
        if (res.success) {
          this.yearList = res.result.records
          console.log('学年')
          console.log(res.result)
        }
      })
    }


  }
}
</script>

<style lang='less' scoped>
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}

.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    font-size: .95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }

  p {
    line-height: 42px;
    margin: 0;

    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>