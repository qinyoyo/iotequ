<template>
  <div class="cg-list cg-list-sysUser">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListUser ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import CgListUser from './CgListUser.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'UserList',
  components: { CgListUser },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'realName',
      path: 'list',
      generatorName: 'sysUser',
      baseUrl: '/framework/sysUser'
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
