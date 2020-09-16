<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu icon="fa fa-commenting-o" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet"
        />
      </div>
      <CgListMessage ref="cgList" :height="clientHeight" @detail="doShowDetail" :fixedQueryRecord="fixedQueryRecord"/>
    </el-card>
  </div>
</template>

<script>
import CgListMessage from './CgListMessage.vue'
import ParentList from '@/views/common-views/components/list'
const Comp = {
  name: 'MessageList',
  components: { CgListMessage },
  mixins: [ParentList],
  data() {
    return {
      path: 'list',
      generatorName: 'sysMessage',
      baseUrl: '/framework/sysMessage'
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
