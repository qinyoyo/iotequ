<template>
  <div class="cg-list cg-list-sysOrg">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListOrg ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('sysUser.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListUser ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="30" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListOrg ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('sysUser.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListUser ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListOrg from './CgListOrg.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListUser from '@/views/framework/sysUser/CgListUser.vue'
const Comp = {
  name: 'OrgList',
  components: { CgListOrg, CgListUser },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      sonCount: 1,
      son0Condition: {
        orgCode: 'null'
      },
      sonPkFields: ['orgCode'],
      path: 'list',
      generatorName: 'sysOrg',
      baseUrl: '/framework/sysOrg'
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
