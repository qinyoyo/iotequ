<template>
  <div class="cg-list cg-list-cgProject">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListCgProject ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import CgListCgProject from './CgListCgProject.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'CgProjectList',
  components: { CgListCgProject },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      path: 'list',
      generatorName: 'cgProject',
      baseUrl: '/codegenerator/cgProject'
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
