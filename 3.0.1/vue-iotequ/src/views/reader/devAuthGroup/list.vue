<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="fa fa-user-plus" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListDevAuthGroup ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('devOrgGroup.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListDevOrgGroup ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('devPeopleGroup.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListDevPeopleGroup ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="vertical">
          <template slot="paneL">
            <div class="cg-father">
              <CgListDevAuthGroup ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('devOrgGroup.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListDevOrgGroup ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('devPeopleGroup.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListDevPeopleGroup ref="cgList_son1" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListDevAuthGroup from './CgListDevAuthGroup.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgListDevOrgGroup from '@/views/reader/devOrgGroup/CgListDevOrgGroup.vue'
import CgListDevPeopleGroup from '@/views/reader/devPeopleGroup/CgListDevPeopleGroup.vue'
const Comp = {
  name: 'DevAuthGroupList',
  components: { CgListDevAuthGroup, CgListDevOrgGroup, CgListDevPeopleGroup },
  mixins: [ParentList],
  data() {
    return {
      sonCount: 2,
      son0Condition: {
        groupId: 'null'
      },
      son1Condition: {
        groupId: 'null'
      },
      sonPkFields: ['id', 'id'],
      path: 'list',
      generatorName: 'devAuthGroup',
      baseUrl: '/reader/devAuthGroup'
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
