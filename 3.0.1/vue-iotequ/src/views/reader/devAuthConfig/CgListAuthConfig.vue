
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-authconfig .el-table__body-wrapper" :visibility-height="200">
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
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter no-tab-index" width="36" />
      <cg-table-column prop="beginAt" type="date" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.beginAt')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.beginAt" :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.beginAt')"  clearable/>
          <span v-else>{{ time2String(scope.row.beginAt,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="endAt" type="date" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.endAt')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.endAt" :align="mobile?'right':'center'" type="date" :title="$t('devAuthConfig.field.endAt')"  clearable/>
          <span v-else>{{ time2String(scope.row.endAt,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="startTime" type="time" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.startTime')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.startTime" :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.startTime')" format="HH:mm" clearable/>
          <span v-else>{{ time2String(scope.row.startTime,'HH:mm') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="endTime" type="time" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.endTime')" align="left" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.endTime" :align="mobile?'right':'center'" type="time" :title="$t('devAuthConfig.field.endTime')" format="HH:mm" clearable/>
          <span v-else>{{ time2String(scope.row.endTime,'HH:mm') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="onlyWorkDay" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.onlyWorkDay')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.onlyWorkDay" />
          <cg-icon v-else :color="scope.row.onlyWorkDay?'#46a6ff':'grey'" :icon="scope.row.onlyWorkDay?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="auth" type="dict" :page="paginationCurrentPage" :label="$t('devAuthConfig.field.auth')" align="right" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.auth" automaticDropdown appendToBody :dictionary="dictionary.dictAuth" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.auth,dictionary.dictAuth,false,true) }}</span>
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
                  wrapperElement=".cg-list-authconfig .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" hide-on-single-page @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="[10, 20, 30, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListAuthConfig',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      rulesObject,
      path: 'list',
      defaultOrder: 'id desc',
      queryRecordFields: [],
      formPath: '/reader/devAuthConfig/record',
      idField: 'id',
      dictionary: {
        dictAuth: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/reader/devAuthConfig/updateSelective')?['beginAt', 'endAt', 'startTime', 'endTime', 'onlyWorkDay', 'auth']:null,
      listName: 'authConfig',
      generatorName: 'devAuthConfig',
      baseUrl: '/reader/devAuthConfig'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,delete,editInline_add,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    newRecordForEditInline() {
      return {
        roleId: 0,
        onlyWorkDay: false,
        auth: 4,
      }
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAuthConfig-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
