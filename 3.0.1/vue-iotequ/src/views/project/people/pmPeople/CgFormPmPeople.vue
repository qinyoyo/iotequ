
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <cg-join v-model="userIdJoinVisible" :readonly="isDetail" >
        <CgListUserJoin slot="popover" ref="userIdJoin" openID="userid-join" :height="500" :joinShow="userIdJoinVisible" :joinMultiple="true"
                        :originSelections="String(record.userId)" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('userId',rows)}"
                        @showJoinList="userIdJoinVisible=true" />
        <el-form-item class="cg-item-text cg-auto-focus" slot="reference" :label="$t('pmPeople.field.userId')" prop="realName" :size="$store.state.app.size" >
          <el-input v-model="record.realName" name="realName" 
                    type="text" 
                    :label="$t('pmPeople.field.userId')" :placeholder="$t('system.message.needValue')" 
                    resize autofocus validate-event 
                     clearable @clear="clearJoinValues(myself,'userIdJoin')"/>
        </el-form-item>
      </cg-join>
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
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormPmPeople',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  components: { CgListUserJoin },
  data() {
    return {
      allowEditRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      fastMultiJoinField: 'userId',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      userIdJoinVisible: false,
      generatorName: 'pmPeople',
      baseUrl: '/project/people/pmPeople'
    }
  },
  computed: {
  },
  methods: {
    newRecord: function() {
        return {
            realName: null,
        }
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        userId: {
          valueField: 'id',
          fields: 'realName=realName,sex=sex,orgCode=orgCode,mobilePhone=mobilePhone'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormPmPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
