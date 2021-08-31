
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-form-item class="cg-item-boolean cg-auto-focus" :label="$t('devOrgGroup.field.isIncludeSubOrg')" prop="isIncludeSubOrg" :size="$store.state.app.size" >
        <el-switch v-model="record.isIncludeSubOrg" name="isIncludeSubOrg" :active-text="mobile?'':$t('devOrgGroup.field.isIncludeSubOrg')" inactive-text="" :disabled="isDetail"  />
      </el-form-item>
      <cg-join v-model="orgIdJoinVisible" :readonly="isDetail" >
        <CgListOrg slot="popover" ref="orgIdJoin" openID="orgid-join" :height="500" :joinShow="orgIdJoinVisible" :joinMultiple="true"
                   :originSelections="String(record.orgId)" selectionKey="orgCode" joinMode @closeJoinList="(rows)=>{ getJoinFields('orgId',rows)}"
                   @showJoinList="orgIdJoinVisible=true" />
        <el-form-item class="cg-item-mltext" slot="reference" :label="$t('sysOrg.field.orgName')" prop="orgName" :size="$store.state.app.size" >
          <cg-input v-model="record.orgName" name="orgName" 
                    type="mltext" 
                    :label="$t('sysOrg.field.orgName')" :placeholder="$t('system.message.needValue')" 
                    resize autofocus validate-event 
                     clearable @clear="clearJoinValues(myself,'orgIdJoin')"/>
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
import CgListOrg from '@/views/framework/sysOrg/CgListOrg.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormDevOrgGroup',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  components: { CgListOrg },
  data() {
    return {
      allowViewRecord: false,
      allowEditRecord: false,
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      fastMultiJoinField: 'orgId',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      orgIdJoinVisible: false,
      generatorName: 'devOrgGroup',
      baseUrl: '/reader/devOrgGroup'
    }
  },
  computed: {
  },
  methods: {
    newRecord: function() {
        return {
            groupId: 0,
            isIncludeSubOrg: true,
            orgName: null,
        }
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        orgId: {
          valueField: 'orgCode',
          fields: 'orgName=name'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevOrgGroup-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
