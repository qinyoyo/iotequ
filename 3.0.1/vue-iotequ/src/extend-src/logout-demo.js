import { showMenu } from '@/utils/menu'
export default {
  doSomethingAfterLogout: function() {
    console.log("doSomethingAfterLogout")
    showMenu('*')
  }
}