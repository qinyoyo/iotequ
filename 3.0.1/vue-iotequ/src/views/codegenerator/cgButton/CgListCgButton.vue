
<template>
  <div :class="joinMode?'cg-join-list':'cg-list'">
    <el-backtop v-if="isTableMode()" ref="cgListBacktop" class="el-backtop" target=".cg-list-cgbutton .el-table__body-wrapper" :visibility-height="200">
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
      <cg-table-column prop="action" :page="1" :label="$t('cgButton.field.action')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.action }}
        </template>

      </cg-table-column>
      <cg-table-column prop="title" :page="1" :label="$t('cgButton.field.title')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>

      </cg-table-column>
      <cg-table-column prop="icon" :page="1" :label="$t('cgButton.field.icon')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.icon }}
        </template>

      </cg-table-column>
      <cg-table-column prop="appendClass" :page="1" :label="$t('cgButton.field.appendClass')" align="left" >
        <template slot-scope="scope">
          {{ scope.row.appendClass }}
        </template>

      </cg-table-column>
      <cg-table-column prop="actionProperty" type="dict" :page="1" :label="$t('cgButton.field.actionProperty')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.actionProperty,dictionary.dictActionProperty,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="rowProperty" type="dict" :page="1" :label="$t('cgButton.field.rowProperty')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.rowProperty,dictionary.dictRowProperty,false,true) }}
        </template>

      </cg-table-column>
      <cg-table-column prop="displayProperties" type="dict" :page="1" :label="$t('cgButton.field.displayProperties')" align="left" >
        <template slot-scope="scope">
          {{ dictValue(scope.row.displayProperties,dictionary.dictDisplayProperties,false,true) }}
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
  </div>
</template>

<script>
import {hasAuthority} from '@/utils/cg'
import ParentTable from '@/views/common-views/components/table'
const Comp = {
  name: 'CgListCgButton',
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
      defaultOrder: 'order_num',
      queryRecordFields: [],
      formPath: '/codegenerator/cgButton/record',
      idField: 'id',
      dictionary: {
        dictActionProperty: this.getDictionary('js,go,pg,aj','cgButton.field.actionProperty_0,cgButton.field.actionProperty_1,cgButton.field.actionProperty_2,cgButton.field.actionProperty_3'),
        dictRowProperty: this.getDictionary('sr,mr,nr','cgButton.field.rowProperty_0,cgButton.field.rowProperty_1,cgButton.field.rowProperty_2'),
        dictDisplayProperties: this.getDictionary('hm,hp,tb,rw,ed,gs','cgButton.field.displayProperties_0,cgButton.field.displayProperties_1,cgButton.field.displayProperties_2,cgButton.field.displayProperties_3,cgButton.field.displayProperties_4,cgButton.field.displayProperties_5')
      },
      listName: 'cgButton',
      multipleSelection: true,
      generatorName: 'cgButton',
      baseUrl: '/codegenerator/cgButton'
    }
  },
  computed: {
    allActions() {
      if (this.joinMode) return 'refresh'
      else return ',list,add,view,edit,delete,batdel,import,export,'
    }
  },
  mounted() {
    this.cgList.list_tableInit(this,'orderNum')
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgListCgButton-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
