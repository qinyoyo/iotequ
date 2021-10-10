<template>
  <div class="wscn-http404-container">
    <div class="wscn-http404">
      <div>
        <img class="pic-404__parent" src="@/assets/404_images/404.png" alt="404">
      </div>
      <div>
        <div class="bullshit__oops">OOPS!</div>
        <div>
        <a class="bullshit__info" style="color:#20a0ff" href="https://www.iotequ.top" target="_blank">All rights reserved</a>
        </div>
        <div class="bullshit__headline">{{ message }}</div>
        <a @click="$router.go(-1)" class="bullshit__return-home">{{$t('system.action.back')}}</a>
        <a href="/" class="bullshit__return-home">{{$t('system.action.backHome')}}</a>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Page404',
  computed: {
    message() {
      return this.$t('system.message.err404') + (this.$route.redirectedFrom ? this.$route.redirectedFrom : '')
    }
  }
}
</script>

<style lang="scss" scoped>
.wscn-http404-container {
  margin-top:100px;
}
.bullshit__return-home {
  margin:10px;
}
.wscn-http404 {
  position: relative;
  top:10%;
  width: 100%;
  padding: 0 50px;
  overflow: hidden;
  .pic-404 {
    position: relative;
    float: left;
    overflow: hidden;
    &__parent {
      width: 50%;
    }
  }
  .bullshit {
    position: relative;
    float: left;
    width: 300px;
    padding: 30px 0;
    overflow: hidden;
    &__oops {
      font-size: 32px;
      font-weight: bold;
      line-height: 40px;
      color: #1482f0;
      opacity: 0;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-fill-mode: forwards;
    }
    &__headline {
      font-size: 20px;
      line-height: 24px;
      color: #222;
      font-weight: bold;
      opacity: 0;
      margin-bottom: 10px;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.1s;
      animation-fill-mode: forwards;
    }
    &__info {
      font-size: 13px;
      line-height: 21px;
      color: grey;
      opacity: 0;
      margin-bottom: 30px;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.2s;
      animation-fill-mode: forwards;
    }
    &__return-home {
      display: block;
      float: left;
      width: 110px;
      height: 36px;
      background: #1482f0;
      border-radius: 100px;
      text-align: center;
      color: #ffffff;
      opacity: 0;
      font-size: 14px;
      line-height: 36px;
      cursor: pointer;
      animation-name: slideUp;
      animation-duration: 0.5s;
      animation-delay: 0.3s;
      animation-fill-mode: forwards;
    }
    @keyframes slideUp {
      0% {
        transform: translateY(60px);
        opacity: 0;
      }
      100% {
        transform: translateY(0);
        opacity: 1;
      }
    }
  }
}
</style>
