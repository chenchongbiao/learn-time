<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('活动表')">导出</a-button>
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

    <activity-modal ref="modalForm" @ok="modalFormOk"></activity-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ActivityModal from './modules/ActivityModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'ActivityList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ActivityModal
    },
    data () {
      return {
        description: '活动表管理页面',
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
            title:'活动发布者',
            align:"center",
            dataIndex: 'adminId_dictText'
          },
          {
            title:'学时类型',
            align:"center",
            dataIndex: 'typeId_dictText'
          },
          {
            title:'活动名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'报名最大人数',
            align:"center",
            dataIndex: 'applyMax'
          },
          {
            title:'学时数量',
            align:"center",
            dataIndex: 'activityTime'
          },
          {
            title:'活动报名开始时间',
            align:"center",
            dataIndex: 'startTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'活动报名结束时间',
            align:"center",
            dataIndex: 'endTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'活动地点',
            align:"center",
            dataIndex: 'address'
          },
          {
            title:'活动封面图片',
            align:"center",
            dataIndex: 'photos',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'已报名数量',
            align:"center",
            dataIndex: 'applyCount'
          },
          {
            title:'活动状态',
            align:"center",
            dataIndex: 'status_dictText'
          },
          {
            title:'活动信息介绍',
            align:"center",
            dataIndex: 'intro',
            scopedSlots: {customRender: 'htmlSlot'}
          },
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
          list: "/learntime/activity/list",
          delete: "/learntime/activity/delete",
          deleteBatch: "/learntime/activity/deleteBatch",
          exportXlsUrl: "/learntime/activity/exportXls",
          importExcelUrl: "learntime/activity/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
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
        fieldList.push({type:'string',value:'adminId',text:'活动发布者',dictCode:'sys_user,realname,id'})
        fieldList.push({type:'string',value:'typeId',text:'学时类型',dictCode:'learin_time_type,name,id'})
        fieldList.push({type:'string',value:'name',text:'活动名称',dictCode:''})
        fieldList.push({type:'int',value:'applyMax',text:'报名最大人数',dictCode:''})
        fieldList.push({type:'int',value:'activityTime',text:'学时数量',dictCode:''})
        fieldList.push({type:'date',value:'startTime',text:'活动报名开始时间'})
        fieldList.push({type:'date',value:'endTime',text:'活动报名结束时间'})
        fieldList.push({type:'string',value:'address',text:'活动地点',dictCode:''})
        fieldList.push({type:'string',value:'photos',text:'活动封面图片',dictCode:''})
        fieldList.push({type:'int',value:'applyCount',text:'已报名数量',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'活动状态',dictCode:'activity'})
        fieldList.push({type:'Text',value:'intro',text:'活动信息介绍',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>