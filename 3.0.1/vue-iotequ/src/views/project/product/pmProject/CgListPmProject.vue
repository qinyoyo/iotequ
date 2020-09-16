
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-pmproject .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit 
              @row-click="(row, column, event)=>cgList.list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>cgList.list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>cgList.list_headClick(myself,{ column, event })" 
              @row-dblclick="(row, column, event)=>cgList.list_rowDblclick(myself,{ row, column, event })" 
              @cell-click="(row, column, cell, event)=>cgList.list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="name" :page="paginationCurrentPage" :label="$t('pmProject.field.name')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="flowState" type="dict" :page="paginationCurrentPage" :label="$t('pmProject.field.flowState')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.flowState,dictionary.dictFlowState,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="type" type="dict" :page="paginationCurrentPage" :label="$t('pmProject.field.type')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.type,dictionary.dictType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="flowRegisterTime" type="datetime" :page="paginationCurrentPage" :label="$t('pmProject.field.flowRegisterTime')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.flowRegisterTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="customer" :page="paginationCurrentPage" :label="$t('pmProject.field.customer')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.customer }}
        </template>

      </cg-table-column>
      <cg-table-column prop="registerByName" :page="paginationCurrentPage" :label="$t('pmProject.field.flowRegisterBy')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.registerByName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="nextOperatorName" :page="paginationCurrentPage" :label="$t('pmProject.field.flowNextOperator')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.nextOperatorName }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords" :cgList="myself"
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="cgList.list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
                  @row-click="(row, event)=>cgList.list_rowClick(myself,{ row, event })" 
                  @row-contextmenu="(row, event)=>cgList.list_rowContextmenu(myself,{ row, event })" 
                  @row-dblclick="(row, event)=>cgList.list_rowDblclick(myself,{ row, event })" 
                  @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
                  @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <nut-scroller v-if="mobile && isTableMode()"
                  wrapperElement=".cg-list-pmproject .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" :flowActions="contextMenu.flowActions"
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="name|pmProject.field.name,flowState|pmProject.field.flowState,type|pmProject.field.type,customer|pmProject.field.customer," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('pmProject.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('pmProject.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('pmProject.field.flowState')" prop="flowState" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.flowState" :dictionary="dictionary.dictFlowState"
                     :disabled="fixedQueryRecord.flowState?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('pmProject.field.type')" prop="type" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.type" :dictionary="dictionary.dictType"
                     :disabled="fixedQueryRecord.type?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('pmProject.field.customer')" prop="customer" :size="$store.state.app.size">
          <el-input v-model="queryRecord.customer" type="textarea" name="customer" :label="$t('pmProject.field.customer')" clearable
                    :readonly="fixedQueryRecord.customer?true:false" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus/>
            
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListPmProject',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'flow_register_time desc',
      queryRecordFields: ['name','flowState','type','customer'],
      formPath: '/project/product/pmProject/record',
      idField: 'id',
      dictionary: {
        dictFlowState: [],
        dictType: []
      },
      needLoadDictionary: true,
      flowActionList: [
        {
          action: 'assess',
          prefix: 'flow.',
          title: this.$t('pmProject.title.assess'),
          icon: 'fa fa-binoculars',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'decision',
          prefix: 'flow.',
          title: this.$t('pmProject.title.decision'),
          icon: 'fa fa-gavel',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'develop',
          prefix: 'flow.',
          title: this.$t('pmProject.title.develop'),
          icon: 'fa fa-cc',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'publish',
          prefix: 'flow.',
          title: this.$t('pmProject.title.publish'),
          icon: 'fa fa-check',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'review',
          prefix: 'flow.',
          title: this.$t('pmProject.title.review'),
          icon: 'fa fa-search-plus',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        },
        {
          action: 'test',
          prefix: 'flow.',
          title: this.$t('pmProject.title.test'),
          icon: 'fa fa-user-md',
          rowProperty: 'sr',
          actionProperty: 'aj',
          groupid: 20
        }
      ],
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      hasSonTables: true,
      listName: 'pmProject',
      multipleSelection: true,
      generatorName: 'pmProject',
      baseUrl: '/project/product/pmProject'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,flow,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        name: null,
        flowState: null,
        type: null,
        flowRegisterTime: null,
        customer: null,
        registerByName: null,
        nextOperatorName: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListPmProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
