import time from '@/utils/time'
export default {
  methods: {
    rowRenderGroupTitle(index) {
      return this.rows[index].orgName
    },
    rowRender(row,index) {
      const sexIcon = 'fa '+ (row.sex=='1' ? 'fa fa-mars':'fa fa-venus')
      const userIcon = this.apiUrl('/res/getUploadImage?path=headPortrait&def=null&name=photo_'+row.userNo+'_01.jpg')
      return (
            <nut-cell show-icon={false} class="cg-list-ckRegister-item">
              <span slot="title"><i class={sexIcon} aria-hidden="true"></i> {row.name} {time.ageOf(row.birthDate)}岁</span>
              <div slot="sub-title"style="padding-bottom:5px">
                <div>
                  <i class="fa fa-calendar" aria-hidden="true" ></i>
                  <span> {time.toString(row.inDate,'YYYY-MM-DD')}</span>
                  <span class="fa fa-sign-in" aria-hidden="true" style="margin-left: 10px;" > {time.toString(row.onTime,'HH:mm')}</span>
                  <span v-show={row.offTime} class="fa fa-sign-out" style="margin-left: 10px;"> {time.toString(row.offTime,'HH:mm')}</span>
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
    }
  }
}