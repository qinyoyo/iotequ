
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-radio cg-auto-focus" :label="$t('devData.field.operation')" prop="operation" :size="$store.state.app.size" >
        <cg-radio v-model="record.operation" name="operation" :dictionary="dictionary.dictOperation" :readonly="isDetail"  />
      </el-form-item>
      <el-form-item class="cg-item-radio" :label="$t('devData.field.deviceSelectionMode')" prop="deviceSelectionMode" :size="$store.state.app.size" >
        <cg-radio v-model="record.deviceSelectionMode" name="deviceSelectionMode" :dictionary="dictionary.dictDeviceSelectionMode" :readonly="isDetail"  />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item v-if="record.deviceSelectionMode=='1'" class="cg-item-select" :label="$t('devData.field.groupSelection')" prop="groupSelection" :size="$store.state.app.size" >
            <cg-cascader v-model="record.groupSelection" name="groupSelection"
                         :readonly="isDetail" :filterable="false" clearable 
                         :dictionary="dictionary.dictGroupSelection" :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="record.deviceSelectionMode=='1'" class="cg-item-boolean" :label="$t('devData.field.includeSubGroup')" prop="includeSubGroup" :size="$store.state.app.size" >
            <el-switch v-model="record.includeSubGroup" name="includeSubGroup" :active-text="mobile?'':$t('devData.field.includeSubGroup')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <cg-join v-model="readerSelectionJoinVisible" :readonly="isDetail" >
        <CgListDevReader slot="popover" ref="readerSelectionJoin" openID="readerselection-join" :height="500" :joinShow="readerSelectionJoinVisible" :joinMultiple="true"
                         :originSelections="record.readerSelection" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('readerSelection',rows)}"
                         @showJoinList="readerSelectionJoinVisible=true" />
        <el-form-item v-if="record.deviceSelectionMode=='2'" class="cg-item-text" slot="reference" :label="$t('devData.field.readerSelection')" prop="readerName" :size="$store.state.app.size" >
          <el-input v-model="record.readerName" name="readerName" 
                    type="text" :maxlength="30" show-word-limit 
                    :label="$t('devData.field.readerSelection')" :placeholder="$t('system.message.needValue')" 
                    readonly resize autofocus validate-event />
        </el-form-item>
      </cg-join>
      <el-form-item class="cg-item-radio" :label="$t('devData.field.userSelectionMode')" prop="userSelectionMode" :size="$store.state.app.size" >
        <cg-radio v-model="record.userSelectionMode" name="userSelectionMode" :dictionary="dictionary.dictUserSelectionMode" :readonly="isDetail"  />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item v-if="record.userSelectionMode=='1'" class="cg-item-select" :label="$t('devData.field.orgSelection')" prop="orgSelection" :size="$store.state.app.size" >
            <cg-cascader v-model="record.orgSelection" name="orgSelection"
                         :readonly="isDetail" :filterable="false" clearable 
                         :dictionary="dictionary.dictOrgSelection" :placeholder="$t('system.message.unknown')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="record.userSelectionMode=='1'" class="cg-item-boolean" :label="$t('devData.field.includeSubOrg')" prop="includeSubOrg" :size="$store.state.app.size" >
            <el-switch v-model="record.includeSubOrg" name="includeSubOrg" :active-text="mobile?'':$t('devData.field.includeSubOrg')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <cg-join v-model="userSelectionJoinVisible" :readonly="isDetail" >
        <CgListDevPeopleSelect slot="popover" ref="userSelectionJoin" openID="userselection-join" :height="500" :joinShow="userSelectionJoinVisible" :joinMultiple="true"
                               :originSelections="record.userSelection" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('userSelection',rows)}"
                               @showJoinList="userSelectionJoinVisible=true" />
        <el-form-item v-if="record.userSelectionMode=='2'" class="cg-item-text" slot="reference" :label="$t('devData.field.userSelection')" prop="userName" :size="$store.state.app.size" >
          <el-input v-model="record.userName" name="userName" 
                    type="text" :maxlength="32" show-word-limit 
                    :label="$t('devData.field.userSelection')" :placeholder="$t('system.message.needValue')" 
                    readonly resize autofocus validate-event />
        </el-form-item>
      </cg-join>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item v-if="record.operation=='upload'" class="cg-item-boolean" :label="$t('devData.field.uploadAllUser')" prop="uploadAllUser" :size="$store.state.app.size" >
            <el-switch v-model="record.uploadAllUser" name="uploadAllUser" :active-text="mobile?'':$t('devData.field.uploadAllUser')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="record.operation=='upload'" class="cg-item-boolean" :label="$t('devData.field.uploadNewUser')" prop="uploadNewUser" :size="$store.state.app.size" >
            <el-switch v-model="record.uploadNewUser" name="uploadNewUser" :active-text="mobile?'':$t('devData.field.uploadNewUser')" inactive-text="" :disabled="isDetail"  />
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
        {{ $t('system.action.ok') }}
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
import CgListDevReader from '@/views/reader/devReader/CgListDevReader.vue'
import CgListDevPeopleSelect from '@/views/reader/devPeople/CgListDevPeopleSelect.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormDevData-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormDevData',
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
  components: { CgListDevReader, CgListDevPeopleSelect },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('devData.title.'+this.path),
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
        dictOperation: this.getDictionary('upload,download,remove','从设备读取用户数据,向设备下发用户数据,删除设备用户数据'),
        dictDeviceSelectionMode: this.getDictionary('1,2','按设备组,按设备'),
        dictGroupSelection: [],
        dictUserSelectionMode: this.getDictionary('1,2','按组织机构,按人员'),
        dictOrgSelection: []
      },
      needLoadDictionary: true,
      readerSelectionJoinVisible: false,
      userSelectionJoinVisible: false,
      generatorName: 'devData',
      baseUrl: '/reader/devData'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-devdata'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'top'
    },
    isDetail() {
      return false
    },
    isNew() {
      return !this.openMode || this.openMode === 'add'
    },
    isEdit() {
      return false
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
            operation: 'upload',
            deviceSelectionMode: '1',
            includeSubGroup: true,
            readerSelection: '',
            readerName: '',
            userSelectionMode: '1',
            includeSubOrg: false,
            userSelection: '',
            userName: '',
            uploadAllUser: true,
            uploadNewUser: true,
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save')
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        readerSelection: {
          valueField: 'id',
          fields: 'readerName=name'
        },
        userSelection: {
          valueField: 'userNo',
          fields: 'userName=realName'
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
