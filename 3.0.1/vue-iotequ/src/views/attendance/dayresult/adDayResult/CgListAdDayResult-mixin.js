import time from '@/utils/time'
export default {
  mounted() {
    this.$refs.query.appendAction({
      action: 'adjust',
      icon: 'fa fa-bolt fa-fw',
      title: 'adDayResult.action.adjust'
    })
    this.$refs.query.$on('adjust',this.adjust)
  },
  methods: {
    useMixinMethodsFirst() {
      return true
    }, 
    adjust(e) {
      this.doAction('adjustAll')
    } ,
    doAction(action, options) {
      if (action=='adjustAll') {
        const listObject = this
        const cgList = listObject.cgUtils().cgList
        const params = cgList.list_getQueryParams(listObject)
        cgList.list_request({ listObject, method: 'get', params, action: 'action/adjust',
        timeout: 0,
        onSuccess: (res) => { 
          cgList.list_getDataFromServer(listObject)
          listObject.$emit('adjust-completed', listObject)
        }
      })
      } else this.super_doAction(action,options)
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
