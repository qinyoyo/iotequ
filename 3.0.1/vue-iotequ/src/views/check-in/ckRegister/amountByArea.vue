<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByArea'.local()"
          icon="svg-chart/chart-rose"
          action="amountByArea"
          legendField=""
          xField="area"
          yField="amount"
          charType="pie"  
          :showCondition="false" 
          :exOption="{series: {radius: [0, '40%']}}"
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkAmountByArea',
  components: { CkBaseStat },
}
</script>
