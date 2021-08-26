<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByAge'.local()"
          icon="svg-chart/chart-rose"
          action="amountByAge"
          legendField="org_name"
          xField="age"
          yField="amount"
          charType="bar"   
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkAmountByAge',
  components: { CkBaseStat },
}
</script>