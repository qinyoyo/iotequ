<template>
  <div :class="computedClasses" class="material-input__component">
    <div :class="icon ? 'has-pre-icon':'no-pre-icon'">
      <i v-if="icon && icon.indexOf('fa-') > -1" :class="icon+' fa-fw material-icon material-preppend-icon'" aria-hidden="true" v-on="$listeners"></i>
      <i v-else-if="icon && icon.indexOf('el-icon') > -1" :class="icon+' material-icon material-preppend-icon'" aria-hidden="true" v-on="$listeners"></i>
      <svg v-else-if="icon" class="material-icon material-preppend-icon" aria-hidden="true">
        <use :href="'#icon-'+icon" />
      </svg>
      <input
        v-if="type === 'email'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :required="required"
        type="email"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <input
        v-if="type === 'url'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :required="required"
        type="url"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <input
        v-if="type === 'number'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :step="step"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :max="max"
        :min="min"
        :minlength="minlength"
        :maxlength="maxlength"
        :required="required"
        type="number"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <input
        v-if="type === 'password' || type === 'visible-password'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :max="max"
        :min="min"
        :required="required"
        :type="passwordType"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <input
        v-if="type === 'tel'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :required="required"
        type="tel"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <input
        v-if="type === 'text'"
        v-model="currentValue"
        :name="name"
        :placeholder="fillPlaceHolder"
        :readonly="readonly"
        :disabled="disabled"
        :autocomplete="autoComplete"
        :minlength="minlength"
        :maxlength="maxlength"
        :required="required"
        type="text"
        class="material-input"
        @focus="handleMdFocus"
        @blur="handleMdBlur"
        @input="handleModelInput"
      >
      <span class="material-input-bar" />
      <label v-if="label" class="material-label">
        {{ label }}
      </label>
      <span v-if="type === 'visible-password'" @click="showPassword">
        <svg class="material-icon material-append-icon">
          <use :href="passwordType === 'password' ? '#icon-eye' : '#icon-eye-open'"/>
        </svg>
      </span>
      <span v-else-if="clearable" @click="handleClear">
        <i class="el-icon-error material-icon material-append-icon" />
      </span>
      <slot></slot>
    </div>
  </div>
</template>

<script>
// source:https://github.com/wemake-services/vue-material-input/blob/master/src/components/MaterialInput.vue
export default {
  name: 'MdInput',
  props: {
    /* eslint-disable */
    icon: String,
    clearable: Boolean,
    name: String,
    type: {
      type: String,
      default: 'text'
    },
    value: [String, Number],
    label: String,
    placeholder: String,
    readonly: Boolean,
    disabled: Boolean,
    min: String,
    max: String,
    step: String,
    minlength: Number,
    maxlength: Number,
    required: {
      type: Boolean,
      default: true
    },
    autoComplete: {
      type: String,
      default: 'off'
    },
    validateEvent: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      currentValue: this.value,
      focus: false,
      fillPlaceHolder: null,
      passwordType: 'password'
    }
  },
  computed: {
    computedClasses() {
      return {
        'material--active': this.focus,
        'material--disabled': this.disabled,
        'material--raised': Boolean(this.focus || this.currentValue) // has value
      }
    }
  },
  watch: {
    value(newValue) {
      this.currentValue = newValue
    }
  },
  methods: {
    showPassword() {
      if (this.passwordType === 'password') {
        this.passwordType = 'text'
      } else {
        this.passwordType = 'password'
      }
    },    
    handleModelInput(event) {
      const value = event.target.value
      this.$emit('input', value)
      if (this.$parent.$options.componentName === 'ElFormItem') {
        if (this.validateEvent) {
          this.$parent.$emit('el.form.change', [value])
        }
      }
      this.$emit('change', value)
    },
    handleClear(event) {
      this.$emit('input', '')
      if (this.$parent.$options.componentName === 'ElFormItem') {
        if (this.validateEvent) {
          this.$parent.$emit('el.form.change', [''])
        }
      }
      this.$emit('change', '')
    },
    handleMdFocus(event) {
      this.focus = true
      this.$emit('focus', event)
      if (this.placeholder && this.placeholder !== '') {
        this.fillPlaceHolder = this.placeholder
      }
    },
    handleMdBlur(event) {
      this.focus = false
      this.$emit('blur', event)
      this.fillPlaceHolder = null
      if (this.$parent.$options.componentName === 'ElFormItem') {
        if (this.validateEvent) {
          this.$parent.$emit('el.form.blur', [this.currentValue])
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  // Fonts:
  $font-size-base: 16px;
  $font-size-small: 18px;
  $font-size-smallest: 12px;
  $font-weight-normal: normal;
  $font-weight-bold: bold;
  $apixel: 1px;
  // Utils
  $spacer: 4px;
  $transition: 0.2s ease all;
  $index: 0px;
  $index-has-icon: 30px;
  // Theme:
  $color-white: white;
  $color-grey: #9E9E9E;
  $color-grey-light: #E0E0E0;
  $color-blue: #2196F3;
  $color-red: #F44336;
  $color-black: black;
  // Base clases:
  %base-bar-pseudo {
    content: '';
    height: 1px;
    width: 0;
    bottom: 0;
    position: absolute;
    transition: $transition;
  }

  // Mixins:
  @mixin slided-top() {
    top: - ($font-size-base + 3*$spacer);
    left: 0;
    font-size: $font-size-base;
    font-weight: $font-weight-bold;
  }
  .material-icon {
    width: $font-size-base;
    height: $font-size-base;
    overflow: hidden;
    font-size: $font-size-base;
    cursor: pointer;
    user-select: none;
  }
  .material-preppend-icon {
    position: absolute;
    left: $spacer;
    bottom: $spacer;
  }
  .material-append-icon {
    position: absolute;
    right: $spacer;
    bottom: $spacer;
  }
  // Component:
  .material-input__component {
    margin-top: $font-size-base + 4;
    position: relative;
    * {
      box-sizing: border-box;
    }
    .has-pre-icon {
      .material-label {
        left: $index-has-icon;
      }
      .material-input {
        text-indent: $index-has-icon;
      }
    }
    .material-input {
      font-size: $font-size-base;
      height: $font-size-base + 2 * $spacer;  
      padding: $spacer $spacer $spacer - $apixel * 10 0;
      background-color: transparent !important;
      display: block;
      width: 100%;
      bottom: $spacer;
      border: none;
      line-height: 1;
      border-radius: 0;
      &:focus {
        outline: none;
        border: none;
        border-bottom: 1px solid transparent; // fixes the height issue
      }
    }
    .material-label {
      font-weight: $font-weight-normal;
      position: absolute;
      pointer-events: none;
      left: $index;
      top: -2 * $spacer;
      transition: $transition;
      font-size: $font-size-base - 6;
    }
    .material-input-bar {
      position: relative;
      display: block;
      width: 100%;
      &:before {
        @extend %base-bar-pseudo;
        left: 50%;
      }
      &:after {
        @extend %base-bar-pseudo;
        right: 50%;
      }
    }
    &.material--disabled {
      .material-input {
        border-bottom-style: dashed;
      }
    }
    &.material--raised {
      .material-label {
        @include slided-top();
        font-size: $font-size-base - 6;
      }
    }
    // Active state:
    &.material--active {
      .material-input-bar {
        &:before,
        &:after {
          width: 50%;
        }
      }
    }
  }

  .material-input__component {
    background: $color-white;
    .material-input {
      background: none;
      color: $color-black;
      text-indent: $index;
      border-bottom: 1px solid $color-grey-light;
    }
    .material-label {
      color: $color-grey;
    }
    .material-input-bar {
      &:before,
      &:after {
        background: $color-blue;
      }
    }
    // Active state:
    &.material--active {
      .material-label {
        color: $color-blue;
      }
    }
    // Errors:
    &.material--has-errors {
      &.material--active .material-label {
        color: $color-red;
      }
      .material-input-bar {
        &:before,
        &:after {
          background: transparent;
        }
      }
    }
  }
</style>
