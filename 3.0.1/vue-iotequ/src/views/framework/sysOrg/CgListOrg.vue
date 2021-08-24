
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-org .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="orgCode" :row-class-name="rowClassName" 
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
      <el-table-column prop="name" width="200" :label="$t('sysOrg.field.name')" sortable align="left" >
        <template slot-scope="scope">
          <cg-input v-if="scope.row.inlineEditting" v-model="scope.row.name" type="text" />
          <span v-else>{{ localeText(scope.row.name) }}</span>
        </template>

      </el-table-column>
      <cg-table-column prop="phone" :page="1" :label="$t('sysOrg.field.phone')" sortable align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.phone" type="text" />
          <span v-else>{{ scope.row.phone }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="fax" :page="1" :label="$t('sysOrg.field.fax')" align="left" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.fax" type="text" />
          <span v-else>{{ scope.row.fax }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="roleList" type="dict" :page="1" :label="$t('sysOrg.field.roleList')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.roleList,dictionary.dictRoleList,false,true) }}
        </template>

      </cg-table-column>
      <cg-action v-model="showActionView" mode="2" :url="baseUrl" :actions="cgList.list_allActions(myself,'main')" @actionClick="doAction" />
    </el-table>
    <cg-card-list v-else ref="cgList" v-loading="listLoading" :render="rowRender" :data="rows" :mainClass="className" row-key="orgCode" :multiple="false" :height="tableHeight()" 
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
        <el-form-item :label="$t('sysOrg.field.name')" prop="name" :size="$store.state.app.size">
          <cg-input v-model="queryRecord.name" type="text" name="name"
                    :readonly="fixedQueryRecord.name?true:false" :label="$t('sysOrg.field.name')" clearable resize autofocus/>
        </el-form-item>
        <el-form-item :label="$t('sysOrg.field.phone')" prop="phone" :size="$store.state.app.size">
          <el-input v-model="queryRecord.phone" type="text" name="phone"
                    :readonly="fixedQueryRecord.phone?true:false" :label="$t('sysOrg.field.phone')" clearable resize autofocus/>
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
  name: 'CgListOrg',
  mixins: [ParentTable],
  props: {
    selectionKey: {
      type: String,
      default: 'orgCode'
    }
  },
  data() {
    return {
      path: 'list',
      defaultOrder: 'org_code',
      queryRecordFields: ['name','phone'],
      formPath: '/framework/sysOrg/record',
      parentField: 'parent',
      idField: 'orgCode',
      dictionary: {
        dictParent: [],
        dictRoleList: []
      },
      needLoadDictionary: true,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/framework/sysOrg/updateSelective')?['name', 'phone', 'fax']:null,
      hasSonTables: true,
      listName: 'org',
      generatorName: 'sysOrg',
      baseUrl: '/framework/sysOrg'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh,query'
      else return 'query,,list,add,view,edit,delete,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        name: null,
        parent: null,
        phone: null,
        fax: null,
        roleList: null,
      }, this.fixedQueryRecord)
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListOrg-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
