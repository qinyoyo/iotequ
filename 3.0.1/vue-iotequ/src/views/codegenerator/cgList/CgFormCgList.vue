
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgList.title.groupCgListTableId')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('cgList.field.tableId')" prop="tableId" :size="$store.state.app.size" >
                <cg-select v-model="record.tableId" name="tableId"
                           :dictionary="dictionary.dictTableId" :readonly="!isNew" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.name')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" :maxlength="50" show-word-limit 
                          :label="$t('cgList.field.name')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.path')" :title="$t('cgList.field.pathValid')" prop="path" :size="$store.state.app.size" >
                <el-input v-model="record.path" name="path" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgList.field.path')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.icon')" prop="icon" :size="$store.state.app.size" >
                <el-input v-model="record.icon" name="icon" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgList.field.icon')" :placeholder="$t('system.message.unknown')" clearable 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.headTitle')" prop="headTitle" :size="$store.state.app.size" >
                <el-input v-model="record.headTitle" name="headTitle" 
                          type="text" :maxlength="64" show-word-limit 
                          :label="$t('cgList.field.headTitle')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.tagTitle')" prop="tagTitle" :size="$store.state.app.size" >
                <el-input v-model="record.tagTitle" name="tagTitle" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgList.field.tagTitle')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgList.field.sortField')" prop="sortField" :size="$store.state.app.size" >
                <cg-select v-model="record.sortField" name="sortField"
                           :dictionary="dictionary.dictTitleField" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.editInline')" prop="editInline" :size="$store.state.app.size" >
                <el-switch v-model="record.editInline" name="editInline" :active-text="mobile?'':$t('cgList.field.editInline')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.detailInline')" prop="detailInline" :size="$store.state.app.size" >
                <el-switch v-model="record.detailInline" name="detailInline" :active-text="mobile?'':$t('cgList.field.detailInline')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgList.title.groupCgListSons')">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('cgList.field.sons')" :title="$t('cgList.field.sonsValid')" prop="sons" :size="$store.state.app.size" >
            <cg-select v-model="record.sons" name="sons"
                       :filterable="true" :allow-create="true"
                       :dictionary="dictionary.dictSons" :readonly="isDetail" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgList.field.sonFields')" :title="$t('cgList.field.sonFieldsValid')" prop="sonFields" :size="$store.state.app.size" >
            <el-input v-model="record.sonFields" name="sonFields" 
                      type="text" :maxlength="100" show-word-limit 
                      :label="$t('cgList.field.sonFields')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-radio" :label="$t('cgList.field.sonAlign')" prop="sonAlign" :size="$store.state.app.size" >
                <cg-radio v-model="record.sonAlign" name="sonAlign" :dictionary="dictionary.dictSonAlign" :readonly="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgList.field.generatorType')" prop="generatorType" :size="$store.state.app.size" >
                <cg-number-input v-model="record.generatorType" name="generatorType" 
                                 :readonly="isDetail" 
                                 :label="$t('cgList.field.generatorType')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-textarea" :label="$t('cgList.field.sonsProperties')" prop="sonsProperties" :size="$store.state.app.size" >
            <el-input v-model="record.sonsProperties" name="sonsProperties" 
                      type="textarea" :maxlength="500" show-word-limit 
                      :label="$t('cgList.field.sonsProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgList.field.treeShowEntity')" prop="treeShowEntity" :size="$store.state.app.size" >
                <cg-select v-model="record.treeShowEntity" name="treeShowEntity"
                           :dictionary="dictionary.dictTitleField" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgList.field.parentEntity')" prop="parentEntity" :size="$store.state.app.size" >
                <cg-select v-model="record.parentEntity" name="parentEntity"
                           :dictionary="dictionary.dictTitleField" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgList.field.spanEntities')" prop="spanEntities" :size="$store.state.app.size" >
                <cg-select v-model="record.spanEntities" name="spanEntities"
                           :dictionary="dictionary.dictTitleField" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgList.title.groupCgListTableHeight')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-number cg-auto-focus" :label="$t('cgList.field.tableHeight')" :title="$t('cgList.field.tableHeightValid')" prop="tableHeight" :size="$store.state.app.size" >
                <cg-number-input v-model="record.tableHeight" name="tableHeight" 
                                 :readonly="isDetail" 
                                 :label="$t('cgList.field.tableHeight')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('cgList.field.maxHeight')" prop="maxHeight" :size="$store.state.app.size" >
                <cg-number-input v-model="record.maxHeight" name="maxHeight" 
                                 :readonly="isDetail" 
                                 :label="$t('cgList.field.maxHeight')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('cgList.field.titleField')" prop="titleField" :size="$store.state.app.size" >
                <cg-select v-model="record.titleField" name="titleField"
                           :dictionary="dictionary.dictTitleField" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.border')" prop="border" :size="$store.state.app.size" >
                <el-switch v-model="record.border" name="border" :active-text="mobile?'':$t('cgList.field.border')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.stripe')" prop="stripe" :size="$store.state.app.size" >
                <el-switch v-model="record.stripe" name="stripe" :active-text="mobile?'':$t('cgList.field.stripe')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.highlightCurrentRow')" prop="highlightCurrentRow" :size="$store.state.app.size" >
                <el-switch v-model="record.highlightCurrentRow" name="highlightCurrentRow" :active-text="mobile?'':$t('cgList.field.highlightCurrentRow')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.pagination')" prop="pagination" :size="$store.state.app.size" >
                <el-switch v-model="record.pagination" name="pagination" :active-text="mobile?'':$t('cgList.field.pagination')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.showSearch')" prop="showSearch" :size="$store.state.app.size" >
                <el-switch v-model="record.showSearch" name="showSearch" :active-text="mobile?'':$t('cgList.field.showSearch')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-radio" :label="$t('cgList.field.toolbarMode')" prop="toolbarMode" :size="$store.state.app.size" >
                <cg-radio v-model="record.toolbarMode" name="toolbarMode" :dictionary="dictionary.dictToolbarMode" :readonly="isDetail" numberic  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.showSummary')" prop="showSummary" :size="$store.state.app.size" >
                <el-switch v-model="record.showSummary" name="showSummary" :active-text="mobile?'':$t('cgList.field.showSummary')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.showHeader')" prop="showHeader" :size="$store.state.app.size" >
                <el-switch v-model="record.showHeader" name="showHeader" :active-text="mobile?'':$t('cgList.field.showHeader')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.multiple')" prop="multiple" :size="$store.state.app.size" >
                <el-switch v-model="record.multiple" name="multiple" :active-text="mobile?'':$t('cgList.field.multiple')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgList.title.groupCgListImages')">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgList.field.images')" prop="images" :size="$store.state.app.size" >
            <el-input v-model="record.images" name="images" 
                      type="text" :maxlength="200" show-word-limit 
                      :label="$t('cgList.field.images')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-checkbox" :label="$t('cgList.field.actionList')" prop="actionList" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.actionList" name="actionList" :dictionary="dictionary.dictActionList" :readonly="isDetail"  />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgList.field.localExport')" :title="$t('cgList.field.localExportValid')" prop="localExport" :size="$store.state.app.size" >
                <el-switch v-model="record.localExport" name="localExport" :active-text="mobile?'':$t('cgList.field.localExport')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item class="cg-item-text" :label="$t('cgList.field.orderBy')" prop="orderBy" :size="$store.state.app.size" >
                <el-input v-model="record.orderBy" name="orderBy" 
                          type="text" :maxlength="100" show-word-limit 
                          :label="$t('cgList.field.orderBy')" :placeholder="$t('system.message.unknown')" clearable 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-textarea" :label="$t('cgList.field.viewProperties')" prop="viewProperties" :size="$store.state.app.size" >
            <el-input v-model="record.viewProperties" name="viewProperties" 
                      type="textarea" 
                      :label="$t('cgList.field.viewProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgList.field.tableProperties')" prop="tableProperties" :size="$store.state.app.size" >
            <el-input v-model="record.tableProperties" name="tableProperties" 
                      type="textarea" 
                      :label="$t('cgList.field.tableProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button v-if="hasAuthorityOf(myself,baseUrl,'edit',record)" class="cg-button" type="primary" plain :disabled="!recordChanged"
                 icon="el-icon-check" @click.native="submit()">
        {{ $t('system.action.save') }}
      </el-button>
      <el-button v-if="!mobile && showInDialog" class="cg-button" plain icon="el-icon-close"
                 @click.native="$emit('closeDialog')">
        {{ $t('system.action.close') }}
      </el-button>
    </div>
  </div>
</template>
<script>
import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormCgList-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgList',
  mixins,
  props: {
    showInDialog: {
      type: Boolean,
      default: false
    },
    height: {
      type: Number,
      default: 0
    },
    queryById: [Number, String]
  },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('cgList.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: Object.assign({
          tableId: null,
          id: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      needDefaultFromServer: false,
      dictionary: {
        dictTableId: [],
        dictSons: [],
        dictSonAlign: this.getDictionary('v,h','左右排列,上下排列'),
        dictTitleField: [],
        dictToolbarMode: this.getDictionary('1,2','工具条,菜单'),
        dictActionList: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'cgList',
      baseUrl: '/codegenerator/cgList'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    hasMenu() {
      return false
    },
    className() {
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-cglist'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'top'
    },
    isDetail() {
      return this.openMode === 'view'
    },
    isNew() {
      return !this.openMode || this.openMode === 'add'
    },
    isEdit() {
      return this.openMode === 'edit'
    }
  },
  watch: {
    record: {
      handler() {
        this.recordChanged = true
        this.just4elInputNumberNullBug()
      },
      deep: true
    },
    'record.tableId': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'sons,actionList')
      }
    },
    'record.id': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'titleField')
      }
    },
    queryById: {
      handler(n, o) {
        if (n) this.doAction('refresh', {id: n})
      },
      immediate: true
    }
  },
  created() {
    this.rules = rulesObject.getRules(this)
    cgForm.form_getQueryDictionary(this)
    if (this.queryById) {
      cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } else if (this.isNew) cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    else if (this.needLoadDictionary) cgForm.form_getDictionary(this)
    else cgForm.form_getDynaDict(this, 'sons,actionList,titleField')
    this.just4elInputNumberNullBug()
  },
  activated() {
    cgForm.form_activedRefresh(this)
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.$route.query
    },
    just4elInputNumberNullBug: function() {
      if (this.record.generatorType === null) this.record.generatorType = undefined
      if (this.record.tableHeight === null) this.record.tableHeight = undefined
      if (this.record.maxHeight === null) this.record.maxHeight = undefined
    },
    newRecord: function() {
        return {
            tableId: '',
            name: '',
            path: 'list',
            headTitle: '',
            editInline: false,
            detailInline: false,
            sonAlign: 'v',
            generatorType: 0,
            tableHeight: 0,
            maxHeight: 0,
            border: false,
            stripe: true,
            highlightCurrentRow: true,
            pagination: false,
            showSearch: false,
            toolbarMode: 2,
            showSummary: false,
            showHeader: true,
            multiple: false,
            localExport: false,
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
