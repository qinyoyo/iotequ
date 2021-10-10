<script>
import { toString } from '@/utils/time'
export default {
    methods: {
      rowRenderGroupTitle(index) {
        return toString(this.rows[index].dateDate,'YYYY-MM-dd') + '  ' + this.dictValue(this.rows[index].orgCode,this.dictionary.dictOrgCode,true,true)
      },
      rowRender(row,index) {
        return (
          <nut-cell show-icon={false} class="cg-list-addata-item">
            <span slot="title">
              <span> {row.employeeNoAdEmployeeRealName}({row.employeeNo})</span>
            </span>
            <span slot="sub-title">
              <span class="span-width">类别: { this.dictValue(row.recType,this.dictionary.dictRecType,false,true) }</span>
              <span class="span-width">来源：{row.recSource} </span>
            </span>
            <span slot="desc">{index}</span>
            <div slot="avatar"><span class="span-width"> {toString(row.dateTime,'HH:mm')}</span></div>
          </nut-cell>
        )
      }
    }
}
</script>
<style scoped>
.span-width {
  display:-moz-inline-box;
  display:inline-block;
  width:80px; 
}
</style>


