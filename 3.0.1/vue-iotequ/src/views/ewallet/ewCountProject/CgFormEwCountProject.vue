
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-image" :label="$t('ewCountProject.field.icon')" prop="icon" :size="$store.state.app.size" >
            <cg-image v-model="blobRecord.icon" name="icon" :title="$t('ewCountProject.field.icon')"  :readonly="isDetail"  :id="record.id" generatorName="ewCountProject" field="icon" 
                      @input="recordChanged=true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('ewCountProject.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('ewCountProject.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewCountProject.field.basePrice')" prop="basePrice" :size="$store.state.app.size" >
            <cg-number-input v-model="record.basePrice" name="basePrice" 
                             :readonly="isDetail" 
                             :label="$t('ewCountProject.field.basePrice')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewCountProject.field.baseValue')" prop="baseValue" :size="$store.state.app.size" >
            <cg-number-input v-model="record.baseValue" name="baseValue" 
                             :readonly="isDetail" 
                             :label="$t('ewCountProject.field.baseValue')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewCountProject.field.bonusPoint')" prop="bonusPoint" :size="$store.state.app.size" >
            <cg-number-input v-model="record.bonusPoint" name="bonusPoint" 
                             :readonly="isDetail" 
                             :label="$t('ewCountProject.field.bonusPoint')" :placeholder="$t('system.message.needValue')" 
                             />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('ewCountProject.field.startTime')" prop="startTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('ewCountProject.field.startTime')}}</span>
            <cg-date-picker v-model="record.startTime" name="startTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('ewCountProject.field.startTime')" :readonly="isDetail" 
                            format="HH:mm" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('ewCountProject.field.endTime')" prop="endTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('ewCountProject.field.endTime')}}</span>
            <cg-date-picker v-model="record.endTime" name="endTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('ewCountProject.field.endTime')" :readonly="isDetail" 
                            format="HH:mm" clearable />
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
  name: 'CgFormEwCountProject',
  mixins: [ParentForm],
  props: {
    dialogParams: {
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
      blobRecord:
      {
        icon: {
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.icon : null,
          blob: null
        },
      },
      generatorName: 'ewCountProject',
      baseUrl: '/ewallet/ewCountProject'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.basePrice === null) this.record.basePrice = undefined
      if (this.record.baseValue === null) this.record.baseValue = undefined
      if (this.record.bonusPoint === null) this.record.bonusPoint = undefined
    },
    newRecord: function() {
        return {
            name: '',
            basePrice: 10,
            baseValue: 1,
            bonusPoint: '',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEwCountProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
