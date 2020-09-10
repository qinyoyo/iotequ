
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <cg-join v-model="userIdJoinVisible" :readonly="isDetail" >
        <CgListUserJoin slot="popover" ref="userIdJoin" openID="userid-join" :height="500" :joinShow="userIdJoinVisible" :joinMultiple="true"
                        :originSelections="record.userId" selectionKey="id" joinMode @closeJoinList="(rows)=>{ getJoinFields('userId',rows)}"
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
import cg from '@/utils/cg'
import cgForm from '@/utils/cgForm'
import CgListUserJoin from '@/views/framework/sysUser/CgListUserJoin.vue'
import rulesObject from './rules.js'
const mixins = []
const mixinContext = require.context('.', false, /CgFormPmPeople-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgFormPmPeople',
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
  components: { CgListUserJoin },
  data() {
    return {
      myself: this,
      path: 'record',
      title: this.$t('pmPeople.title.'+this.path),
      rules: {},
      idField: 'id',
      fastMultiJoinField: 'userId',
      idSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.id : null,
      onChange: typeof this.openParams().onChange === 'function' ? this.openParams().onChange : null,
      recordChanged: false,
      recordLoading: false,
      fixedFields: typeof this.openParams().fixedFields === 'object' ? this.openParams().fixedFields : {},
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      record: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record : {},
      needDefaultFromServer: false,
      userIdJoinVisible: false,
      generatorName: 'pmPeople',
      baseUrl: '/project/people/pmPeople'
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
      return (this.mobile?'cg-form-cell ':'')+'cg-no-border cg-form-pmpeople'
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
      return !this.openMode || this.openMode === 'add'
    },
    isEdit() {
      return false
    }
  },
  watch: {
    record: {
      handler() {
        this.recordChanged = true
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
  },
  mounted() {
    cgForm.form_mounted(this)
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    newRecord: function() {
        return {
            realName: null,
        }
    },
    submit: function() {
      if (this.recordChanged) cgForm.form_submit(this, 'save',true)
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
    },
    doAction(action, options) {
      cgForm.form_doAction(this, action, options)
    },
    ...cg
  }
}
</script>
