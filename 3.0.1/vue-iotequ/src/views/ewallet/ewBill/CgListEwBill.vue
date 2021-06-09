
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-ewbill .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="no" :row-class-name="rowClassName" 
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
      <cg-table-column prop="no" :page="paginationCurrentPage" :label="$t('ewBill.field.no')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.no }}
        </template>

      </cg-table-column>
      <cg-table-column prop="canceled" :page="paginationCurrentPage" :label="$t('ewBill.field.canceled')" align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.canceled?'#46a6ff':'grey'" :icon="scope.row.canceled?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="isCharge" :page="paginationCurrentPage" :label="$t('ewBill.field.isCharge')" sortable align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.isCharge?'#46a6ff':'grey'" :icon="scope.row.isCharge?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="userNo" :page="paginationCurrentPage" :label="$t('ewBill.field.userNo')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.userNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="batchNo" :page="paginationCurrentPage" :label="$t('ewBill.field.batchNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.batchNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="dt" type="datetime" :page="paginationCurrentPage" :label="$t('ewBill.field.dt')" sortable align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.dt,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="costType" type="dict" :page="paginationCurrentPage" :label="$t('ewBill.field.costType')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.costType,dictionary.dictCostType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="projectName" :page="paginationCurrentPage" :label="$t('ewBill.field.projectName')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.projectName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="projectPrice" :page="paginationCurrentPage" :label="$t('ewBill.field.projectPrice')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.projectPrice }}
        </template>

      </cg-table-column>
      <cg-table-column prop="amount" :page="paginationCurrentPage" :label="$t('ewBill.field.amount')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.amount }}
        </template>

      </cg-table-column>
      <cg-table-column prop="deviceNo" :page="paginationCurrentPage" :label="$t('ewBill.field.deviceNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.deviceNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="shopId" :page="paginationCurrentPage" :label="$t('ewBill.field.shopId')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.shopId }}
        </template>

      </cg-table-column>
      <cg-table-column prop="deviceDt" type="datetime" :page="paginationCurrentPage" :label="$t('ewBill.field.deviceDt')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.deviceDt,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="tradeNo" :page="paginationCurrentPage" :label="$t('ewBill.field.tradeNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.tradeNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="operatorNo" :page="paginationCurrentPage" :label="$t('ewBill.field.operatorNo')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.operatorNo }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="no" :multiple="false" :height="tableHeight()" 
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
                  wrapperElement=".cg-list-ewbill .el-table__body-wrapper"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="no|ewBill.field.no,isCharge|ewBill.field.isCharge,userNo|ewBill.field.userNo,dt|ewBill.field.dt,costType|ewBill.field.costType,shopId|ewBill.field.shopId,operatorNo|ewBill.field.operatorNo," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('ewBill.field.no')" prop="no" :size="$store.state.app.size">
          <el-input v-model="queryRecord.no" type="text" name="no"
                    :readonly="fixedQueryRecord.no?true:false" :label="$t('ewBill.field.no')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.isCharge')" prop="isCharge" :size="$store.state.app.size">
          <el-checkbox-group v-model="queryRecord.isCharge" :max="1">
            <el-checkbox name="isCharge" :label="true" :disabled="fixedQueryRecord.isCharge?true:false">{{ $t('system.action.yes') }}</el-checkbox>
            <el-checkbox name="isCharge" :label="false" :disabled="fixedQueryRecord.isCharge?true:false">{{ $t('system.action.no') }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.userNo')" prop="userNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.userNo" type="text" name="userNo"
                    :readonly="fixedQueryRecord.userNo?true:false" :label="$t('ewBill.field.userNo')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.dt')" prop="dt" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.dt" :title="$t('ewBill.field.dt')" name="dt" :align="mobile?'right':'center'" type="datetimerange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.dt?true:false"  clearable />
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.costType')" prop="costType" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.costType" :dictionary="dictionary.dictCostType"
                     :disabled="fixedQueryRecord.costType?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.shopId')" prop="shopId" :size="$store.state.app.size">
          <el-input v-model="queryRecord.shopId" type="text" name="shopId"
                    :readonly="fixedQueryRecord.shopId?true:false" :label="$t('ewBill.field.shopId')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewBill.field.operatorNo')" prop="operatorNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.operatorNo" type="text" name="operatorNo"
                    :readonly="fixedQueryRecord.operatorNo?true:false" :label="$t('ewBill.field.operatorNo')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListEwBill',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'no'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'no desc',
      queryRecordFields: ['no','isCharge','userNo','dt','costType','shopId','operatorNo'],
      formPath: '/ewallet/ewBill/record',
      idField: 'no',
      dictionary: {
        dictCostType: this.getDictionary('1,2,3','ewBill.field.costType_0,ewBill.field.costType_1,ewBill.field.costType_2')
      },
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      listName: 'ewBill',
      generatorName: 'ewBill',
      baseUrl: '/ewallet/ewBill'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,view,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        no: null,
        canceled: null,
        isCharge: [],
        userNo: null,
        batchNo: null,
        dt: null,
        costType: null,
        projectName: null,
        deviceNo: null,
        shopId: null,
        deviceDt: null,
        tradeNo: null,
        operatorNo: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListEwBill-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
