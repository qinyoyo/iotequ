<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.distributionByAge'.local()"
          icon="svg-chart/chart-ring"
          action="distributionByAge"
          legendField=""
          xField="age"
          yField="amount"
          yFieldName="人次"
          xFieldName="年龄段"
          charType="pie"
          :exOption="{series:{roseType: 'radius'}}"
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkDistributionByAge',
  components: { CkBaseStat },
}
</script>