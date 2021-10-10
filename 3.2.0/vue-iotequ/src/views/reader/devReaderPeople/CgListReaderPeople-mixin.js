import cgList from '@/utils/cgList'
import cgForm from '@/utils/cgForm'
export default {
    created () {
        const that = this
        cgForm.form_request({ 
          formObject: that, 
          method: 'get', 
          params: {
            readerNo: this.fixedQueryRecord.readerNo
          }, 
          action: 'action/reportPeople',
          silence: true,
          onSuccess: res => {
            setTimeout(function(){
              that.doAction('refresh')
            },1000);
          },
          onError:res=>{
            console.log(1)
          }
        })
        
      },
      methods:{
        useMixinMethodsFirst () {
        return true
        },
        doAction(action, options) {
          if(action=='add'){
            this.formPath= '/reader/devReaderPeople/record'
          }else if(action=='edit'){

            this.formPath= '/reader/devPeople/record'
            const row=cgList.list_checkSelections(this, false)
            if (row && !row.userNoDevPeopleUserNo) {
              options=Object.assign({},options,{needRow: true,record:row,needRefresh:true})
              action="add"
            }
          }
          this.super_doAction(action, options)
        }, download(action, options, id){
          if(action.action=='download'){
            const that=this
            cgForm.form_request({ 
              formObject: that, 
              method: 'get', 
              params: {
                readerNo: this.fixedQueryRecord.readerNo
              }, 
              action: 'action/download',
              silence: true,
              onSuccess: res => {
                cgForm.form_request({ 
                  formObject: that, 
                  method: 'get', 
                  params: {
                    readerNo: this.fixedQueryRecord.readerNo
                  }, 
                  action: 'action/reportPeople',
                  silence: true,
                  onSuccess: res => {
                    setTimeout(function(){
                      that.doAction('refresh')
                    },1000);
                  },
                  onError:res=>{
                    console.log(1)
                  }
                })
              }
            })
          }
        }
    },
    watch:{
      $route(newValue, oldValue) {
        const that = this
        if(newValue.hasOwnProperty('query')&&newValue.query.hasOwnProperty('fixedQueryRecord')&&newValue.query.fixedQueryRecord.hasOwnProperty('action')&&newValue.query.fixedQueryRecord.action=='reportPeople'){
          cgForm.form_request({ 
            formObject: that, 
            method: 'get', 
            params: {
              readerNo: this.fixedQueryRecord.readerNo
            }, 
            action: 'action/reportPeople',
            silence: true,
            onSuccess: res => {
              setTimeout(function(){
                that.doAction('refresh')
              },500);
            }
          })
        }
      }
    }


}