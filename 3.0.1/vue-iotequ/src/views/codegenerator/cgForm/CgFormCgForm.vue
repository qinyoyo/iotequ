
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgForm.title.groupCgFormTableId')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select cg-auto-focus" :label="$t('cgForm.field.tableId')" prop="tableId" :size="$store.state.app.size" >
                <cg-select v-model="record.tableId" name="tableId"
                           :dictionary="dictionary.dictTableId" :readonly="!isNew" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.name')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgForm.field.name')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.isFlow')" prop="isFlow" :size="$store.state.app.size" >
                <el-switch v-model="record.isFlow" name="isFlow" :active-text="mobile?'':$t('cgForm.field.isFlow')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.isDialog')" prop="isDialog" :size="$store.state.app.size" >
                <el-switch v-model="record.isDialog" name="isDialog" :active-text="mobile?'':$t('cgForm.field.isDialog')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-boolean" :label="$t('cgForm.field.continueAdd')" prop="continueAdd" :size="$store.state.app.size" >
                <el-switch v-model="record.continueAdd" name="continueAdd" :active-text="mobile?'':$t('cgForm.field.continueAdd')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.path')" :title="$t('cgForm.field.pathValid')" prop="path" :size="$store.state.app.size" >
            <el-input v-model="record.path" name="path" 
                      type="text" :maxlength="200" show-word-limit 
                      :label="$t('cgForm.field.path')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.icon')" :title="$t('cgForm.field.iconValid')" prop="icon" :size="$store.state.app.size" >
            <el-input v-model="record.icon" name="icon" 
                      type="text" :maxlength="300" show-word-limit 
                      :label="$t('cgForm.field.icon')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.headTitle')" :title="$t('cgForm.field.headTitleValid')" prop="headTitle" :size="$store.state.app.size" >
            <el-input v-model="record.headTitle" name="headTitle" 
                      type="text" :maxlength="400" show-word-limit 
                      :label="$t('cgForm.field.headTitle')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.tagTitle')" prop="tagTitle" :size="$store.state.app.size" >
                <el-input v-model="record.tagTitle" name="tagTitle" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgForm.field.tagTitle')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgForm.field.labelPosition')" prop="labelPosition" :size="$store.state.app.size" >
                <el-input v-model="record.labelPosition" name="labelPosition" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgForm.field.labelPosition')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgForm.title.groupCgFormImages')">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgForm.field.images')" prop="images" :size="$store.state.app.size" >
            <el-input v-model="record.images" name="images" 
                      type="text" :maxlength="200" show-word-limit 
                      :label="$t('cgForm.field.images')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-checkbox" :label="$t('cgForm.field.actionList')" prop="actionList" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.actionList" name="actionList" :dictionary="dictionary.dictActionList" :readonly="isDetail"  />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgForm.field.formProperties')" prop="formProperties" :size="$store.state.app.size" >
            <el-input v-model="record.formProperties" name="formProperties" 
                      type="textarea" 
                      :label="$t('cgForm.field.formProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgForm.field.viewProperties')" prop="viewProperties" :size="$store.state.app.size" >
            <el-input v-model="record.viewProperties" name="viewProperties" 
                      type="textarea" 
                      :label="$t('cgForm.field.viewProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgForm.field.slotTemplates')" prop="slotTemplates" :size="$store.state.app.size" >
            <el-input v-model="record.slotTemplates" name="slotTemplates" 
                      type="text" 
                      :label="$t('cgForm.field.slotTemplates')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize autofocus validate-event />
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
const mixinContext = require.context('.', false, /CgFormCgForm-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgForm',
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
      title: this.$t('cgForm.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: Object.assign({
          tableId: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      needDefaultFromServer: false,
      dictionary: {
        dictTableId: [],
        dictActionList: []
      },
      needLoadDictionary: true,
      tabSelected: '0',
      generatorName: 'cgForm',
      baseUrl: '/codegenerator/cgForm'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-cgform'
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
      },
      deep: true
    },
    'record.tableId': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'actionList')
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
    else cgForm.form_getDynaDict(this, 'actionList')
  },
  activated() {
    cgForm.form_activedRefresh(this)
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.$route.query
    },
    newRecord: function() {
        return {
            tableId: '',
            name: '',
            isFlow: false,
            isDialog: false,
            continueAdd: true,
            path: 'record',
            headTitle: '',
            tagTitle: '',
            labelPosition: 'top',
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
