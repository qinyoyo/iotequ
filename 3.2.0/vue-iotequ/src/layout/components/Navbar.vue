<template>

  <div v-if="device!=='mobile' || $route.path === '/home'" class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />
        <!-- remove
        <error-log class="errLog-container right-menu-item hover-effect" />
        <el-tooltip :content="$t('system.layout.size')" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
        -->
      </template>
      <lang-select class="right-menu-item hover-effect" />

      <router-link class="right-menu-item hover-effect" to="/framework/sysMessage/list">
        <div>
          <i class="fa fa-commenting-o"></i>
          <div class="message-notify" v-if="newMessages" > <span class="heartbit"></span> <span class="point"></span> </div>
        </div>
      </router-link>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatarName" class="user-avatar">
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if="authentication" :to="{path:'/profile',query:{openMode:'edit'}}"> 
            <span @click = "profileDialog">
              {{ $t('system.layout.profile') }}
            </span>
          </el-dropdown-item>
          <el-dropdown-item v-if="device!=='mobile'">
            <span @click="visionSetting">{{ $t('system.layout.settings') }}</span>
          </el-dropdown-item>
          <el-dropdown-item v-if="!authentication">
            <span style="display:block;" @click="register">{{ $t('login.register') }}</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{ loginTitle() }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from './Breadcrumb'
import Hamburger from './Hamburger'
import ErrorLog from './ErrorLog'
import SizeSelect from './SizeSelect'
import LangSelect from './LangSelect'
import Search from './HeaderSearch'
import { jump2Url } from '@/utils/cg' 
export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    SizeSelect,
    LangSelect,
    Search
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device',
      'userId',
      'newMessages',
      'authentication'
    ]),
    avatarName() {
      return this.$store.getters.authentication && this.$store.getters.avatar ? 
        this.$store.getters.avatar + '&tick='+new Date().getTime() : require('@/icons/png/user.png')
    }
  },
  methods: {
    visionSetting(){
      this.$eventBus.$emit("showRightPanel",true)
    },
    profileDialog() {
      jump2Url('/profile')
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    loginTitle() {
      if (this.$store.state.user.authentication) return this.$i18n.t('system.layout.logOut')
      else return this.$i18n.t('system.layout.login')
    },
    logout() {
      if (this.$store.state.user.authentication) {
        const that=this
        this.$store.dispatch('user/logout').then(() => {
          if (that.$route.path!='/home') that.$router.push('/')
        }).catch(_ => {})
      } else {
        this.$router.push('/login')
      }
    },
    register() {
      if (!this.$store.state.user.authentication) {
        this.$router.push({
          path: '/login',
          query: {
            isRegister: true
          }
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;
        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 7px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: auth;
          height: 24px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
  .message-notify {
    position: relative;
    top: -25px;
    right:-7px;
    .heartbit {
        position: absolute;
        top: -20px;
        right: -4px;
        height: 25px;
        width: 25px;
        z-index: 10;
        border: 5px solid #fc4b6c;
        border-radius: 70px;
        -moz-animation: heartbit 1s ease-out;
        -moz-animation-iteration-count: infinite;
        -o-animation: heartbit 1s ease-out;
        -o-animation-iteration-count: infinite;
        -webkit-animation: heartbit 1s ease-out;
        -webkit-animation-iteration-count: infinite;
        animation-iteration-count: infinite;
    }
    .point {
        width: 6px;
        height: 6px;
        -webkit-border-radius: 30px;
        -moz-border-radius: 30px;
        border-radius: 30px;
        background-color: #fc4b6c;
        position: absolute;
        right: 6px;
        top: -10px;
    }
  }
}
@-moz-keyframes heartbit {
  0% {
    -moz-transform: scale(0);
    opacity: 0.0; }
  25% {
    -moz-transform: scale(0.1);
    opacity: 0.1; }
  50% {
    -moz-transform: scale(0.5);
    opacity: 0.3; }
  75% {
    -moz-transform: scale(0.8);
    opacity: 0.5; }
  100% {
    -moz-transform: scale(1);
    opacity: 0.0; } }

@-webkit-keyframes heartbit {
  0% {
    -webkit-transform: scale(0);
    opacity: 0.0; }
  25% {
    -webkit-transform: scale(0.1);
    opacity: 0.1; }
  50% {
    -webkit-transform: scale(0.5);
    opacity: 0.3; }
  75% {
    -webkit-transform: scale(0.8);
    opacity: 0.5; }
  100% {
    -webkit-transform: scale(1);
    opacity: 0.0; } }

</style>
