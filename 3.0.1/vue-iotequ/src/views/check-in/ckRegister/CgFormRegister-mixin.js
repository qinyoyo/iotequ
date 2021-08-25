import {u53Disconnect,u53Connect,u53Read} from '@/views/common-views/login/u53read'
import { request } from '@/utils/request'
export default {
    created() {
      const that = this
      this.ignoreRecordChanged = true
      u53Disconnect(_=>{
        u53Connect(0, _=>{
          that.hasU53 = true
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
        },60000)
      },
      u53login(template) {
        const _this=this
        const img = document.querySelector('img[src="/static/img/input.gif"]')
        img.style.opacity = 0.3
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
          request(req, false).then(res => {
              setTimeout(_=>{
                _this.record.mode = 'auto'  
                img.style.opacity = 1
                _this.u53read()
              },3000)
          }).catch(error => {
            setTimeout(_=>{
                img.style.opacity = 1
                _this.record.mode = 'auto'  
                _this.u53read()
              },3000)
          })   
        }
      }
   }
}
  