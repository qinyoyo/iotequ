import time from '@/utils/time'
export default {
  methods: {
    rowRenderGroupTitle(index) {
      return time.toString(this.rows[index].datDate,'YYYY-MM-DD')
    },
    rowRender(row,index) {
      let orgName = this.dictValue(row.orgCode,this.dictionary.dictOrgCode,true,true)
      let status = this.dictValue(row.status,this.dictionary.dictStatus,true,true)
      return (
            <nut-cell show-icon={false} class="cg-list-devEvent-item">
              <span slot="title"> {time.toString(row.datTime,'HH:mm')} {orgName}:{row.devType}-{row.devNo} {row.realName} {status}</span>
            </nut-cell>
      ) 
    }
  }
}