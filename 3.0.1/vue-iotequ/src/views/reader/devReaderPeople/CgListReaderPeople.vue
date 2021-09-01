
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-readerpeople .el-table__body-wrapper" :visibility-height="200">
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
              @sort-change="(options)=>cgList.list_sortChange(myself, options)" 
    >
      <cg-icon slot="empty" icon="el-icon-minus" color="grey" />
      <el-table-column v-if="!mobile" type="index" width="50" align="center" class-name="drag-filter" label-class-name="pointer-cursor" header-align="center">
        <i slot="header" class="el-icon-menu"/>
      </el-table-column>
      <el-table-column v-if="multiple" type="selection" align="center" reserve-selection class-name="drag-filter" width="36" />
      <cg-table-column prop="userNo" :page="1" :label="$t('devReaderPeople.field.userNo')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.userNo }}
        </template>

      </cg-table-column>
      <cg-table-column prop="orgCode" type="dict" :page="1" :label="$t('devPeople.field.orgCode')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.orgCode,dictionary.dictOrgCode,true,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="realName" :page="1" :label="$t('devPeople.field.realName')" align="left" >
        <template slot-scope="scope">
          {{ localeText(scope.row.realName) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="sex" type="dict" :page="1" :label="$t('devPeople.field.sex')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.sex,dictionary.dictSex,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="mobilePhone" :page="1" :label="$t('devPeople.field.mobilePhone')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.mobilePhone }}
        </template>

      </cg-table-column>
      <cg-table-column prop="email" :page="1" :label="$t('devPeople.field.email')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>

      </cg-table-column>
      <cg-table-column prop="regFingers" :page="1" :label="$t('devPeople.field.regFingers')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.regFingers }}
        </template>

      </cg-table-column>
      <cg-table-column prop="status" type="dict" :page="1" :label="$t('devReaderPeople.field.status')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.status,dictionary.dictStatus,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="type" type="dict" :page="1" :label="$t('devReaderPeople.field.type')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.type,dictionary.dictType,false,true) }}
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
      <div>
        <el-form-item :label="$t('devReaderPeople.field.readerNo')" prop="readerNo" :size="$store.state.app.size">
          <el-input v-model="queryRecord.readerNo" type="text" name="readerNo"
                    :readonly="fixedQueryRecord.readerNo?true:false" :label="$t('devReaderPeople.field.readerNo')" clearable resize autofocus/>
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
  name: 'CgListReaderPeople',
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
      defaultOrder: 'id desc',
      queryRecordFields: ['readerNo'],
      formPath: '/reader/devReaderPeople/record',
      idField: 'id',
      dictionary: {
        dictIdType: [],
        dictRegisterType: [],
        dictUserType: this.getDictionary('1,2','devReaderPeople.field.userType_0,devReaderPeople.field.userType_1'),
        dictOrgCode: [],
        dictSex: [],
        dictStatus: this.getDictionary('0,1','devReaderPeople.field.status_0,devReaderPeople.field.status_1'),
        dictType: this.getDictionary('0,1,2','devReaderPeople.field.type_0,devReaderPeople.field.type_1,devReaderPeople.field.type_2')
      },
      needLoadDictionary: true,
      listName: 'readerPeople',
      multipleSelection: true,
      generatorName: 'devReaderPeople',
      baseUrl: '/reader/devReaderPeople'
    }
  },
  computed: {
    additionalActions() {
      if (this.joinMode) return []
      else return [
        {
          action: 'copyDelete',
          icon: 'fa fa-trash-o',
          title: 'devReaderPeople.action.copyDelete',
          groupid: 10,
          confirm: '',
          rowProperty: 'mr',
          actionProperty: 'aj',
          displayProperties: 'tb',
          appendClass: '',
          needRefresh: true
        },
        {
          action: 'download',
          icon: 'fa fa-sort-amount-asc',
          title: 'devReaderPeople.action.download',
          groupid: 10,
          confirm: '',
          rowProperty: 'nr',
          actionProperty: 'pg',
          displayProperties: 'tb',
          appendClass: 'this.download()',
          needRefresh: true
        }
      ]
    },
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,edit,download,copyDelete,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        idType: null,
        registerType: null,
        userType: null,
        userNo: null,
        readerNo: null,
        orgCode: null,
        realName: null,
        sex: null,
        mobilePhone: null,
        email: null,
        status: null,
        type: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListReaderPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
