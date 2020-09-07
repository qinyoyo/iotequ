
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('cgTable.title.groupCgTableModule')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <cg-join v-model="projectIdJoinVisible" :readonly="isDetail" >
                <CgListCgProject slot="popover" ref="projectIdJoin" openID="projectid-join" :height="500" :joinShow="projectIdJoinVisible" :joinMultiple="false"
                                 :originSelections="record.projectId" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('projectId',rows)}"
                                 @showJoinList="projectIdJoinVisible=true" />
                <el-form-item class="cg-item-text cg-auto-focus" slot="reference" :label="$t('cgProject.field.name')" :title="$t('cgProject.field.nameValid')" prop="module" :size="$store.state.app.size" >
                  <el-input v-model="record.module" name="module" 
                            type="text" 
                            :label="$t('cgProject.field.name')" :placeholder="$t('system.message.needValue')" 
                            resize autofocus validate-event 
                            readonly :maxlength="36" show-word-limit />
                </el-form-item>
              </cg-join>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgProject.field.groupId')" :title="$t('cgProject.field.groupIdValid')" prop="groupId" :size="$store.state.app.size" >
                <el-input v-model="record.groupId" name="groupId" 
                          type="text" 
                          :label="$t('cgProject.field.groupId')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgTable.field.code')" :title="$t('cgTable.field.codeValid')" prop="code" :size="$store.state.app.size" >
                <el-input v-model="record.code" name="code" 
                          @input="record.entity=uppercaseFirst(camelString(record.project+'_'+record.code))"
                          type="text" 
                          :label="$t('cgTable.field.code')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgTable.field.title')" :title="$t('cgTable.field.titleValid')" prop="title" :size="$store.state.app.size" >
                <el-input v-model="record.title" name="title" 
                          type="text" 
                          :label="$t('cgTable.field.title')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgTable.field.name')" :title="$t('cgTable.field.nameValid')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('cgTable.field.name')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('cgTable.field.template')" :title="$t('cgTable.field.templateValid')" prop="template" :size="$store.state.app.size" >
                <cg-select v-model="record.template" name="template"
                           :dictionary="dictionary.dictTemplate" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgTable.field.entity')" :title="$t('cgTable.field.entityValid')" prop="entity" :size="$store.state.app.size" >
                <el-input v-model="record.entity" name="entity" 
                          type="text" 
                          :label="$t('cgTable.field.entity')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="32" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-text" :label="$t('cgTable.field.subPackage')" :title="$t('cgTable.field.subPackageValid')" prop="subPackage" :size="$store.state.app.size" >
                <el-input v-model="record.subPackage" name="subPackage" 
                          type="text" 
                          :label="$t('cgTable.field.subPackage')" :placeholder="$t('system.message.unknown')" 
                          resize autofocus validate-event 
                          :readonly="isDetail" :maxlength="30" show-word-limit clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgTable.field.trialLicence')" prop="trialLicence" :size="$store.state.app.size" >
                <cg-number-input v-model="record.trialLicence" name="trialLicence" 
                                 :readonly="isDetail" 
                                 :label="$t('cgTable.field.trialLicence')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-number" :label="$t('cgTable.field.trialDays')" prop="trialDays" :size="$store.state.app.size" >
                <cg-number-input v-model="record.trialDays" name="trialDays" 
                                 :readonly="isDetail" 
                                 :label="$t('cgTable.field.trialDays')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 :min="0" :max="365" :step="7" :title="$t('system.message.valueRange') + ': 0 - 365'" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('cgTable.title.groupCgTableImports')">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgTable.field.imports')" prop="imports" :size="$store.state.app.size" >
            <el-input v-model="record.imports" name="imports" 
                      type="text" 
                      :label="$t('cgTable.field.imports')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="500" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-checkbox" :label="$t('cgTable.field.actionList')" :title="$t('cgTable.field.actionListValid')" prop="actionList" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.actionList" name="actionList" :dictionary="dictionary.dictActionList" :readonly="isDetail"  />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item v-if="record.actionList && record.actionList.indexOf('flow')>=0" class="cg-item-select" :label="$t('cgTable.field.flowDynaFieldsOp')" prop="flowDynaFieldsOp" :size="$store.state.app.size" >
                <cg-select v-model="record.flowDynaFieldsOp" name="flowDynaFieldsOp"
                           :dictionary="dictionary.dictFlowDynaFieldsOp" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="record.actionList && record.actionList.indexOf('flow')>=0" class="cg-item-select" :label="$t('cgTable.field.flowDynaFieldsCp')" prop="flowDynaFieldsCp" :size="$store.state.app.size" >
                <cg-select v-model="record.flowDynaFieldsCp" name="flowDynaFieldsCp"
                           :dictionary="dictionary.dictFlowDynaFieldsOp" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('cgTable.field.pojoImports')" prop="pojoImports" :size="$store.state.app.size" >
            <el-input v-model="record.pojoImports" name="pojoImports" 
                      type="text" 
                      :label="$t('cgTable.field.pojoImports')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="500" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-text" :label="$t('cgTable.field.pojoExtends')" prop="pojoExtends" :size="$store.state.app.size" >
            <el-input v-model="record.pojoExtends" name="pojoExtends" 
                      type="text" 
                      :label="$t('cgTable.field.pojoExtends')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="200" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('cgTable.field.pojoJavaCode')" prop="pojoJavaCode" :size="$store.state.app.size" >
            <el-input v-model="record.pojoJavaCode" name="pojoJavaCode" 
                      type="textarea" 
                      :label="$t('cgTable.field.pojoJavaCode')" :placeholder="$t('system.message.unknown')" clearable 
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
import CgListCgProject from '@/views/codegenerator/cgProject/CgListCgProject.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormCgTable-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgTable',
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
  components: { CgListCgProject },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('cgTable.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: Object.assign({
          id: null
        }, this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {}),
      needDefaultFromServer: false,
      dictionary: {
        dictTemplate: this.getDictionary('vue-element'),
        dictActionList: this.getDictionary('list,add,view,edit,delete,batdel,import,export,flow,query,editInline_add'),
        dictFlowDynaFieldsOp: []
      },
      needLoadDictionary: true,
      projectIdJoinVisible: false,
      tabSelected: '0',
      generatorName: 'cgTable',
      baseUrl: '/codegenerator/cgTable'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-cgtable'
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
    'record.id': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowDynaFieldsOp')
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
    else cgForm.form_getDynaDict(this, 'flowDynaFieldsOp')
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
      if (this.record.trialLicence === null) this.record.trialLicence = undefined
      if (this.record.trialDays === null) this.record.trialDays = undefined
    },
    newRecord: function() {
        return {
            creator: this.$store.state.user.name,
            code: '',
            title: '',
            template: '',
            actionList: 'add,view,edit,delete,batdel,impo',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        projectId: {
          valueField: 'id',
          fields: 'project=code,groupId=groupId,module=name'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
