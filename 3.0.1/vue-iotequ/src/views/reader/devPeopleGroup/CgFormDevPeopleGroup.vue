
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <cg-join v-model="userNoJoinVisible" :readonly="isDetail" >
        <CgListDevPeopleSelect slot="popover" ref="userNoJoin" openID="userno-join" :height="500" :joinShow="userNoJoinVisible" :joinMultiple="true"
                               :originSelections="String(record.userNo)" selectionKey="userNo" joinMode @closeJoinList="(rows)=>{ getJoinFields('userNo',rows)}"
                               @showJoinList="userNoJoinVisible=true" />
        <el-form-item class="cg-item-mltext cg-auto-focus" slot="reference" :label="$t('devPeople.field.realName')" prop="realName" :size="$store.state.app.size" >
          <cg-input v-model="record.realName" name="realName" 
                    type="mltext" 
                    :label="$t('devPeople.field.realName')" :placeholder="$t('system.message.needValue')" 
                    resize autofocus validate-event 
                     clearable @clear="clearJoinValues(myself,'userNoJoin')"/>
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
import CgListDevPeopleSelect from '@/views/reader/devPeople/CgListDevPeopleSelect.vue'
import rulesObject from './rules.js'
import ParentForm from '@/views/common-views/components/form'
const Comp = {
  name: 'CgFormDevPeopleGroup',
  mixins: [ParentForm],
  props: {
    routeParams: {
      type: Object,
      default: null
    }
  },
  components: { CgListDevPeopleSelect },
  data() {
    return {
      defaultLabelPosition: 'top',
      rulesObject,
      isDialogForm: true,
      path: 'record',
      idField: 'id',
      fastMultiJoinField: 'userNo',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      userNoJoinVisible: false,
      generatorName: 'devPeopleGroup',
      baseUrl: '/reader/devPeopleGroup'
    }
  },
  computed: {
  },
  methods: {
    newRecord: function() {
        return {
            groupId: 0,
            realName: null,
        }
    },
    getJoinFields(field,rows) {
      const joinDefine = {
        userNo: {
          valueField: 'userNo',
          fields: 'realName=realName,orgCode=orgCode'
        },
      }
      this[field+'JoinVisible'] = false
      this.setJoinValues(this.record, field, joinDefine[field], rows)
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormDevPeopleGroup-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
