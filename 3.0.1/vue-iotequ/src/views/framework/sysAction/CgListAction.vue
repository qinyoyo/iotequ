
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-action .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="note" :page="paginationCurrentPage" :label="$t('sysAction.field.note')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.note }}
        </template>

      </cg-table-column>
      <cg-table-column prop="value" :page="paginationCurrentPage" :label="$t('sysAction.field.value')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.value }}
        </template>

      </cg-table-column>
      <cg-table-column prop="params" :page="paginationCurrentPage" :label="$t('sysAction.field.params')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.params }}
        </template>

      </cg-table-column>
      <cg-table-column prop="method" type="dict" :page="paginationCurrentPage" :label="$t('sysAction.field.method')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.method,dictionary.dictMethod,false,true) }}
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
                  wrapperElement=".cg-list-action .el-table__body-wrapper"
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
        <el-form-item :label="$t('sysAction.field.note')" prop="note" :size="$store.state.app.size">
          <el-input v-model="queryRecord.note" type="text" name="note"
                    :readonly="fixedQueryRecord.note?true:false" :label="$t('sysAction.field.note')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysAction.field.value')" prop="value" :size="$store.state.app.size">
          <el-input v-model="queryRecord.value" type="text" name="value"
                    :readonly="fixedQueryRecord.value?true:false" :label="$t('sysAction.field.value')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysAction.field.params')" prop="params" :size="$store.state.app.size">
          <el-input v-model="queryRecord.params" type="text" name="params"
                    :readonly="fixedQueryRecord.params?true:false" :label="$t('sysAction.field.params')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysAction.field.method')" prop="method" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.method" :dictionary="dictionary.dictMethod"
                     :disabled="fixedQueryRecord.method?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListAction',
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
      defaultOrder: 'value',
      queryRecordFields: ['note','value','params','method'],
      formPath: '/framework/sysAction/record',
      idField: 'id',
      dictionary: {
        dictMethod: this.getDictionary('GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE')
      },
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      listName: 'action',
      generatorName: 'sysAction',
      baseUrl: '/framework/sysAction'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'initial',
          icon: 'fa fa-bolt fa-fw',
          title: 'sysAction.action.initial',
          groupid: 10,
          confirm: 'sysAction.action.initialConfirm',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'hm,tb,gs',
          appendClass: '',
          needRefresh: true
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,edit,initial,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        note: null,
        value: null,
        params: null,
        method: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListAction-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
