
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('devDownloadPlan.field.userNo')" prop="userNo" :size="$store.state.app.size" >
            <el-input v-model="record.userNo" name="userNo" 
                      type="text" 
                      :label="$t('devDownloadPlan.field.userNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('devDownloadPlan.field.readerNo')" prop="readerNo" :size="$store.state.app.size" >
            <el-input v-model="record.readerNo" name="readerNo" 
                      type="text" 
                      :label="$t('devDownloadPlan.field.readerNo')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="20" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('devDownloadPlan.field.type')" prop="type" :size="$store.state.app.size" >
            <cg-number-input v-model="record.type" name="type" 
                             :readonly="isDetail" 
                             :label="$t('devDownloadPlan.field.type')" :placeholder="$t('system.message.needValue')" 
                             />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('devDownloadPlan.field.downloadNum')" prop="downloadNum" :size="$store.state.app.size" >
            <cg-number-input v-model="record.downloadNum" name="downloadNum" 
                             :readonly="isDetail" 
                             :label="$t('devDownloadPlan.field.downloadNum')" :placeholder="$t('system.message.needValue')" 
                             />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('devDownloadPlan.field.time')" prop="time" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('devDownloadPlan.field.time')}}</span>
            <cg-date-picker v-model="record.time" name="time" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('devDownloadPlan.field.time')" :readonly="isDetail" 
                            format="yyyy-MM-dd HH:mm" />
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
  name: 'CgFormDownloadPlan',
  mixins: [ParentForm],
  props: {
  },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      generatorName: 'devDownloadPlan',
      baseUrl: '/reader/devDownloadPlan'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.type === null) this.record.type = undefined
      if (this.record.downloadNum === null) this.record.downloadNum = undefined
    },
    newRecord: function() {
        return {
            userNo: '',
            readerNo: '',
            type: 0,
            downloadNum: 3,
            time: new Date(),
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDownloadPlan-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
