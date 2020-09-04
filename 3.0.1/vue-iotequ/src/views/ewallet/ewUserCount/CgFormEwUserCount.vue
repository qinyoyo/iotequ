
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-left_join cg-auto-focus" :label="$t('ewUserCount.field.userNo')" prop="userNo" :size="$store.state.app.size" >
        <el-input v-model="record.userNo" name="userNo" 
                  type="left_join" :maxlength="45" show-word-limit 
                  :label="$t('ewUserCount.field.userNo')" :placeholder="$t('system.message.needValue')" 
                  :readonly="isDetail" resize autofocus validate-event />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-left_join" :label="$t('ewUserCount.field.countId')" prop="countId" :size="$store.state.app.size" >
            <el-input v-model="record.countId" name="countId" 
                      type="left_join" 
                      :label="$t('ewUserCount.field.countId')" :placeholder="$t('system.message.needValue')" 
                      :readonly="isDetail" resize autofocus validate-event />
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
import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormEwUserCount-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormEwUserCount',
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
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('ewUserCount.title.'+this.path),
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
      generatorName: 'ewUserCount',
      baseUrl: '/ewallet/ewUserCount'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-ewusercount'
    },
    labelWidth() {
      return this.className.indexOf('cg-form-cell')>=0? '100px' : undefined
    },
    labelPosition() {
      return this.className.indexOf('cg-form-cell')>=0? 'left':'top'
    },
    isDetail() {
      return this.openMode === 'view'
    },
    isNew() {
      return false
    },
    isEdit() {
      return this.openMode === 'edit'
    }
  },
  watch: {
    record: {
      handler() {
        this.recordChanged = true
        this.just4elInputNumberNullBug()
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
    if (this.queryById) {
      cgForm.form_getRecordFromServer(this,this.queryById)
      this.queryRefreshId = this.queryById
    } else if (this.isNew) cgForm.form_createNewRecord(this)
    else if ((this.isEdit || this.isDetail) && this.openParams().id && typeof this.openParams().record !== 'object') {
      cgForm.form_getRecordFromServer(this,this.openParams().id)
      this.queryRefreshId = this.openParams().id
    }
    this.just4elInputNumberNullBug()
  },
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    just4elInputNumberNullBug: function() {
      if (this.record.amountCount === null) this.record.amountCount = undefined
      if (this.record.costLimit === null) this.record.costLimit = undefined
      if (this.record.dayLimit === null) this.record.dayLimit = undefined
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
