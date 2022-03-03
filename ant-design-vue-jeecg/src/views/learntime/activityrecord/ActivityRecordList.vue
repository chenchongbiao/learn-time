<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="12">
            <a-form-item label="学号">
              <a-input placeholder="请输入学号查询" v-model="queryParam.uid"></a-input>
<!--              <j-input placeholder="输入学号模糊查询" v-model="queryParam.uid"></j-input>-->
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="姓名">
              <j-input placeholder="请输入姓名模糊查询" v-model="queryParam.name"></j-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="专业班级">
              <j-input placeholder="请输入专业班级模糊查询" v-model="queryParam.clazz"></j-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="活动名称">
                <j-input placeholder="输入活动名称模糊查询" v-model="queryParam.activityName"></j-input>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="所在学院">
                <a-select v-model="queryParam.sponsor" placeholder="请选择">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="depart in departList" :key="depart.id" :value="depart.id">
                    {{ depart.departName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="学年">
                <a-select v-model="queryParam.year" placeholder="请选择">
                  <a-select-option value="">请选择</a-select-option>
                  <a-select-option v-for="year in yearList" :key="year.id" :value="year.itemValue">
                    {{ year.itemText }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="创建人">
                <j-input placeholder="输入创建人模糊查询" v-model="queryParam.createBy"></j-input>
              </a-form-item>
            </a-col>
            <!--            <a-col :md="6" :sm="8">-->
            <!--              <a-form-item label="手机号码">-->
            <!--                <a-input placeholder="请输入手机号码查询" v-model="queryParam.phone"></a-input>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->

<!--            <a-col :md="6" :sm="8">-->
<!--              <a-form-item label="用户状态">-->
<!--                <a-select v-model="queryParam.status" placeholder="请选择">-->
<!--                  <a-select-option value="">请选择</a-select-option>-->
<!--                  <a-select-option value="1">正常</a-select-option>-->
<!--                  <a-select-option value="2">冻结</a-select-option>-->
<!--                </a-select>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXlsTamp('广州商学院“4+X”活动学时认定登记表')">下载模板</a-button>-->
      <a-button
        :ghost="true"
        type="primary"
        icon="download"
        size="small"
        @click="downloadFile('http://10.0.0.43/file/%E5%B9%BF%E5%B7%9E%E5%95%86%E5%AD%A6%E9%99%A2%E2%80%9C4+X%E2%80%9D%E6%B4%BB%E5%8A%A8%E5%AD%A6%E6%97%B6%E8%AE%A4%E5%AE%9A%E7%99%BB%E8%AE%B0%E8%A1%A8(2022%E7%89%88).xls')">
        下载模板
      </a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXls('广州商学院“4+X”活动学时认定登记表')">导出</a-button>-->
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <activity-record-modal ref="modalForm" @ok="modalFormOk"></activity-record-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ActivityRecordModal from './modules/ActivityRecordModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getAction } from '@api/manage'
  import { queryDepartTreeSync,treeList } from '@api/api'

  export default {
    name: 'ActivityRecordList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ActivityRecordModal
    },
    data () {
      return {
        description: '活动记录表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'活动名称',
            align:"center",
            dataIndex: 'activityName'
          },
          {
            title:'主办方',
            align:"center",
            dataIndex: 'sponsor'
          },
          {
            title:'学号',
            align:"center",
            dataIndex: 'uid'
          },
          {
            title:'姓名',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'所在学院',
            align:"center",
            dataIndex: 'academy_dictText'
          },
          {
            title:'班级',
            align:"center",
            dataIndex: 'clazz'
          },
          {
            title:'参加类型',
            align:"center",
            dataIndex: 'joinType_dictText'
          },
          {
            title:'获奖情况',
            align:"center",
            dataIndex: 'award'
          },
          {
            title:'学时类型',
            align:"center",
            dataIndex: 'creditType_dictText'
          },
          {
            title:'学时数量',
            align:"center",
            dataIndex: 'credit'
          },
          {
            title:'填报人及联系方式',
            align:"center",
            dataIndex: 'contact'
          },
          {
            title:'审核人',
            align:"center",
            dataIndex: 'toName'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'学年',
            align:"center",
            dataIndex: 'year_dictText'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy'
          },
          {
            title:'更新日期',
            align:"center",
            dataIndex: 'updateTime'
          },
          // {
          //   title:'所属部门',
          //   align:"center",
          //   dataIndex: 'sysOrgCode'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/activityrecord/activityRecord/list",
          yearList: "/sys/dictItem/list",
          departList: "/sys/sysDepart/listAll",
          delete: "/activityrecord/activityRecord/delete",
          deleteBatch: "/activityrecord/activityRecord/deleteBatch",
          exportXlsUrl: "/activityrecord/activityRecord/exportXls",
          exportXlsUrlTamp: "/activityrecord/activityRecord/exportXlsTamp",
          importExcelUrl: "activityrecord/activityRecord/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
        departList:[],
        yearList:[]
      }
    },
    created() {
      this.getSuperFieldList();
      this.getDepartList();
      this.getYearList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'id',text:'主键',dictCode:''})
        fieldList.push({type:'string',value:'activityName',text:'活动名称',dictCode:''})
        fieldList.push({type:'string',value:'sponsor',text:'主办方学院',dictCode:'sys_depart,depart_name,id'})
        fieldList.push({type:'string',value:'uid',text:'学号',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'姓名',dictCode:''})
        fieldList.push({type:'string',value:'academy',text:'所在学院',dictCode:'sys_depart,depart_name,id'})
        fieldList.push({type:'string',value:'clazz',text:'班级',dictCode:''})
        fieldList.push({type:'int',value:'joinType',text:'参加类型',dictCode:'join_type'})
        fieldList.push({type:'string',value:'award',text:'获奖情况',dictCode:''})
        fieldList.push({type:'int',value:'creditType',text:'学时类型',dictCode:'learn_time_type'})
        fieldList.push({type:'double',value:'credit',text:'学时数量',dictCode:''})
        fieldList.push({type:'string',value:'contact',text:'填报人及联系方式',dictCode:''})
        fieldList.push({type:'string',value:'toName',text:'审核人',dictCode:''})
        fieldList.push({type:'int',value:'year',text:'学年',dictCode:'year'})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'Date',value:'createTime',text:'创建日期',dictCode:''})
        fieldList.push({type:'string',value:'updateBy',text:'更新人',dictCode:''})
        fieldList.push({type:'Date',value:'updateTime',text:'更新日期',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'所属部门',dictCode:''})
        this.superFieldList = fieldList
      },
      getDepartList() {
        // 各学院的父id
        let parentId = "604023e70310485d9b9b779c8983f34c"
        queryDepartTreeSync({pid:parentId}).then((res) => {
          if (res.success) {
            this.departList = res.result
          }
        })
      },
      getYearList() {
        let dictId = "1481940297742143489"
        getAction(`${this.url.yearList}?dictId=${dictId}`).then(res => {
          if (res.success) {
            console.log("学年")
            console.log(res.result)
            this.yearList = res.result.records
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>