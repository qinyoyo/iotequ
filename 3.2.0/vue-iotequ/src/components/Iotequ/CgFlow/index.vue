<template>
  <div class="cg-flow">
    <el-timeline>
      <el-timeline-item placement="top"
        v-for="(item, index) in flowList"
        :key="index"
        :icon="item.icon"
        :type="item.type ? item.type : (item.color ? '' : 'primary')"
        :color="item.color"
        :size="item.size"
        :timestamp="item.timestamp">
        <el-card>
          <p>{{item.operation.local()}} by {{item.operator}} {{item.selection ? ' : '+item.selection.local() : ''}}</p>
          <p v-if=item.note>{{item.note}}</p>
          <p v-if=item.nextOperator>下一步处理人 ：{{item.nextOperator}}</p>
        </el-card>
        </el-timeline-item>
    </el-timeline>
  </div>
</template>
<script>
import { request } from '@/utils/request'
import { toString } from '@/utils/time'
export default {
  name: 'CgFlow',
  props: {
    queryById: [String,Number],
    url: {
      type: String,
      default: '/framework/sysFlowProcess/list'
    },
    stateStyle: Function
  },
  data() {
    return {
      flowList:[]
    }
  },
  watch: {
    queryById : {
      handler(n) {
       this.getFlowList()
      },
      immediate: true
    }
  },
  methods: {
    getFlowList() {
      new Promise((resolve, reject) => {
        let req = {
            url: this.url,
            method: 'get',
            params: { 
                flowId: this.queryById,
                needLoadDictionary: false,
                queryFlowProcess: true,
                queryEntities: 'flowId',
                defaultOrder: 'time desc'
            }
        }
        request(req, true).then(res => {
            if (res && res.hasOwnProperty('success') && res.success && res.data) {
              const data=res.data
              this.flowList = []
              data.forEach(r=>{
                  if (typeof this.stateStyle == 'function') {
                      const style = this.stateStyle(r.state1)
                      if (style) r=Object.assign(r,style)
                  }
                  r.timestamp = toString(r.time) + ' ' + r.state
                  this.flowList.push(r)
              })
            }
            resolve(res)
        }).catch(error => {
            reject(error)
        })
      })
    }
  }
}
</script>
<style>
 .el-timeline-item__timestamp {
     color: #1890ff;
     font-weight: bold;
 }
 .el-tab-pane .el-timeline {
     padding:0;
 }
 .mobile .el-tab-pane .el-timeline {
     padding:0 10px;
 }
 .el-timeline-item__wrapper .el-card {
     padding: 0 5px;
 }
</style>
