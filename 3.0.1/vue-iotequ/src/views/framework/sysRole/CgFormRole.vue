
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysRole.field.code')" :title="$t('sysRole.field.codeValid')" prop="code" :size="$store.state.app.size" >
        <el-input v-model="record.code" name="code" 
                  type="text" 
                  :label="$t('sysRole.field.code')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="8" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysRole.field.name')" prop="name" :size="$store.state.app.size" >
        <el-input v-model="record.name" name="name" 
                  type="text" 
                  :label="$t('sysRole.field.name')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="45" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysRole.field.note')" prop="note" :size="$store.state.app.size" >
        <el-input v-model="record.note" name="note" 
                  type="text" 
                  :label="$t('sysRole.field.note')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="64" show-word-limit clearable />
      </el-form-item>
    </el-form>
    <div v-if="!isDetail" class="cg-form-buttons">
      <el-divider />
      <el-button v-if="!mobile && hasAuthorityOf(myself,baseUrl,'add',record)" class="cg-button" type="default" plain icon="fa fa-plus"
                 @click.native="doAction('add')">
        {{ $t('system.action.new') }}
      </el-button>
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
  name: 'CgFormRole',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      allowViewRecord: false,
      allowEditRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      generatorName: 'sysRole',
      baseUrl: '/framework/sysRole'
    }
  },
  methods: {
    newRecord: function() {
        return {
            code: '',
            name: '',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormRole-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
