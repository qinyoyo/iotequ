<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Home from './home'

export default {
  name: 'Dashboard',
  components: { Home },
  data() {
    return {
      currentRole: 'Home'
    }
  }
}
</script>
