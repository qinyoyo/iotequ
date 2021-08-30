import {u53Disconnect,u53Connect,u53Read} from '@/views/common-views/login/u53read'
import { request } from '@/utils/request'
import { Message } from 'element-ui'

export default {
    created() {
      const that = this
      this.ignoreRecordChanged = true
      this.keepLogin()
      u53Disconnect(_=>{
        u53Connect(0, this.foundU53, this.notFoundU53)
      })
    },
    destroyed () {
      if (this.hasU53) {
        this.hasU53=false
        u53Disconnect()
      }
    },
    methods: {
      foundU53() {
        this.hasU53 = true
        document.querySelector('img.cg-header-image').src = '/static/img/input.gif'
        this.u53read()
      },
      notFoundU53(){
        this.hasU53 = false
        document.querySelector('img.cg-header-image').src = '/static/img/not_found.png'
        u53Connect(0, this.foundU53, this.notFoundU53)
      },
      u53read() {
        const that=this
        if (this.hasU53) {
          u53Read((data)=>{
            if(data.isSucc){
                that.u53login(data.Msg)
            }
          },_=>{
            u53Connect(0, that.foundU53, that.notFoundU53)
          })
        }
      },
      showMessage(res,onClose) {
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
          duration: 3000,
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
  