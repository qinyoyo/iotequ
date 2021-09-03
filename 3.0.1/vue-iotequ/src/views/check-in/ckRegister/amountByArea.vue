<template>
  <div class="cg-stat">
    <CkBaseStat 
          :title = "'ckStat.title.amountByArea'.local()"
          icon="svg-chart/chart-2"
          action="amountByArea"
          legendField=""
          xField="area"
          yField="amount"
          yFieldName="人数"
          xFieldName="户籍"
          charType="pie"  
          :dateLabel="'devPeople.field.regTime'.local()"
          :initRange="[startOf(null,'year'),endOf(null,'year')]"
          :showCondition="true" 
          :exOption="{series: {radius: [0, '60%']}}"
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
