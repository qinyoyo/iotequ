
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-ademployee .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit 
              @row-click="(row, column, event)=>list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>list_headClick(myself,{ column, event })" 
              @row-dblclick="(row, column, event)=>list_rowDblclick(myself,{ row, column, event })" 
              @cell-click="(row, column, cell, event)=>list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>list_selectionChange(myself, selection)" 
              @current-change="(selection)=>list_selectionChange(myself, selection)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <cg-table-column prop="realName" :page="1" :label="$t('adEmployee.field.id')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.realName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="1" :label="$t('sysUser.field.sex')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="employeeNo" :page="1" :label="$t('adEmployee.field.employeeNo')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.employeeNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="emLevel" :page="1" :label="$t('adEmployee.field.emLevel')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.emLevel }}
        </template>

      </cg-table-column>
      <cg-table-column prop="isAttendance" :page="1" :label="$t('adEmployee.field.isAttendance')" sortable align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.isAttendance?'#46a6ff':'grey'" :icon="scope.row.isAttendance?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="enterDate" type="date" :page="1" :label="$t('adEmployee.field.enterDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.enterDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="leaveDate" type="date" :page="1" :label="$t('adEmployee.field.leaveDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.leaveDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="shiftId" type="dict" :page="1" :label="$t('adEmployee.field.shiftId')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.shiftId,dictionary.dictShiftId,false,true) }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
                  :isUnMore="true" :cgList="myself"
                  @doAction="(a,row)=>doAction(a,{row})"
                  @loadMore="list_loadMore(myself)"
                  @refresh="doAction('refresh',{ isPullDownEvent : true})"
                  @row-click="(row, event)=>list_rowClick(myself,{ row, event })" 
                  @row-contextmenu="(row, event)=>list_rowContextmenu(myself,{ row, event })" 
                  @row-dblclick="(row, event)=>list_rowDblclick(myself,{ row, event })" 
                  @selection-change="(selection)=>list_selectionChange(myself, selection)" 
                  @current-change="(selection)=>list_selectionChange(myself, selection)" 
    >
      <template slot="append">
	      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="list_allActions(myself,'main')" @actionClick="doAction" />
      </template>
    </cg-card-list>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="realName|adEmployee.field.id,sex|sysUser.field.sex,employeeNo|adEmployee.field.employeeNo,isAttendance|adEmployee.field.isAttendance," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <cg-join v-model="idJoinVisible">
          <CgListUserJoin slot="popover" ref="idJoin" openID="id-join" :height="joinHeight()" :joinShow="idJoinVisible" joinMultiple
            :originSelections="queryRecord.id" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('id',rows)}" @showJoinList="idJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('adEmployee.field.id')" prop="realName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('adEmployee.field.id')" clearable resize autofocus @clear="clearJoinValues(myself,'idJoin')"/>
        </el-form-item>
        </cg-join>
        <el-form-item slot="reference" :label="$t('sysUser.field.sex')" prop="sex" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.sex" :dictionary="dictionary.dictSex"
                     :disabled="fixedQueryRecord.sex?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        </cg-join>
        <el-form-item :label="$t('adEmployee.field.employeeNo')" prop="employeeNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.employeeNo" type="text" name="employeeNo"
                    :readonly="fixedQueryRecord.employeeNo?true:false" :label="$t('adEmployee.field.employeeNo')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('adEmployee.field.isAttendance')" prop="isAttendance" :size="$store.state.app.size">
          <el-checkbox-group v-model="queryRecord.isAttendance" :max="1">
            <el-checkbox name="isAttendance" :label="true" :disabled="fixedQueryRecord.isAttendance?true:false">{{ $t('system.action.yes') }}</el-checkbox>
            <el-checkbox name="isAttendance" :label="false" :disabled="fixedQueryRecord.isAttendance?true:false">{{ $t('system.action.no') }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import cgList from '@/utils/cgList'
import cg from '@/utils/cg'
import {hasAuthority} from '@/utils/cg'
import time from '@/utils/time'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgListAdEmployee-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAdEmployee',
  mixins,
  props: {
    joinMode: {
      type: Boolean,
      default: false
    },
    joinMultiple: {
      type: Boolean,
      default: false
    },
    joinShow: {
      type: Boolean,
      default: true
    },
    openID: {
      type: String,
      default: ''
    },
    originSelections: {
      type: String,
      default: ''
    },
    selectionKey: {
      type: String,
      default: 'id'
    },
    height: {
      type: Number,
      default: 0
    },
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    }
  },
  components: { CgListUserJoin },
  data() {
    return {
      cgList,
      myself: this,
      rulesObject,
      path: 'list',
      title: this.$t('adEmployee.title.list'),
      showQuery: false,
      showActionView: false,
      defaultOrder: 'org_code,employee_no',
      queryRecord: this.initialQueryRecord(),
      queryRecordFields: ['id','id','employeeNo','isAttendance'],
      formPath: '/attendance/employee/adEmployee/record',
      listLoading: false,
      rows: [],
      selections: this.originSelections,
      localExport: true,
      parentField: null,
      idField: 'id',
			dictionary: {
			  dictSex: [],
			  dictShiftId: [],
			  dictOrgCode: []
		  },
      needLoadDictionary: true,
      idJoinVisible: false,
      paginationPageSize: 0,
      sortableFields: [],
      sortableFieldsOrder: [],
      editInlineFields: null,
      contextMenu: { top: 0, left: 0, visible: false, row: null, actions: [], trElement: null },
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    multiple() {
      return this.joinMode ? this.joinMultiple : false
    },
    className() {
      return this.openID ? 'cg-list-ademployee cg-list-ademployee' + '-'+this.openID : 'cg-list-ademployee'
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,view,edit,localExport,'
    }
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        n && Object.keys(n).length && Object.keys(n).some(k=>{
          if (cg.hasValue(n[k])) return true
        }) && this.doAction('refresh')
      },
      deep: true,
      immediate: true
    },
    joinShow(newValue, oldValue) {
      if (oldValue && !newValue && this.joinMode) {
        this.$emit('closeJoinList', this.multiple ? this.$refs.cgList.store.states.selection : [this.$refs.cgList.store.states.currentRow])
      }
    }
  },
  created() {
    this.doAction('refresh')
  },
  mounted() {
    cgList.list_tableInit(this)
  },
  activated() {
    cgList.list_activedRefresh(this)
  },
  destroyed() {
    cgList.list_destroyScroll(this)
  },
  methods: {
    rowClassName({row, rowIndex}){
      return ''
    },
    defaultEditMode(row) {
      if (this.hasAuthorityOf(this,this.baseUrl,'edit',row)) return 'edit'
      else if (this.hasAuthorityOf(this,this.baseUrl,'view',row)) return 'view'
      else return ''
    },
    isTableMode() {
      return this.joinMode || !this.mobile || this.isLandscape() || typeof this.rowRender !== 'function'
    },
    hasMenu() {
      return this.mobile
    },
    isLandscape() {
      return window.orientation == 90 || window.orientation == -90
    },
    showActionSheet(hidden){
      this.showActionView = !hidden
    },
    tableHeight() {
      if (this.height > 0) return this.height
      else return (this.containerHeight())
    },
    initialQueryRecord() {
      return Object.assign({
        realName: null,
        sex: null,
        employeeNo: null,
        isAttendance: [],
        enterDate: null,
        leaveDate: null,
        shiftId: null,
        orgCode: null,
        birthDate: null,
      }, this.fixedQueryRecord)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        id: {
          valueField: 'id',
          fields: 'realName=realName,sex=sex,orgCode=orgCode,birthDate=birthDate'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
    doAction(action, options) {
      this.queryRecord = Object.assign(this.queryRecord, this.fixedQueryRecord)
      cgList.list_doAction(this, action, options)
    },
    ...cg,
    ...cgList,
    time2String: time.toString
  }
}
</script>
