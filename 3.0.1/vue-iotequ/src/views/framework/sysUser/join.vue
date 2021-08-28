<template>
  <div class="cg-list cg-list-sysUser">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListUserJoin ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListUserJoin from './CgListUserJoin.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'UserJoinList',
  components: { CgListUserJoin },
  mixins: [ParentList],
  data() {
    return {
      path: 'join',
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
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
