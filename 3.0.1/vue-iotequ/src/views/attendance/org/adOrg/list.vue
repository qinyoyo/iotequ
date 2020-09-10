<template>
  <div class="cg-list">
    <el-card shadow="hover">
      <div slot="header">
        <cg-header homeMenu :showMaster="getShowMaster()" icon="el-icon-s-operation" :hasMenu="hasMenu()"
                   :title="title" :content="content" @goBack="goBack" @menuAction="showActionSheet" @showDetail="showDetail"
        />
      </div>
      <div v-if="mobile">
        <div v-show="showMaster" class="cg-father">
          <CgListAdOrg ref="cgList" @detail="doShowDetail" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" />
        </div>
        <el-tabs v-show="!showMaster" v-model="selectedSon" class="cg-child">
            <el-tab-pane :label="$t('adEmployee.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
              <CgListAdEmployee ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
            </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="components-container" :style="'height:'+clientHeight+'px'">
        <split-pane :default-percent="50" split="horizontal" @resize="resize">
          <template slot="paneL">
            <div class="cg-father">
              <CgListAdOrg ref="cgList" @rowClick="rowClick" @refreshed="refreshed" :fixedQueryRecord="fixedQueryRecord" :height="fatherHeight" />
            </div>
          </template>
          <template slot="paneR">
            <el-tabs v-model="selectedSon" class="cg-child" type="border-card">
              <el-tab-pane :label="$t('adEmployee.title.list')" name="cgList_son0" :style="{height: childHeight+'px'}">
                <CgListAdEmployee ref="cgList_son0" :height="childHeight" :fixedQueryRecord.sync="son0Condition" />
              </el-tab-pane>
            </el-tabs>
          </template>
        </split-pane>
      </div>
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import cgList from '@/utils/cgList'
import CgListAdOrg from './CgListAdOrg.vue'
import CgListAdEmployee from '@/views/attendance/employee/adEmployee/CgListAdEmployee.vue'
const mixins = []
const mixinContext = require.context('.', false, /list-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdOrgList',
  components: { CgListAdOrg, CgListAdEmployee },
  mixins,
  props: {
    fixedQueryRecord: {
      type: Object,
      default: () => { return {} }
    },
    height: {
      type: Number,
      default: 0
    },
  },
  data() {
    return {
      clientHeight: this.height ? this.height : cg.containerHeight(),
      fatherHeight: (this.height ? this.height : cg.containerHeight()) * (this.$store.state.app.device === 'mobile' ? 1: 50 / 100),
      childHeight: (this.height?this.height:cg.containerHeight())*(this.$store.state.app.device === 'mobile' ? 1 : (100- 50)/100)-70,
      showMaster: true,
      selectedSon: 'cgList_son0',
      son0Condition: {
        orgCode: 'null'
      },
      contentSubTitle: '',
      path: 'list',
      generatorName: 'adOrg',
      baseUrl: '/attendance/org/adOrg'
    }
  },
  created() {
    this.$route.query && this.$route.query.fixedQueryRecord && (this.fixedQueryRecord = Object.assign(this.fixedQueryRecord,this.$route.query.fixedQueryRecord))
  },
  watch: {
    fixedQueryRecord: {
      handler(n, o) {
        if (n && Object.keys(n).length > 0 && this.$refs.cgList && typeof this.$refs.cgList.doAction === 'function') this.$refs.cgList.doAction('refresh')
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    title() {
      if (this.showMaster || !this.mobile) return this.$t('system.action.list')
      else return this.$t('system.action.detail')
    },
    content() {
      if (this.showMaster || !this.mobile) return this.$t('adOrg.title.list') + (!this.mobile && this.contentSubTitle?' - '+this.contentSubTitle:'')
      else return this.contentSubTitle ? this.contentSubTitle : this.$t('adOrg.title.list')
    }
  },
  methods: {
    goBack() {
      if (this.mobile) {
        if (this.showMaster) cg.goBack()
        else {
          if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) {
            if (!this.$refs[this.selectedSon].showMaster) this.$refs[this.selectedSon].showMaster = true
            else this.showMaster = true
          } else this.showMaster = true
        }
      }
    },
    hasMenu() {
      if (!this.mobile) return false
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].hasMenu === 'function') return this.$refs[this.selectedSon].hasMenu()
      else return false
    },
    showActionSheet() {
      if (this.showMaster && typeof this.$refs.cgList.showActionSheet === 'function') this.$refs.cgList.showActionSheet()
      else if (this.$refs[this.selectedSon] && typeof this.$refs[this.selectedSon].showActionSheet === 'function') this.$refs[this.selectedSon].showActionSheet()
    },
    resize(percent) {
      this.fatherHeight = this.clientHeight * percent / 100
      this.childHeight = this.clientHeight - this.fatherHeight - 70
    },
    getShowMaster() {
      if (!this.mobile) return undefined
      else if (this.showMaster) return true
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster !== undefined) return this.$refs[this.selectedSon].showMaster
      else return undefined
    },
    showDetail() {
      if (!this.mobile) return
      else if (this.showMaster) this.doShowDetail()
      else if (this.$refs[this.selectedSon] && this.$refs[this.selectedSon].showMaster && typeof this.$refs[this.selectedSon].doShowDetail ==='function') this.$refs[this.selectedSon].doShowDetail()
    },
    setChildrenParams(row) {
      if (row && row.orgCode) this.son0Condition.orgCode = row.orgCode
      else this.son0Condition.orgCode = 'null'
    },
    rowClick({ row, column, event }) {
      this.contentSubTitle = row ? row.name : ''
      if (!this.mobile) this.setChildrenParams(row)
    },
    refreshed(listObject) {
      const row = cgList.list_getCurrentRow(listObject)
      if (this.$refs.cgList_son0 && this.$refs.cgList_son0.hasOwnProperty('needLoadDictionary')) this.$refs.cgList_son0.needLoadDictionary = true
      this.setChildrenParams(row)
    },
    doShowDetail(row) {
      if (!row) row = cgList.list_getCurrentRow(this.$refs.cgList)
      this.showMaster = false
      this.setChildrenParams(row)
    },
  }
}
</script>
