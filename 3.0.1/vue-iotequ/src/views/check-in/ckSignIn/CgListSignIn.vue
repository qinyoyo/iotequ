
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-signin .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe highlight-current-row fit 
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
      <cg-table-column prop="orgCode" type="dict" :page="paginationCurrentPage" :label="$t('ckSignIn.field.orgCode')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('ckSignIn.field.realName')" sortable align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="paginationCurrentPage" :label="$t('devPeople.field.sex')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="birthDate" type="date" :page="paginationCurrentPage" :label="$t('devPeople.field.birthDate')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.birthDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="recSource" :page="paginationCurrentPage" :label="$t('ckSignIn.field.recSource')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.recSource }}
        </template>

      </cg-table-column>
      <cg-table-column prop="recSourceType" :page="paginationCurrentPage" :label="$t('ckSignIn.field.recSourceType')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.recSourceType }}
        </template>

      </cg-table-column>
      <cg-table-column prop="eventType" type="dict" :page="paginationCurrentPage" :label="$t('ckSignIn.field.eventType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.eventType,dictionary.dictEventType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="dateDate" type="date" :page="paginationCurrentPage" :label="$t('ckSignIn.field.dateDate')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.dateDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="dateTime" type="time" :page="paginationCurrentPage" :label="$t('ckSignIn.field.dateTime')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.dateTime,'HH:mm') }}
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
                  wrapperElement=".cg-list-signin .el-table__body-wrapper"
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
      <div>
        <el-form-item :label="$t('ckSignIn.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.orgCode" :dictionary="dictionary.dictOrgCode"
                     :disabled="fixedQueryRecord.orgCode?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <cg-join v-model="userNoJoinVisible">
          <CgListDevPeople slot="popover" ref="userNoJoin" openID="userno-join" :height="joinHeight()" :joinShow="userNoJoinVisible" joinMultiple
            :originSelections="queryRecord.userNo" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('userNo',rows)}" @showJoinList="userNoJoinVisible=true"/>
        <el-form-item slot="reference" :label="$t('ckSignIn.field.realName')" prop="realName" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('ckSignIn.field.realName')" clearable resize autofocus @clear="clearJoinValues(myself,'userNoJoin')"/>
        </el-form-item>
        </cg-join>
        <el-form-item :label="$t('ckSignIn.field.recTime')" prop="recTime" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.recTime" :title="$t('ckSignIn.field.recTime')" name="recTime" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.recTime?true:false"  clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import CgListDevPeople from '@/views/reader/devPeople/CgListDevPeople.vue'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListSignIn',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  components: { CgListDevPeople },
  data() {
    return {
      path: 'list',
      defaultOrder: 'rec_time desc',
      queryRecordFields: ['orgCode','userNo','recTime'],
      formPath: '/check-in/ckSignIn/record',
      localExport: true,
      idField: 'id',
      dictionary: {
        dictOrgCode: [],
        dictSex: [],
        dictEventType: this.getDictionary('1,2,3','ckSignIn.field.eventType_0,ckSignIn.field.eventType_1,ckSignIn.field.eventType_2')
      },
      needLoadDictionary: true,
      userNoJoinVisible: false,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      listName: 'signIn',
      generatorName: 'ckSignIn',
      baseUrl: '/check-in/ckSignIn'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,view,localExport,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        orgCode: null,
        userNo: null,
        realName: null,
        sex: null,
        birthDate: null,
        recSource: null,
        recSourceType: null,
        eventType: null,
        dateDate: null,
        dateTime: null,
        recTime: null,
      }, this.fixedQueryRecord)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        userNo: {
          valueField: 'userNo',
          fields: 'realName=realName,sex=sex,birthDate=birthDate'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.queryRecord, field, joinDefine[field], rows)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListSignIn-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
