<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByArea'.local()"
          icon="svg-chart/chart-rose"
          action="amountByArea"
          legendField=""
          xField="area"
          yField="amount"
          yFieldName="人数"
          xFieldName="户籍"
          charType="pie"  
          :initRange="[startOf(null,'year'),endOf(null,'year')]"
          :showCondition="true" 
          :exOption="{series: {radius: [0, '40%']}}"
    />
  </div>
</template>
<script>
import CkBaseStat from './baseStat.vue'
import { startOf, endOf } from '@/utils/time'
export default {
  name: 'CkAmountByArea',
  components: { CkBaseStat },
  methods: {
    startOf, 
    endOf
  }
}
</script>
