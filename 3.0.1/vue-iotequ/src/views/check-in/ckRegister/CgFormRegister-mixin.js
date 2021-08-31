import {u53Disconnect,u53Connect,u53Read} from '@/views/common-views/login/u53read'
import { request } from '@/utils/request'
import { Message } from 'element-ui'

export default {

    created() {
      const that = this
      this.dialogClosed = false
      this.ignoreRecordChanged = true
      this.registerId = new Date().getDate()
      this.runBackground = this.routeParams && this.routeParams.background
      this.keepLogin()
      this.checkU53Busy(_=>{  // 等待上一次调用结束
        u53Disconnect()
        that.u53read()
      })
    },
    methods: {
      checkU53Busy(callback) {
        if (!window.registerId || window.registerId ==this.registerId) {
          window.registerId = this.registerId
          console.log('start u53 read 1')
          callback()
          return
        }
        const _this = this
        let timeId = window.setInterval(_=>{
          if (_this.dialogClosed()) window.clearInterval(timeId)
          else if (!window.registerId || window.registerId ==this.registerId) {
            window.clearInterval(timeId)
            console.log('start u53 read 2')
            callback()
          }
        },2000)
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
        if (!this.dialogClosed) {
          const that=this
          u53Connect(0,_=>{
            that.foundU53()

            u53Read((data)=>{
              u53Disconnect()
              if(data.isSucc) that.u53login(data.Msg)
              else that.u53read()
            },_=>{
              u53Disconnect()
              that.notFoundU53()
              that.u53read()
            })
          },
          _=>{
            that.notFoundU53()
            that.u53read()
          })
        } else {
          u53Disconnect()
          window.registerId = 0
          console.log('close u53 read')
        }
      },
      openNewWindow(res,onClose) {
          let url = window.location.origin + '/static/message.html'
          let height=32
          let params = []
          if (!res) {
            params.push("message="+encodeURI('未知错误'))
            height += 64
          }
          else if (typeof res === 'string') {
            params.push("message="+encodeURI(res))
            height += 64
          }
          else {
            if (res.success) params.push("success=true")
            if (res.data && res.data.name) {
              params.push("name="+encodeURI(res.data.name))
              height += 64
            }
            if (res.message) {
              params.push("message="+encodeURI(res.message))
              height += 64
            }
            if (res.data && res.data.sound) params.push("sound="+encodeURI(res.data.sound))
          }
          if (params.length>0) url = url + '?' + params.join('&')
          let left = (window.innerWidth - 380)/2, top = (window.innerHeight - height)/2
          window.open(url,url,"channelmode=yes, fullscreen=yes, titlebar=no, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, "
           +"left=" + left +", top="+top+", width=380, height="+height)
          setTimeout(onClose,2000)
      },
      showMessage(res,onClose) {
        if (this.runBackground) {
          this.openNewWindow(res,onClose)
          return
        }
        let msg = ''
        let type = 'error'
        let height=32
        const textDiv = '<div style="height: 64px; line-height:64px; vertical-align: middle; font-size:36px; '
        if (!res) {
          msg = textDiv + 'color:red;">未知错误</div>'
          height += 64
        }
        else if (typeof res === 'string') {
          msg = textDiv +'color:red; ">'+res+'</div>'
          height += 64
        }
        else {
          if (res.success) type = 'success'
          if (res.data && res.data.name) {
            msg = textDiv + 'color: blue;">' + res.data.name + '</div>'
            height += 64
          }
          if (res.message) {
            msg = msg + textDiv + 'color:'+(res.success ? 'black' : 'red')+';">'+res.message+'</div>'
            height += 64
          }
          if (res.data && res.data.sound) msg = msg + '<audio src="/static/sound/'+res.data.sound+'" autoplay style="display:none"></audio>'
        }
        Message({
          dangerouslyUseHTMLString:true,
          duration: 2000,
          customClass: 'ck-register-message',
          center: true,
          type: type,
          offset: (window.innerHeight - height)/2,
          onClose: onClose,
          message: msg
        })
      },
      keepLogin() {
        const _this=this
        setTimeout(_=>{
          const req = {
            url: _this.baseUrl + '/action/register',
            method: 'post',
            data: { 
                mode: 'check',
                orgCode: _this.record.orgCode
            }
          }
          request(req, true).finally(_=>{
              _this.keepLogin()
          })
        },300000)
      },
      u53login(template) {
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
  