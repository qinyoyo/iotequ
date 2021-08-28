<template>
  <div class="cg-list cg-list-adSpecialShiftTime">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAdSpecialShiftTime ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListAdSpecialShiftTime from './CgListAdSpecialShiftTime.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'AdSpecialShiftTimeList',
  components: { CgListAdSpecialShiftTime },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'adSpecialShiftTime',
      baseUrl: '/attendance/specialshifttime/adSpecialShiftTime'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
