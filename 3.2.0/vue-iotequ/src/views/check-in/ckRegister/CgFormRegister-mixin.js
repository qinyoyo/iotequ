import {u53Connect,u53Read2,u53DisplayMessage, u53MessageCapacity, u53Version, u53Cancel} from '@/utils/u53'
import { fullScreen, isFullScreen, exitFullScreen} from '@/layout/components/Screenfull/index'
import { request } from '@/utils/request'
import { Message } from 'element-ui'

export default {
    created () {
      const that = this
      this.ignoreRecordChanged = true
      this.dialogClosed = false
      this.checkAndConnectU5(_=>{
        that.keepLogin()
        that.u53read()
      })
    },
    destroyed() {
      this.dialogClosed = true
      if (this.keepLoginTimerId) clearInterval(this.keepLoginTimerId)
      if (!this.isFullScreen && isFullScreen()) exitFullScreen()
      u53Cancel()
    },
    methods: {
      checkAndConnectU5(callback) {
        const _this=this
        if (!_this || _this.dialogClosed) return
        const connect = (supportMessage)=>{
          if (!_this || _this.dialogClosed) return
          u53Connect(0, _=>{
            _this.foundU53()
            if (_this.routeParams && _this.routeParams.background) {
              if (!supportMessage) {
                _this.showMessage('驱动版本不支持后台消息')
              }
              _this.runBackground = supportMessage
            } 
            if (!_this.runBackground) {
              _this.isFullScreen = isFullScreen()
              if (!_this.isFullScreen) fullScreen()
            }
            callback() 
          },(res)=>{
            if (supportMessage && res && res.retCode == 20) {
              _this.showMessage('设备忙',_=>{_this.$emit('closeDialog')})
            } else 
            _this.showMessage('设备连接失败',_=>{_this.$emit('closeDialog')})
          })
        }
        u53Version((res)=>{ 
          if (res.Msg >= '3.2') {
            u53MessageCapacity((r)=>{
              connect(r.isSucc && r.window) 
            },_=>{
              connect(false)
            })
          } 
          else {
            _this.showMessage('需要3.2及以上的驱动版本',_=>{_this.$emit('closeDialog')})
          }
        },(res)=>{
          _this.showMessage('需要开启3.2及以上的驱动服务',_=>{_this.$emit('closeDialog')})
        })
      },
      foundU53() {
        this.hasU53 = true
        if (document.querySelector('img.cg-header-image')) {
          document.querySelector('img.cg-header-image').src = '/static/img/input.gif'
        }
      },
      notFoundU53(){
        this.hasU53 = false
        if (document.querySelector('img.cg-header-image')) document.querySelector('img.cg-header-image').src = '/static/img/not_found.png'
      },
      u53read() {
        const that=this
        if (that && !that.dialogClosed) {
          u53Connect(0,_=>{
              that.foundU53()
              u53Read2((data)=>{
                if(data.isSucc){
                  that.u53login(data.Msg,data.Msg1)
                } 
                else that.u53read()
              },_=>{
                that.notFoundU53()
                that.u53read()
              },{
                compress:true
              })
            },
            _=>{
              that.notFoundU53()
              that.u53read()
            })
        }
      },
      showMessage(res,onClose) {
        let options = {
          type: 'error',
          message: '',
          name: '',
          delay: 3000,
          playSound: false
        }
        if (!res) {
          options.message = '未知错误'
          options.playSound = true
        }
        else if (typeof res === 'string') {
          options.message = res
        }
        else {
          if (res.success) options.type = 'success'
          if (res.data && res.data.name) {
            options.name = res.data.name
          }
          if (res.message) {
            options.message = res.message
          }
          if (res.data && res.data.sound) options.playSound = true
        }
        if (this.runBackground) {
          u53DisplayMessage(options,_=>{
            if (typeof onClose === 'function') onClose()
          },_=>{
            if (typeof onClose === 'function') onClose()
          })
        } else {
          let height=32
          let msg = ''
          const textDiv = '<div style="height: 64px; line-height:64px; vertical-align: middle; font-size:36px; '
          if (options.name) {
            msg = textDiv + 'color: blue;">' + options.name + '</div>'
            height += 64
          }
          if (options.message) {
            msg = msg + textDiv + 'color:'+(options.type=='error' ? 'red' : 'black')+';">'+options.message+'</div>'
            height += 64
          }
          if (res.data && res.data.sound) msg = msg + '<audio src="/static/sound/'+res.data.sound+'" autoplay style="display:none"></audio>'
          Message({
            dangerouslyUseHTMLString:true,
            duration: 2000,
            customClass: 'ck-register-message',
            center: true,
            type: options.type,
            offset: (window.innerHeight - height)/2,
            onClose: onClose,
            message: msg
          })
        }
      },      
      keepLogin() {
        const _this=this
        this.keepLoginTimerId = setInterval(_=>{
          const req = {
            url: _this.baseUrl + '/action/register',
            method: 'post',
            data: { 
                mode: 'check',
                orgCode: _this.record.orgCode
            }
          }
          request(req, true)
        },300000)
      },
      u53login(template,image) {
        const _this=this
        const imageElement = document.querySelector('img.cg-header-image')
        const startRead = function() {
          _this.record.mode = 'auto'  
          imageElement.style.opacity = 1
          imageElement.src= '/static/img/input.gif'
          _this.u53read()
        }
        imageElement.style.opacity = 0.3
        imageElement.src= '/static/img/output.gif'
        if (template && template.length == 576) {
          const req = {
            url: _this.baseUrl + '/action/register',
            method: 'post',
            data: { 
                template:template,  
                image:image,
                mode: _this.record.mode+'',
                orgCode: _this.record.orgCode+''
            }
          }
          request(req, true).then(res => {
            _this.showMessage(res,startRead)
          }).catch(error => {
            _this.showMessage(error,startRead)
          })   
        }
      }
   }
}
  