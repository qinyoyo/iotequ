
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('system.action.detail')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <cg-join v-model="employeeJoinVisible" readonly >
                <CgListUserJoin slot="popover" ref="employeeJoin" openID="employee-join" :height="500" :joinShow="employeeJoinVisible" :joinMultiple="false" :fixedQueryRecord="employeeDynaCondition"
                                :originSelections="String(record.employee)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('employee',rows)}"
                                @showJoinList="employeeJoinVisible=true" />
                <el-form-item class="cg-item-text" slot="reference" :label="$t('adAdjust.field.employee')" prop="realName" :size="$store.state.app.size" >
                  <el-input v-model="record.realName" name="realName" 
                            type="text" 
                            :label="$t('adAdjust.field.employee')" :placeholder="$t('system.message.needValue')" 
                            resize autofocus validate-event 
                            readonly :maxlength="32" show-word-limit />
                </el-form-item>
              </cg-join>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('adAdjust.field.orgCode')" prop="orgCode" :size="$store.state.app.size" >
                <cg-cascader v-model="record.orgCode" name="orgCode"
                             readonly :filterable="false" numberic 
                             :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('adAdjust.field.adjustType')" prop="adjustType" :size="$store.state.app.size" >
                <cg-select v-model="record.adjustType" name="adjustType"
                           :dictionary="dictionary.dictAdjustType" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.unknown')" clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="cg-item-select" :label="$t('adAdjust.field.state')" prop="state" :size="$store.state.app.size" >
                <cg-select v-model="record.state" name="state"
                           :dictionary="dictionary.dictState" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="12">
              <el-form-item :label="timeLabel('start')" class="cg-item-datetime" prop="startTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{timeLabel('start')}}</span>
                <cg-date-picker v-model="record.startTime" name="startTime" 
                                :title="timeLabel('start')"
                                :align="mobile?'right':'center'" type="datetime" readonly 
                                clearable />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="timeLabel('end')" class="cg-item-datetime" prop="endTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{timeLabel('end')}}</span>
                <cg-date-picker v-model="record.endTime" name="endTime" 
                                :title="timeLabel('end')"
                                :align="mobile?'right':'center'" type="datetime" readonly 
                                clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-textarea" :label="$t('adAdjust.field.description')" prop="description" :size="$store.state.app.size" >
            <el-input v-model="record.description" name="description" 
                      type="textarea" :maxlength="500" show-word-limit 
                      :label="$t('adAdjust.field.description')" :placeholder="$t('system.message.needValue')" 
                      readonly resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-file" :label="$t('adAdjust.field.addFile')" prop="addFile" :size="$store.state.app.size" >
            <cg-file v-model="blobRecord.addFile" name="addFile" :id="record.id" :baseUrl="baseUrl" field="addFile" 
                     accept=".pdf,.doc,.docx,.txt,image/*" multiple readonly 
                     @input="recordChanged=true"  />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane :label="groupPaneTitle('system.action.process')">
          <el-collapse accordion>
            <el-collapse-item :title="$t('system.action.flow')">
              <CgFlow :url="baseUrl+'/list'" :queryById.sync="record.id" />
            </el-collapse-item>
          </el-collapse>
          <el-form-item class="cg-item-radio cg-auto-focus" :label="$t('system.action.pleaseSelect')" prop="flowSelection" :size="$store.state.app.size" >
            <cg-radio v-model="record.flowSelection" name="flowSelection" :dictionary="dictionary.dictFlowSelection" :readonly="isDetail"  />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('system.action.flowNote')" prop="flowNote" :size="$store.state.app.size" >
            <el-input v-model="record.flowNote" name="flowNote" 
                      type="textarea" 
                      :label="$t('system.action.flowNote')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
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
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button v-if="hasAuthority(baseUrl+'/f_'+flowAction)" v-show="tabSelected=='1'" class="cg-button" type="default" plain :disabled="!recordChanged"
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
import cgForm from '@/utils/cgForm'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const mixins = [ParentForm]
const mixinContext = require.context('.', false, /CgFormAdjustApprove-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormAdjustApprove',
  mixins,
  props: {
    dialogParams: {
      type: Object,
      default: null
    }
  },
  components: { CgListUserJoin },
  data() {
    return {
      isFlowRecord: true,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: this.openParams().flowAction ? this.openParams().flowAction : 'approve',
      flowAction: this.openParams().flowAction ? this.openParams().flowAction : 'approve',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
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
        dictOrgCode: [],
        dictAdjustType: [],
        dictState: [],
        dictFlowSelection: [],
        dictFlowNextOperator: [],
        dictFlowCopyToList: []
      },
      needLoadDictionary: true,
      employeeJoinVisible: false,
      tabSelected: '0',
      generatorName: 'adAdjust',
      baseUrl: '/attendance/adjust/adAdjust'
    }
  },
  computed: {
    employeeDynaCondition() {
      return {
        orgCode: this.record.orgCode
      }
    },
  },
  watch: {
    'record.flowSelection': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowNextOperator')
      }
    }
  },
  methods: {
	  initDynaDict: function() {
	    cgForm.form_getDynaDict(this, 'flowSelection,flowNextOperator,flowCopyToList')
	  },
    newRecord: function() {
        return {
            registerTime: new Date(),
            realName: null,
            orgCode: 0,
            adjustType: 10,
            state: 0,
            description: '',
            flowSelection: 'approve',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        employee: {
          valueField: 'id',
          fields: 'realName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
</script>
