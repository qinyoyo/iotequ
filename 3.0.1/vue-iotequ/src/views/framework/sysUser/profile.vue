<template>
  <div class="cg-form">
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" :top="mobile? '0px':'100px'" :close-on-click-modal="false" 
      destroy-on-close :append-to-body="true" :show-close="!mobile || isDetail" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="isNew?'el-icon-circle-plus':(isEdit?'el-icon-edit-outline':'el-icon-view')"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   :title="(isNew?$t('system.action.new'):(isDetail?$t('system.action.view'):$t('system.action.edit')))" :content="$t('sysUser.title.profile')" 
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormProfile ref="cgForm"
                     :showInDialog="showDialog"
                     @closeDialog="close"
                     @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import CgFormProfile from './CgFormProfile'
const mixins = []
const mixinContext = require.context('.', false, /profile-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'ProfileForm',
  directives: { elDragDialog },
  components: { CgFormProfile },
  mixins,
  data() {
    return {
      path: 'profile',
      openMode: this.$route.query.openMode ? this.$route.query.openMode : null,
      showDialog: true,
      generatorName: 'sysUser',
      baseUrl: '/login/profile'
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
    }
  },
  methods: {
    close() {
      this.showDialog = false
      this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
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
