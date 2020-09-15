
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-adorg .el-table__body-wrapper" :visibility-height="200">
      <i class="el-icon-caret-top" />
    </el-backtop>
    <el-table ref="cgList" v-if="isTableMode()" v-loading="listLoading" :data="rows" :class="className" row-key="orgCode" :row-class-name="rowClassName" 
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
      <el-table-column prop="name" width="200" :label="$t('sysOrg.field.name')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>

      </el-table-column>
      <cg-table-column prop="shiftId" type="dict" :page="1" :label="$t('adOrg.field.shiftId')" align="left" >
        <template slot-scope="scope">
          <cg-select v-if="scope.row.inlineEditting" v-model="scope.row.shiftId" automaticDropdown appendToBody :dictionary="dictionary.dictShiftId" allow-create numberic />
          <span v-else>{{ dictValue(scope.row.shiftId,dictionary.dictShiftId,false,true) }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="manageLimit" :page="1" :label="$t('adOrg.field.manageLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.manageLimit" type="text" />
          <span v-else>{{ scope.row.manageLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="deviation" :page="1" :label="$t('adOrg.field.deviation')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.deviation" type="text" />
          <span v-else>{{ scope.row.deviation }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="floatLimit" :page="1" :label="$t('adOrg.field.floatLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.floatLimit" type="text" />
          <span v-else>{{ scope.row.floatLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="absentLimit" :page="1" :label="$t('adOrg.field.absentLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.absentLimit" type="text" />
          <span v-else>{{ scope.row.absentLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="freeWorkLimit" :page="1" :label="$t('adOrg.field.freeWorkLimit')" align="right" >
        <template slot-scope="scope">
          <el-input v-if="scope.row.inlineEditting" v-model="scope.row.freeWorkLimit" type="text" />
          <span v-else>{{ scope.row.freeWorkLimit }}</span>
        </template>

      </cg-table-column>
      <cg-table-column prop="managerName" :page="1" :label="$t('adOrg.field.manager')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.managerName }}
        </template>

      </cg-table-column>
      <cg-table-column prop="hrName" :page="1" :label="$t('adOrg.field.hr')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.hrName }}
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
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import rulesObject from './rules.js'
import ParentTable from '@/views/common-views/components/table'
const mixins = [ParentTable]
const mixinContext = require.context('.', false, /CgListAdOrg-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListAdOrg',
  mixins,
  props: {
    selectionKey: {
      type: String,
      default: 'orgCode'
    }
  },
  data() {
    return {
      rulesObject,
      path: 'list',
      defaultOrder: 'org_code',
      queryRecordFields: [],
      formPath: '/attendance/org/adOrg/record',
      localExport: true,
      removeLeftRecordOnRightJoin: true,
      parentField: 'parent',
      idField: 'orgCode',
      dictionary: {
        dictParent: [],
        dictShiftId: [],
        dictManagerOrgCode: []
      },
      needLoadDictionary: true,
      totalEdittingRows: 0,
      editInlineFields: hasAuthority('/attendance/org/adOrg/updateSelective')?['shiftId', 'manageLimit', 'deviation', 'floatLimit', 'absentLimit', 'freeWorkLimit']:null,
      hasSonTables: true,
      listName: 'adOrg',
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,view,edit,import,localExport,delete,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this)
  },
  methods: {
    initialQueryRecord() {
      return Object.assign({
        orgCode: null,
        name: null,
        parent: null,
        shiftId: null,
        managerName: null,
        hrName: null,
        hr: null,
        manager: null,
        managerOrgCode: null,
      }, this.fixedQueryRecord)
    },
  }
}
</script>
