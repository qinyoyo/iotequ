/* istanbul ignore next */

import Vue from 'vue';

const isServer = Vue.prototype.$isServer;
const SPECIAL_CHARS_REGEXP = /([\:\-\_]+(.))/g;
const MOZ_HACK_REGEXP = /^moz([A-Z])/;
const ieVersion = isServer ? 0 : Number(document.documentMode);

/* istanbul ignore next */
const trim = function(string) {
  return (string || '').replace(/^[\s\uFEFF]+|[\s\uFEFF]+$/g, '');
};
/* istanbul ignore next */
const camelCase = function(name) {
  return name.replace(SPECIAL_CHARS_REGEXP, function(_, separator, letter, offset) {
    return offset ? letter.toUpperCase() : letter;
  }).replace(MOZ_HACK_REGEXP, 'Moz$1');
};

/* istanbul ignore next */
export const on = (function() {
  if (!isServer && document.addEventListener) {
    return function(element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false);
      }
    };
  } else {
    return function(element, event, handler) {
      if (element && event && handler) {
        element.attachEvent('on' + event, handler);
      }
    };
  }
})();

/* istanbul ignore next */
export const off = (function() {
  if (!isServer && document.removeEventListener) {
    return function(element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false);
      }
    };
  } else {
    return function(element, event, handler) {
      if (element && event) {
        element.detachEvent('on' + event, handler);
      }
    };
  }
})();

const elementStyle = document.createElement('div').style
let vendor = (() => {
  // first pick up standard to fix #743
  let transformNames = {
    standard: 'transform',
    webkit: 'webkitTransform',
    Moz: 'MozTransform',
    O: 'OTransform',
    ms: 'msTransform'
  }

  for (let key in transformNames) {
    if (elementStyle[transformNames[key]] !== undefined) {
      return key
    }
  }
  return false
})()

function prefixStyle(style) {
  if (vendor === false) {
    return false
  }
  if (vendor === 'standard') {
    if (style === 'transitionEnd') {
      return 'transitionend'
    }
    return style
  }
  return vendor + style.charAt(0).toUpperCase() + style.substr(1)
}
const transitionEnd = prefixStyle('transitionEnd')

export const onTransitionEnd = function (element, handler) {
  on(element, transitionEnd, handler)
}
export const offTransitionEnd = function (element, handler) {
  off(element, transitionEnd, handler)
}
/* istanbul ignore next */
export const once = function(el, event, fn) {
  var listener = function() {
    if (fn) {
      fn.apply(this, arguments);
    }
    off(el, event, listener);
  };
  on(el, event, listener);
};

/* istanbul ignore next */
export function hasClass(el, cls) {
  if (!el || !cls) return false;
  if (cls.indexOf(' ') !== -1) throw new Error('className should not contain space.');
  if (el.classList) {
    return el.classList.contains(cls);
  } else {
    return (' ' + el.className + ' ').indexOf(' ' + cls + ' ') > -1;
  }
};

/* istanbul ignore next */
export function addClass(el, cls) {
  if (!el) return;
  var curClass = el.className;
  var classes = (cls || '').split(' ');

  for (var i = 0, j = classes.length; i < j; i++) {
    var clsName = classes[i];
    if (!clsName) continue;

    if (el.classList) {
      el.classList.add(clsName);
    } else if (!hasClass(el, clsName)) {
      curClass += ' ' + clsName;
    }
  }
  if (!el.classList) {
    el.className = curClass;
  }
};

/* istanbul ignore next */
export function removeClass(el, cls) {
  if (!el || !cls) return;
  var classes = cls.split(' ');
  var curClass = ' ' + el.className + ' ';

  for (var i = 0, j = classes.length; i < j; i++) {
    var clsName = classes[i];
    if (!clsName) continue;

    if (el.classList) {
      el.classList.remove(clsName);
    } else if (hasClass(el, clsName)) {
      curClass = curClass.replace(' ' + clsName + ' ', ' ');
    }
  }
  if (!el.classList) {
    el.className = trim(curClass);
  }
};

/* istanbul ignore next */
export const getStyle = ieVersion < 9 ? function(element, styleName) {
  if (isServer) return;
  if (!element || !styleName) return null;
  styleName = camelCase(styleName);
  if (styleName === 'float') {
    styleName = 'styleFloat';
  }
  try {
    switch (styleName) {
      case 'opacity':
        try {
          return element.filters.item('alpha').opacity / 100;
        } catch (e) {
          return 1.0;
        }
      default:
        return (element.style[styleName] || element.currentStyle ? element.currentStyle[styleName] : null);
    }
  } catch (e) {
    return element.style ? element.style[styleName] : null;
  }
} : function(element, styleName) {
  if (isServer) return;
  if (!element || !styleName) return null;
  styleName = camelCase(styleName);
  if (styleName === 'float') {
    styleName = 'cssFloat';
  }
  try {
    var computed = document.defaultView.getComputedStyle(element, '');
    return element.style[styleName] || computed ? computed[styleName] : null;
  } catch (e) {
    return element.style ? element.style[styleName] : null;
  }
};

/* istanbul ignore next */
export function setStyle(element, styleName, value) {
  if (!element || !styleName) return;
  if (typeof styleName === 'object') {
    for (var prop in styleName) {
      if (styleName.hasOwnProperty(prop)) {
        setStyle(element, prop, styleName[prop]);
      }
    }
  } else if (value) {
    styleName = camelCase(styleName);
    if (styleName === 'opacity' && ieVersion < 9) {
      element.style.setProperty('filter', isNaN(value) ? '' : 'alpha(opacity=' + value * 100 + ')')
    } else {
      element.style.setProperty(styleName,value)
    }
  } else if (typeof styleName === 'string') {
    if (styleName.indexOf(';')==0) element.style.cssText = element.style.cssText + styleName
    else element.style.cssText = styleName
  }
};
export function setAttribute(e, k, value, appendMode) {
  if (k=='readonly' || k=='disabled') {
    if (value) e.setAttribute(k, k)
    else e.removeAttribute(k)
  } else if (k=='class') {
    if (appendMode) addClass(e, value)
    else e.className = value
  } else if (k=='style') {
    if (typeof styleName === 'object') setStyle(e, value)
    else setStyle(e, appendMode ? ';'+value : value)
  } else if (appendMode) {
    let attr = e.getAttribute(k)
    if (attr) e.setAttribute(k, e + value)
    else e.setAttribute(k, value)
  } else e.setAttribute(k, value) 
}

export const isScroll = (el, vertical) => {
  if (isServer) return;

  const determinedDirection = vertical !== null || vertical !== undefined;
  const overflow = determinedDirection
    ? vertical
      ? getStyle(el, 'overflow-y')
      : getStyle(el, 'overflow-x')
    : getStyle(el, 'overflow');

  return overflow.match(/(scroll|auto)/);
};


export const getParent = (el, tag, parentClass) => {
  if (!el) return null
  if (!tag && !parentClass) return el.parentNode
  while ( el && el.tagName.toLowerCase() != "body") {
    if (tag && !parentClass && el.tagName.toLowerCase() === tag) return el
    else if (!tag && parentClass && el.classList.contains(parentClass)) return el
    else if (tag && parentClass && el.tagName.toLowerCase() === tag && el.classList.contains(parentClass)) return el
    el = el.parentNode;
  }
  return null;
};

export const isVisible = (el) => {
  const hidden = function(e) {
    const dis = getStyle(e,'display')
    return dis === 'none' 
  }
  if (hidden(el)) return false
  let p = getParent(el)
  while (p) {
    if (hidden(p)) return false
    p=getParent(p)
  }
  return true
};

export const getScrollContainer = (el, vertical) => {
  if (isServer) return;

  let parent = el;
  while (parent) {
    if ([window, document, document.documentElement].includes(parent)) {
      return window;
    }
    if (isScroll(parent, vertical)) {
      return parent;
    }
    parent = parent.parentNode;
  }

  return parent;
};

export const isInContainer = (el, container) => {
  if (isServer || !el || !container) return false;

  const elRect = el.getBoundingClientRect();
  let containerRect;

  if ([window, document, document.documentElement, null, undefined].includes(container)) {
    containerRect = {
      top: 0,
      right: window.innerWidth,
      bottom: window.innerHeight,
      left: 0
    };
  } else {
    containerRect = container.getBoundingClientRect();
  }

  return elRect.top < containerRect.bottom &&
    elRect.bottom > containerRect.top &&
    elRect.right > containerRect.left &&
    elRect.left < containerRect.right;
};

export function prepend(el, target) {
  if (target.firstChild) {
    before(el, target.firstChild)
  } else {
    target.appendChild(el)
  }
}

export function before(el, target) {
  target.parentNode.insertBefore(el, target)
}
export function addBodyNode(id, nodeType) {
  const body = document.body
  const tempNode = document.createElement(nodeType ? String(nodeType) : 'div')
  tempNode.id = String(id)
  body.appendChild(tempNode)
  return tempNode
}

export function removeBodyNode(id) {
  const body = document.body
  let afterNode = false
  const dom2remove = []
  body.childNodes.forEach(node => {
    if (node.id === id) {
      afterNode = true
      dom2remove.push(node)
    }
    if (afterNode && (' ' + node.className + ' ').indexOf(' v-modal ') > -1) dom2remove.push(node)
  })
  dom2remove.forEach(node => {
    body.removeChild(node)
  })
}

export function getRect(el) {
  if (el instanceof window.SVGElement) {
    let rect = el.getBoundingClientRect()
    return {
      top: rect.top,
      left: rect.left,
      width: rect.width,
      height: rect.height
    }
  } else {
    return {
      top: el.offsetTop,
      left: el.offsetLeft,
      width: el.offsetWidth,
      height: el.offsetHeight
    }
  }
}
export function offset(el) {
  let left = 0
  let top = 0

  while (el) {
    left -= el.offsetLeft
    top -= el.offsetTop
    el = el.offsetParent
  }

  return {
    left,
    top
  }
}

export function offsetToBody(el) {
  let rect = el.getBoundingClientRect()

  return {
    left: -(rect.left + window.pageXOffset),
    top: -(rect.top + window.pageYOffset)
  }
}

export function scrollTop(){
  return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0
}
export function scrollLeft(){
    return window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0
}  