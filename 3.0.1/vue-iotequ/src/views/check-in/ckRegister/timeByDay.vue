<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.timeByDay'.local()"
          icon="svg-chart/chart-rose"
          action="timeByDay"
          legendField="org_name"
          xField="in_date"
          yField="amount"
          yFieldName="时长"
          xFieldName="日期"
          charType="line"            
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkTimeByDay',
  components: { CkBaseStat },
}
</script>
