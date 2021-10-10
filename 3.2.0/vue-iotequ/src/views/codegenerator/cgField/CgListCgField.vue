
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgfield .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="entityName" :page="1" :label="$t('cgField.field.entityName')" sortable :sort-method="(a,b)=>chineseSort(a.entityName,b.entityName)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.entityName" type="text" />
          <span v-else>{{ scope.row.entityName }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="name" :page="1" :label="$t('cgField.field.name')" sortable :sort-method="(a,b)=>chineseSort(a.name,b.name)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ scope.row.name }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="1" :label="$t('cgField.field.title')" sortable :sort-method="(a,b)=>chineseSort(a.title,b.title)" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.title" type="text" />
          <span v-else>{{ scope.row.title }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="showType" type="dict" :page="1" :label="$t('cgField.field.showType')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.showType,dictionary.dictShowType,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="type" type="dict" :page="1" :label="$t('cgField.field.type')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.type" automaticDropdown appendToBody :dictionary="dictionary.dictType" allow-create />
          <span v-else>{{ dictValue(scope.row.type,dictionary.dictType,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="length" :page="1" :label="$t('cgField.field.length')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.length" type="text" />
          <span v-else>{{ scope.row.length }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="keyType" type="dict" :page="1" :label="$t('cgField.field.keyType')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.keyType" automaticDropdown appendToBody :dictionary="dictionary.dictKeyType" allow-create />
          <span v-else>{{ dictValue(scope.row.keyType,dictionary.dictKeyType,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="isNull" :page="1" :label="$t('cgField.field.isNull')" align="left" >
        <template slot-scope="scope">
          <el-switch v-if="scope.row.inlineEditting" v-model="scope.row.isNull" />
          <cg-icon v-else :color="scope.row.isNull?'#46a6ff':'grey'" :icon="scope.row.isNull?'fa fa-check-circle':'fa fa-circle-o'"/>
        </template>

      </cg-table-column>
      <cg-table-column prop="defaultValue" :page="1" :label="$t('cgField.field.defaultValue')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.defaultValue" type="text" />
          <span v-else>{{ scope.row.defaultValue }}</span>
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
        <cg-select v-model="queryRecord.searchFields" dictionary="entityName|cgField.field.entityName,name|cgField.field.name,title|cgField.field.title," multiple/>
      </el-form-item>
      <el-divider />
      <div v-show="!queryRecord.search">
        <el-form-item :label="$t('cgField.field.entityName')" prop="entityName" :size="$store.state.app.size">
          <el-input v-model="queryRecord.entityName" type="text" name="entityName"
                    :readonly="fixedQueryRecord.entityName?true:false" :label="$t('cgField.field.entityName')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('cgField.field.name')" prop="name" :size="$store.state.app.size">
          <el-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('cgField.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('cgField.field.title')" prop="title" :size="$store.state.app.size">
          <el-input v-model="queryRecord.title" type="text" name="title"
                    :readonly="fixedQueryRecord.title?true:false" :label="$t('cgField.field.title')" clearable resize autofocus/>
        </el-form-item>
      </div>
    </cg-query-condition>
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgField',
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
      defaultOrder: 'table_id,order_num',
      queryRecordFields: ['entityName','name','title'],
      formPath: '/codegenerator/cgField/record',
      idField: 'id',
      dictionary: {
        dictFkTable: [],
        dictFkColumn: [],
        dictFkOnDelete: this.getDictionary('CASCADE,SET NULL,NO ACTION,RESTRICT','cgField.field.fkOnDelete_0,cgField.field.fkOnDelete_1,cgField.field.fkOnDelete_2,cgField.field.fkOnDelete_3'),
        dictFkOnUpdate: this.getDictionary('CASCADE,SET NULL,NO ACTION,RESTRICT','cgField.field.fkOnUpdate_0,cgField.field.fkOnUpdate_1,cgField.field.fkOnUpdate_2,cgField.field.fkOnUpdate_3'),
        dictShowType: this.getDictionary('text,mltext,textarea,boolean,false_if_null,checkbox,radio,select,inner_join,left_join,right_join,full_join,dict_list,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode'),
        dictType: this.getDictionary('int,varchar,boolean,datetime,tinyint,smallint,mediumint,bigint,double,float,decimal,date,time,timestamp,char,tinytext,text,mediumtext,longtext,tinyblob,blob,mediumblob,longblob,bit,binary,varbinary'),
        dictKeyType: this.getDictionary('0,1,2,3,4,5,11,12,13','cgField.field.keyType_0,cgField.field.keyType_1,cgField.field.keyType_2,cgField.field.keyType_3,cgField.field.keyType_4,cgField.field.keyType_5,cgField.field.keyType_6,cgField.field.keyType_7,cgField.field.keyType_8')
      },
      needLoadDictionary: true,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/codegenerator/cgField/updateSelective')?['entityName', 'name', 'title', 'type', 'length', 'keyType', 'isNull', 'defaultValue']:null,
      listName: 'cgField',
      generatorName: 'cgField',
      baseUrl: '/codegenerator/cgField'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,batdel,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this,'orderNum')
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        fkTable: null,
        fkColumn: null,
        fkOnDelete: null,
        fkOnUpdate: null,
        entityName: null,
        name: null,
        title: null,
        showType: null,
        type: null,
        keyType: null,
        isNull: null,
        defaultValue: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
