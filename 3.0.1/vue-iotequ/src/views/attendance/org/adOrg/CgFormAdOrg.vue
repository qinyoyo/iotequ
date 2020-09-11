
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <cg-join v-model="orgCodeJoinVisible" readonly >
            <CgListOrg slot="popover" ref="orgCodeJoin" openID="orgcode-join" :height="500" :joinShow="orgCodeJoinVisible" :joinMultiple="false"
                       :originSelections="String(record.orgCode)" selectionKey="orgCode" joinMode @closeJoinList="(rows)=>{ getJoinFields('orgCode',rows)}"
                       @showJoinList="orgCodeJoinVisible=true" />
            <el-form-item class="cg-item-text" slot="reference" :label="$t('sysOrg.field.name')" prop="name" :size="$store.state.app.size" >
              <el-input v-model="record.name" name="name" 
                        type="text" 
                        :label="$t('sysOrg.field.name')" :placeholder="$t('system.message.unknown')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit clearable />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('adOrg.field.shiftId')" prop="shiftId" :size="$store.state.app.size" >
            <cg-select v-model="record.shiftId" name="shiftId"
                       :dictionary="dictionary.dictShiftId" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <cg-join v-model="hrJoinVisible" :readonly="isDetail" >
            <CgListUserJoin slot="popover" ref="hrJoin" openID="hr-join" :height="500" :joinShow="hrJoinVisible" :joinMultiple="false"
                            :originSelections="String(record.hr)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('hr',rows)}"
                            @showJoinList="hrJoinVisible=true" />
            <el-form-item class="cg-item-text" slot="reference" :label="$t('adOrg.field.hr')" prop="hrName" :size="$store.state.app.size" >
              <el-input v-model="record.hrName" name="hrName" 
                        type="text" 
                        :label="$t('adOrg.field.hr')" :placeholder="$t('system.message.unknown')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit clearable />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="12">
          <cg-join v-model="managerJoinVisible" :readonly="isDetail" >
            <CgListUserJoin slot="popover" ref="managerJoin" openID="manager-join" :height="500" :joinShow="managerJoinVisible" :joinMultiple="false"
                            :originSelections="String(record.manager)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('manager',rows)}"
                            @showJoinList="managerJoinVisible=true" />
            <el-form-item class="cg-item-text" slot="reference" :label="$t('adOrg.field.manager')" prop="managerName" :size="$store.state.app.size" >
              <el-input v-model="record.managerName" name="managerName" 
                        type="text" 
                        :label="$t('adOrg.field.manager')" :placeholder="$t('system.message.unknown')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit clearable />
            </el-form-item>
          </cg-join>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('adOrg.field.manageLimit')" prop="manageLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.manageLimit" name="manageLimit" 
                             :readonly="isDetail" 
                             :label="$t('adOrg.field.manageLimit')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('adOrg.field.deviation')" prop="deviation" :size="$store.state.app.size" >
            <cg-number-input v-model="record.deviation" name="deviation" 
                             :readonly="isDetail" 
                             :label="$t('adOrg.field.deviation')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('adOrg.field.floatLimit')" prop="floatLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.floatLimit" name="floatLimit" 
                             :readonly="isDetail" 
                             :label="$t('adOrg.field.floatLimit')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adOrg.field.absentLimit')" prop="absentLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.absentLimit" name="absentLimit" 
                             :readonly="isDetail" 
                             :label="$t('adOrg.field.absentLimit')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adOrg.field.freeWorkLimit')" prop="freeWorkLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.freeWorkLimit" name="freeWorkLimit" 
                             :readonly="isDetail" 
                             :label="$t('adOrg.field.freeWorkLimit')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
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
import CgListOrg from '@/views/framework/sysOrg/CgListOrg.vue'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormAdOrg-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormAdOrg',
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
  components: { CgListOrg, CgListUserJoin },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('adOrg.title.'+this.path),
      rules: {},
      idField: 'orgCode',
      orgCodeSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.orgCode : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: false,
      dictionary: {
        dictParent: [],
        dictShiftId: [],
        dictManagerOrgCode: []
      },
      needLoadDictionary: true,
      orgCodeJoinVisible: false,
      hrJoinVisible: false,
      managerJoinVisible: false,
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-adorg'
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
      return false
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
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    just4elInputNumberNullBug: function() {
      if (this.record.manageLimit === null) this.record.manageLimit = undefined
      if (this.record.deviation === null) this.record.deviation = undefined
      if (this.record.floatLimit === null) this.record.floatLimit = undefined
      if (this.record.absentLimit === null) this.record.absentLimit = undefined
      if (this.record.freeWorkLimit === null) this.record.freeWorkLimit = undefined
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        orgCode: {
          valueField: 'orgCode',
          fields: 'sysOrgCode=orgCode,name=name,parent=parent'
        },
        hr: {
          valueField: 'id',
          fields: 'hrName=realName'
        },
        manager: {
          valueField: 'id',
          fields: 'managerName=realName,managerOrgCode=orgCode'
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
