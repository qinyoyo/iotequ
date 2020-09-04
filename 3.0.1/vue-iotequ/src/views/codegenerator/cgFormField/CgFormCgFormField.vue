
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgFormField.title.groupCgFormFieldEntityField')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgFormField.field.entityField')" prop="entityField" :size="$store.state.app.size" >
                <el-input v-model="record.entityField" name="entityField" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgFormField.field.entityField')" :placeholder="$t('system.message.needValue')" 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgFormField.field.groupTitle')" prop="groupTitle" :size="$store.state.app.size" >
                <el-input v-model="record.groupTitle" name="groupTitle" 
                          type="text" :maxlength="45" show-word-limit 
                          :label="$t('cgFormField.field.groupTitle')" :placeholder="$t('system.message.unknown')" clearable 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('cgFormField.field.showType')" prop="showType" :size="$store.state.app.size" >
                <cg-select v-model="record.showType" name="showType"
                           :dictionary="dictionary.dictShowType" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgFormField.field.icon')" prop="icon" :size="$store.state.app.size" >
                <el-input v-model="record.icon" name="icon" 
                          type="text" :maxlength="64" show-word-limit 
                          :label="$t('cgFormField.field.icon')" :placeholder="$t('system.message.unknown')" clearable 
                          :readonly="isDetail" resize autofocus validate-event />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgFormField.field.width')" prop="width" :size="$store.state.app.size" >
                <cg-number-input v-model="record.width" name="width" 
                                 :readonly="isDetail" 
                                 :label="$t('cgFormField.field.width')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgFormField.field.orderNum')" prop="orderNum" :size="$store.state.app.size" >
                <cg-number-input v-model="record.orderNum" name="orderNum" 
                                 :readonly="isDetail" 
                                 :label="$t('cgFormField.field.orderNum')" :placeholder="$t('system.message.needValue')" :precision="0" 
                                 />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgFormField.field.hidden')" prop="hidden" :size="$store.state.app.size" >
                <el-switch v-model="record.hidden" name="hidden" :active-text="mobile?'':$t('cgFormField.field.hidden')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgFormField.field.validateAsTitle')" prop="validateAsTitle" :size="$store.state.app.size" >
                <el-switch v-model="record.validateAsTitle" name="validateAsTitle" :active-text="mobile?'':$t('cgFormField.field.validateAsTitle')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgFormField.field.readonly')" prop="readonly" :size="$store.state.app.size" >
                <el-switch v-model="record.readonly" name="readonly" :active-text="mobile?'':$t('cgFormField.field.readonly')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-boolean" :label="$t('cgFormField.field.mustInput')" prop="mustInput" :size="$store.state.app.size" >
                <el-switch v-model="record.mustInput" name="mustInput" :active-text="mobile?'':$t('cgFormField.field.mustInput')" inactive-text="" :disabled="isDetail"  />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgFormField.title.groupCgFormFieldFormItemProperties')">
          <el-form-item class="cg-item-textarea cg-auto-focus" :label="$t('cgFormField.field.formItemProperties')" prop="formItemProperties" :size="$store.state.app.size" >
            <el-input v-model="record.formItemProperties" name="formItemProperties" 
                      type="textarea" 
                      :label="$t('cgFormField.field.formItemProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgFormField.field.itemProperties')" prop="itemProperties" :size="$store.state.app.size" >
            <el-input v-model="record.itemProperties" name="itemProperties" 
                      type="textarea" 
                      :label="$t('cgFormField.field.itemProperties')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgFormField.field.slotTemplates')" prop="slotTemplates" :size="$store.state.app.size" >
            <el-input v-model="record.slotTemplates" name="slotTemplates" 
                      type="textarea" 
                      :label="$t('cgFormField.field.slotTemplates')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgFormField.field.href')" prop="href" :size="$store.state.app.size" >
            <el-input v-model="record.href" name="href" 
                      type="text" :maxlength="64" show-word-limit 
                      :label="$t('cgFormField.field.href')" :placeholder="$t('system.message.unknown')" clearable 
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
const mixinContext = require.context('.', false, /CgFormCgFormField-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgFormField',
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
      title: this.$t('cgFormField.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: false,
      dictionary: {
        dictShowType: this.getDictionary('text,textarea,boolean,false_if_null,password,date,time,datetime,number,email,search,file,image,url,tel,color,html,2dcode')
      },
      tabSelected: '0',
      generatorName: 'cgFormField',
      baseUrl: '/codegenerator/cgFormField'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-cgformfield'
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
      if (this.record.width === null) this.record.width = undefined
      if (this.record.orderNum === null) this.record.orderNum = undefined
    },
    newRecord: function() {
        return {
            formId: '',
            entityField: '',
            width: 24,
            orderNum: 0,
            hidden: false,
            validateAsTitle: false,
            readonly: false,
            mustInput: false,
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
