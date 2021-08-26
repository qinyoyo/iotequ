<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByMonth'.local()"
          icon="svg-chart/chart-rose"
          action="amountByMonth"
          legendField="org_name"
          xField="month"
          yField="amount"
          charType="bar"
          :initRange="[startOf(null,'year'),endOf(null,'year')]"   
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
import { startOf, endOf } from '@/utils/time'
export default {
  name: 'CkAmountByMonth',
  components: { CkBaseStat },
  methods: {
    startOf, 
    endOf
  }
}
</script>