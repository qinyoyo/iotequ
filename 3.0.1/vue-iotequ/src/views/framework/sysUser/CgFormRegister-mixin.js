import { toThousandFilter } from "@/filters"

export default {
  created() { 
    this.baseUrl = '/login/register' // 修改注册路径
  },
  mounted() {
    const that=this
    setTimeout(()=>{
      that.record = that.newRecord()
      that.$refs.cgForm.clearValidate()
    },1000)
  },
  methods: {
    useMixinMethodsFirst() {
      return true
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