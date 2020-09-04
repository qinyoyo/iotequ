
import time from '@/utils/time'
export default {
  methods: {
    rowRender(row,index) {
      const icons = ["fa fa-spinner","fa fa-refresh","fa fa-check-square-o","fa fa-times-circle"]
      const colors = ['rgb(144, 147, 153)','rgb(230, 162, 60)','rgb(103, 194, 58)','rgb(245, 108, 108)']
      const type = this.dictValue(row.adjustType,this.dictionary.dictAdjustType,false,true)
      return (
            <nut-cell show-icon={true} class="cg-list-adjust-item" onDetail={()=>this.$emit('detail',row)}>
              <span slot="title"> {type} {row.employeeSysUserRealName} {time.toString(row.registerTime,'YYYY-MM-DD HH:mm')} </span>
              <div slot="sub-title" v-show="row.description" style="padding-bottom:5px">
                <span> {row.description}</span>
              </div>
              <div slot="avatar">
                <cg-icon style="margin-right:10px" 
                  size={24} color={colors[row.state]}
                  icon={ icons[row.state] } 
                />
              </div>
            </nut-cell>
      ) 
    }
  }
}


