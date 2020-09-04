<template>
  <div class="cg-form">
    <el-card shadow="hover">
      <div slot="header" :class="titleColor">
        <cg-header hoemMenu :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="goBack"
        />
      </div>
      <CgFormAdShift ref="cgForm"
                     @openModeChanged="openModeChanged" />
    </el-card>
  </div>
</template>

<script>
import cg from '@/utils/cg'
import CgFormAdShift from './CgFormAdShift'
const mixins = []
const mixinContext = require.context('.', false, /record-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'AdShiftForm',
  components: { CgFormAdShift },
  mixins,
  data() {
    return {
      path: 'record',
      openMode: null,
      generatorName: 'adShift',
      baseUrl: '/attendance/shift/adShift'
    }
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    isDetail() {
      return this.openMode === 'detail' || this.openMode === 'view'
    },
    isNew() {
      return !this.openMode || this.openMode === 'new' || this.openMode === 'add'
    },
    isEdit() {
      return this.openMode === 'edit' || this.openMode === 'modify'
    },
    titleColor() {
      if (this.isNew) return 'color-danger'
      else if (this.isEdit) return 'color-warning'
      else return 'color-info'
    },
    icon() {
      if (this.$route.query.icon) return this.$route.query.icon
      else return 'fa'
    },
    content() {
      if (this.$route.query.content) return this.$route.query.content
      else return this.$t('adShift.title.'+this.path)
    },
    title() {
      if (this.$route.query.title) return this.$route.query.title
      else return this.isNew ? this.$t('system.action.new'):(this.isDetail ? this.$t('system.action.view') : this.$t('system.action.edit'))
    }
  },
  activated() {
    this.openMode = this.$route.query.openMode ? this.$route.query.openMode : null
  },
  methods: {
    goBack() {
      cg.goBack()
    },
    submit() {
      this.$refs.cgForm.submit()
    },
    openModeChanged(v) {
      this.openMode = v
    }
  }
}
</script>
