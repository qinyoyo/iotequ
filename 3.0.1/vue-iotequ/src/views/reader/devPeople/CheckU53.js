import {u53Version} from '@/utils/u53'
import { Message } from 'element-ui'
export default {
  data() {
    return {
      hasU533_2:false
    }
  },
  created() {
    const _this = this
    u53Version((res)=>{
      if (res.Msg >= '3.2') {
        _this.hasU533_2 = true
      } 
      else {
        Message({
          message: '需要3.2及以上版本',
          type: 'warn',
          duration: 2 * 1000
        })
      }
    },_=>{
      Message({
        message: '没有发现svein driver',
        type: 'warn',
        duration: 2 * 1000
      })
    })
  },
  computed: {
    admin() {
      return this.$store.state.user.name === 'admin'
    },
    enableSample() {
      return this.record.userNo && this.hasU533_2
    }
  },  
  methods: {
    disabledAction(btn) {
      if (btn.action=='sample') return !this.enableSample
      else return false
    }
   }
}
