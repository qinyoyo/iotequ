
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgProject.field.code')" prop="code" :size="$store.state.app.size" >
            <el-input v-model="record.code" name="code" 
                      type="text" 
                      :label="$t('cgProject.field.code')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="10" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.name')" :title="$t('cgProject.field.nameValid')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('cgProject.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.version')" prop="version" :size="$store.state.app.size" >
            <el-input v-model="record.version" name="version" 
                      type="text" 
                      :label="$t('cgProject.field.version')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.groupId')" prop="groupId" :size="$store.state.app.size" >
            <el-input v-model="record.groupId" name="groupId" 
                      type="text" 
                      :label="$t('cgProject.field.groupId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('cgProject.field.note')" prop="note" :size="$store.state.app.size" >
        <el-input v-model="record.note" name="note" 
                  type="text" 
                  :label="$t('cgProject.field.note')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-select" :label="$t('cgProject.field.modules')" prop="modules" :size="$store.state.app.size" >
        <cg-select v-model="record.modules" name="modules"
                   :dictionary="dictionary.dictModules" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
      </el-form-item>
      <el-form-item class="cg-item-url" :label="$t('cgProject.field.mavenServer')" prop="mavenServer" :size="$store.state.app.size" >
        <el-input v-model="record.mavenServer" name="mavenServer" 
                  type="url" 
                  :label="$t('cgProject.field.mavenServer')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit clearable />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.springModule')" prop="springModule" :size="$store.state.app.size" >
            <el-switch v-model="record.springModule" name="springModule" :active-text="mobile?'':$t('cgProject.field.springModule')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.mavenModule')" prop="mavenModule" :size="$store.state.app.size" >
            <el-switch v-model="record.mavenModule" name="mavenModule" :active-text="mobile?'':$t('cgProject.field.mavenModule')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.test')" prop="test" :size="$store.state.app.size" >
            <el-switch v-model="record.test" name="test" :active-text="mobile?'':$t('cgProject.field.test')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-textarea" :label="$t('cgProject.field.addtionalDependencies')" prop="addtionalDependencies" :size="$store.state.app.size" >
        <el-input v-model="record.addtionalDependencies" name="addtionalDependencies" 
                  type="textarea" 
                  :label="$t('cgProject.field.addtionalDependencies')" :placeholder="$t('system.message.unknown')" clearable 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
      </el-form-item>
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
const mixinContext = require.context('.', false, /CgFormCgProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormCgProject',
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
      title: this.$t('cgProject.title.'+this.path),
      rules: {},
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: true,
      dictionary: {
        dictModules: []
      },
      needLoadDictionary: true,
      generatorName: 'cgProject',
      baseUrl: '/codegenerator/cgProject'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-cgproject'
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
            creator: this.$store.state.user.name,
            code: '',
            name: '',
            groupId: 'top.iotequ',
            modules: 'framework',
            springModule: false,
            mavenModule: true,
            test: false,
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
