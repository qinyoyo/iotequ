import time from '@/utils/time'
export default {
  methods: {
    useMixinMethodsFirst() {
      return true
    },   
    doAction(action, options) {
      if (action!=='adjust' || (options && options.row)) this.super_doAction(action,options)
      else {
        const listObject = this
        const params = listObject.list_getQueryParams(listObject)
        listObject.list_request({ listObject, method: 'get', params, action: 'action/adjust',
        timeout: 0,
        onSuccess: (res) => { 
          listObject.list_getDataFromServer(listObject)
          listObject.$emit('adjust-completed', listObject)
        }
      })
      }
    },
    rowRenderGroupTitle(index) {
      return this.rows[index].orgName 
    },
    
    rowRender(row,index) {
      const userIcon = (index==0 || this.rows[index-1].employee!=row.employee ? 
        this.apiUrl('/res/getUploadImage?id='+row.employee+'&path=sysUser&field=icon') : null)
      return (
            <nut-cell show-icon={false} class="cg-list-day-result-item">
              <span slot="title" > {time.toString(row.adDate,'YYYY-MM-dd')} No. {row.employeeNo} {row.realName}</span>
              <div slot="sub-title"  style="padding-bottom:5px">
                <span> 班次: {row.shiftName}({row.times}次) 工作时长: {row.workMinutes}/{row.minutes}分钟</span>
              </div>
              <div slot="desc">
                <span>{row.stateName}</span>
              </div>
              <div slot="avatar">
                <el-avatar style="margin-right:10px"
                  shape="square" 
                  size={24} 
                  fit="scale-down" 
                  src={ userIcon } 
                />
              </div>
            </nut-cell>
      ) 
    },
  }
}
