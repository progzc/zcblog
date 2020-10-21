# 0 技术方案

> `Vue Cli4 + Vue + Vuex + Vue router`： vue全家桶；
>
> `axios`： 发送Ajax请求；
>
> `element-ui`：前端UI组件；
>
> `echarts` + `vue-count-to`：来自于百度的图表制作插件 + 数字滚动插件；
>
> `mavon-editor` + `marked`：实现markdown文档的编辑  + markdown转html（可自动为h1~h6生成id属性）；
>
> `vue-cookie`：处理浏览器的cookie（本项目中主要作用是将登录的token封装到cookie中，实现跨域访问后台）；
>
> `lodash`：是一个一致性、模块化、高性能的 JavaScript 实用工具库（主要用来处理数组、集合、日期以及提供一些工具函数）；
>
> `node-sass`+`sass-loader`：可以将sass转换成css；
>
> `svg-sprite-loader`：可以将加载的svg图片拼接成雪碧图（亦称精灵图）。

# # 常见问题

## ## JS生成随机UUID（模拟）

- 算法1：

  ```javascript
  function uuid() {
      var s = []
      var hexDigits = "0123456789abcdef"
      for (var i = 0; i < 36; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
      }
      s[14] = "4"  // bits 12-15 of the time_hi_and_version field to 0010
      s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)  // bits 6-7 of the clock_seq_hi_and_reserved to 01
      s[8] = s[13] = s[18] = s[23] = "-"
      var uuid = s.join("")
      return uuid
  
  }
  ```

- 算法2（本项目所采用）：

  ```javascript
  function guid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
          var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8)
          return v.toString(16)
      })
  }
  ```

- 算法3：

  ```javascript
  function guid() {
      function S4() {
         return (((1+Math.random())*0x10000)|0).toString(16).substring(1)
      }
      return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4())
  }
  ```

- 算法4：

  ```javascript
  function uuid(len, radix) {
      var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
      var uuid = [], i;
      radix = radix || chars.length;
      if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
      } else {
        // rfc4122, version 4 form
        var r
        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'
        uuid[14] = '4'
        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
          if (!uuid[i]) {
            r = 0 | Math.random()*16;
            uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r]
          }
        }
      }
      return uuid.join('')
  }
  ```

## ## localStorage与sessionStorage

- 相同点：
  1. 均只能存储字符串类型的对象。

> **注意事项**：JavaScript中的JSON对象提供的stringify方法将其他数据类型转化成字符串，再存储到storage中；JavaScript中的JSON对象提供的parse方法可以将storage中取出的字符串转化成对象。
>
> ```javascript
> // 存储
> var obj = {"name":"xiaoming","age":"16"}
> localStorage.setItem("userInfo",JSON.stringify(obj))
> // 读取
> var user = JSON.parse(localStorage.getItem("userInfo"))
> // 删除
> localStorage.remove("userInfo)
> // 清空
> localStorage.clear()
> ```

- 不同点：
  1. localStorage生命周期是永久，这意味着除非用户显示在浏览器提供的UI上清除localStorage信息，否则这些信息将永远存在；sessionStorage生命周期为当前窗口或标签页，一旦窗口或标签页被永久关闭了，那么所有通过sessionStorage存储的数据也就被清空了。
  2. 不同浏览器无法共享localStorage或sessionStorage中的信息；相同浏览器的不同页面间可以共享相同的 localStorage；不同页面或标签页间无法共享sessionStorage的信息

> **注意事项**：页面及标签页仅指顶级窗口，如果一个标签页包含多个iframe标签且他们属于同源页面（同源指相同的相同的协议、主机和端口），那么他们之间可以共享sessionStorage。

## ## Lodash的extend/assign/merge

相似点：

- 均不能用于数组。
- extend和assign用法相同。

不同点：

- merge可以合并子对象。
- extend和assign会从根级别复写对象。

```javascript
// They all handle members at the root in similar ways.
_.assign      ({}, { a: 'a' }, { a: 'bb' }) // => { a: "bb" }
_.merge       ({}, { a: 'a' }, { a: 'bb' }) // => { a: "bb" }

// _.assign handles undefined but _.merge will skip it
_.assign      ({}, { a: 'a'  }, { a: undefined }) // => { a: undefined }
_.merge       ({}, { a: 'a'  }, { a: undefined }) // => { a: "a" }\

// They all handle null the same
_.assign      ({}, { a: 'a'  }, { a: null }) // => { a: null }
_.merge       ({}, { a: 'a'  }, { a: null }) // => { a: null }

// only _.merge will merge child objects
_.assign      ({}, {a:{a:'a'}}, {a:{b:'bb'}}) // => { "a": { "b": "bb" }}
_.merge       ({}, {a:{a:'a'}}, {a:{b:'bb'}}) // => { "a": { "a": "a", "b": "bb" }}

// none of them will merge arrays it seems
_.assign      ({}, {a:['a']}, {a:['bb']}) // => { "a": [ "bb" ] }
_.merge       ({}, {a:['a']}, {a:['bb']}) // => { "a": [ "bb" ] }

// All modify the target object
a={a:'a'}; _.assign      (a, {b:'bb'}); // a => { a: "a", b: "bb" }
a={a:'a'}; _.merge       (a, {b:'bb'}); // a => { a: "a", b: "bb" }

// None really work as expected on arrays.(Lodash treats arrays as objects where the keys are the index into the array.)
_.assign      ([], ['a'], ['bb']) // => [ "bb" ]
_.merge       ([], ['a'], ['bb']) // => [ "bb" ]
_.assign      ([], ['a','b'], ['bb']) // => [ "bb", "b" ]
_.merge       ([], ['a','b'], ['bb']) // => [ "bb", "b" ]
```

## ## axios拦截器返回response.data

在读取数据的时候还需要加一层data: response.data.data，这样的编码易引起混淆，可以在拦截器中配置axios让返回值直接能获取到data，而不是response.data.data。

```javascript
axios.interceptors.response.use(res => {
　　return res.data;
})
```

## ## HTTPS加密机制及登录界面的实现

> 背景：HTTP（**未加密**） --> 对称加密（**秘钥**） --> 非对称加密 （**公钥+私钥**）--> HTTPS（**证书+数字签名**）
>
> 总结：HTTPS = HTTP + TLS/SSL

相关博客文章：[HTTPS加密机制](https://blog.csdn.net/akunshouyoudou/article/details/95184254)、[网站启用HTTPS加密传输协议](https://blog.csdn.net/weixin_30426065/article/details/97928605)

# # Vue相关

##  ## 严格模式

strict：默认值为false，值为true则表示Vuex store进入严格模式，在严格模式下，任何 mutation 处理函数以外修改 Vuex state 都会抛出错误。

**注意事项**：不要在发布环境下启用严格模式。

```javascript
const store = new Vuex.Store({
  // ...
  strict: process.env.NODE_ENV !== 'production'
})
```

## ## 智能懒加载

vue懒加载：要求在开发环境不使用懒加载，生产环境才使用懒加载，可以如下配置：

```javascript
// router/index.js
import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
const _import = require('./_import_' + process.env.NODE_ENV) // 开发环境不使用懒加载，生产环境不使用懒加载
// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
]

// router/_import_development.js
module.exports = file => require('@/views/' + file + '.vue').default // vue-loader at least v13.0.0+

// router/_import_production.js
module.exports = file => () => import('@/views/' + file + '.vue')
```

## ## 三种导航守卫的执行时机

> 举个例子：vue项目常见的3种导航守卫有全局守卫、独享守卫、组件内守卫。例如：`beforeEach：路由全局前置守卫`、`beforeEnter：路由独享守卫`、`beforeRouteEnter：组件内前置守卫`。[三种常见导航守卫](https://blog.csdn.net/weixin_44781409/article/details/106461212?utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduend~default-3-106461212.nonecase&utm_term=vue%20%E5%85%A8%E5%B1%80%E5%89%8D%E7%BD%AE%E5%AE%88%E5%8D%AB%20%E6%89%A7%E8%A1%8C%E5%A4%9A%E6%AC%A1%20%E8%BF%98%E6%98%AF%E4%B8%80%E6%AC%A1&spm=1000.2123.3001.4430)

完整的导航守卫的执行时机如下：

1. 导航被触发。
2. 在失活的组件里调用离开守卫（组件内后置钩子）。
3. **调用全局的beforeEach守卫。**
4. 在重用的组件里调用beforeRouteUpdate守卫 (2.2+)。
5. **在路由配置里调用beforeEnter。**
6. 解析异步路由组件。
7. **在被激活的组件里调用beforeRouteEnter。**
8. 调用全局的beforeResolve守卫 (2.5+)。
9. 导航被确认。
10. 调用全局的afterEach钩子。
11. 触发DOM更新。
12. 用创建好的实例调用beforeRouteEnter守卫中传给next的回调函数。



























