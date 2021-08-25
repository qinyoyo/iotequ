
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="hide-item-label" prop="orgCode" :size="$store.state.app.size" >
        <cg-cascader v-model="record.orgCode" name="orgCode"
                     readonly :filterable="false" numberic 
                     :dictionary="dictionary.dictOrgCode" show-all-levels :placeholder="$t('system.message.needValue')" />
      </el-form-item>
      <el-form-item class="hide-item-label" prop="mode" :size="$store.state.app.size" >
        <cg-radio v-model="record.mode" name="mode" :dictionary="dictionary.dictMode" :readonly="isDetail"  />
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
  name: 'CgFormRegister',
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
      continueAdd: true,
      path: 'record',
      idField: 'id',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      dictionary: {
        dictSex: [],
        dictOrgCode: [],
        dictMode: this.getDictionary('auto,on,off,cancel,remove','ckRegister.field.mode_0,ckRegister.field.mode_1,ckRegister.field.mode_2,ckRegister.field.mode_3,ckRegister.field.mode_4')
      },
      needLoadDictionary: true,
      generatorName: 'ckRegister',
      baseUrl: '/check-in/ckRegister'
    }
  },
  methods: {
    newRecord: function() {
        return {
            userNo: '',
            name: '',
            sex: '1',
            orgName: '',
            inDate: new Date(),
            onTime: new Date(),
            orgCode: this.$store.state.user.org,
            mode: 'auto',
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormRegister-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
