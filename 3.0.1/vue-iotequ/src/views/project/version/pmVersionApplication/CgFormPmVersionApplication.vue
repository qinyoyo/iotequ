
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <cg-join v-model="flowRegisterByJoinVisible" readonly >
            <CgListUserJoin slot="popover" ref="flowRegisterByJoin" openID="flowregisterby-join" :height="500" :joinShow="flowRegisterByJoinVisible" :joinMultiple="false"
                            :originSelections="String(record.flowRegisterBy)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('flowRegisterBy',rows)}"
                            @showJoinList="flowRegisterByJoinVisible=true" />
            <el-form-item class="cg-item-mltext" slot="reference" :label="$t('pmVersionApplication.field.flowRegisterBy')" prop="registerByName" :size="$store.state.app.size" >
              <cg-input v-model="record.registerByName" name="registerByName" 
                        type="mltext" 
                        :label="$t('pmVersionApplication.field.flowRegisterBy')" :placeholder="$t('system.message.needValue')" 
                        resize autofocus validate-event 
                        readonly :maxlength="32" show-word-limit />
            </el-form-item>
          </cg-join>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-select" :label="$t('pmVersionApplication.field.flowState')" prop="flowState" :size="$store.state.app.size" >
            <cg-select v-model="record.flowState" name="flowState"
                       :dictionary="dictionary.dictFlowState" readonly :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-datetime" :label="$t('pmVersionApplication.field.flowRegisterTime')" prop="flowRegisterTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('pmVersionApplication.field.flowRegisterTime')}}</span>
            <cg-date-picker v-model="record.flowRegisterTime" name="flowRegisterTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('pmVersionApplication.field.flowRegisterTime')" readonly 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('pmVersionApplication.field.applicationType')" prop="applicationType" :size="$store.state.app.size" >
            <cg-select v-model="record.applicationType" name="applicationType"
                       :dictionary="dictionary.dictApplicationType" :readonly="isDetail" :filterable="false" :allow-create="false" numberic :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item class="cg-item-select" :label="$t('pmVersionApplication.field.project')" prop="project" :size="$store.state.app.size" >
            <cg-select v-model="record.project" name="project"
                       :dictionary="dictionary.dictProject" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-text" :label="$t('pmVersionApplication.field.contractNo')" prop="contractNo" :size="$store.state.app.size" >
            <el-input v-model="record.contractNo" name="contractNo" 
                      type="text" 
                      :label="$t('pmVersionApplication.field.contractNo')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item class="cg-item-text" :label="$t('pmVersionApplication.field.customer')" prop="customer" :size="$store.state.app.size" >
            <el-input v-model="record.customer" name="customer" 
                      type="text" 
                      :label="$t('pmVersionApplication.field.customer')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="20" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('pmVersionApplication.field.licence')" prop="licence" :size="$store.state.app.size" >
        <el-input v-model="record.licence" name="licence" 
                  type="text" 
                  :label="$t('pmVersionApplication.field.licence')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-textarea" :label="$t('pmVersionApplication.field.description')" prop="description" :size="$store.state.app.size" >
        <el-input v-model="record.description" name="description" 
                  type="textarea" :maxlength="200" show-word-limit 
                  :label="$t('pmVersionApplication.field.description')" :placeholder="$t('system.message.needValue')" 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
      </el-form-item>
      <el-form-item class="cg-item-textarea" :label="$t('pmVersionApplication.field.versionInfo')" prop="versionInfo" :size="$store.state.app.size" >
        <el-input v-model="record.versionInfo" name="versionInfo" 
                  type="textarea" :maxlength="200" show-word-limit 
                  :label="$t('pmVersionApplication.field.versionInfo')" :placeholder="$t('system.message.unknown')" clearable 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
      </el-form-item>
      <el-form-item class="cg-item-file" :label="$t('pmVersionApplication.field.addFile')" prop="addFile" :size="$store.state.app.size" >
        <cg-file v-model="blobRecord.addFile" name="addFile" :id="record.id" :baseUrl="baseUrl" field="addFile" 
                 accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt" multiple :readonly="isDetail" 
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
import cgForm from '@/utils/cgForm'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormPmVersionApplication',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  components: { CgListUserJoin },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
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
        dictFlowState: [],
        dictApplicationType: [],
        dictProject: [],
        dictFlowNextOperator: [],
        dictFlowCopyToList: []
      },
      needLoadDictionary: true,
      flowRegisterByJoinVisible: false,
      generatorName: 'pmVersionApplication',
      baseUrl: '/project/version/pmVersionApplication'
    }
  },
  computed: {
  },
  methods: {
    newRecord: function() {
        return {
            registerByName: null,
            flowState: 1,
            applicationType: 0,
            project: '',
            customer: '',
            licence: '',
            description: '',
        }
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
const mixinContext = require.context('.', false, /CgFormPmVersionApplication-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
