<script>
import time from '@/utils/time'
export default {
  methods: {
    rowRenderGroupTitle(index) {
      return this.dictValue(this.rows[index].orgCode,this.dictionary.dictOrgCode,true,true)
    },
    rowRender(row,index) {
      const lockIcon = 'fa '+ (row.locked ? 'fa-lock':'fa-unlock-alt')
      const sexIcon = 'fa '+ (row.sex==1 ? 'fa fa-mars':'fa fa-venus')
      const birthIcon = 'fa '+ (row.sex==1 ? 'fa fa-mars':'fa fa-venus')
      const orgName = this.dictValue(row.orgCode,this.dictionary.dictOrgCode,true,true)
      const userIcon = this.apiUrl('/res/getUploadImage?id='+row.id+'&path=sysUser&def=null&field=icon&name='+row.icon)
      return (
            <nut-cell show-icon={true} class="cg-list-user-item" onDetail={()=>this.$emit('detail',row)}>
              <span slot="title" class={row.state ? '' : ' disable'}><i class={sexIcon} aria-hidden="true"></i> {row.name}({row.realName}) {row.mobilePhone}</span>
              <div slot="sub-title" class={row.state ? '' : ' disable'} style="padding-bottom:5px">
                <div>No. {row.employeeNo}</div>
                <div v-show={row.regTime || row.expiredTime}>
                  <i class="fa fa-calendar" aria-hidden="true" ></i>
                  <span> {time.toString(row.regTime)} - {time.toString(row.expiredTime)}</span>
                </div>
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
    },
    rowClassName({row,rowIndex}) {
      if (!row.state || row.locked || (row.expiredTime && time.subtract(row.expiredTime)<=0)) return 'cg-list-user-item disable'
    }
  }
}
</script>
<style>
.disable {
    color: rgb(200, 200, 200)!important;
  }
</style>
