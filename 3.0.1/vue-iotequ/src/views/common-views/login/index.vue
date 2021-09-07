<template>
  <div class="login-container" @click="errorMessage=''">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" 
             class="form-container login-form" :style="loginStyle"
             autocomplete="on" label-position="left">
      <span><img src="/static/logo.png" class="logo-img"></span>
      <div class="title-container">
        <div class="title">{{ $t(isRegister?'login.register':'login.title') }}</div>
        <lang-select class="set-language" />
        <div class="subtitle">{{ $t('login.subtitle') + this.$store.getters.title }}</div>
      </div>

      <el-form-item v-if="!isWechat" prop="userName">
        <MDinput v-model="loginForm.userName" required
          @keyup.enter.native="handleLogin"
          ref="userName"
          :icon="isSmsRandCode?'fa fa-mobile':'user'"
          name = "userName"
          :type = "isSmsRandCode?'number':'text'"
          value = "String"
          :clearable = "true"
          :readonly = "false"
          :disabled = "false"
          :maxlength = "50"
          :label = "$t(isSmsRandCode?(isRegister?'login.phone':'login.mobile'):'login.userName')"
          :placeholder="$t(isSmsRandCode?(isRegister?'login.phone':'login.mobile'):'login.userName')"
        >
        </MDinput>
      </el-form-item>

      <el-tooltip v-if="isUserName || (isSmsRandCode && smsDelay)" v-model="capsTooltip" :content="$t('login.capsLock')" placement="right" manual :tabindex="-1">
        <el-form-item prop="password">
          <MDinput v-model="loginForm.password" required
            @keyup.native="checkCapslock"
            @blur="capsTooltip = false"
            @keyup.enter.native="handleLogin"
            ref="password"
            icon = "password"
            name = "password"
            :type = "isSmsRandCode ? 'number' : 'visible-password'"
            value = "String"
            :readonly = "false"
            :disabled = "false"
            :maxlength = "16"
            :label = "isSmsRandCode ? $t('login.smsRandCode') : $t('login.password')"
            :placeholder="isSmsRandCode ? $t('login.smsRandCode') : $t('login.password')"
          >
          </MDinput>
        </el-form-item>
      </el-tooltip>

      <el-form-item v-if="isUserName || (isSmsRandCode && !smsDelay)" prop="randCode">
        <MDinput v-model="loginForm.randCode" required
          @keyup.enter.native="handleLogin"
          @click.native="loginForm.randCode=''"
          ref="randCode"
          icon = "international"
          name = "randCode"
          :type = "isMobile?'number':'text'"
          value = "String"
          :clearable = "false"
          :readonly = "false"
          :disabled = "false"
          :maxlength = "6"
          :label = "$t('login.randCode')"
          :placeholder="$t('login.randCode')"
        >
        <span class="rand-code-icon">
          <img :src="randCode" :title="$t('login.refresh')" @click="refreshRandCode">
        </span>
        </MDinput>
      </el-form-item>

      <img v-if="isWechat" class="qr-img" :src="qrurl" :title="$t('login.type_wechat')" />

      <el-form-item v-if="!isRegister" prop="rememberMe">
        <el-checkbox
          ref="rememberMe"
          v-model="loginForm.rememberMe"
          name="remember-me"
          :label="$t('login.remember')"
        />      
      </el-form-item>

      <el-button v-if="!isWechat" :loading="loading" type="primary" style="width:100%;margin-bottom:10px;" 
                 @click.native.prevent="handleLogin">
        {{ loginText() }}
      </el-button>
      <div v-if="isRegister" class="text-command">
        <span @click="isRegister=false">{{$t('login.title')}}</span>
      </div>
      <div v-else class="text-command">
        <span @click.stop="forget">{{$t('login.forget')}}</span>
        <span style="color:grey"> ∣ </span>
        <span @click="setLoginType('register')">{{$t('login.register')}}</span>
      </div>
      <div v-if="!isRegister" style="text-align:center;width:100%;margin-bottom:30px;">
          <cg-icon :size="24" color="#009cea" icon="icon-span svg-key pointer-cursor"  @click="setLoginType('user')" />
          <cg-icon :size="24" color="#009cea" icon="icon-span svg-sms pointer-cursor"  @click="setLoginType('mobile')" />
          <cg-icon :size="24" color="green" icon="icon-span svg-wechat pointer-cursor" @click="setLoginType('wechat')" />
          <cg-icon v-if="hasU53" :title="$t('login.useU53')" :size="24" style="right:30px;position:absolute" color="#74bcff" icon="icon-span svg-finger-w" />
      </div>
      <div v-if="isRegister" style="padding:15px" />
      <el-alert class="login-alert"
        v-show="errorMessage"
        :title="errorMessage"
        :type="errorMessage===$t('login.findPassword')?'info':'error'"
        :closable="false"
        show-icon>
      </el-alert>
    </el-form>
  </div>
</template>

<script>
import LangSelect from '@/layout/components/LangSelect'
import MDinput from '@/components/MDinput'
import { apiUrl } from '@/utils/requestService'
import { request } from '@/utils/request'
import Cookies from 'js-cookie'
import { generateTitle } from '@/utils/i18n'
import {u53Disconnect,u53Connect,u53Read} from '@/utils/u53'
export default {
  name: 'Login',
  components: { LangSelect, MDinput },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (this.isWechat) callback()
      else if (!value || !value.trim()) {
        callback(new Error(this.$i18n.t('login.v_user')))
      } else if (this.isSmsRandCode && !(new RegExp('^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$', 'gm').test(value))) {
        callback(new Error(this.$i18n.t('login.v_mobile')))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (this.isWechat || (this.isSmsRandCode && !this.smsDelay)) callback()
      else if (!value || !value.trim()) {
        callback(new Error(this.$i18n.t('login.v_pass')))
      } else {
        callback()
      }
    }
    const validateRandCode = (rule, value, callback) => {
      if (this.isWechat || (this.isSmsRandCode && this.smsDelay)) callback()
      if (!value || !value.trim()) {
        callback(new Error(this.$i18n.t('login.v_vc')))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        userName: this.$route.query.isRegister?'':Cookies.get('user'),
        password: '',
        randCode: '',
        rememberMe: false
      },
      loginRules: {
        userName: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        randCode: [{ required: true, trigger: 'blur', validator: validateRandCode }]
      },
      qrurl: '',
      openid: '',
      isRegister: this.$route.query.isRegister?true:false,
      isUserName: this.$route.query.isRegister?false:true,
      isSmsRandCode: this.$route.query.isRegister?true:false,
      isWechat: false,
      smsDelay: 0,
      smsTimer: null,
      errorMessage: '',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      randCode: '',
      otherQuery: {},
      hasU53: false,
      loginStyle: window.userSettings.loginStyle,
      isMobile: this.$store.state.app.device === 'mobile'
    }
  },
  computed: {
    isMobilePhone() {
      return this.loginForm.userName && new RegExp('^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$', 'gm').test(this.loginForm.userName)
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    const that = this
    u53Disconnect(_=>{
      u53Connect(0, _=>{
        that.hasU53 = true
        that.u53read()
      })
    })
  },
  mounted() {
    this.refreshRandCode()
    document.getElementsByName('userName')[0].focus()    
  },
  destroyed () {
    if (this.hasU53) {
      this.hasU53=false
      u53Disconnect()
    }
  },
  methods: {
    u53read() {
      const that=this
      if (this.hasU53) {
        u53Read((data)=>{
          if(data.isSucc){
              that.u53login(data.Msg)
          }
        },_=>{
          that.u53read()
        })
      }
    },
    u53login(template) {
      const _this=this
      if (template && template.length == 576) {
        const submitData = { password:template,  userName: 'svas_vein_information' }
        submitData['remember-me'] = _this.loginForm.rememberMe
        submitData.locale = _this.$store.getters.language
        try {
          _this.$store.dispatch('user/login', submitData)
            .then(() => {
              _this.$store.dispatch('user/getInfo').then(() => {
                if (_this.smsTimer) {
                  clearTimeout(_this.smsTimer)
                  _this.smsTimer = null
                }
                _this.$router.push({ path: _this.redirect || '/', query: _this.otherQuery })
              }).catch((error) => {
                _this.handleError(error)
                if (_this.hasU53) _this.u53read()
              })
            })
            .catch((error) => {
              _this.handleError(error)
              if (_this.hasU53) _this.u53read()
            })
        } catch (error) {
            _this.handleError(error)
            if (_this.hasU53) _this.u53read()
        }      
      }
    },
    loginText() {
      if (this.isUserName) return this.$t('login.type_user')
      else if (this.isWechat) return this.$t('login.type_wechat')
      else if (this.isRegister) return this.$t('login.send')
      else if (this.smsDelay) return this.$t('login.type_mobile') + '(' + this.smsDelay + 's)'
      else return this.$t('login.send')
    },
    forget() {
      this.errorMessage=this.$t('login.findPassword')
      this.setLoginType('mobile')
    },
    setLoginType(type) {
      const _this = this
      if (type ==='user') {
        this.isUserName = true
        this.isSmsRandCode = false
        this.isWechat = false
        this.isRegister = false
      } else if (type==='mobile') {
        this.isUserName = false
        this.isSmsRandCode = true
        this.isWechat = false
        this.isRegister = false
      } else if (type==='register') {
        this.isUserName = false
        this.isSmsRandCode = true
        this.isWechat = false
        this.isRegister = true
        this.loginForm.userName = ''
        document.getElementsByName('userName')[0].focus()
      } 
      else if (type==='wechat') {
        const req = {
          url: '/login/wechatQrurl',
          method: 'get'
        }
        request(req, false).then(res => {
          if (res && res.success && res.qrurl) {
            this.isSmsRandCode = false
            this.isUserName = false
            this.isWechat = true
            this.isRegister = false
            this.qrurl = res.qrurl
            setTimeout(_=>{
              _this.checkOpenId()
            },1000)
          }
        })
      } else {
        this.isSmsRandCode = false
        this.isWechat = false
        this.isUserName = true
      }
    },
    checkOpenId() {
      const _this = this
      if (this.isWechat) {
        const req = {
          url: '/res/checkopenid',
          method: 'post'
        }
        request(req, true).then(res => {
          if (res && res.success && res.openid) {
            _this.openid = res.openid
            _this.handleLogin()
          } else {
            _this.checkOpenId()
          }
        }).catch(_ =>  _this.checkOpenId() )      
      }
    },
    sendSms() {
      const that = this
      if (this.loginForm.randCode && that.loginForm.userName) {
        const req = {
          url: '/login/sendMobileCode',
          method: 'post',
          params: { mobilePhone : that.loginForm.userName,randCode: that.loginForm.randCode, isRegister: that.isRegister }
        }
        request(req, false).then(res => {
          if (res) {
            if (res.success || res.error === 'sms_too_frequently') {
              if (that.isRegister) {
                that.$router.push({ path: '/register', 
                  query: { 
                    record : {
                      mobilePhone : that.loginForm.userName,
                      randCode: ''
                    }
                  }
                })
              } else {
                this.smsDelay = 120
                this.smsTimer = setInterval(function () {
                  if (that.smsDelay > 0) {
                    that.smsDelay --
                  } else {
                    that.smsDelay = 0
                    clearTimeout(that.smsTimer)
                    that.smsTimer = null
                  }
                }, 1000)
              }
            } else that.handleError(res)
          }
        }).catch(error=>{
          that.handleError(error)
        })
      }
    },
    checkCapslock({ shiftKey, key } = {}) {
      if (key && key.length === 1) {
        if (shiftKey && (key >= 'a' && key <= 'z') || !shiftKey && (key >= 'A' && key <= 'Z')) {
          this.capsTooltip = true
        } else {
          this.capsTooltip = false
        }
      }
      if (key === 'CapsLock' && this.capsTooltip === true) {
        this.capsTooltip = false
      }
    },
    refreshRandCode() {
      this.randCode = apiUrl('/res/randCodeImage?click=' + new Date().getTime())
      this.loginForm.randCode=''
    },
    handleError(error) {
      if (typeof(error) === 'string') this.errorMessage = error
      else {
        let msg = ''
        if (typeof error.error === 'string') msg=generateTitle((error.error.indexOf('.')>0 ? '' : 'error.')+error.error)
        else if (typeof error.message === 'string' && error.message.toLowerCase().indexOf('proxy')< 0 ) msg = error.message 
        else msg = generateTitle('error.network_error')
        this.errorMessage = msg
      }
      this.refreshRandCode()
      this.loading = false
    },
    handleLogin() {
      const _this = this
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          if (_this.isSmsRandCode && (_this.isRegister || !_this.smsDelay)) {
             _this.sendSms()
          } else {
            _this.loading = true
            const submitData = { ..._this.loginForm }
            submitData['remember-me'] = _this.loginForm.rememberMe
            submitData.locale = _this.$store.getters.language
            if (_this.isSmsRandCode) {
              submitData.isSmsRandCode = true
              submitData.randCode = _this.loginForm.password
            }
            else if (_this.isWechat) {
              submitData.isWechat = true
              if (_this.openid) submitData.mpOpenId = _this.openid
            }
            try {
              _this.$store.dispatch('user/login', submitData)
                .then(() => {
                  _this.$store.dispatch('user/getInfo').then(() => {
                    if (_this.smsTimer) {
                      clearTimeout(_this.smsTimer)
                      _this.smsTimer = null
                    }
                    _this.$router.push({ path: _this.redirect || '/', query: _this.otherQuery })
                    _this.loading = false
                  }).catch((error) => {
                    _this.handleError(error)
                  })
                })
                .catch((error) => {
                  _this.handleError(error)
                })
            } catch (error) {
                _this.handleError(error)
            }
          }
        } else {
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
  }
}
</script>


<style lang="scss" scoped>
.logo-img {
  height:36px;
  padding-bottom: 10px;
  width:auto;
}
.qr-img {
  width: 100%;
  height: auto;
}
.icon-span {
  margin: 0 8px;
}
.login-container {
  display:flex;
  align-items: center;
  height: 100%;
  width: 100%;
  background-size:cover;
  background-color: #fff0;
  box-sizing: border-box;
  overflow: hidden;
  .login-form {
    position: relative;
    width: 360px;
    max-width: 100%;
    max-height: 100%;
    padding: 36px 36px 0;
    background-color: #fff;
    margin: 0 auto;
    overflow: auto;
    .el-form-item {
      margin: 0 0 10 0;
    }
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .title-container {
    position: relative;

    .title {
      font-size: 24px;
      text-align: left;
      font-weight: bold;
    }
    .subtitle {
      margin:4px 0 10px 0;
      font-size: 12px;
      text-align: left;
    }
    .set-language {
      color: #fff;
      position: absolute;
      top: 3px;
      font-size: 18px;
      right: 0px;
      cursor: pointer;
    }
  }

  .rand-code-icon {
    position: absolute;
    right: 2px;
    top: -12px;
    height: 16px;
    cursor: pointer;
    user-select: none;
  }
  .login-alert {
    margin: 0 0 36px;
  }
  .text-command {
    font-size: 10px;
    text-align: right;
    padding-bottom: 10px;
    span:hover {
      color: green
    }
  }
}
@media screen and (max-aspect-ratio: 4/3) {  // portrait
  .login-container {
    background-image: url(/static/login_back_v.jpg)!important;
  }
}
@media screen and (min-aspect-ratio: 4/3) {
  .login-container {
    background-image: url(/static/login_back_h.jpg)!important;
  }   
}
</style>
