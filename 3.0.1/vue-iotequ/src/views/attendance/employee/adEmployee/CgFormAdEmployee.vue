
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <cg-join v-model="idJoinVisible" readonly >
            <CgListUserJoin slot="popover" ref="idJoin" openID="id-join" :height="500" :joinShow="idJoinVisible" :joinMultiple="false"
                            :originSelections="record.id" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('id',rows)}"
                            @showJoinList="idJoinVisible=true" />
            <el-form-item class="cg-item-text" slot="reference" :label="$t('adEmployee.field.id')" prop="realName" :size="$store.state.app.size" >
              <el-input v-model="record.realName" name="realName" 
                        type="text" :maxlength="32" show-word-limit 
                        :label="$t('adEmployee.field.id')" :placeholder="$t('system.message.needValue')" 
                        readonly resize autofocus validate-event />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysUser.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgCode" name="orgCode"
                         readonly :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('adEmployee.field.employeeNo')" prop="employeeNo" :size="$store.state.app.size" >
            <el-input v-model="record.employeeNo" name="employeeNo" 
                      type="text" :maxlength="32" show-word-limit 
                      :label="$t('adEmployee.field.employeeNo')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('adEmployee.field.emLevel')" prop="emLevel" :size="$store.state.app.size" >
            <cg-number-input v-model="record.emLevel" name="emLevel" 
                             :readonly="isDetail" 
                             :label="$t('adEmployee.field.emLevel')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('adEmployee.field.isAttendance')" prop="isAttendance" :size="$store.state.app.size" >
            <el-switch v-model="record.isAttendance" name="isAttendance" :active-text="mobile?'':$t('adEmployee.field.isAttendance')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('adEmployee.field.enterDate')" prop="enterDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adEmployee.field.enterDate')}}</span>
            <cg-date-picker v-model="record.enterDate" name="enterDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adEmployee.field.enterDate')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-date" :label="$t('adEmployee.field.leaveDate')" prop="leaveDate" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar" :size="14" :width="20"/>{{$t('adEmployee.field.leaveDate')}}</span>
            <cg-date-picker v-model="record.leaveDate" name="leaveDate" 
                            :align="mobile?'right':'center'" type="date" :title="$t('adEmployee.field.leaveDate')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('adEmployee.field.shiftId')" prop="shiftId" :size="$store.state.app.size" >
            <cg-select v-model="record.shiftId" name="shiftId"
                       :dictionary="dictionary.dictShiftId" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
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
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormAdEmployee-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormAdEmployee',
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
  components: { CgListUserJoin },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('adEmployee.title.'+this.path),
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
        dictOrgCode: [],
        dictShiftId: []
      },
      needLoadDictionary: true,
      idJoinVisible: false,
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-ademployee'
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
  activated() {
    cgForm.form_activedRefresh(this)
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.$route.query
    },
    just4elInputNumberNullBug: function() {
      if (this.record.emLevel === null) this.record.emLevel = undefined
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        id: {
          valueField: 'id',
          fields: 'realName=realName,sex=sex,orgCode=orgCode,birthDate=birthDate'
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
