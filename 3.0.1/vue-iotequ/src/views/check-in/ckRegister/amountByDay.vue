<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByDay'.local()"
          icon="svg-chart/chart-rose"
          action="amountByDay"
          legendField="org_name"
          xField="in_date"
          yField="amount"
          charType="line"  
          :exOption="{series: {areaStyle: {}}}" 
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkAmountByDay',
  components: { CkBaseStat },
}
</script>
