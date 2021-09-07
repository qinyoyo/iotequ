<script>
import { fullScreen, exitFullScreen, isFullScreen} from '@/layout/components/Screenfull/index'
import { addClass,removeClass } from '@/utils/dom'
import { u53Disconnect, u53Connect } from '@/utils/u53'
import { Message } from 'element-ui'
export default {
    created() {
      this.u53Connected = false
      const _this=this
      let errorMessage = {
            dangerouslyUseHTMLString:true,
            duration: 2000,
            customClass: 'ck-register-message',
            center: true,
            type: 'error',
            offset: (window.innerHeight - 98)/2,
            onClose: _=>{
              _this.close()
            },
            message: '<div style="height: 64px; line-height:64px; vertical-align: middle; font-size:36px; color:red;">未找到驱动程序</div>'
        }
      u53Disconnect(_=>{
        u53Connect(0, _=>{
          _this.u53Connected = true
          if (!this.routeParams || !this.routeParams.background) {
            document.addEventListener("fullscreenchange", function (e) {
              if (document.fullscreenElement) {
                  addClass(_this.$refs.dialog.$el,'hide-close')
                } else {
                  removeClass(_this.$refs.dialog.$el,'hide-close')
              }
            })
            _this.isFullScreen = isFullScreen()
            if (!_this.isFullScreen) fullScreen()
          }
        },_=>{
          errorMessage.message = '<div style="height: 64px; line-height:64px; vertical-align: middle; font-size:36px; color:red;">设备连接失败</div>'
          Message(errorMessage)
        })
      },_=>{
          Message(errorMessage)
      })
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
        if (this.u53Connected) u53Disconnect()
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
  