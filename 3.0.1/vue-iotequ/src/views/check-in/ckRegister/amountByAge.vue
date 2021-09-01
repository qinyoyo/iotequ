<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByAge'.local()"
          icon="svg-chart/chart-rose"
          action="amountByAge"
          legendField="org_name"
          xField="age"
          yField="amount"
          yFieldName="人次"
          xFieldName="年龄段"
          charType="polar"  
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