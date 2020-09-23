
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk enctype="multipart/form-data" >
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-image" :label="$t('ewTimeProject.field.icon')" prop="icon" :size="$store.state.app.size" >
            <cg-image v-model="blobRecord.icon" name="icon" :title="$t('ewTimeProject.field.icon')"  :readonly="isDetail"  :id="record.id" generatorName="ewTimeProject" field="icon" 
                      @input="recordChanged=true" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('ewTimeProject.field.name')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('ewTimeProject.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-number" :label="$t('ewTimeProject.field.baseValue')" prop="baseValue" :size="$store.state.app.size" >
            <cg-number-input v-model="record.baseValue" name="baseValue" 
                             :readonly="isDetail" 
                             :label="$t('ewTimeProject.field.baseValue')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewTimeProject.field.basePrice')" prop="basePrice" :size="$store.state.app.size" >
            <cg-number-input v-model="record.basePrice" name="basePrice" 
                             :readonly="isDetail" 
                             :label="$t('ewTimeProject.field.basePrice')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewTimeProject.field.bonusPoint')" prop="bonusPoint" :size="$store.state.app.size" >
            <cg-number-input v-model="record.bonusPoint" name="bonusPoint" 
                             :readonly="isDetail" 
                             :label="$t('ewTimeProject.field.bonusPoint')" :placeholder="$t('system.message.needValue')" 
                             />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('ewTimeProject.field.startTime')" prop="startTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('ewTimeProject.field.startTime')}}</span>
            <cg-date-picker v-model="record.startTime" name="startTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('ewTimeProject.field.startTime')" :readonly="isDetail" 
                            format="HH:mm" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-time" :label="$t('ewTimeProject.field.endTime')" prop="endTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-clock-o" :size="14" :width="20"/>{{$t('ewTimeProject.field.endTime')}}</span>
            <cg-date-picker v-model="record.endTime" name="endTime" 
                            :align="mobile?'right':'center'" type="time" :title="$t('ewTimeProject.field.endTime')" :readonly="isDetail" 
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
  name: 'CgFormEwTimeProject',
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
      blobRecord:
      {
        icon: {
          name: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.icon : null,
          blob: null
        },
      },
      generatorName: 'ewTimeProject',
      baseUrl: '/ewallet/ewTimeProject'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.baseValue === null) this.record.baseValue = undefined
      if (this.record.basePrice === null) this.record.basePrice = undefined
      if (this.record.bonusPoint === null) this.record.bonusPoint = undefined
    },
    newRecord: function() {
        return {
            name: '',
            baseValue: 60,
            basePrice: 10,
            bonusPoint: '',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEwTimeProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
