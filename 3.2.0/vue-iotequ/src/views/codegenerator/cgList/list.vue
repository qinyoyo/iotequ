<template>
  <div class="cg-list cg-list-cgList">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListCgList ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('cgList.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgFormCgList ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
            </el-tab-pane>
            <el-tab-pane :label="$t('cgListField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
              <CgListCgListField ref="cgList_son1" :tableId="tableId" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="15" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListCgList ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('cgList.title.record')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgFormCgList ref="cgList_son0" :height="childHeight" :queryById.sync="son0Condition" />
              </el-tab-pane>
              <el-tab-pane :label="$t('cgListField.title.list')" name="cgList_son1" :style="{height: childHeight+'px'}">
                <CgListCgListField ref="cgList_son1" :tableId="tableId" :height="childHeight" :fixedQueryRecord.sync="son1Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import CgListCgList from './CgListCgList.vue'
import ParentList from '@/views/common-views/components/list-sons'
import CgFormCgList from '@/views/codegenerator/cgList/CgFormCgList.vue'
import CgListCgListField from '@/views/codegenerator/cgListField/CgListCgListField.vue'
const Comp = {
  name: 'CgListList',
  components: { CgListCgList, CgFormCgList, CgListCgListField },
  mixins: [ParentList],
  data() {
    return {
      fatherHeightPercent: 15,
      titleField: 'name',
      sonCount: 2,
      son0Condition: 'null',
      son1Condition: {
        listId: 'null'
      },
      sonPkFields: ['id', 'id'],
      path: 'list',
      generatorName: 'cgList',
      baseUrl: '/codegenerator/cgList'
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
