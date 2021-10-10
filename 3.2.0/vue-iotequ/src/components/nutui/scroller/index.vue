<template>
    <div class="nut-scroller">
        <template v-if="type === 'vertical'">
            <nut-vert-scroll ref="scroller"
                :wrapperElement="wrapperElement"
                :stretch="stretch"
                :is-un-more="isUnMore"
                :is-loading="isLoading"
                :is-busy="isBusy"
                :threshold="threshold"
                :pulldown-txt="pulldownTxt"
                :load-more-txt="loadMoreTxt"
                :unload-more-txt="unloadMoreTxt"
                :props-time="propsTime"
                :scroll-to="scrollTo"
                :backtop="backtop"
                @loadMore="loadMore"
                @pulldown="pulldown"
                @scrollToCbk="scrollToCbk"
            >
                <slot name="list"  slot="list"></slot>
                <slot name="more"  slot="more"></slot>

            </nut-vert-scroll>
        </template>
        <template v-else-if="type === 'horizontal'">
            <nut-hor-scroll ref="scroller"
            :wrapperElement="wrapperElement"
            :stretch="stretch" 
            :scroll-to="scrollTo"
            @jump="jump" 
            @scrollToCbk="scrollToCbk"
        >
                <slot name="list"  slot="list"></slot>
                <slot name="more"  slot="more"></slot>
                <slot name="arrow" slot="arrow"></slot>
            </nut-hor-scroll>
        </template>
    </div>
</template>
<script>
import './scroller.scss'
import nutVertScroll from "./vertical-scroll.vue";
import nutHorScroll from "./horizontal-scroll.vue";
export default {
    name:'nut-scroller',
    props: {
        wrapperElement: {
            type : [Object, String],
            default : ()=>null
        },
        type: {
            type: String,
            default: 'horizontal' 
        },
        stretch: {
            type: Number,
            default: 100
        },
        backtop: {
            type: Boolean,
            default: true
        },
        isUnMore: {
            type: Boolean,
            default: false
        },
        isLoading: {
            type: Boolean,
            default: false
        },
        isBusy: {
            type: Boolean,
            default: false
        },
        threshold: {
            type: Number,
            default: 100
        },
        pulldownTxt: {
            type: String,
            default: '下拉刷新'
        },
        loadMoreTxt: {
            type: String,
            default: '上拉加载'
        },
        unloadMoreTxt: {
            type: String,
            default: '没有更多了'
        },
        propsTime: {
            type:Number,
            default: 0
        },
        scrollTo: {
            type: Number,
            default: 1
        }
    },
    data() {
        return {};
    },
    components: {
        [nutVertScroll.name]: nutVertScroll,
        [nutHorScroll.name]: nutHorScroll
    },
    methods: {
        loadMore() {
            this.$emit('loadMore');
        },

        jump() {
            this.$emit('jump');
        },

        pulldown() {
            this.$emit('pulldown');
        },

        scrollToCbk() {
            this.$emit('scrollToCbk');
        },
        reset() {
            this.$refs.scroller.reset()
        }
    }
}
</script>
