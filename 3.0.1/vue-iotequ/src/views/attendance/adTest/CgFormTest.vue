
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-left_join cg-auto-fucus" :label="$t('adTest.field.id')" prop="id" :size="$store.state.app.size" >
            <el-input v-model="record.id" name="id" 
                      type="left_join" :maxlength="32" show-word-limit 
                      :label="$t('adTest.field.id')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <cg-join v-model="idJoinVisible" :readonly="isDetail" >
            <CgListAdEmployee slot="popover" ref="idJoin" openID="id-join" :height="500" :joinShow="idJoinVisible" :joinMultiple="false"
                              :originSelections="record.id" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('id',rows)}"
                              @showJoinList="idJoinVisible=true" />
            <el-form-item class="cg-item-text" slot="reference" :label="$t('adTest.field.id')" prop="realName" :size="$store.state.app.size" >
              <el-input v-model="record.realName" name="realName" 
                        type="text" :maxlength="32" show-word-limit 
                        :label="$t('adTest.field.id')" :placeholder="$t('system.message.needValue')" 
                        readonly resize autofocus validate-event />
            </el-form-item>
          </cg-join>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('adEmployee.field.isAttendance')" prop="isAttendance" :size="$store.state.app.size" >
            <el-switch v-model="record.isAttendance" name="isAttendance" :active-text="mobile?'':$t('adEmployee.field.isAttendance')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysUser.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgCode" name="orgCode"
                         :readonly="isDetail" :filterable="false" numberic clearable 
                         :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button v-if="!mobile && hasAuthorityOf(myself,baseUrl,'add',record)" class="cg-button" type="default" plain icon="fa fa-plus"
                 @click.native="doAction('add')">
        {{ $t('system.action.new') }}
      </el-button>
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
import CgListAdEmployee from '@/views/attendance/employee/adEmployee/CgListAdEmployee.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormTest-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormTest',
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
  components: { CgListAdEmployee },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('adTest.title.'+this.path),
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
        dictOrgCode: []
      },
      needLoadDictionary: true,
      idJoinVisible: false,
      generatorName: 'adTest',
      baseUrl: '/attendance/adTest'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-test'
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
            realName: '',
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        id: {
          valueField: 'id',
          fields: 'realName=realName,isAttendance=isAttendance,orgCode=orgCode'
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
