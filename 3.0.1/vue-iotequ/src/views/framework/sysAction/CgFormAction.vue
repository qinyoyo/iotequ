
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-text cg-auto-focus" :label="$t('sysAction.field.note')" prop="note" :size="$store.state.app.size" >
        <el-input v-model="record.note" name="note" 
                  type="text" 
                  :label="$t('sysAction.field.note')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="200" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysAction.field.value')" prop="value" :size="$store.state.app.size" >
        <el-input v-model="record.value" name="value" 
                  type="text" 
                  :label="$t('sysAction.field.value')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysAction.field.params')" prop="params" :size="$store.state.app.size" >
        <el-input v-model="record.params" name="params" 
                  type="text" 
                  :label="$t('sysAction.field.params')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-checkbox" :label="$t('sysAction.field.method')" prop="method" :size="$store.state.app.size" >
        <cg-checkbox v-model="record.method" name="method" :dictionary="dictionary.dictMethod" :readonly="isDetail"  />
      </el-form-item>
      <el-form-item class="cg-item-html" :label="$t('sysAction.field.htmlNote')" prop="htmlNote" :size="$store.state.app.size" >
        <div name="htmlNote">
          <div v-html="$t('sysAction.field.htmlNoteValid')"></div>
          <div v-html="record.htmlNote"></div>
        </div>
      </el-form-item>
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
  name: 'CgFormAction',
  mixins: [ParentForm],
  props: {
    dialogParams: {
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
      dictionary: {
        dictMethod: this.getDictionary('GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE')
      },
      generatorName: 'sysAction',
      baseUrl: '/framework/sysAction'
    }
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormAction-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
