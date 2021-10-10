
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-ademployee .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              v-set-input:no-tab-index="{tabIndex: -1}" v-table-enter-tab stripe :border="!mobile" highlight-current-row fit 
              @row-click="(row, column, event)=>cgList.list_rowClick(myself,{ row, column, event })" 
              @row-contextmenu="(row, column, event)=>cgList.list_rowContextmenu(myself,{ row, column, event })" 
              @header-click="(column, event)=>cgList.list_headClick(myself,{ column, event })" 
              @row-dblclick="(row, column, event)=>cgList.list_rowDblclick(myself,{ row, column, event })" 
              @cell-click="(row, column, cell, event)=>cgList.list_cellClick(myself,{ row, column, cell, event })" 
              @selection-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @current-change="(selection)=>cgList.list_selectionChange(myself, selection)" 
              @sort-change="(options)=>cgList.list_sortChange(myself, options)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <cg-table-column prop="realName" :page="1" :label="$t('sysUser.field.realName')" sortable :sort-method="(a,b)=>chineseSort(a.realName,b.realName)" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="1" :label="$t('sysUser.field.sex')" sortable :sort-method="(a,b)=>chineseSort(a.sex,b.sex)" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="employeeNo" :page="1" :label="$t('adEmployee.field.employeeNo')" sortable :sort-method="(a,b)=>chineseSort(a.employeeNo,b.employeeNo)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.employeeNo" type="text" />
          <span v-else>{{ scope.row.employeeNo }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="emLevel" :page="1" :label="$t('adEmployee.field.emLevel')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.emLevel" type="text" />
          <span v-else>{{ scope.row.emLevel }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="isAttendance" :page="1" :label="$t('adEmployee.field.isAttendance')" sortable align="center" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.isAttendance" />
          <cg-icon v-else :color="scope.row.isAttendance?'#46a6ff':'grey'" :icon="scope.row.isAttendance?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="enterDate" type="date" :page="1" :label="$t('adEmployee.field.enterDate')" align="center" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.enterDate" :align="mobile?'right':'center'" type="date" :title="$t('adEmployee.field.enterDate')"  clearable/>
          <span v-else>{{ time2String(scope.row.enterDate,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="leaveDate" type="date" :page="1" :label="$t('adEmployee.field.leaveDate')" align="center" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.leaveDate" :align="mobile?'right':'center'" type="date" :title="$t('adEmployee.field.leaveDate')"  clearable/>
          <span v-else>{{ time2String(scope.row.leaveDate,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="shiftId" type="dict" :page="1" :label="$t('adEmployee.field.shiftId')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.shiftId" automaticDropdown appendToBody :dictionary="dictionary.dictShiftId" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.shiftId,dictionary.dictShiftId,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" 
                  :isUnMore="true" :cgList="myself"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="realName|sysUser.field.realName,sex|sysUser.field.sex,employeeNo|adEmployee.field.employeeNo,isAttendance|adEmployee.field.isAttendance," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <cg-join v-model="idJoinVisible">
          <CgListUserJoin slot="popover" ref="idJoin" openID="id-join" :height="joinHeight()" :joinShow="idJoinVisible" joinMultiple
            :originSelections="queryRecord.id" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('id',rows)}" @showJoinList="idJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('sysUser.field.realName')" prop="realName" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('sysUser.field.realName')" clearable resize autofocus @clear="clearJoinValues(myself,'idJoin')"/>
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
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListAdEmployee',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  components: { CgListUserJoin },
  data() {
    return {
      rulesObject,
      path: 'list',
      defaultOrder: 'org_code,employee_no',
      queryRecordFields: ['id','id','employeeNo','isAttendance'],
      formPath: '/attendance/employee/adEmployee/record',
      localExport: true,
      idField: 'id',
      dictionary: {
        dictSex: [],
        dictShiftId: [],
        dictOrgCode: []
      },
      needLoadDictionary: true,
      idJoinVisible: false,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/attendance/employee/adEmployee/updateSelective')?['employeeNo', 'emLevel', 'isAttendance', 'enterDate', 'leaveDate', 'shiftId']:null,
      listName: 'adEmployee',
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,view,edit,localExport,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
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
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAdEmployee-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
