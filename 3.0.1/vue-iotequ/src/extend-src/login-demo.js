import { hideMenu } from '@/utils/menu'
export default {
  doSomethingAfterLogin: function() {
    console.log("doSomethingAfterLogin")
    setTimeout(_=>{
      hideMenu('/login')
    },200)
  }
}