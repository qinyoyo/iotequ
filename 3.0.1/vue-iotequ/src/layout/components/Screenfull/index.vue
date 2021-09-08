<template>
  <div @click="handleFullScreen">
    <cg-icon :icon="isFullscreen?'svg-exit-fullscreen':'svg-fullscreen'" />
    <span> {{ $t('system.layout.screenFull') }}</span>
  </div>
</template>

<script>
export function exitFullScreen() {
    let element = document.documentElement;
    try {
      if (document.exitFullscreen) {
          document.exitFullscreen();
      } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
      } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
      } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
      }
    } catch (e) {
      console.log(e)
    }
}
export function fullScreen() {
    try {
      let element = document.documentElement;
      if (element.requestFullscreen) {
          element.requestFullscreen();
      } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
      } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
      } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
      }
    } catch (e) {
      console.log(e)
    }
}
export function isFullScreen() {
    const fullscreenEnabled = document.fullscreenEnabled       ||
        document.mozFullScreenEnabled    ||
        document.webkitFullscreenEnabled ||
        document.msFullscreenEnabled;
    if (fullscreenEnabled) {
        const fullscreenElement = document.fullscreenElement    ||
            document.mozFullScreenElement ||
            document.webkitFullscreenElement;
        return  fullscreenElement
    } else return 0
}
export default {
  name: 'Screenfull',
  data() {
    return {
      isFullscreen: false
    }
  },
  methods: {
    handleFullScreen(){
      let element = document.documentElement;
      if (this.isFullscreen) {
        exitFullScreen()
      } else {
        fullScreen()
      }
      this.isFullscreen = !this.isFullscreen;
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>
