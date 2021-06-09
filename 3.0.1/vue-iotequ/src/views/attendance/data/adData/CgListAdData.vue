
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-addata .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe :border="!mobile" highlight-current-row fit :span-method="groupFields" 
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
      <cg-table-column prop="dateDate" type="date" :page="paginationCurrentPage" :label="$t('adData.field.dateDate')" sortable align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.dateDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <el-table-column prop="orgCode" type="dict" width="200" :label="$t('sysUser.field.orgCode')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </el-table-column>
      <cg-table-column prop="employeeNo" :page="paginationCurrentPage" :label="$t('adData.field.employeeNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.employeeNo }}
        </template>

      </cg-table-column>
      <el-table-column prop="realName" width="100" :label="$t('adData.field.realName')" sortable align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </el-table-column>
      <cg-table-column prop="dateTime" type="time" :page="paginationCurrentPage" :label="$t('adData.field.dateTime')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.dateTime,'HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="recType" type="dict" :page="paginationCurrentPage" :label="$t('adData.field.recType')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.recType,dictionary.dictRecType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="recSourceType" :page="paginationCurrentPage" :label="$t('adData.field.recSourceType')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.recSourceType }}
        </template>

      </cg-table-column>
      <cg-table-column prop="recSource" :page="paginationCurrentPage" :label="$t('adData.field.recSource')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.recSource }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'dateDate,orgCode,employeeNo,realName'"
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
                  wrapperElement=".cg-list-addata .el-table__body-wrapper"
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
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" ref="query" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="dateDate|adData.field.dateDate,realName|adData.field.realName,recType|adData.field.recType,recSourceType|adData.field.recSourceType,recSource|adData.field.recSource," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('adData.field.dateDate')" prop="dateDate" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.dateDate" :title="$t('adData.field.dateDate')" name="dateDate" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.dateDate?true:false"  clearable />
        </el-form-item>
        <cg-join v-model="employeeNoJoinVisible">
          <CgListAdEmployee slot="popover" ref="employeeNoJoin" openID="employeeno-join" :height="joinHeight()" :joinShow="employeeNoJoinVisible" joinMultiple
            :originSelections="queryRecord.employeeNo" selectionKey="employeeNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('employeeNo',rows)}" @showJoinList="employeeNoJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('adData.field.realName')" prop="realName" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('adData.field.realName')" clearable resize autofocus @clear="clearJoinValues(myself,'employeeNoJoin')"/>
        </el-form-item>
        </cg-join>
        <el-form-item :label="$t('adData.field.recType')" prop="recType" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.recType" :dictionary="dictionary.dictRecType"
                     :disabled="fixedQueryRecord.recType?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('adData.field.recSourceType')" prop="recSourceType" :size="$store.state.app.size">
          <el-input v-model="queryRecord.recSourceType" type="text" name="recSourceType"
                    :readonly="fixedQueryRecord.recSourceType?true:false" :label="$t('adData.field.recSourceType')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('adData.field.recSource')" prop="recSource" :size="$store.state.app.size">
          <el-input v-model="queryRecord.recSource" type="text" name="recSource"
                    :readonly="fixedQueryRecord.recSource?true:false" :label="$t('adData.field.recSource')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import CgListAdEmployee from '@/views/attendance/employee/adEmployee/CgListAdEmployee.vue'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListAdData',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  components: { CgListAdEmployee },
  data() {
    return {
      path: 'list',
      defaultOrder: 'date_date desc, org_code, date_time',
      queryRecordFields: ['dateDate','employeeNo','recType','recSourceType','recSource'],
      formPath: '/attendance/data/adData/record',
      localExport: true,
      idField: 'id',
      dictionary: {
        dictOrgCode: [],
        dictRecType: []
      },
      needLoadDictionary: true,
      employeeNoJoinVisible: false,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      groupByEntityFields: 'dateDate,orgCode,employeeNo,realName',
      listName: 'adData',
      generatorName: 'adData',
      baseUrl: '/attendance/data/adData'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,localExport,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        dateDate: this.cgUtils().time.timeRange('this month'),
        orgCode: null,
        employeeNo: null,
        realName: null,
        dateTime: null,
        recType: null,
        recSourceType: null,
        recSource: null,
        recTime: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        employeeNo: {
          valueField: 'employeeNo',
          fields: 'realName=realName,isAttendance=isAttendance,orgCode=orgCode'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAdData-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
