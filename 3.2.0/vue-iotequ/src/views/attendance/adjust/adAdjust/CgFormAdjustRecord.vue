
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
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormAdjustRecord',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
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
	    cgForm.form_getDynaDict(this, 'flowNextOperator')
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
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAdjustRecord-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
