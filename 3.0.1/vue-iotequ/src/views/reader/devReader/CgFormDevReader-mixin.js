import { request } from '@/utils/request'
import cgForm from '@/utils/cgForm'
import {selectNewDeviceDialog} from '@/views/reader/devReader/selectNewDeviceDialog'
export default {
    watch: {
        isNew(n) {
          if(this.record.type.indexOf("D10")!=-1||this.record.type.indexOf("D30")!=-1){
            this.displayTabpane(this.$refs.tabs,1,!n)
          }
          
          if(this.record.type.indexOf("D30")!=-1){
            this.displayTabpane(this.$refs.tabs,2,!n)
          }
        },
        'record.type' :{
          handler(n,o){
            const that=this
            var v1=false;
            var v2=false;
              if(n&&(n.indexOf("D10")!=-1)){
                  v1=true
              }else if(n&&(n.indexOf("D30")!=-1)){
                  v1=true
                  v2=true
              }
              that.displayTabpane(that.$refs.tabs,1,v1)
              that.displayTabpane(that.$refs.tabs,2,v2)           
          },
          immediate: true
        } 
        },

    methods: {
        useMixinMethodsFirst() {
            return true
        }, 
        labelForNo() {
           return 'devReader.field.readerNo'.local() + '(双击选择)'
        },
        disabledAction(btn) {
            return this.isNew
        },
        selectNewDevice() {
          const _this = this
          selectNewDeviceDialog({
            callback: (data)=>{
              if (data) {
                _this.record.ip = data.ip
                _this.record.readerNo = data.readerNo
                _this.record.snNo = data.snNo
                _this.record.type = data.type
              }
            }
          })
        }     
    },
    created() {
        const that=this
        const _this=this
        if(!that.isNew){
          cgForm.form_request({ 
            formObject: that, 
            method: 'get', 
            params: {
              id: that.record.id
            }, 
            action: 'action/selectParam',
            onSuccess: res => {
              console.log(res)
              if (res && res.success&&res.status) {
                that.record.alignMethod=res.reader.alignMethod,
                that.record.blacklightTime=res.reader.blacklightTime,
                that.record.voiceprompt=res.reader.voiceprompt,
                that.record.menuTime=res.reader.menuTime,
                that.record.wengenform=res.reader.wengenform,
                that.record.wengenOutput=res.reader.wengenOutput,
                that.record.wengenOutArea=res.reader.wengenOutArea
                that.record.regfingerOutTime=res.reader.regfingerOutTime,
                that.record.authfingerOutTime=res.reader.authfingerOutTime
                if(that.record.type.indexOf("D30")!=-1){
                  that.record.wgOrder=res.reader.wgOrder,
                  that.record.relayTime=res.reader.relayTime,
                  that.record.fixedValue=res.reader.fixedValue,
                  that.record.alarmEnable=res.reader.alarmEnable,
                  that.record.openEnable=res.reader.openEnable,
                  that.record.doorbellEnable=res.reader.doorbellEnable,
                  that.record.relayEnable=res.reader.relayEnable,
                  that.record.doorState=res.reader.doorState,
                  that.record.wifiSsid=res.reader.wifiSsid,
                  that.record.wifiPsw=res.reader.wifiPsw
                }
              }
            } 
          }).then(_=>{            
              _this.just4elInputNumberNullBug()
              _this.initialized()
              if (_this.isDialogForm) {
                 cgForm.form_mounted(_this)
                 _this.recordChanged = false
              } else {
                cgForm.form_activedRefresh(_this)
                _this.recordChanged = false      
              }
          })
        }
       }
}