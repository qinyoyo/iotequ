<template>
  <div class="menu-wrapper">
    <el-submenu v-if="item.children && item.children.length>0 && (!mobile || !item.mobileHidden)" ref="subMenu" :index="'id_'+item.id" popper-append-to-body>
      <template slot="title">
        <item :icon="item.icon" :title="generateTitle(item.name)" />
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="'id_'+child.id"
        :is-nest="true"
        :item="child"
        :class="item.className ? item.className+' nest-menu' : 'nest-menu'"
      />
    </el-submenu>
    <template v-else-if="item.jsCmd && (!mobile || !item.mobileHidden)">
      <el-menu-item :index="'id_'+item.id" :class="itemClass(item)" @click.native="jsCmd(item)">
        <item :icon="item.icon" :title="generateTitle(item.name)"  />
      </el-menu-item>
    </template>
    <template v-else-if="!mobile || !item.mobileHidden">
      <el-menu-item v-if="isDialog(item.action)" :index="'id_'+item.id" :class="itemClass(item)" @click.native="openDialog(item.action)">
        <item :icon="item.icon" :title="generateTitle(item.name)" />
      </el-menu-item>      
      <app-link v-else-if="item.action" :to="item.action">
        <el-menu-item :index="'id_'+item.id" :class="itemClass(item)">
          <item :icon="item.icon" :title="generateTitle(item.name)" />
        </el-menu-item>
      </app-link>
    </template>
  </div>
</template>

<script>

import { generateTitle } from '@/utils/i18n'
import { isExternal } from '@/utils/validate'
import { findRouteByUrl } from '@/utils/filterRoutes'

import Item from './Item'
import AppLink from './Link'
import FixiOSBug from './FixiOSBug'
import { request } from '@/utils/request'

let menuActions = {}
const context = require.context('@/extend-src', false, /\/menu\-action(\-[a-z0-9]+)*\.js$/)
context.keys().forEach(key=>{
  menuActions = Object.assign(menuActions,context(key).default)
})

export default {
  name: 'SidebarItem',
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      mobile: this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    generateTitle,
    isDialog(url) {
      if (this.mobile) return false
      const route = findRouteByUrl(url,this.$router)
      return (route && route.meta && route.meta.dialog)
    },
    itemClass(item) {
      const cls = []
      if (!this.isNest) cls.push('submenu-title-noDropdown')
      if (item.className) cls.push(item.className)
      return cls.join(' ')
    },
    openDialog(url) {
      const route = findRouteByUrl(url,this.$router)
      if (route && route.meta && route.meta.dialog) {
        window.$vue.$dialog(route.components,{routeParams: {}})
      }
    },
    jsCmd(menu) {
      let options = {}
      try {
        const ap = eval('(' + menu.dataAction + ')')
        if (typeof ap === 'object') options = Object.assign(options, ap)
      } catch {}
      if (menu.jsCmd === 'request' && menu.action) {
        let req = {
          url: menu.action,
          method: 'get'
        }
        if (options) {
          if (options.request) {
            req = Object.assign(req, options.request)
            delete options.request
          }
          req.params = options
        }
        request(req, false)
      } else if (typeof menuActions[menu.jsCmd] === 'function') {
        menuActions[menu.jsCmd](options)
      }
    }
  }
}
</script>
<style scoped>
.el-menu-item i {
  font-size:16px;
  width: 20px;
}
</style>
