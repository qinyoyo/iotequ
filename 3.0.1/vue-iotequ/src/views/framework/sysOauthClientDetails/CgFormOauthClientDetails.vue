
<template>
  <div :class="mobile || height==0 ? 'cg-form':'cg-form just-v-scroll'" :style="mobile || height==0?'':'height:'+height+'px'">
    <el-form ref="cgForm" v-loading="recordLoading" :model="record" v-cg-form-adjust
                          :class="className" :rules="rules" 
             :label-position="labelPosition" :label-width="labelWidth" :size="$store.state.app.size" 
             hide-required-asterisk >
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select cg-auto-focus" :label="$t('sysOauthClientDetails.field.authorizedGrantTypes')" prop="authorizedGrantTypes" :size="$store.state.app.size" >
            <cg-select v-model="record.authorizedGrantTypes" name="authorizedGrantTypes"
                       :dictionary="dictionary.dictAuthorizedGrantTypes" :readonly="isDetail" :filterable="false" :allow-create="false" :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.scope')" prop="scope" :size="$store.state.app.size" >
            <el-input v-model="record.scope" name="scope" 
                      type="text" 
                      :label="$t('sysOauthClientDetails.field.scope')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="255" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.clientId')" prop="clientId" :size="$store.state.app.size" >
            <el-input v-model="record.clientId" name="clientId" 
                      type="text" 
                      :label="$t('sysOauthClientDetails.field.clientId')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="255" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.clientSecret')" prop="clientSecret" :size="$store.state.app.size" >
            <el-input v-model="record.clientSecret" name="clientSecret" 
                      type="text" 
                      :label="$t('sysOauthClientDetails.field.clientSecret')" :placeholder="$t('system.message.needValue')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="255" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-select" :label="$t('sysOauthClientDetails.field.authorities')" prop="authorities" :size="$store.state.app.size" >
            <cg-select v-model="record.authorities" name="authorities"
                       :dictionary="dictionary.dictAuthorities" :readonly="isDetail" :filterable="false" :allow-create="false" multiple :placeholder="$t('system.message.needValue')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-show="record.authorizedGrantTypes=='authorization_code' || record.authorizedGrantTypes=='implicit'" class="cg-item-text" :label="$t('sysOauthClientDetails.field.webServerRedirectUri')" prop="webServerRedirectUri" :size="$store.state.app.size" >
            <el-input v-model="record.webServerRedirectUri" name="webServerRedirectUri" 
                      type="text" 
                      :label="$t('sysOauthClientDetails.field.webServerRedirectUri')" :placeholder="$t('system.message.unknown')" 
                      resize autofocus validate-event 
                      :readonly="isDetail" :maxlength="255" show-word-limit clearable />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="mobile?0:20">
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('sysOauthClientDetails.field.accessTokenValidity')" prop="accessTokenValidity" :size="$store.state.app.size" >
            <cg-number-input v-model="record.accessTokenValidity" name="accessTokenValidity" 
                             :readonly="isDetail" 
                             :label="$t('sysOauthClientDetails.field.accessTokenValidity')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="cg-item-number" :label="$t('sysOauthClientDetails.field.refreshTokenValidity')" prop="refreshTokenValidity" :size="$store.state.app.size" >
            <cg-number-input v-model="record.refreshTokenValidity" name="refreshTokenValidity" 
                             :readonly="isDetail" 
                             :label="$t('sysOauthClientDetails.field.refreshTokenValidity')" :placeholder="$t('system.message.needValue')" :precision="0" 
                             :min="0" :step="1" :title="$t('system.message.valueRange') + ': 0 - *'" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item v-show="record.authorizedGrantTypes=='authorization_code'" class="cg-item-textarea" :label="$t('sysOauthClientDetails.field.additionalInformation')" prop="additionalInformation" :size="$store.state.app.size" >
        <el-input v-model="record.additionalInformation" name="additionalInformation" 
                  type="textarea" :maxlength="500" show-word-limit 
                  :label="$t('sysOauthClientDetails.field.additionalInformation')" :placeholder="$t('system.message.unknown')" clearable 
                  :readonly="isDetail" resize :autosize="{ minRows: 1, maxRows: 10 }" autofocus validate-event />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.decription')" prop="decription" :size="$store.state.app.size" >
        <el-input v-model="record.decription" name="decription" 
                  type="text" 
                  :label="$t('sysOauthClientDetails.field.decription')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  :readonly="isDetail" :maxlength="200" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.secret')" prop="secret" :size="$store.state.app.size" >
        <el-input v-model="record.secret" name="secret" 
                  type="text" 
                  :label="$t('sysOauthClientDetails.field.secret')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  readonly :maxlength="36" show-word-limit clearable />
      </el-form-item>
      <el-form-item class="cg-item-text" :label="$t('sysOauthClientDetails.field.authUrl')" prop="authUrl" :size="$store.state.app.size" >
        <el-input v-model="record.authUrl" name="authUrl" 
                  type="text" 
                  :label="$t('sysOauthClientDetails.field.authUrl')" :placeholder="$t('system.message.unknown')" 
                  resize autofocus validate-event 
                  readonly :maxlength="36" show-word-limit clearable />
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
  name: 'CgFormOauthClientDetails',
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
      idField: 'clientId',
      clientIdSaved: this.openParams().record && typeof this.openParams().record === 'object' ? this.openParams().record.clientId : null,
      dictionary: {
        dictAuthorizedGrantTypes: this.getDictionary('password,authorization_code,implicit,client_credentials','密码模式,授权码模式,简化模式,客户端模式'),
        dictAuthorities: [],
        dictAutoapprove: this.getDictionary('true,false')
      },
      needLoadDictionary: true,
      generatorName: 'sysOauthClientDetails',
      baseUrl: '/framework/sysOauthClientDetails'
    }
  },
  methods: {
    just4elInputNumberNullBug: function() {
      if (this.record.accessTokenValidity === null) this.record.accessTokenValidity = undefined
      if (this.record.refreshTokenValidity === null) this.record.refreshTokenValidity = undefined
    },
    newRecord: function() {
        return {
            authorizedGrantTypes: '',
            scope: '',
            clientSecret: '',
            authorities: '',
            accessTokenValidity: 86400,
            refreshTokenValidity: 0,
            autoapprove: '1',
            locked: false,
            enabled: true,
        }
    },
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /CgFormOauthClientDetails-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
    mixins
}
</script>
