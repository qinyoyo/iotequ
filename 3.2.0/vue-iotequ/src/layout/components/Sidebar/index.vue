<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" logo="/static/logo_s.png" :title="title" url="http://www.svein.cn"/>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="menu in menus" :key="'id_'+menu.id" :item="menu" :base-path="menu.action" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

function filterMenu(menu,hiddenMenu) {
  const r = []
  menu.forEach(m => {
    if (hiddenMenu.indexOf(m.action)<0) {
      if (m.children && m.children.length>0) {
        m.children = filterMenu(m.children,hiddenMenu)
      }
      r.push(m)
    }
  })
  return r.length==0 ? null : r
}

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      hiddenMenus: [],
      menus: this.showMenus()
    }
  },
  created() {
    window.$sidebar = this
  },
  computed: {
    ...mapGetters([
      'sidebar'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    title() {
      return this.$store.state.settings.title
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    showMenus() {
      const menus = this.$store.state.user.menus
      if (!this.hiddenMenus || this.hiddenMenus.length==0) return menus
      else return filterMenu(menus, this.hiddenMenus)
    },
    changeHiddenMenus(path,hidden)  {
      let changed = false
      if (path=='*' && !hidden && this.hiddenMenus.length) {
        this.hiddenMenus=[]
        changed = true
      } else {
        const index = this.hiddenMenus.indexOf(path)
        if (hidden && index < 0) {
          this.hiddenMenus.push(path)
          changed = true
        }
        else if (!hidden && index >=0) {
          this.hiddenMenus.splice(index,1)
          changed = true
        }
      }
      if (changed) this.menus = this.showMenus()
    }
  }
}
</script>
