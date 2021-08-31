<script>
import { fullScreen, exitFullScreen, isFullScreen} from '@/layout/components/Screenfull/index'
import { addClass,removeClass } from '@/utils/dom'
export default {
    created() {
      const _this=this
      if (!this.routeParams || !this.routeParams.background) {
        document.addEventListener("fullscreenchange", function (e) {
          if (document.fullscreenElement) {
              addClass(_this.$refs.dialog.$el,'hide-close')
            } else {
              removeClass(_this.$refs.dialog.$el,'hide-close')
          }
        })
        this.isFullScreen = isFullScreen()
        if (!this.isFullScreen) fullScreen()
      }
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
      close() {
        this.showDialog = false
        this.$refs.cgForm.dialogClosed = true
        this.$emit('close')
        if (!this.routeParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
        if ((!this.routeParams || !this.routeParams.background) && !this.isFullScreen && isFullScreen()) exitFullScreen()
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
  