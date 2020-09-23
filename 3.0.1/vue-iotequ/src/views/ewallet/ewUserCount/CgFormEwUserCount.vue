
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-left_join cg-auto-focus" :label="$t('ewUserCount.field.userNo')" prop="userNo" :size="$store.state.app.size" >
        <el-input v-model="record.userNo" name="userNo" 
                  type="left_join" 
                  :label="$t('ewUserCount.field.userNo')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="45" show-word-limit />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('ewUserCount.field.countId')" prop="countId" :size="$store.state.app.size" >
            <el-input v-model="record.countId" name="countId" 
                      type="left_join" 
                      :label="$t('ewUserCount.field.countId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewUserCount.field.amountCount')" prop="amountCount" :size="$store.state.app.size" >
            <cg-number-input v-model="record.amountCount" name="amountCount" 
                             :readonly="isDetail" 
                             :label="$t('ewUserCount.field.amountCount')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewUserCount.field.costLimit')" prop="costLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.costLimit" name="costLimit" 
                             :readonly="isDetail" 
                             :label="$t('ewUserCount.field.costLimit')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewUserCount.field.dayLimit')" prop="dayLimit" :size="$store.state.app.size" >
            <cg-number-input v-model="record.dayLimit" name="dayLimit" 
                             :readonly="isDetail" 
                             :label="$t('ewUserCount.field.dayLimit')" :placeholder="$t('system.message.needValue')" :precision="0" 
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
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormEwUserCount',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      allowAddRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      generatorName: 'ewUserCount',
      baseUrl: '/ewallet/ewUserCount'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.amountCount === null) this.record.amountCount = undefined
      if (this.record.costLimit === null) this.record.costLimit = undefined
      if (this.record.dayLimit === null) this.record.dayLimit = undefined
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEwUserCount-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
