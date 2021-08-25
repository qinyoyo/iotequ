<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="svg-chart/chart-rose" :hasMenu="hasMenu()"
                   :content="'ckStat.title.amountByAge'.local()" @goBack="goBack"
        />
      </div>
      <el-divider />
      <el-row>
        <el-col :span="24">
          <cg-chart ref="chart" width="100%" :height="clientHeight-120" :options="options" />
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import { request } from '@/utils/request'

// 引入柱状图组件
require('echarts/lib/chart/bar')
require("echarts/lib/component/polar")
//import echarts from 'echarts'
function getEChartsOptions(data) {

}
export default {
  name: 'CkAmountByAge',
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      baseUrl: '/check-in/ckRegister',
      options: null
    }
  },
  mounted() {
    if (!this.options) this.refresh()
  },
  
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    toString,
    dateAdd,
    refresh() {
        const that = this
        let req = {
            url: this.baseUrl + '/query',
            method: 'get',
            params: {action: 'amountByAge', month: this.toString(this.queryDate,'YYYY-MM') }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success) {
              that.options = getEChartsOptions(res.data)

            }
        }).catch(error => {
        })
    },
    goBack() {
      if (this.mobile) cg.goBack()
    },
    hasMenu() {
      return this.mobile
    }
  }
}
</script>
