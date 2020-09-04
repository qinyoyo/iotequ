<template>
  <div id="app" :class="appClass">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      appClass: 'app-main-container cg-size-'+this.$store.state.app.size
    }
  }
}
</script>
<style lang="scss">
  @import "~@/styles/mixin.scss";
  @import "~@/styles/cg.scss";
  @import "~@/styles/cg-mobile.scss";
</style>