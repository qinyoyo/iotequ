<template>
  <div class="cg-header">
    <div class="el-page-header" :style="mobile?'padding-left: 10px;':''">
      <div :class="'el-page-header__left'+(mobile ? '':' no-action')" :style="content?'':'position: unset'" >
        <cg-icon :icon="mobile ? 'el-icon-back green-color' : icon" :size="20" @click = "doAction(mobile?'goBack':'')"/>
        <div class="el-page-header__title">
          <cg-icon v-if="mobile && homeMenu" icon="svg-menu" color="black" :size="20" extStyle="margin-left: 8px" @click = "doAction('homeMenu')"/>
          <slot v-if="!mobile" name="title">{{title}}</slot>
        </div>
      </div>
      <div v-if="content || title" class="el-page-header__content">
        <slot name="content" >{{ (mobile && title ?  title + (content?' : ':''):'') + content }}</slot>
      </div>
      <div v-if="mobile" class="green-color float-right" >
        <i v-if="showMaster" class="el-icon-arrow-down" @click = "doAction('showDetail')" />
        <i v-if="hasMenu" :class="rightIcon" style="margin: 0 16px 0 10px" @click = "doAction('menuAction')" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CgHeader',
  props: {
    showMaster:  {
      type: Boolean,
      default: undefined
    },
    width: {
      width: String,
      default: '100%'
    },
    icon: {
      type: String,
      default: 'el-icon-menu'
    },
    rightIcon: {
      type: String,
      default: 'el-icon-menu'
    },
    title: {
      type: String,
      default: ''
    },
    hasMenu: {
      type: Boolean,
      default: false
    },
    homeMenu: {
      type: Boolean,
      default: false
    },
    content: String
  },
  computed: {
    mobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    doAction(action) {
      if (action==='homeMenu') this.$store.dispatch('app/toggleSideBar')
      else if (action)  this.$emit(action)
    }
  }
}
</script>
<style scoped>
.green-color{
  color :forestgreen;
}
.float-right {
  right: 0;
  cursor: pointer; 
  position :absolute;
}

.cg-header i {
    font-size: 18px;
    margin-right: 6px;
    -ms-flex-item-align: center;
    align-self: center;
  }
</style>
