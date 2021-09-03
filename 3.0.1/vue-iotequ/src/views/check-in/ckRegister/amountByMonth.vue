<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByMonth'.local()"
          icon="svg-chart/chart-bar"
          action="amountByMonth"
          legendField="org_name"
          xField="month"
          yField="amount"
          yFieldName="人次"
          xFieldName="月份"
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