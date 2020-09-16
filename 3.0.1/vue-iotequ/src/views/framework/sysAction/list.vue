<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListAction ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord" @rowClick="rowClick"/>
    </el-card>
  </div>
</template>

<script>
import CgListAction from './CgListAction.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'ActionList',
  components: { CgListAction },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'value',
      path: 'list',
      generatorName: 'sysAction',
      baseUrl: '/framework/sysAction'
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
