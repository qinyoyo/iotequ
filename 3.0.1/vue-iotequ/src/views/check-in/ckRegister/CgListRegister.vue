
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-register .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="id" :row-class-name="rowClassName" 
              style="width: 100%" :height="tableHeight()" :size="$store.state.app.size" 
              stripe highlight-current-row fit :span-method="groupFields" 
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
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="orgName" :page="paginationCurrentPage" :label="$t('ckRegister.field.orgName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.orgName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="paginationCurrentPage" :label="$t('ckRegister.field.name')" sortable :sort-method="(a,b)=>chineseSort(a.name,b.name)" align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="paginationCurrentPage" :label="$t('ckRegister.field.sex')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="birthDate" type="date" :page="paginationCurrentPage" :label="$t('ckRegister.field.birthDate')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.birthDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="inDate" type="date" :page="paginationCurrentPage" :label="$t('ckRegister.field.inDate')" sortable align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.inDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="onTime" type="time" :page="paginationCurrentPage" :label="$t('ckRegister.field.onTime')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.onTime,'HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="offTime" type="time" :page="paginationCurrentPage" :label="$t('ckRegister.field.offTime')" align="left" >
        <template slot-scope="scope">
          {{ time2String(scope.row.offTime,'HH:mm') }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'orgName'"
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
                  wrapperElement=".cg-list-register .el-table__body-wrapper"
                  :isLoading="listLoading"
                  :isUnMore="paginationCurrentPage * paginationPageSize >= paginationTotalRecords"
                  type="vertical"
                  :pulldownTxt="$t('system.message.pullDownRefresh')"
                  :loadMoreTxt="$t('system.message.pullUpLoad')"
                  :unloadMoreTxt="$t('system.message.noMoreData')"
                  @loadMore="cgList.list_loadMore(myself)"
                  @pulldown="doAction('refresh',{ isPullDownEvent : true})"
    />
    <el-pagination v-if="!mobile" hide-on-single-page @size-change="doAction('refresh')" @current-change="doAction('refresh')" :page-sizes="pageSizeSelections" layout="total, sizes, prev, pager, next, jumper"
      :current-page.sync="paginationCurrentPage" :page-size.sync="paginationPageSize" :total="paginationTotalRecords">
    </el-pagination>
    <cg-context-menu :show="contextMenu.visible" :actions="contextMenu.actions"
                     :top="contextMenu.top" :left="contextMenu.left" 
                     @hide="contextMenu.visible = false" @select="(a)=>doAction(a, {row: contextMenu.row, trElement: contextMenu.trElement})"
    />
    <cg-query-condition v-model="showQuery" ref="query" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <div>
        <el-form-item :label="$t('ckRegister.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.orgCode" :dictionary="dictionary.dictOrgCode"
                     :disabled="fixedQueryRecord.orgCode?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('ckRegister.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('ckRegister.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ckRegister.field.inDate')" prop="inDate" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.inDate" :title="$t('ckRegister.field.inDate')" name="inDate" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.inDate?true:false"  clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListRegister',
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
      defaultOrder: 'in_date desc, on_time desc',
      queryRecordFields: ['orgCode','name','inDate'],
      formPath: '/check-in/ckRegister/record',
      localExport: true,
      idField: 'id',
      dictionary: {
        dictOrgCode: [],
        dictSex: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      groupByEntityFields: 'orgName',
      listName: 'register',
      generatorName: 'ckRegister',
      baseUrl: '/check-in/ckRegister'
    }
  },
  computed: {
    pageSizeSelections() {
      if (this.localExport) {
        let total = this.paginationTotalRecords
        if (total<=10) return [10]
        else if (total<=20)  return [10,20]
        else if (total<=30)  return [10,20,30]
        else if (total<=50)  return [10,20,30,50]
        else if (total<=100) return [10,20,30,50,100]
        else  return [10,20,30,50,100,total]
      } else return [10, 20, 30, 50, 100, 200]
    },
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
      this.paginationPageSize = (this.$store.state.app.device === 'mobile' ? 10 : 30)
      return Object.assign({
        orgCode: null,
        orgName: null,
        name: null,
        sex: null,
        birthDate: null,
        inDate: null,
        onTime: null,
        offTime: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListRegister-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
