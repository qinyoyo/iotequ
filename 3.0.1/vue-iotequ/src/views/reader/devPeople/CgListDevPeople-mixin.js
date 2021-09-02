import time from '@/utils/time'
export default {
  methods: {
    rowRenderGroupTitle(index) {
      return this.dictValue(this.rows[index].orgCode,this.dictionary.dictOrgCode,true,true)
    },
    rowRender(row,index) {
      const sexIcon = 'fa '+ (row.sex=='1' ? 'fa fa-mars':'fa fa-venus')
      const userIcon = this.apiUrl('/res/getUploadImage?path=headPortrait&def=null&name=photo_'+row.userNo+'_01.jpg')
      return (
            <nut-cell show-icon={true} class="cg-list-devPeople-item" onDetail={()=>this.$emit('detail',row)}>
              <span slot="title"><i class={sexIcon} aria-hidden="true"></i> {row.realName} {row.idNation} {time.ageOf(row.birthDate)}岁 {row.mobilePhone}</span>
              <div slot="sub-title"style="padding-bottom:5px">
                <div v-show={row.homeAddr}>
                  <span class="fa fa-map-marker" aria-hidden="true"> {row.homeAddr}</span>
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