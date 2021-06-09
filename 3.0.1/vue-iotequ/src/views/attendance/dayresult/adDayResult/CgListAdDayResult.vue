
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-addayresult .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="orgName" :page="paginationCurrentPage" :label="$t('adDayResult.field.orgName')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.orgName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="employeeNo" :page="paginationCurrentPage" :label="$t('adDayResult.field.employeeNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.employeeNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('adDayResult.field.realName')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="adDate" type="date" :page="paginationCurrentPage" :label="$t('adDayResult.field.adDate')" sortable align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.adDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="shiftName" :page="paginationCurrentPage" :label="$t('adDayResult.field.shiftName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.shiftName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="stateName" :page="paginationCurrentPage" :label="$t('adDayResult.field.stateName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.stateName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="times" :page="paginationCurrentPage" :label="$t('adDayResult.field.times')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.times }}
        </template>

      </cg-table-column>
      <cg-table-column prop="minutes" :page="paginationCurrentPage" :label="$t('adDayResult.field.minutes')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.minutes }}
        </template>

      </cg-table-column>
      <cg-table-column prop="workMinutes" :page="paginationCurrentPage" :label="$t('adDayResult.field.workMinutes')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.workMinutes }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'orgName,employeeNo,realName'"
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
                  wrapperElement=".cg-list-addayresult .el-table__body-wrapper"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="orgCode|adDayResult.field.orgCode,employeeNoAdEmployeeRealName|sysUser.field.realName,adDate|adDayResult.field.adDate," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('adDayResult.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode"/>
        </el-form-item>
        <cg-join v-model="employeeNoJoinVisible">
          <CgListAdEmployee slot="popover" ref="employeeNoJoin" openID="employeeno-join" :height="joinHeight()" :joinShow="employeeNoJoinVisible" joinMultiple
            :originSelections="queryRecord.employeeNo" selectionKey="employeeNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('employeeNo',rows)}" @showJoinList="employeeNoJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('sysUser.field.realName')" prop="employeeNoAdEmployeeRealName" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.employeeNoAdEmployeeRealName" type="text" name="employeeNoAdEmployeeRealName"
                    :readonly="fixedQueryRecord.employeeNoAdEmployeeRealName?true:false" :label="$t('sysUser.field.realName')" clearable resize autofocus @clear="clearJoinValues(myself,'employeeNoJoin')"/>
        </el-form-item>
        </cg-join>
        <el-form-item :label="$t('adDayResult.field.adDate')" prop="adDate" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.adDate" :title="$t('adDayResult.field.adDate')" name="adDate" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.adDate?true:false"  clearable />
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
  name: 'CgListAdDayResult',
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
      defaultOrder: 'org_code,employee_no,ad_date,shift_id,state',
      queryRecordFields: ['orgCode','employeeNo','adDate'],
      formPath: '/attendance/dayresult/adDayResult/record',
      idField: 'id',
      dictionary: {
        dictOrgCode: []
      },
      needLoadDictionary: true,
      employeeNoJoinVisible: false,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      groupByEntityFields: 'orgName,employeeNo,realName',
      listName: 'adDayResult',
      generatorName: 'adDayResult',
      baseUrl: '/attendance/dayresult/adDayResult'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'adjust',
          icon: 'fa fa-bolt fa-fw',
          title: 'adDayResult.action.adjust',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: true
        },
        {
          action: '_export',
          icon: 'e',
          title: 'adDayResult.action._export',
          groupid: 10,
          confirm: 'adDayResult.action._exportConfirm',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,_export,adjust,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        orgCode: null,
        orgName: null,
        employeeNo: null,
        employeeNoAdEmployeeRealName: null,
        realName: null,
        adDate: [this.cgUtils().time.startOf(this.cgUtils().time.dateAdd(new Date(),-1,'day'),'day'),this.cgUtils().time.endOf(this.cgUtils().time.dateAdd(new Date(),-1,'day'))],
        shiftName: null,
        stateName: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        employeeNo: {
          valueField: 'employeeNo',
          fields: 'isAttendance=isAttendance,employeeNoAdEmployeeRealName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAdDayResult-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
