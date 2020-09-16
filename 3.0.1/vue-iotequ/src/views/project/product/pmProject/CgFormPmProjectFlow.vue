
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-tabs ref="tabs" v-model="tabSelected" @tab-click="autoFocus">
        <el-tab-pane :label="groupPaneTitle('system.action.detail')">
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('pmProject.field.type')" prop="type" :size="$store.state.app.size" >
                <cg-select v-model="record.type" name="type"
                           :dictionary="dictionary.dictType" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item class="cg-item-text" :label="$t('pmProject.field.name')" prop="name" :size="$store.state.app.size" >
                <el-input v-model="record.name" name="name" 
                          type="text" 
                          :label="$t('pmProject.field.name')" :placeholder="$t('system.message.needValue')" 
                          resize autofocus validate-event 
                          readonly :maxlength="45" show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-select" :label="$t('pmProject.field.flowState')" prop="flowState" :size="$store.state.app.size" >
                <cg-select v-model="record.flowState" name="flowState"
                           :dictionary="dictionary.dictFlowState" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <cg-join v-model="flowRegisterByJoinVisible" readonly >
                <CgListUserJoin slot="popover" ref="flowRegisterByJoin" openID="flowregisterby-join" :height="500" :joinShow="flowRegisterByJoinVisible" :joinMultiple="false"
                                :originSelections="String(record.flowRegisterBy)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('flowRegisterBy',rows)}"
                                @showJoinList="flowRegisterByJoinVisible=true" />
                <el-form-item class="cg-item-text" slot="reference" :label="$t('pmProject.field.flowRegisterBy')" prop="registerByName" :size="$store.state.app.size" >
                  <el-input v-model="record.registerByName" name="registerByName" 
                            type="text" 
                            :label="$t('pmProject.field.flowRegisterBy')" :placeholder="$t('system.message.needValue')" 
                            resize autofocus validate-event 
                            readonly :maxlength="32" show-word-limit />
                </el-form-item>
              </cg-join>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-datetime" :label="$t('pmProject.field.flowRegisterTime')" prop="flowRegisterTime" :size="$store.state.app.size" >
                <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('pmProject.field.flowRegisterTime')}}</span>
                <cg-date-picker v-model="record.flowRegisterTime" name="flowRegisterTime" 
                                :align="mobile?'right':'center'" type="datetime" :title="$t('pmProject.field.flowRegisterTime')" readonly 
                                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-textarea" :label="$t('pmProject.field.customer')" prop="customer" :size="$store.state.app.size" >
            <el-input v-model="record.customer" name="customer" 
                      type="textarea" :maxlength="200" show-word-limit 
                      :label="$t('pmProject.field.customer')" :placeholder="$t('system.message.unknown')" clearable 
                      readonly resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-row :gutter="mobile?0:20">
            <el-col :span="8">
              <el-form-item class="cg-item-number cg-auto-focus" :label="$t('pmProject.field.marketSize')" prop="marketSize" :size="$store.state.app.size" >
                <cg-number-input v-model="record.marketSize" name="marketSize" 
                                 :readonly="isDetail" 
                                 :label="$t('pmProject.field.marketSize')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('pmProject.field.humanCost')" prop="humanCost" :size="$store.state.app.size" >
                <cg-number-input v-model="record.humanCost" name="humanCost" 
                                 :readonly="isDetail" 
                                 :label="$t('pmProject.field.humanCost')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item class="cg-item-number" :label="$t('pmProject.field.materialCost')" prop="materialCost" :size="$store.state.app.size" >
                <cg-number-input v-model="record.materialCost" name="materialCost" 
                                 :readonly="isDetail" 
                                 :label="$t('pmProject.field.materialCost')" :placeholder="$t('system.message.unknown')" clearable :precision="0" 
                                 :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item class="cg-item-text" :label="$t('pmProject.field.code')" prop="code" :size="$store.state.app.size" >
            <el-input v-model="record.code" name="code" 
                      type="text" 
                      :label="$t('pmProject.field.code')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
          <el-form-item class="cg-item-textarea" :label="$t('pmProject.field.description')" prop="description" :size="$store.state.app.size" >
            <el-input v-model="record.description" name="description" 
                      type="textarea" :maxlength="500" show-word-limit 
                      :label="$t('pmProject.field.description')" :placeholder="$t('system.message.unknown')" clearable 
                      :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
          </el-form-item>
          <el-form-item class="cg-item-file" :label="$t('pmProject.field.addFile')" prop="addFile" :size="$store.state.app.size" >
            <cg-file v-model="blobRecord.addFile" name="addFile" appendonly :id="record.id" :baseUrl="baseUrl" field="addFile" 
                     accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,image/*" multiple :readonly="isDetail" 
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
                      type="textarea" :maxlength="1000" show-word-limit 
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
const Comp = {
  name: 'CgFormPmProjectFlow',
  mixins: [ParentForm],
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
      path: this.openParams().flowAction ? this.openParams().flowAction : 'assess',
      flowAction: this.openParams().flowAction ? this.openParams().flowAction : 'assess',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      record: Object.assign({
          flowSelection: null,
          type: null,
          flowState: null
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
        dictType: [],
        dictFlowState: [],
        dictFlowSelection: [],
        dictFlowNextOperator: [],
        dictFlowCopyToList: []
      },
      needLoadDictionary: true,
      flowRegisterByJoinVisible: false,
      tabSelected: '0',
      generatorName: 'pmProject',
      baseUrl: '/project/product/pmProject'
    }
  },
  computed: {
  },
  watch: {
    'record.flowSelection': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowNextOperator')
      }
    },
    'record.type': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowNextOperator,flowCopyToList')
      }
    },
    'record.flowState': {
      handler(n,o)
      {
        cgForm.form_getDynaDict(this, 'flowNextOperator')
      }
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.marketSize === null) this.record.marketSize = undefined
      if (this.record.humanCost === null) this.record.humanCost = undefined
      if (this.record.materialCost === null) this.record.materialCost = undefined
    },
	  initDynaDict: function() {
	    cgForm.form_getDynaDict(this, 'flowSelection,flowNextOperator,flowCopyToList')
	  },
    newRecord: function() {
        return {
            type: 1,
            name: '',
            flowState: 1,
            registerByName: null,
            flowSelection: 'approve',
        }
    },
    groupPaneTitle: function(defTitle) {
      return this.$t(defTitle)
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        flowRegisterBy: {
          valueField: 'id',
          fields: 'registerByName=realName'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormPmProjectFlow-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
