<template>
  <div class="cg-form">
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" top="0px" :close-on-click-modal="false" 
               :class="dialogClass" :append-to-body="true" @closed="close">
      <div slot="title" class="color-warning">
        <cg-header icon="fa fa-unlock-alt" :content="$t('login.changePassword')" @goBack="close"
        />
      </div>
      <el-form ref="cgForm" v-loading="recordLoading" :model="record" class="cg-no-border" :rules="rules" 
              label-position="top" label-suffix=":" :size="$store.state.app.size" 
              hide-required-asterisk enctype="multipart/form-data" >
        <el-form-item class="cg-item-password" :label="$t(useRandCode?'login.smsRandCode':'login.oldPassword')" prop="password" :size="$store.state.app.size" >
          <el-input v-model="record.password" name="password" 
                    type="text" :maxlength="16" show-word-limit 
                    :label="$t(useRandCode?'login.smsRandCode':'login.oldPassword')" :placeholder="$t(useRandCode?'login.smsRandCode':'login.oldPassword')" 
                    :show-password="!useRandCode" resize autofocus validate-event/>
        </el-form-item>
        <el-form-item class="cg-item-password" :label="$t('login.newPassword')" prop="newPassword" :size="$store.state.app.size" >
          <el-input v-model="record.newPassword" name="newPassword" 
                    type="text" :maxlength="16" show-word-limit 
                    :label="$t('login.newPassword')" :placeholder="$t('login.newPassword')" 
                    show-password resize autofocus validate-event  />
        </el-form-item>        
        <el-form-item class="cg-item-password" :label="$t('system.message.passwordConfirm')" prop="newPasswordConfirm" :size="$store.state.app.size" >
          <el-input v-model="record.newPasswordConfirm" name="newPasswordConfirm" 
                    type="text" :maxlength="16" show-word-limit 
                    :label="$t('system.message.passwordConfirm')" :placeholder="$t('system.message.needValue')" 
                    show-password resize autofocus validate-event />
        </el-form-item>              
      </el-form>
      <div class="cg-form-buttons">
        <el-divider />
        <el-button class="cg-button" type="primary" plain icon="el-icon-check"  @click.native="submit()">
          {{ $t('system.action.save') }}
        </el-button>
        <el-button v-if="!mobile" class="cg-button" plain icon="el-icon-close" @click.native="close">
          {{ $t('system.action.close') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import {request} from '@/utils/request'
import {validPasswordMessage} from '@/utils/validate'
export default {
  name: 'CgUserChgPass',
  directives: { elDragDialog },
  props: {
    routeParams: {
      type: Object,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    },    
    useRandCode: Boolean
  },
  data() {
    return {
      showDialog: true,
      rules: {
        password:[
          { required: true, message: this.$t('system.message.needValue') + ':' + this.$t('sysUser.field.password'), trigger: 'blur' }
        ],
        newPassword:[
          { required: true, message: this.$t('system.message.needValue') + ':' + this.$t('sysUser.field.password'), trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (!this.useRandCode && value === this.record.password) callback(new Error(this.$t('system.message.nochange')))
              else {
                const msg = validPasswordMessage(value)
                if (msg) callback(new Error(msg))
                else callback()
              }
            },
            trigger: 'blur'
          }
        ],
        newPasswordConfirm:[
          { required: true, message: this.$t('system.message.needValue') + ':' + this.$t('system.message.passwordConfirm'), trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.record.newPassword) callback(new Error('system.message.passwordConfirmValid'.local()))
              else callback()
            },
            trigger: 'blur'
          }
        ],
      },
      recordLoading: false,
      record: {
        password: '',
        newPassword : '',
        newPasswordConfirm : '',
        id: this.$store.state.user.id,
      },
      baseUrl: '/profile/password'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    close() {
      this.showDialog = false
      this.$emit('close')
      if (!this.routeParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
    },
    submit() {
      const _this = this
      this.$refs.cgForm.validate(valid => {
        if (valid) {
          _this.recordLoading = true
          const req = {
            url: '/login/profile/password',
            method: 'POST',
            params: { ..._this.record, useRandCode: this.useRandCode }
          }
          request(req, false).then(res => {
            _this.recordLoading = false
            if (res && res.hasOwnProperty('success') && res.success) {
              _this.close()
            }
            if (typeof res === 'string') this.$router.push('/')
          }).catch(error => {
            _this.recordLoading = false
          })
        }
      })
    }
  }
}
</script>
