<script>
import { addClass,removeClass } from '@/utils/dom'
export default {
    created() {
      document.addEventListener("fullscreenchange", this.fullscreenchangeEvent)
    },
    computed: {
      content() {
        return this.$store.state.user.orgName
      },
      title() {
        return 'ckRegister.action.register'.local()
      }
    },
    methods: {
      fullscreenchangeEvent(e) {
        let $el = document.querySelector('.cg-form-ckRegister.el-dialog__wrapper')
        if ($el) {
          if (document.fullscreenElement) {
            addClass($el,'hide-close')
          } else {
            removeClass($el,'hide-close')
          }
        } else document.removeEventListener("fullscreenchange",this.fullscreenchangeEvent)
      }
    }
}
</script>
<style lang="scss" scope>
.cg-form-ckRegister {
  background-image: url(/static/login_back_h.jpg)!important;
  .el-page-header__content {
    font-size: 32px;
    color:blue;
  }
  .img-center-box {
    margin: 10px;
  }
  .el-dialog__body {
    padding-bottom: 0;
  }
}
.hide-close .el-dialog__headerbtn {
  display:none;
}
.ck-register-message .el-message__icon {
  font-size:24px;
}
</style>
  