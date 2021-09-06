function urlParameters(name, value) {
    name = name.replace(/=/g, "");
    var n = [];
    switch (value.constructor) {
        case String:
        case Number:
        case Boolean:
            n.push(encodeURIComponent(name) + "=" + encodeURIComponent(value));
            break;
        case Array:
            let vv=[]
            value.forEach(function(o) {
               vv.push(o+'')
            })
            n.push(encodeURIComponent(name) + "=" + encodeURIComponent(vv.join(',')))
            break;
        case Object:
            Object.keys(value).forEach((function(r) {
                var c = value[r];
                n = n.concat(urlParameters(name + "[" + r + "]", c))
            }))
    }
    return n
}
export function jsonp(url, config, timeout) {
    if (void 0 === config && (config = {}), "string" != typeof url) throw new Error('Type of param "url" is not string.')
    if ("object" != typeof config || !config) throw new Error("Invalid params, should be an object.")
    var timeout = "number" == typeof timeout ? timeout: 0
    return new Promise((function(resolve, reject) {
        var callbackQuery = "string" == typeof config.callbackQuery ? config.callbackQuery: "callback",
            callbackName = "string" == typeof config.callbackName ? config.callbackName: "jsonp_" + (Math.floor(1e5 * Math.random()) * Date.now()).toString(16);
        config[callbackQuery] = callbackName
        delete config.callbackQuery
        delete config.callbackName
        var params = []
        Object.keys(config).forEach((function(key) {
            params = params.concat(urlParameters(key, config[key]))
        }))
        var requestParams = params.join("&"),
            timer = null,
            rmError = function() {
                script.removeEventListener("error", onError)
            },
            restore = function() {
                document.body.removeChild(script)
                delete window[callbackName]
            },
            onError = function() {
                rmError()
                restore()
                if (timer) clearTimeout(timer)
                reject({
                    status: 400,
                    statusText: "Bad Request"
                })
            };
        if (timeout > 0) {
            timer = setTimeout((function() {
                rmError()
                restore()
                reject({
                    statusText: "Request Timeout",
                    status: 408
                })
            }), timeout)
        }
        window[callbackName] = function(e) {
            clearTimeout(timer)
            rmError()
            restore()
            resolve(e)
        }
        var script = document.createElement("script");
        script.addEventListener("error", onError),
        script.src = url + (/\?/.test(url) ? "&": "?") + requestParams
        document.body.appendChild(script)
    }))
}

