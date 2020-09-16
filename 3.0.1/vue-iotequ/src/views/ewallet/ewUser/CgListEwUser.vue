
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-ewuser .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="userNo" :row-class-name="rowClassName" 
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
      <cg-table-column prop="userNo" :page="paginationCurrentPage" :label="$t('ewUser.field.userNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.userNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="isActive" :page="paginationCurrentPage" :label="$t('ewUser.field.isActive')" sortable align="center" >
        <template slot-scope="scope">
          <cg-icon :color="scope.row.isActive?'#46a6ff':'grey'" :icon="scope.row.isActive?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="paginationCurrentPage" :label="$t('ewUser.field.name')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </cg-table-column>
      <cg-table-column prop="gender" type="dict" :page="paginationCurrentPage" :label="$t('ewUser.field.gender')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.gender,dictionary.dictGender,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="idType" type="dict" :page="paginationCurrentPage" :label="$t('ewUser.field.idType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.idType,dictionary.dictIdType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="idNo" :page="paginationCurrentPage" :label="$t('ewUser.field.idNo')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.idNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="mobilePhone" :page="paginationCurrentPage" :label="$t('ewUser.field.mobilePhone')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.mobilePhone }}
        </template>

      </cg-table-column>
      <cg-table-column prop="email" :page="paginationCurrentPage" :label="$t('ewUser.field.email')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>

      </cg-table-column>
      <cg-table-column prop="wechatOpenid" :page="paginationCurrentPage" :label="$t('ewUser.field.wechatOpenid')" align="center" >
        <template slot-scope="scope">
          <cg-icon color="#46a6ff" :icon="scope.row.wechatOpenid?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="birthDate" type="date" :page="paginationCurrentPage" :label="$t('ewUser.field.birthDate')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.birthDate,'YYYY-MM-DD') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="memberGroup" :page="paginationCurrentPage" :label="$t('ewUser.field.memberGroup')" sortable align="left" >
        <template slot-scope="scope">
          {{ scope.row.memberGroup }}
        </template>

      </cg-table-column>
      <cg-table-column prop="bonusPoint" :page="paginationCurrentPage" :label="$t('ewUser.field.bonusPoint')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.bonusPoint }}
        </template>

      </cg-table-column>
      <cg-table-column prop="amountMoney" :page="paginationCurrentPage" :label="$t('ewUser.field.amountMoney')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.amountMoney }}
        </template>

      </cg-table-column>
      <cg-table-column prop="costLimit" :page="paginationCurrentPage" :label="$t('ewUser.field.costLimit')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.costLimit }}
        </template>

      </cg-table-column>
      <cg-table-column prop="dayLimit" :page="paginationCurrentPage" :label="$t('ewUser.field.dayLimit')" align="right" >
        <template slot-scope="scope">
          {{ scope.row.dayLimit }}
        </template>

      </cg-table-column>
      <cg-table-column prop="activeSince" type="datetime" :page="paginationCurrentPage" :label="$t('ewUser.field.activeSince')" sortable align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.activeSince,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="expireAt" type="datetime" :page="paginationCurrentPage" :label="$t('ewUser.field.expireAt')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.expireAt,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="userNo" :multiple="false" :height="tableHeight()" 
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
                  wrapperElement=".cg-list-ewuser .el-table__body-wrapper"
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
    <cg-query-condition v-model="showQuery" :modal="!joinMode" :queryRecord="queryRecord"
                        @refresh="doAction('refresh')" @reset="queryRecord=initialQueryRecord()">
      <el-form-item :label="$t('system.action.fuzzyQuery')" prop="search" :size="$store.state.app.size">
        <el-input v-model="queryRecord.search" type="text" name="search" clearable resize autofocus
                  prefix-icon="el-icon-search" :placeholder="$t('system.message.fuzzyQueryTip')" @keyup.enter.native="doAction('refresh')" />
      </el-form-item>
      <el-form-item v-show="queryRecord.search" :label="$t('system.action.field')" prop="searchFields" :size="$store.state.app.size">
        <cg-select v-model="queryRecord.searchFields" dictionary="isActive|ewUser.field.isActive,name|ewUser.field.name,idNo|ewUser.field.idNo,mobilePhone|ewUser.field.mobilePhone,email|ewUser.field.email,memberGroup|ewUser.field.memberGroup,activeSince|ewUser.field.activeSince," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('ewUser.field.isActive')" prop="isActive" :size="$store.state.app.size">
          <el-checkbox-group v-model="queryRecord.isActive" :max="1">
            <el-checkbox name="isActive" :label="true" :disabled="fixedQueryRecord.isActive?true:false">{{ $t('system.action.yes') }}</el-checkbox>
            <el-checkbox name="isActive" :label="false" :disabled="fixedQueryRecord.isActive?true:false">{{ $t('system.action.no') }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('ewUser.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.idNo')" prop="idNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.idNo" type="text" name="idNo"
                    :readonly="fixedQueryRecord.idNo?true:false" :label="$t('ewUser.field.idNo')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size">
          <el-input v-model="queryRecord.mobilePhone" type="text" name="mobilePhone"
                    :readonly="fixedQueryRecord.mobilePhone?true:false" :label="$t('ewUser.field.mobilePhone')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.email')" prop="email" :size="$store.state.app.size">
          <el-input v-model="queryRecord.email" type="text" name="email"
                    :readonly="fixedQueryRecord.email?true:false" :label="$t('ewUser.field.email')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.memberGroup')" prop="memberGroup" :size="$store.state.app.size">
          <el-input v-model="queryRecord.memberGroup" type="text" name="memberGroup"
                    :readonly="fixedQueryRecord.memberGroup?true:false" :label="$t('ewUser.field.memberGroup')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('ewUser.field.activeSince')" prop="activeSince" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.activeSince" :title="$t('ewUser.field.activeSince')" name="activeSince" :align="mobile?'right':'center'" type="datetimerange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.activeSince?true:false"  clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListEwUser',
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
      defaultOrder: 'user_no',
      queryRecordFields: ['isActive','name','idNo','mobilePhone','email','memberGroup','activeSince'],
      formPath: '/ewallet/ewUser/record',
      idField: 'userNo',
      dictionary: {
        dictGender: [],
        dictIdType: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      hasSonTables: true,
      listName: 'ewUser',
      multipleSelection: true,
      generatorName: 'ewUser',
      baseUrl: '/ewallet/ewUser'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        userNo: null,
        isActive: [],
        name: null,
        gender: null,
        idType: null,
        idNo: null,
        mobilePhone: null,
        email: null,
        wechatOpenid: [],
        birthDate: null,
        memberGroup: null,
        activeSince: null,
        expireAt: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListEwUser-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
