
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-datetime cg-auto-focus" :label="$t('sysMessage.field.readTime')" prop="readTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('sysMessage.field.readTime')}}</span>
            <cg-date-picker v-model="record.readTime" name="readTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('sysMessage.field.readTime')" :readonly="isDetail" 
                            clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-datetime" :label="$t('sysMessage.field.createTime')" prop="createTime" :size="$store.state.app.size" >
            <span slot="label"><cg-icon icon="fa fa-calendar-plus-o" :size="14" :width="20"/>{{$t('sysMessage.field.createTime')}}</span>
            <cg-date-picker v-model="record.createTime" name="createTime" 
                            :align="mobile?'right':'center'" type="datetime" :title="$t('sysMessage.field.createTime')" :readonly="isDetail" 
                            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMessage.field.receiverName')" prop="receiverName" :size="$store.state.app.size" >
            <el-input v-model="record.receiverName" name="receiverName" 
                      type="text" 
                      :label="$t('sysMessage.field.receiverName')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="50" show-word-limit clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysMessage.field.senderName')" prop="senderName" :size="$store.state.app.size" >
            <el-input v-model="record.senderName" name="senderName" 
                      type="text" 
                      :label="$t('sysMessage.field.senderName')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="50" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('sysMessage.field.title')" prop="title" :size="$store.state.app.size" >
        <el-input v-model="record.title" name="title" 
                  type="text" 
                  :label="$t('sysMessage.field.title')" :placeholder="$t('system.message.needValue')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item class="cg-item-textarea" :label="$t('sysMessage.field.content')" prop="content" :size="$store.state.app.size" >
        <el-input v-model="record.content" name="content" 
                  type="textarea" :maxlength="500" show-word-limit 
                  :label="$t('sysMessage.field.content')" :placeholder="$t('system.message.needValue')" 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
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
  name: 'CgFormMessage',
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
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      generatorName: 'sysMessage',
      baseUrl: '/framework/sysMessage'
    }
  },
  methods: {
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormMessage-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
