<template>
  <div class="cg-form" :class="dialogClass">
    <el-dialog v-el-drag-dialog :visible.sync="showDialog" top="0px" :class="dialogClass" :close-on-click-modal="false"
               :append-to-body="true" :show-close="!mobile || isDetail" @closed="close">
      <div slot="title" :class="titleColor">
        <cg-header :icon="icon" :content="content" :title="title"
                   :hasMenu="mobile && !isDetail" rightIcon = 'el-icon-check color-primary'
                   @menuAction="submit" @goBack="close"
        />
      </div>
      <CgFormDevVeinSample ref="cgForm"
                           :dialogParams="dialogParams"
                           :showInDialog="showDialog"
                           @closeDialog="close"
                           @openModeChanged="openModeChanged" />
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import CgFormDevVeinSample from './CgFormDevVeinSample'
const mixins = []
const mixinContext = require.context('.', false, /sample-mixin\.(js|vue)$/)
mixinContext.keys().forEach(key => { mixins.push(mixinContext(key).default) })
export default {
  name: 'DevVeinSampleForm',
  directives: { elDragDialog },
  components: { CgFormDevVeinSample },
  mixins,
  props: {
    dialogParams: {
      type: Object,
      default: null
    },
    dialogClass: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      path: 'sample',
      openMode: this.openParams().openMode ? this.openParams().openMode : null,
      showDialog: true,
      generatorName: 'devPeople',
      baseUrl: '/reader/devPeople'
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
      if (this.openParams().icon) return this.openParams().icon
      else return 'fa fa-eyedropper'
    },
    content() {
      if (this.openParams().content) return this.openParams().content
      else return this.$t('devPeople.title.'+this.path)
    },
    title() {
      if (this.openParams().title) return this.openParams().title
      else return this.isNew ? this.$t('system.action.new'):(this.isDetail ? this.$t('system.action.view') : this.$t('system.action.edit'))
    }
  },
  methods: {
    openParams: function() {
      return this.dialogParams ? this.dialogParams : this.$route.query
    },
    close() {
      this.showDialog = false
      this.$emit('close')
      if (!this.dialogParams) this.$store.dispatch('tagsView/activeLastAfterRemove', this.$route)
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
