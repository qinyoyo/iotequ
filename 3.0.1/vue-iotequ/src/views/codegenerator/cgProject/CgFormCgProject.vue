
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text cg-auto-focus" :label="$t('cgProject.field.code')" prop="code" :size="$store.state.app.size" >
            <el-input v-model="record.code" name="code" 
                      type="text" 
                      :label="$t('cgProject.field.code')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="10" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.name')" :title="$t('cgProject.field.nameValid')" prop="name" :size="$store.state.app.size" >
            <el-input v-model="record.name" name="name" 
                      type="text" 
                      :label="$t('cgProject.field.name')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.version')" prop="version" :size="$store.state.app.size" >
            <el-input v-model="record.version" name="version" 
                      type="text" 
                      :label="$t('cgProject.field.version')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="36" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('cgProject.field.groupId')" prop="groupId" :size="$store.state.app.size" >
            <el-input v-model="record.groupId" name="groupId" 
                      type="text" 
                      :label="$t('cgProject.field.groupId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="45" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-text" :label="$t('cgProject.field.note')" prop="note" :size="$store.state.app.size" >
        <el-input v-model="record.note" name="note" 
                  type="text" 
                  :label="$t('cgProject.field.note')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-select" :label="$t('cgProject.field.modules')" prop="modules" :size="$store.state.app.size" >
        <cg-select v-model="record.modules" name="modules"
                   :dictionary="dictionary.dictModules" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.unknown')" clearable />
      </el-form-item>
      <el-form-item class="cg-item-url" :label="$t('cgProject.field.mavenServer')" prop="mavenServer" :size="$store.state.app.size" >
        <el-input v-model="record.mavenServer" name="mavenServer" 
                  type="url" 
                  :label="$t('cgProject.field.mavenServer')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="100" show-word-limit clearable />
      </el-form-item>
      <el-row :gutter="mobile?0:20">
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.springModule')" prop="springModule" :size="$store.state.app.size" >
            <el-switch v-model="record.springModule" name="springModule" :active-text="mobile?'':$t('cgProject.field.springModule')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.mavenModule')" prop="mavenModule" :size="$store.state.app.size" >
            <el-switch v-model="record.mavenModule" name="mavenModule" :active-text="mobile?'':$t('cgProject.field.mavenModule')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item class="cg-item-boolean" :label="$t('cgProject.field.test')" prop="test" :size="$store.state.app.size" >
            <el-switch v-model="record.test" name="test" :active-text="mobile?'':$t('cgProject.field.test')" inactive-text="" :disabled="isDetail"  />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item class="cg-item-textarea" :label="$t('cgProject.field.addtionalDependencies')" prop="addtionalDependencies" :size="$store.state.app.size" >
        <el-input v-model="record.addtionalDependencies" name="addtionalDependencies" 
                  type="textarea" 
                  :label="$t('cgProject.field.addtionalDependencies')" :placeholder="$t('system.message.unknown')" clearable 
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
  name: 'CgFormCgProject',
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
      needDefaultFromServer: true,
      dictionary: {
        dictModules: []
      },
      needLoadDictionary: true,
      generatorName: 'cgProject',
      baseUrl: '/codegenerator/cgProject'
    }
  },
  methods: {
    newRecord: function() {
        return {
            creator: this.$store.state.user.name,
            code: '',
            name: '',
            groupId: 'top.iotequ',
            modules: 'framework',
            springModule: false,
            mavenModule: true,
            test: false,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormCgProject-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
