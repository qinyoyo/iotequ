import { toThousandFilter } from "@/filters"

export default {
  data() {
    return {
      baseUrl: '/login/register' // 修改注册路径
    }
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    },
    afterTransitionEnd() {
      this.super_afterTransitionEnd()
      this.record = that.newRecord()
      this.$refs.cgForm.clearValidate()
    },
    extendActionFilter(action) {
      if (action === 'add' || action === 'changePassword') return false
      else return true
    },    
    newRecord() {
      return {
        orgCode: 0,
        locked: false,
        state: true,
        passwordErrorTimes: 0,
        name: '',
        realName: '',
        sex: '1',
        idType: 2,
        idNumber: this.$route.query.record.mobilePhone,
        password: '',
        passwordConfirm: '',
        randCode: '',
        mobilePhone : this.$route.query.record.mobilePhone
      }
    }
  }
}