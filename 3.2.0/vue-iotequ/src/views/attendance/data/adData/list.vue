<template>
  <div class="cg-list cg-list-adData">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAdData ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListAdData from './CgListAdData.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'AdDataList',
  components: { CgListAdData },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'adData',
      baseUrl: '/attendance/data/adData'
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
