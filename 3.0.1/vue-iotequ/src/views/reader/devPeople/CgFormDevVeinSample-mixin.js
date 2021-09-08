import { Message } from 'element-ui'
import cgForm from '@/utils/cgForm'
import clipboard from '@/utils/clipboard'
import u53 from '@/utils/u53'
function showError(data) {
    Message({
      message: data && data.Msg ? data.Msg : 'reader.u53error'.local(),
      code: '',
      type: 'error',
      duration: 3 * 1000
    })
}
export default {
  data() {
    return {
      connected: false,
      hasFingerService: false,
      template: '',
      record: {
        fingerType: "1",
        warning: false
      }
    }
  },
  computed: {
  },
  watch: {
  },
  created () {
    const that = this
    this.record.fingerType = 1
    this.record.warning = false
    this.ignoreRecordChanged = true
    this.testService(_=>{
      that.connect(0)
    })
    cgForm.form_request({ 
      formObject: that, 
      method: 'get', 
      params: {
        userNo: that.record.userNo
      }, 
      action: 'action/getRegistered',
      silence: true
    })
  },
  destroyed () {
    if (this.hasFingerService && this.connected) this.disconnect()
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    },  
    extendActionFilter(action) {
      return action!='edit'
    },
    disabledAction(btn) {
      if (btn.action=='removeFinger') return !this.record.regFingers
      else if (btn.action=='verifyFinger') return !this.record.regFingers || !this.hasFingerService || !this.connected
      else if (btn.action=='registerFinger') return !this.record.userNo || !this.hasFingerService || !this.connected || this.record.regFingers>=2 
      else return !this.hasFingerService
    },
    disconnect(onSuccess,onError) {
      u53.u53Cancel();
      u53.u53Disconnect(onSuccess,onError)
    },
    connect(type){
      if (!this.hasFingerService) return
      const that = this
      u53.u53Connect(type,
          (data)=>{
            if (!data.isSucc) {
              if (type==0) that.connect(1)
              else {
                that.connected=false
              }
            } else {
              that.connected=true
            }
          },
          (data)=> {
            that.connected=false
            showError(data)
          }
      )
    },
    testService(onSuccess) {
      const that = this
      this.disconnect(_=>{
          that.connected=false
          that.hasFingerService=true
          if (typeof onSuccess === 'function') onSuccess()
        }, _=> {
          that.connected=false;
          that.hasFingerService=false;	
          Message({
            message: 'error.resource_not_found'.local() + ' : '+'reader.checkU53'.local(),
            code: '',
            type: 'error',
            duration: 3 * 1000
          })   
        }
      )
    },
    resetU53(){
      if (!this.hasFingerService) return;
      u53.u53Reset((data) => {
          Message({
            message: 'reader.resetOK'.local(),
            code: '',
            type: 'success',
            duration: 3 * 1000
          })
        }, showError)
    },
    sample() {
      const that = this
      if (!this.hasFingerService) return
      const fingerType = that.record.fingerType
      if (this.record.fingerNo1 && this.record.fingerNo2 && fingerType!=this.record.fingerNo1 && fingerType!=this.record.fingerNo2) {
        Message({
          message: 'reader.deleteOneFinger'.local(), 
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
        return
      }
      let update = false
      let fingerId = 1     
      if (fingerType == that.record.fingerNo1) {
        fingerId = 1
        update = true
      }
      else if (fingerType == that.record.fingerNo2) {
        fingerId = 2
        update = true
      } else if (that.record.fingerNo1) fingerId = 2
      
      u53.u53Register(that.record.userNo,fingerId,
          (data) => {
            if(data.isSucc){
              that.template = data.Msg
              cgForm.form_request({ 
                formObject: that, 
                method: 'get', 
                params: {
                  userNo: that.record.userNo,
                  fingerIndex: fingerId,
                  fingerType,
                  template: that.template,
                  update,
                  isWarning: that.record.warning
                }, 
                action: 'action/registerFinger'
              })
            }
          },showError
      )		
    },
    auth() {
      if (!this.hasFingerService) return
      const type = this.record.fingerType
      let index = (type==this.record.fingerNo1 ? 1 : (type==this.record.fingerNo2 ? 2 : 0) )
      if (!index) {
        Message({
          message: 'reader.noneAuth'.local(), 
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
        return        
      }
      cgForm.form_request({ 
        formObject: this, 
        method: 'get', 
        params: {
          userNo: this.record.userNo,
          fingerIndex: index
        }, 
        action: 'action/verifyFinger',
        silence: true,
        onSuccess: (res) => {
          if (res.success) {
            const template = res.template
            u53.u53Auth(this.record.userNo,index,template,null,showError)
          }
        }
      })
    },
    match() {
      const that = this
      if (!this.hasFingerService) return
      const instance=Message({
          showClose: true,
          type: 'success',
          duration: 60000,
          message: 'reader.readFromDevice'.local()
      }) 
      u53.u53Read(
          (data) => {
            instance.close()
            if(data.isSucc){
              that.template = data.Msg
              cgForm.form_request({ 
                formObject: that, 
                method: 'get', 
                params: {
                  template: that.template,
                  thresh:0
                }, 
                action: 'action/matchFinger',
                silence: true,
                onSuccess: (data)=>{
                  if (data.success && data.matchInfo) {
                    const info = data.matchInfo
                    let msg = '<a id="copytemplate">'+'reader.authInfo.bytes'.local()+':'+that.template.length/2 + '</a><br>'+'reader.authInfo.matches'.local()+':'+info.count+'<br>'
                    if (info.dictSize) msg += ('reader.authInfo.capacity'.local()+':'+that.printf('%,d',info.dictSize)+'<br>')
                    if (info.matchUsed) msg += ('reader.authInfo.authTimes'.local()+':'+that.printf('%,d',info.matchUsed)+'us<br>')
                    if (info.usUsed) msg += ('reader.authInfo.totalTimes'.local()+':'+that.printf('%,d',info.usUsed)+'us<br>')
                    if (info.state) msg += ('reader.authInfo.status'.local()+':'+info.state+'<br>')
                    if (info.count) {
                      msg += ('reader.authInfo.details'.local()+':'+'<br>')
                      for (let i=0;i<info.count;i++) {
                        const f=info.list[i];
                        const fi = f.name + '('+f.idNo+'):'+f.score +'reader.authInfo.score'.local() +' '+that.dictValue(f.fingerType,that.dictionary.dictFingerType,false,true)
                        if (f.warning) msg += '<div style="color:red">'+fi+'</div>'
                        else msg += (fi+'<br>')
                      }
                    }
                    Message({
                      showClose: true,
                      type: 'success',
                      duration: 0,
                      dangerouslyUseHTMLString:true,
                      message: msg
                    }).$el.querySelector('#copytemplate').onclick = function(e) {
                      clipboard(that.template,e)
                    }
                  } else if (data.message)  Message(data.message)
                  else Message('error.network_error'.local())
                }
              })
            }
          },
          (data)=>{
            instance.close()
          }
      )		
    },
    removeFinger() {
      const that = this
      if (!this.hasFingerService) return
      const type = this.record.fingerType
      let index = (type==this.record.fingerNo1 ? 1 : (type==this.record.fingerNo2 ? 2 : 0) )
      if (!index) {
        Message({
          message: 'reader.noneAuth'.local(),
          code: '',
          type: 'warning',
          duration: 3 * 1000
        })
        return        
      }      
      cgForm.form_request({ 
        formObject: that, 
        method: 'get', 
        params: {
          userNo: that.record.userNo,
          fingerIndex: index
        }, 
        action: 'action/removeFinger'
      })
    }
  }
}
