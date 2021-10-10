<script>
import cgList from '@/utils/cgList'
import time from '@/utils/time'
import {jump2Url} from '@/utils/cg'
export default {
    methods: {
        read(options) {
          let newMessages = this.$store.state.user.newMessages  
          if (!options) return
          const row = options.row ? options.row : cgList.list_checkSelections(this, false)  
          if (row && row.id && !row.readTime) {
              row.readTime = new Date()
              cgList.list_request({ 
                  listObject: this, 
                  method: 'get', 
                  silence: true, 
                  params: { id : row.id},
                  action: 'action/read',
                  onSuccess: _=> {
                      if (newMessages>0) {
                        newMessages --
                        this.$store.dispatch('user/setNewMessages', newMessages)
                      }
                  }
                })
          }
        },
        useMixinMethodsFirst() {
            return true
        },  
        rowDblclick(row, event) {
            if (row) {
                if (row.url) this.process({row})
                else cgList.list_rowDblclick(this,{ row, event })
            }
        },
        process(options) {
            const row = options.row
            if (row) {
                if (!row.readTime) this.read({row})
                if (row.url) jump2Url(row.url,null,this.$router)
            }
        },
        doAction(action, options) {
            if (action=='view' || action=='edit') {
               const row = options && options.row ? options.row : cgList.list_checkSelections(this, false)
               if (row) {
                if (!row.readTime) this.read({row})
                this.super_doAction(action,options)
               }
            } else if (action=='readAll') {
                const listObject = this
                this.$confirm(this.$t('sysMessage.action.readAllConfirm'), this.$t('system.action.confirm'), {
                    confirmButtonText: this.$t('system.action.ok'),
                    cancelButtonText: this.$t('system.action.cancel'),
                    closeOnClickModal: false,
                    type: 'warning'
                }).then(_ => {
                    cgList.list_request({ 
                        listObject, 
                        method: 'get', 
                        silence: true, 
                        action: 'action/readAll',
                        onSuccess: _=> {
                            listObject.$store.dispatch('user/setNewMessages', 0)
                            listObject.super_doAction('refresh')
                        }
                      })  
                }).catch(_ => {
                })
            
            } else this.super_doAction(action,options)
        },
        extendActionFilter(action, row) {
            if (row) {
                if (action==='read' && row.readTime) return false
                else if (action === 'process' && !row.url) return false
            } 
            return true
        },
        rowRender(row,index) {
            const readIcon = row.readTime?'fa fa-check':''
            const userIcon = this.apiUrl('/res/getUploadImage?id='+row.sender+'&path=sysUser&field=icon')
            return (
                  <nut-cell show-icon={true} class="cg-list-message-item" onDetail={()=>this.$emit('detail',row)}>
                    <div slot="title">
                    <span >{time.toShortString(row.createTime)} {row.senderName == 'system' ? '系统消息' : '来自：' + row.senderName} </span>
                    <span class="right-span"> <i class={readIcon} aria-hidden="true"></i></span>
                    <div style={row.url?{color:'#5f86f7'}:null}><router-link to={row.url?row.url:''}  >{row.title}</router-link></div>
                    </div>
                    <div slot="sub-title" class={row.state ? '' : ' disable'} style="padding-bottom:5px">
                      <div> {row.content}</div>
                    </div>
                    <div slot="avatar">
                      <el-avatar style="margin-right:10px"
                        shape="square" 
                        size={36} 
                        fit="scale-down" 
                        src={ userIcon } 
                      />
                    </div>
                  </nut-cell>
            ) 
          }
    }
}
</script>
<style scoped>
.right-span {
    position: absolute;
    right: 10px;
    font-size: 8px;
    color:#e0edfb;
}
</style>