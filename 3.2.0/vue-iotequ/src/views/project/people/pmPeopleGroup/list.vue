<template>
  <div class="cg-list cg-list-pmPeopleGroup">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListPmPeopleGroup ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('pmPeople.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListPmPeople ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="60" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListPmPeopleGroup ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('pmPeople.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListPmPeople ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListPmPeopleGroup from './CgListPmPeopleGroup.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListPmPeople from '@/views/project/people/pmPeople/CgListPmPeople.vue'
const Comp = {
  name: 'PmPeopleGroupList',
  components: { CgListPmPeopleGroup, CgListPmPeople },
  mixins: [ParentList],
  data() {
    return {
      titleField: 'name',
      sonCount: 1,
      son0Condition: {
        groupId: 'null'
      },
      sonPkFields: ['id'],
      path: 'list',
      generatorName: 'pmPeopleGroup',
      baseUrl: '/project/people/pmPeopleGroup'
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
