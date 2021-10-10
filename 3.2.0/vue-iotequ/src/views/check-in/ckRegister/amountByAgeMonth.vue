<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByAgeMonth'.local()"
          icon="svg-chart/chart-bar"
          action="amountByAgeMonth"
          legendField="age"
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
import { startOf, endOf } from '@/utils/time'
import CkBaseStat from './baseStat.vue'
export default {
  name: 'CkAmountByAgeMonth',
  components: { CkBaseStat },
  methods: {
    startOf, 
    endOf
  }
}
</script>