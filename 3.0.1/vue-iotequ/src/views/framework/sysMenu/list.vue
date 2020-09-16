<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListMenu ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import CgListMenu from './CgListMenu.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'MenuList',
  components: { CgListMenu },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      path: 'list',
      generatorName: 'sysMenu',
      baseUrl: '/framework/sysMenu'
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
