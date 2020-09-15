
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-user .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="orgCode" type="dict" :page="paginationCurrentPage" v-if="!fixedQueryRecord.orgCode" :label="$t('sysUser.field.orgCode')" sortable align="left" >
        <template slot-scope="scope">
          <cg-cascader v-if="scope.row.inlineEditting" v-model="scope.row.orgCode" :dictionary="dictionary.dictOrgCode" numberic show-all-levels />
          <span v-else>{{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="icon" :page="paginationCurrentPage" :label="$t('sysUser.field.icon')" align="left" >
        <template slot-scope="scope">
          <el-avatar v-if="scope.row.icon" shape="square" :size="50" fit="scale-down" :src="apiUrl('/res/getUploadImage?name='+scope.row.icon+(scope.row.icon.indexOf('/')>=0 || scope.row.icon.indexOf('\\')>=0 ? '' : '&id='+scope.row.id+'&path=sysUser&field=icon'))"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="paginationCurrentPage" :label="$t('sysUser.field.name')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ scope.row.name }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="paginationCurrentPage" :label="$t('sysUser.field.realName')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.realName" type="text" />
          <span v-else>{{ scope.row.realName }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="paginationCurrentPage" :label="$t('sysUser.field.sex')" sortable align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.sex" automaticDropdown appendToBody :dictionary="dictionary.dictSex" />
          <span v-else>{{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="mobilePhone" :page="paginationCurrentPage" :label="$t('sysUser.field.mobilePhone')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.mobilePhone" type="text" />
          <span v-else>{{ scope.row.mobilePhone }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="email" :page="paginationCurrentPage" :label="$t('sysUser.field.email')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.email" type="text" />
          <span v-else>{{ scope.row.email }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="idType" type="dict" :page="paginationCurrentPage" :label="$t('sysUser.field.idType')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.idType,dictionary.dictIdType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="birthDate" type="date" :page="paginationCurrentPage" :label="$t('sysUser.field.birthDate')" sortable align="center" >
        <template slot-scope="scope">
          <cg-date-picker v-if="scope.row.inlineEditting" v-model="scope.row.birthDate" :align="mobile?'right':'center'" type="date" :title="$t('sysUser.field.birthDate')"  clearable/>
          <span v-else>{{ time2String(scope.row.birthDate,'YYYY-MM-DD') }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="roleList" type="dict" :page="paginationCurrentPage" :label="$t('sysUser.field.roleList')" sortable align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.roleList,dictionary.dictRoleList,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="regTime" type="datetime" :page="paginationCurrentPage" :label="$t('sysUser.field.regTime')" align="center" >
        <template slot-scope="scope">
          {{ time2String(scope.row.regTime,'YYYY-MM-DD HH:mm') }}
        </template>

      </cg-table-column>
      <cg-table-column prop="wechatOpenid" :page="paginationCurrentPage" :label="$t('sysUser.field.wechatOpenid')" align="center" >
        <template slot-scope="scope">
          <cg-icon color="#46a6ff" :icon="scope.row.wechatOpenid?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="id" :multiple="false" :height="tableHeight()" 
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
                  wrapperElement=".cg-list-user .el-table__body-wrapper"
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
        <cg-select v-model="queryRecord.searchFields" dictionary="orgCode|sysUser.field.orgCode,name|sysUser.field.name,realName|sysUser.field.realName,sex|sysUser.field.sex,mobilePhone|sysUser.field.mobilePhone,idType|sysUser.field.idType,birthDate|sysUser.field.birthDate,roleList|sysUser.field.roleList," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('sysUser.field.orgCode')" prop="orgCode" :size="$store.state.app.size">
          <cg-cascader v-model="queryRecord.orgCode" name="orgCode"  multiple collapse-tags clearable
                       :disabled="fixedQueryRecord.orgCode?true:false" :dictionary="dictionary.dictOrgCode" show-all-levels/>
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('sysUser.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.realName')" prop="realName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.realName" type="text" name="realName"
                    :readonly="fixedQueryRecord.realName?true:false" :label="$t('sysUser.field.realName')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.sex')" prop="sex" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.sex" :dictionary="dictionary.dictSex"
                     :disabled="fixedQueryRecord.sex?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.mobilePhone')" prop="mobilePhone" :size="$store.state.app.size">
          <el-input v-model="queryRecord.mobilePhone" type="text" name="mobilePhone"
                    :readonly="fixedQueryRecord.mobilePhone?true:false" :label="$t('sysUser.field.mobilePhone')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.idType')" prop="idType" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.idType" :dictionary="dictionary.dictIdType"
                     :disabled="fixedQueryRecord.idType?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.birthDate')" prop="birthDate" :size="$store.state.app.size">
          <cg-date-picker v-model="queryRecord.birthDate" :title="$t('sysUser.field.birthDate')" name="birthDate" :align="mobile?'right':'center'" type="daterange" :picker-options="datePickerOptions()"
                          :readonly="fixedQueryRecord.birthDate?true:false"  clearable />
        </el-form-item>
        <el-form-item :label="$t('sysUser.field.roleList')" prop="roleList" :size="$store.state.app.size">
          <cg-select v-model="queryRecord.roleList" :dictionary="dictionary.dictRoleList"
                     :disabled="fixedQueryRecord.roleList?true:false"  :allow-create="!mobile" multiple clearable />
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const mixins = [ParentTable]
const mixinContext = require.context('.', false, /CgListUser-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListUser',
  mixins,
  props: {
    selectionKey: {
      type: String,
      default: 'id'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'org_code,real_name',
      queryRecordFields: ['orgCode','name','realName','sex','mobilePhone','idType','birthDate','roleList'],
      formPath: '/framework/sysUser/record',
      idField: 'id',
      dictionary: {
        dictOrgPrivilege: [],
        dictOrgCode: [],
        dictSex: [],
        dictIdType: [],
        dictRoleList: []
      },
      needLoadDictionary: true,
      paginationCurrentPage: 1,
      paginationPageSize: this.$store.state.app.device === 'mobile' ? 10 : 30,
      paginationTotalRecords: 0,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/framework/sysUser/updateSelective')?['orgCode', 'name', 'realName', 'sex', 'mobilePhone', 'email', 'birthDate']:null,
      groupByEntityFields: 'orgCode',
      listName: 'user',
      multipleSelection: true,
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'resetPassword',
          icon: 'fa fa-key',
          title: 'sysUser.action.resetPassword',
          groupid: 10,
          confirm: 'sysUser.action.resetPasswordConfirm',
          rowProperty: 'sr',
          actionProperty: 'aj',
          displayProperties: 'rw,tb',
          appendClass: '',
          needRefresh: false
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,resetPassword,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        orgPrivilege: null,
        orgCode: null,
        icon: null,
        name: null,
        realName: null,
        sex: null,
        mobilePhone: null,
        email: null,
        idType: null,
        birthDate: null,
        roleList: null,
        regTime: null,
        wechatOpenid: [],
      }, this.fixedQueryRecord)
    },
    groupFields({ row, column, rowIndex, columnIndex }) {
      return this.cgList.list_groupFields(this, this.groupByEntityFields.split(','), row, column, rowIndex, columnIndex)
    },
  }
}
</script>
