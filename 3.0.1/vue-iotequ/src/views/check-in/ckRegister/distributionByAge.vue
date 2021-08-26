<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.distributionByAge'.local()"
          icon="svg-chart/chart-rose"
          action="distributionByAge"
          legendField=""
          xField="age"
          yField="amount"
          charType="pie"   
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