
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-devpeople .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="userNo" :row-class-name="rowClassName" 
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
      <cg-table-column prop="orgCode" type="dict" :page="paginationCurrentPage" :label="$t('devPeople.field.orgCode')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('devPeople.field.realName')" sortable align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="paginationCurrentPage" :label="$t('devPeople.field.sex')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="birthDate" type="date" :page="paginationCurrentPage" :label="$t('devPeople.field.birthDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.birthDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="idType" type="dict" :page="paginationCurrentPage" :label="$t('devPeople.field.idType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.idType,dictionary.dictIdType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="mobilePhone" :page="paginationCurrentPage" :label="$t('devPeople.field.mobilePhone')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.mobilePhone }}
        </template>

      </cg-table-column>
      <cg-table-column prop="email" :page="paginationCurrentPage" :label="$t('devPeople.field.email')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>

      </cg-table-column>
      <cg-table-column prop="regFingers" :page="paginationCurrentPage" :label="$t('devPeople.field.regFingers')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.regFingers }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="userNo" :multiple="false" :height="tableHeight()" 
                  :isLoading="listLoading" :groupBy="rowRenderGroupTitle?rowRenderGroupTitle:'orgCode'"
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
                  wrapperElement=".cg-list-devpeople .el-table__body-wrapper"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="orgCode|devPeople.field.orgCode,realName|devPeople.field.realName,mobilePhone|devPeople.field.mobilePhone," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('devPeople.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode" show-all-levels/>
        </el-form-item>
        <el-form-item :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('devPeople.field.realName')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('devPeople.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size">
          <el-input v-model="queryRecord.mobilePhone" type="text" name="mobilePhone"
                    :readonly="fixedQueryRecord.mobilePhone?true:false" :label="$t('devPeople.field.mobilePhone')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {localeText} from '@/lang'
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListDevPeople',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'userNo'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'org_code,sex,real_name',
      queryRecordFields: ['orgCode','realName','mobilePhone'],
      formPath: '/reader/devPeople/record',
      idField: 'userNo',
      dictionary: {
        dictFingerType: this.getDictionary('1,2,3,4,5,6,11,12','devPeople.field.fingerType_0,devPeople.field.fingerType_1,devPeople.field.fingerType_2,devPeople.field.fingerType_3,devPeople.field.fingerType_4,devPeople.field.fingerType_5,devPeople.field.fingerType_6,devPeople.field.fingerType_7'),
        dictOrgCode: [],
        dictSex: [],
        dictIdType: [],
        dictUserType: this.getDictionary('1,2','devPeople.field.userType_0,devPeople.field.userType_1'),
        dictRegisterType: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      groupByEntityFields: 'orgCode',
      listName: 'devPeople',
      multipleSelection: true,
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'syncRegFingers',
          icon: 'fa fa-undo',
          title: 'devPeople.action.syncRegFingers',
          groupid: 10,
          confirm: 'devPeople.action.syncRegFingersConfirm',
          rowProperty: 'nr',
          actionProperty: 'aj',
          displayProperties: 'hm,gs,tb',
          appendClass: '',
          needRefresh: true
        },
        {
          action: 'sample',
          icon: 'fa fa-eyedropper',
          title: 'devPeople.action.sample',
          groupid: 10,
          confirm: '',
          rowProperty: 'sr',
          actionProperty: 'go',
          displayProperties: 'tb,ed,rw',
          appendClass: '{url:"/reader/devPeople/sample",openMode:"edit"}',
          needRefresh: false
        },
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,syncRegFingers,sample,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        fingerNo1: null,
        fingerNo2: null,
        fingerType: null,
        userNo: null,
        orgCode: null,
        realName: null,
        sex: null,
        birthDate: null,
        idType: null,
        mobilePhone: null,
        email: null,
        dutyRank: null,
        cardNo: null,
        idNumber: null,
        userType: null,
        registerType: null,
        validDate: null,
        expiredDate: null,
        regTime: null,
        devPassword: null,
        note: null,
        idNation: null,
        photo: null,
        homeAddr: null,
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListDevPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
