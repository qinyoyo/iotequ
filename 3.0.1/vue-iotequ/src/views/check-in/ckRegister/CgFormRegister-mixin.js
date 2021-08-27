import {u53Disconnect,u53Connect,u53Read} from '@/views/common-views/login/u53read'
import { request } from '@/utils/request'
import { Message } from 'element-ui'
export default {
    created() {
      const that = this
      this.ignoreRecordChanged = true
      u53Disconnect(_=>{
        u53Connect(0, _=>{
          that.hasU53 = true
          document.querySelector('img.cg-header-image').src = '/static/img/input.gif'
          that.u53read()
          that.keepLogin()
        })
      })
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
      showMessage(res,onClose) {
        let msg = ''
        let type = 'error'
        const textDiv = '<div style="height: 64px; font-size:36px; '
        if (!res) msg = textDiv + 'color:red;">未知错误</div>'
        else if (typeof res === 'string') msg = textDiv +'color:red; ">'+res+'</div>'
        else {
          if (res.success) type = 'success'
          if (res.data && res.data.name) msg = textDiv + 'color: blue;">' + res.data.name + '</div>'
          if (res.message) msg = msg + textDiv + 'color:'+(res.success ? 'black' : 'red')+';">'+res.message+'</div>'
          if (res.data && res.data.sound) msg = msg + '<audio src="/static/sound/'+res.data.sound+'" autoplay style="display:none"></audio>'
        }
        Message({
          dangerouslyUseHTMLString:true,
          duration: 3000,
          center: true,
          type: type,
          offset: 400,
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
  