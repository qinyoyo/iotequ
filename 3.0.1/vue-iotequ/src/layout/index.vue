<template>
  <div :class="classObj">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" v-if="authentication"/>
    <div :class="mainClass">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar />
        <tags-view v-if="needTagsView && authentication" v-show="visitedViews.length > 0" />
      </div>
      <app-main />
      <right-panel v-if="showSettings" ref="visionSetting">
        <settings />
      </right-panel>
    </div>
    <el-footer v-if="footer && (!mobile || $route.path === '/home')" class="footer" v-html="footer"></el-footer>
  </div>
</template>

<script>
import RightPanel from './components/RightPanel'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { mapState } from 'vuex'

export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,
      footer: state => state.settings.footer,
      device: state => state.app.device,
      showSettings: state => state.settings.showSettings,
      needTagsView: state => state.settings.tagsView,
      visitedViews: state => state.tagsView.visitedViews,
      fixedHeader: state => state.settings.fixedHeader,
      authentication: state => state.user.authentication
    }),
    mobile() {
      return this.$store.state.app.device === 'mobile'
    },
    classObj() {
      return {
        appWrapper: true, 
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    mainClass() {
      return (this.$store.state.settings.tagsView && this.$store.state.tagsView.visitedViews.length > 0 ? 'hasTagsView' : '') + 
        (this.$store.state.user.authentication ? ' main-container' : '')
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .appWrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
  .footer {
    width: 100%;
    font-size: 12px;
    height: 36px!important;
    padding-top: 12px;
    bottom: 0px;
    position: fixed;
    color: #2f2f30;
    text-align: center;
    vertical-align: middle;
    border-top: 1px solid rgba(120, 130, 140, 0.13);
    background: #c4c4c4;
  }
  .openSidebar .footer {
    padding-left: 210px;
  }
  .hideSidebar .footer {
    padding-left: 54px;
  }
  .mobile .footer {
    padding-left: 0;
  }
</style>
