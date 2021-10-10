<template>
    <div class="nut-leftslip">
        <div class="nut-leftslip-item" ref="slipItem" :style="deleteSlider">
            <div @touchstart="touchStart($event)" @touchmove="touchMove($event)" @touchend="touchEnd($event)">
                <slot name="slip-main"></slot>
            </div>
            <div>
              <div class="slipbtns">
                <a v-for="a in actions" :style="'background-color:'+a.color" :key="a.action" @click="doAction(a)"><cg-icon v-if="a.icon" :icon="a.icon"/>{{a.icon?'':a.title}}</a>
              </div>
            </div>
        </div>
    </div>
</template>
<script>
import './leftslip.scss'
export default {
    name: 'nut-leftslip',
    props: {
        actions: {
            type: Array,
            default:()=>[]
        }
    },
    data() {
        return {
            startX: 0,
            startY: 0,
            moveX: 0,
            moveY: 0,
            left: 0,
            buttonWidth : 60,
            totalButtonsWidth: 0,
            disX: 0, //移动距离
            deleteSlider: '', //滑动时的效果,使用v-bind:style="deleteSlider"
            delBtnStyle: '' //单个删除键拖拽删除效果
        };
    },
    mounted() {
        this.totalButtonsWidth = (this.actions.length) * this.buttonWidth
        window.addEventListener('scroll', this.handleScroll, true);
    },
    beforeDestroy() {
        // 移除监听
        window.removeEventListener('scroll', this.handleScroll, true);
    },
    methods: {
        doAction(item) {
            this.$emit('doAction',(item.prefix?item.prefix:'')+item.action)
            this.restSlide();
        },
        handleScroll() {
            if (this.disX) {
                this.restSlide();
            }
        },
        handleClick() {
            this.restSlide();
        },
        touchStart(e) {
            this.restSlide();
            e = e || event;
            //等于1时表示此时有只有一只手指在触摸屏幕
            if (e.touches.length == 1) {
                this.startX = e.touches[0].clientX;
                this.startY = e.touches[0].clientY;
            }
        },
        touchMove(e) {
            e = e || event;
            //获取当前滑动对象
            let parentElement = e.currentTarget.parentElement;
            //获取删除按钮的宽度，此宽度为滑块左滑的最大距离
            let itemWd = this.$refs.slipItem.offsetWidth;
            let wd = this.totalButtonsWidth;

            if (e.touches.length == 1) {
                // 滑动时距离浏览器左侧实时距离
                this.moveY = e.touches[0].clientY;
                this.moveX = e.touches[0].clientX;
                if (Math.abs(this.moveY - this.startY) < this.buttonWidth) {
                    //起始位置减去 实时的滑动的距离，得到手指实时偏移距离
                    this.disX = this.startX - this.moveX;
                    // console.log(this.disX);
                    // 如果是向右滑动或者不滑动，不改变滑块的位置
                    if (this.disX < wd / 4 || this.disX == 0) {
                        this.deleteSlider = 'transform:translateX(0px)';
                        parentElement.dataset.type = 0;
                    } else if (this.disX > wd / 4) {
                        parentElement.dataset.type = 1;
                        this.$emit('leftslipStart')
                        this.deleteSlider = 'transform:translateX(-' + this.disX + 'px)';
                        // 最大也只能等于删除按钮宽度
                        if (this.disX * 1.5 >= wd) {
                            parentElement.dataset.type = 1;
                            if (wd >= itemWd) {
                                this.deleteSlider = 'transform:translateX(-' + (itemWd - this.buttonWidth) + 'px)';
                            } else {
                                this.deleteSlider = 'transform:translateX(-' + wd + 'px)';
                            }
                        }
                    }
                }
            }
        },
        touchEnd(e) {
            e = e || event;
            let parentElement = e.currentTarget.parentElement;
            let itemWd = this.$refs.slipItem.offsetWidth;
            let wd = this.totalButtonsWidth;
            if (e.changedTouches.length == 1) {
                let endY = e.changedTouches[0].clientY;
                if (Math.abs(this.startY - endY) < this.buttonWidth) {
                    let endX = e.changedTouches[0].clientX;
                    this.disX = this.startX - endX;
                    // console.log('touchEndthis.disX:', this.disX);
                        //如果距离小于删除按钮的四分之一,强行回到起点

                    if (this.disX < wd / 4) {
                        parentElement.dataset.type = 0;
                        this.deleteSlider = 'transform:translateX(0px)';
                    } else {
                        //大于一半 滑动到最大值
                        parentElement.dataset.type = 1;
                        if (wd >= itemWd) {
                            //按钮数不可超出整行宽度
                            this.deleteSlider = 'transform:translateX(-' + (itemWd - this.buttonWidth) + 'px)';
                        } else {
                            this.deleteSlider = 'transform:translateX(-' + wd + 'px)';
                        }
                    }

                    // console.log('touchEnd:dataset', parentElement.dataset.type);
                }
            }
        },
        restSlide() {
            this.totalButtonsWidth = (this.actions.length) * this.buttonWidth
            let listItems = document.querySelectorAll('.nut-leftslip-item');
            // 复位
            for (let i = 0; i < listItems.length; i++) {
                listItems[i].style = 'transform:translateX(0' + 'px)';
                listItems[i].dataset.type = 0;//是否展开标志位默认0，左滑展开为1，右滑隐藏为0
            }
             this.$emit('leftslipEnd')
            
        }
    }
};
</script>
