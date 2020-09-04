
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('adAdjust.field.adjustType')" prop="adjustType" :size="$store.state.app.size" >
            <cg-select v-model="record.adjustType" name="adjustType"
                       :dictionary="dictionary.dictAdjustType" :readonly="!isNew" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('adAdjust.field.registerTime')" prop="registerTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('adAdjust.field.registerTime')}}</span>
            <cg-date-picker v-model="record.registerTime" name="registerTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('adAdjust.field.registerTime')" readonly 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item :label="timeLabel('start')" class="cg-item-datetime" prop="startTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{timeLabel('start')}}</span>
            <cg-date-picker v-model="record.startTime" name="startTime" 
                            :minValue="dateAdd(new Date(),-7,'day')" :title="timeLabel('start')"
                            :align="mobile?'right':'center'" type="datetime" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="timeLabel('end')" class="cg-item-datetime" prop="endTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{timeLabel('end')}}</span>
            <cg-date-picker v-model="record.endTime" name="endTime" 
                            :minValue="record.startTime" :title="timeLabel('end')"
                            :align="mobile?'right':'center'" type="datetime" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-textarea" :label="$t('adAdjust.field.description')" prop="description" :size="$store.state.app.size" >
        <el-input v-model="record.description" name="description" 
                  type="textarea" :maxlength="500" show-word-limit 
                  :label="$t('adAdjust.field.description')" :placeholder="$t('system.message.needValue')" 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
      </el-form-item>
      <el-form-item class="cg-item-file" :label="$t('adAdjust.field.addFile')" prop="addFile" :size="$store.state.app.size" >
        <cg-file v-model="blobRecord.addFile" name="addFile" :id="record.id" :baseUrl="baseUrl" field="addFile" 
                 accept=".pdf,.doc,.docx,.txt,image/*" multiple :readonly="isDetail" 
                 @input="recordChanged=true"  />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('system.action.flowNextOperator')" prop="flowNextOperator" :size="$store.state.app.size" >
            <cg-select v-model="record.flowNextOperator" name="flowNextOperator"
                       :dictionary="dictionary.dictFlowNextOperator" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item class="cg-item-select" :label="$t('system.action.flowCopyToList')" prop="flowCopyToList" :size="$store.state.app.size" >
            <cg-select v-model="record.flowCopyToList" name="flowCopyToList"
                       :dictionary="dictionary.dictFlowCopyToList" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
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
const mixinContext = require.context('.', false, /CgFormAdjustRecord-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormAdjustRecord',
  mixins,
  props: {
    dialogParams: {
      type: Object,
      default: null
    },
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
      title: this.$t('adAdjust.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: Object.assign({
          flowSelection: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      blobRecord:
      {
        addFile: {
          multiple: true,
          name: this.openParams().record && typeof this.openParams().record === 'object' && this.openParams().record.addFile ? this.openParams().record.addFile.split(',') : [],
          blob: []
        },
      },
      needDefaultFromServer: true,
      dictionary: {
        dictState: [],
        dictOrgCode: [],
        dictAdjustType: [],
        dictFlowNextOperator: [],
        dictFlowCopyToList: []
      },
      needLoadDictionary: true,
      generatorName: 'adAdjust',
      baseUrl: '/attendance/adjust/adAdjust'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-adjustrecord'
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
    'record.flowSelection': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowNextOperator')
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
    else cgForm.form_getDynaDict(this, 'flowNextOperator')
  },
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    newRecord: function() {
        return {
            state: 0,
            employee: '',
            orgCode: 0,
            adjustType: 10,
            registerTime: new Date(),
            description: '',
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
