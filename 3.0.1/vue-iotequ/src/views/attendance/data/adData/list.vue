<template>
  <div class="cg-list">
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
const mixins = [ParentList]
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdDataList',
  components: { CgListAdData },
  mixins,
  data() {
    return {
      path: 'list',
      generatorName: 'adData',
      baseUrl: '/attendance/data/adData'
    }
  }
}
</script>
