export default {
    watch: {
        isNew(n) {
          this.displayTabpane(this.$refs.tabs,1,!n)
        }
    },
    mounted () {
        const that=this
        this.$nextTick(_=>{
            that.displayTabpane(that.$refs.tabs,1,!that.isNew)
        })
    },

    methods: {
        useMixinMethodsFirst() {
            return true
        }, 
        disabledAction(btn) {
            return this.isNew
        }        
    }
}