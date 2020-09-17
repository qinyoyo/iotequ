<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListDevPeopleGroup ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListDevPeopleGroup from './CgListDevPeopleGroup.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'DevPeopleGroupList',
  components: { CgListDevPeopleGroup },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'devPeopleGroup',
      baseUrl: '/reader/devPeopleGroup'
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
