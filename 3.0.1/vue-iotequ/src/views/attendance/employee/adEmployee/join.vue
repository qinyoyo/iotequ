<template>
  <div class="cg-list cg-list-adEmployee">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAdEmployeeJoin ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListAdEmployeeJoin from './CgListAdEmployeeJoin.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'AdEmployeeJoinList',
  components: { CgListAdEmployeeJoin },
  mixins: [ParentList],
  data() {
    return {
      path: 'join',
      generatorName: 'adEmployee',
      baseUrl: '/attendance/employee/adEmployee'
    }
  }
}
const mixins = [Comp]
const mixinContext = require.context('.', false, /join-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default mixins.length < 2 ? Comp : {
  mixins
}
</script>
