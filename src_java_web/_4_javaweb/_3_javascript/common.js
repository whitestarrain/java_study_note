/* 以后都有兼容性处理库，不用自己写。原理大致为下面那样 ，只是大致能用*/

/* 通过id获取元素 */
function my$(id) {
    return document.getElementById(id);
}

/* 处理nextElementSibling兼容性问题 */
function getNextElementSibling(element) {
    var e = element.nextSibling;
    while (e && 1 !== e.nodeType) e = e.nextSibling;
    return e;
}

/* 处理innerText兼容性问题 */
function setInnerText(element, content) {
    if (typeof element.innerText === 'string') {
        element.innerText = content;
    } else {
        element.textContent = content;
    }
}

/* 添加事件兼容性处理 */
function addEventListener(element, type, fn) {
    if (element.addEventListener) {
        element.addEventListener(type, fn, false);
    } else if (element.attachEvent) {
        element.attachEvent('on' + type, fn);
    } else {
        element['on' + type] = fn;
    }
}

/* 移除事件兼容性处理 */
function removeEventListener(element, type, fn) {
    if (element.removeEventListener) {
        element.removeEventListener(type, fn, false);
    } else if (element.detachEvent) {
        element.detachEvent('on' + type, fn);
    } else {
        element['on' + type] = null;
    }
}

/* 获取文档滚动距离，处理兼容性 */
function getScroll() {
    return {
        scrollLeft:
            document.body.scrollLeft || document.documentElement.scrollLeft,
        scrollTop: document.body.scrollTop || document.documentElement.scrollTop
    };
}

/* 处理获得pageX,pageY兼容性 */
function getPage(e) {
    return {
        pageX: e.pageX || e.clientX + getScroll().scrollLeft,
        pageY: e.pageY || e.clientY + getScroll.scrollTop
    };
}
