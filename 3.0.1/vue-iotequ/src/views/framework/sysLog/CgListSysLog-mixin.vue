<script>
import { toString } from '@/utils/time'
export default {
    methods: {
      rowRender(row,index) {
        return (
          <nut-cell show-icon={false} class="cg-list-log-item">
            <span slot="title">
              <span> {toString(row.time,'YYYY-MM-dd HH:mm')} {row.keyword} </span>
              <span class="right-span">{ row.usertype }</span>
            </span>
            <span slot="sub-title">
              <span>{ row.userinfo } {row.note}</span>
            </span>
            <span slot="desc">{index}</span>
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
    font-size: 8px
}
</style>


