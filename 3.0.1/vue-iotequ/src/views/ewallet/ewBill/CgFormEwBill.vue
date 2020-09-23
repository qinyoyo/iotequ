
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('ewBill.field.no')" prop="no" :size="$store.state.app.size" >
            <el-input v-model="record.no" name="no" 
                      type="text" 
                      :label="$t('ewBill.field.no')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="32" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('ewBill.field.canceled')" prop="canceled" :size="$store.state.app.size" >
            <el-switch v-model="record.canceled" name="canceled" :active-text="mobile?'':$t('ewBill.field.canceled')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-boolean" :label="$t('ewBill.field.isCharge')" prop="isCharge" :size="$store.state.app.size" >
            <el-switch v-model="record.isCharge" name="isCharge" :active-text="mobile?'':$t('ewBill.field.isCharge')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.userNo')" prop="userNo" :size="$store.state.app.size" >
            <el-input v-model="record.userNo" name="userNo" 
                      type="text" 
                      :label="$t('ewBill.field.userNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.batchNo')" prop="batchNo" :size="$store.state.app.size" >
            <el-input v-model="record.batchNo" name="batchNo" 
                      type="text" 
                      :label="$t('ewBill.field.batchNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('ewBill.field.dt')" prop="dt" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('ewBill.field.dt')}}</span>
            <cg-date-picker v-model="record.dt" name="dt" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('ewBill.field.dt')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.operationType')" prop="operationType" :size="$store.state.app.size" >
            <cg-number-input v-model="record.operationType" name="operationType" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.operationType')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-checkbox" :label="$t('ewBill.field.costType')" prop="costType" :size="$store.state.app.size" >
            <cg-checkbox v-model="record.costType" name="costType" :dictionary="dictionary.dictCostType" :readonly="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.projectId')" prop="projectId" :size="$store.state.app.size" >
            <cg-number-input v-model="record.projectId" name="projectId" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.projectId')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.projectName')" prop="projectName" :size="$store.state.app.size" >
            <el-input v-model="record.projectName" name="projectName" 
                      type="text" 
                      :label="$t('ewBill.field.projectName')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.projectPrice')" prop="projectPrice" :size="$store.state.app.size" >
            <cg-number-input v-model="record.projectPrice" name="projectPrice" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.projectPrice')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="1" :step="1" :title="$t('system.message.valueRange') + ': 1 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.amount')" prop="amount" :size="$store.state.app.size" >
            <cg-number-input v-model="record.amount" name="amount" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.amount')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.amountBefore')" prop="amountBefore" :size="$store.state.app.size" >
            <cg-number-input v-model="record.amountBefore" name="amountBefore" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.amountBefore')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.bonus')" prop="bonus" :size="$store.state.app.size" >
            <cg-number-input v-model="record.bonus" name="bonus" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.bonus')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('ewBill.field.bonusBefore')" prop="bonusBefore" :size="$store.state.app.size" >
            <cg-number-input v-model="record.bonusBefore" name="bonusBefore" 
                             :readonly="isDetail" 
                             :label="$t('ewBill.field.bonusBefore')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.deviceNo')" prop="deviceNo" :size="$store.state.app.size" >
            <el-input v-model="record.deviceNo" name="deviceNo" 
                      type="text" 
                      :label="$t('ewBill.field.deviceNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('ewBill.field.shopId')" prop="shopId" :size="$store.state.app.size" >
            <el-input v-model="record.shopId" name="shopId" 
                      type="left_join" 
                      :label="$t('ewBill.field.shopId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.deviceStream')" prop="deviceStream" :size="$store.state.app.size" >
            <el-input v-model="record.deviceStream" name="deviceStream" 
                      type="text" 
                      :label="$t('ewBill.field.deviceStream')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('ewBill.field.deviceDt')" prop="deviceDt" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('ewBill.field.deviceDt')}}</span>
            <cg-date-picker v-model="record.deviceDt" name="deviceDt" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('ewBill.field.deviceDt')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.tradeNo')" prop="tradeNo" :size="$store.state.app.size" >
            <el-input v-model="record.tradeNo" name="tradeNo" 
                      type="text" 
                      :label="$t('ewBill.field.tradeNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.operatorNo')" prop="operatorNo" :size="$store.state.app.size" >
            <el-input v-model="record.operatorNo" name="operatorNo" 
                      type="text" 
                      :label="$t('ewBill.field.operatorNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('ewBill.field.linkNo')" prop="linkNo" :size="$store.state.app.size" >
            <el-input v-model="record.linkNo" name="linkNo" 
                      type="text" 
                      :label="$t('ewBill.field.linkNo')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit clearable />
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
  name: 'CgFormEwBill',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      allowEditRecord: false,
      allowAddRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'no',
      noSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.no : null,
      dictionary: {
        dictCostType: this.getDictionary('1,2,3','钱包,计次,计时')
      },
      generatorName: 'ewBill',
      baseUrl: '/ewallet/ewBill'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.operationType === null) this.record.operationType = undefined
      if (this.record.projectId === null) this.record.projectId = undefined
      if (this.record.projectPrice === null) this.record.projectPrice = undefined
      if (this.record.amount === null) this.record.amount = undefined
      if (this.record.amountBefore === null) this.record.amountBefore = undefined
      if (this.record.bonus === null) this.record.bonus = undefined
      if (this.record.bonusBefore === null) this.record.bonusBefore = undefined
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormEwBill-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
