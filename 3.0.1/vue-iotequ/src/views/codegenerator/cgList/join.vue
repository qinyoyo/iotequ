<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListCgListJoin ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListCgListJoin from './CgListCgListJoin.vue'
import ParentList from '@/views/common-views/components/list'
const mixins = [ParentList]
const mixinContext = require.context('.', false, /join-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'CgListJoinList',
  components: { CgListCgListJoin },
  mixins,
  data() {
    return {
      path: 'join',
      generatorName: 'cgList',
      baseUrl: '/codegenerator/cgList'
    }
  }
}
</script>
