<template>
  <content-box class="article-box">
    <div class="article-container" slot="content">
      <!--文章标题-->
      <h1 class="article-title">{{article.title}}</h1>
      <!--文章信息-->
      <article-info class="article-info" :article="article"></article-info>
      <!--文章概述-->
      <p class="article-desc">{{article.description}}</p>
      <!--文章内容-->
      <article-page-content :article="article" id="src-toc" ref="src-toc"></article-page-content>
      <!--打赏功能-->
      <article-reward></article-reward>
      <!--文章版权声明-->
      <article-copyright></article-copyright>
      <!--文章标签-->
      <tag-wall class="article-tag-wall" :tagList="article.tagList"></tag-wall>
      <!--上一页/下一页-->
      <div class="article-context">
        <div class="article-previous" @click="handleToArticle()" v-if="true">
          <span class="iconfont">&#xe581;</span>
          <span class="title-ellipsis">JAVA非对称加密算法RSA</span> <!--根据article.id计算上一篇文章-->
        </div>
        <div class="article-next" @click="handleToArticle()" v-if="true">
          <span class="title-ellipsis">Spring Cloud微服务权限系统搭建教程</span> <!--根据article.id计算下一篇文章-->
          <span class="iconfont">&#xe580;</span>
        </div>
      </div>
    </div>
    <!--添加评论系统-->
    <div class="article-common" slot="common">
      <valine-common></valine-common>
    </div>
  </content-box>
</template>

<script type="text/ecmascript-6">
import ContentBox from 'components/content/ContentBox'
import ArticleInfo from 'components/content/ArticleInfo'
import ArticlePageContent from 'components/content/article/ArticlePageContent'
import ArticleReward from 'components/content/article/ArticleReward'
import ArticleCopyright from 'components/content/article/ArticleCopyright'
import TagWall from 'components/content/TagWall'
import ValineCommon from 'components/common/ValineCommon'

import tocbot from 'tocbot'
import { makeIds } from 'common/js/utils'

export default {
  name: 'ArticleContent',
  components: {
    'content-box': ContentBox,
    'article-info': ArticleInfo,
    'article-page-content': ArticlePageContent,
    'article-reward': ArticleReward,
    'article-copyright': ArticleCopyright,
    'tag-wall': TagWall,
    'valine-common': ValineCommon
  },
  data () {
    return {
      tocbotControl: undefined,
      article: {
        id: 1,
        top: 1,
        tagList: [{ id: 0, name: 'Linux' },
          { id: 1, name: 'SpringBoot' },
          { id: 2, name: 'SpringCloud' },
          { id: 3, name: 'Nuxt.js' }],
        title: 'Java设计模式学习',
        createTime: '2020-03-12',
        updateTime: '2020-05-15',
        readNum: 10002,
        likeNum: 2025,
        description: '非对称加密和对称加密算法相比，多了一把秘钥，' +
          '为双秘钥模式，一个公开称为公钥，一个保密称为私钥。遵循公钥加密私钥解密，' +
          '或者私钥加密公钥解密。非对称加密算法源于DH算法，后又有基于椭圆曲线加密' +
          '算法的密钥交换算法ECDH，不过目前最为流行的非对称加密算法是RSA，本文简单记录下RSA的使用。',
        contentFormat: `<h1>1 创建型模式</h1>
                    <h2>1.1 简单工厂模式</h2>
                    <p>简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。</p>
                    <p>简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。
                    简单工厂模式严格意义上来说，并不属于设计模式中的一种，不过这里还是简单记录下。</p>
                    <p>定义：由一个工厂对象决定创建出哪一种类型实例。客户端只需传入工厂类的参数，无心关心创建过程。</p>
                    <p>优点：具体产品从客户端代码中抽离出来，解耦。</p>
                    <p>缺点：工厂类职责过重，增加新的类型时，得修改工程类得代码，违背开闭原则。</p>
                    <h2>1.2 工厂方法模式</h2>
                    <p>为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。
                    为了解决简单工厂模式的缺点，诞生了工厂方法模式（Factory method pattern）。</p>
                    <p>定义：定义创建对象的接口，让实现这个接口的类来决定实例化哪个类，工厂方法让类的实例化推迟到了子类进行。</p>
                    <h2>1.3 抽象工厂模式</h2>
                    <p>抽象工厂模式（Abstract factory pattern）提供了一系列相关或者相互依赖的对象的接口，关键字是“一系列”。
                    具体产品从客户端代码中抽离出来，解耦。
                    将一个系列的产品族统一到一起创建。
                    拓展新的功能困难，需要修改抽象工厂的接口；
                    综上所述，抽象工厂模式适合那些功能相对固定的产品族的创建。
                    举例：新建水果抽象类Fruit，包含buy抽象方法：
                    举例：新建水果抽象类Fruit，包含buy抽象方法。
                    举例：新建水果抽象类Fruit，包含buy抽象方法：
                    举例：新建水果抽象类Fruit，包含buy抽象方法：
                    举例：新建水果抽象类Fruit，包含buy抽象方法：
                    举例：新建水果抽象类Fruit，包含buy抽象方法：
                    举例：新建水果抽象类Fruit，包含buy抽象方法：</p>
                <h1>2 结构型模式</h1>
                    <h2>2.1 外观模式</h2>
                    <p>外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。</p>
                    <h2>2.2 装饰者模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                    <h2>2.3 适配器模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                    <h2>2.4 享元模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                <h1>3 行为模式</h1>
                    <h2>3.1 组合模式</h2>
                    <p>外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。
                    外观模式又叫门面模式，提供了统一得接口，用来访问子系统中的一群接口。</p>
                    <h2>3.2 桥接模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                    <h2>3.3 代理模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                    <h2>3.4 享元模式</h2>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                        <h3>3.4.1 享元模式</h3>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>
                        <h3>3.4.2 享元模式</h3>
                    <p>在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。
                    在不改变原有对象的基础之上，将功能附加到对象上，提供了比继承更有弹性的替代方案。</p>`
      }
    }
  },
  mounted () {
    makeIds(document.querySelector('#src-toc'))
    this.tocbotControl = tocbot.init({
      tocSelector: '#dest-toc', // ArticlePageToc的id,在ArticleSideBar.vue中设置
      contentSelector: '#src-toc', // ArticlePageContent的id,在ArticleContent.vue中设置
      headingSelector: 'h1, h2, h3',
      hasInnerContainers: true,
      linkClass: 'toc-link',
      activeLinkClass: 'is-active-link',
      listClass: 'toc-list',
      isCollapsedClass: 'is-collapsed',
      collapsibleClass: 'is-collapsible',
      listItemClass: 'toc-list-item',
      collapseDepth: 0,
      scrollSmooth: true,
      scrollSmoothDuration: 420,
      headingsOffset: 1,
      throttleTimeout: 50,
      positionFixedClass: 'is-position-fixed',
      fixedSidebarOffset: 'auto',
      includeHtml: true,
      onClick: false
    })
    tocbot.refresh()
  },
  beforeDestroy () {
    if (this.tocbotControl !== undefined) {
      this.tocbotControl.destroy()
    }
  },
  methods: {
    handleToArticle () {
      console.log('跳转到相应文章')
    }
  }
}
</script>

<style lang="stylus" type="text/stylus" rel="stylesheet/stylus" scoped>
  @import '~common/stylus/index.styl'
  .article-container, .article-common
    display block
    padding 1rem 2rem 1rem 2rem
    margin 0 10px 10px 10px
    border-radius 6px
    background-color $color-content-background
    font-weight 400
    color $color-article-font
    .article-title
      margin 1rem 0 0
      font-size 1.15rem
      font-weight 400
    .article-info
      margin 5px 0
    .article-desc
      padding 5px 10px 5px 10px
      margin 0.5rem 0 1rem
      line-height 1.6rem
      text-indent 2em
      background-color #F7F7FC
      border-left 3px solid $color-nav
      text-align justify
    .article-tag-wall
      margin 1.5rem 0
    .article-context
      position relative
      margin 1.5rem 0 2.3rem 0
      border-top 1px solid #f5f5f5
      font-weight 400
      .article-previous,
        display block
        position absolute
        left 0
        float left
        padding-top 0.8rem
        &:hover
          cursor pointer
          color $color-on-hover
        .iconfont
          float left
          margin-right 5px
          font-size 0.75rem
        .title-ellipsis
          display inline-block
          width 20rem // 长度超过20rem显示...
          overflow hidden
          text-overflow ellipsis
          white-space nowrap
      .article-next
        display inline-block
        position absolute
        right 0
        float right
        padding-top 0.8rem
        text-align right
        &:hover
          cursor pointer
          color $color-on-hover
        .iconfont
          float right
          margin-left 5px
          font-size 0.75rem
        .title-ellipsis
          display inline-block
          width 20rem // 长度超过20rem显示...
          overflow hidden
          text-overflow ellipsis
          white-space nowrap
  .article-common
    margin-bottom $footer-height-pageContent
  @media screen and (max-width: $size-xxl)
    .title-ellipsis
      width 16rem !important
  @media screen and (max-width: $size-xl)
    .title-ellipsis
      width 13rem !important
  @media screen and (max-width: $size-lg)
    .title-ellipsis
      width 11rem !important
  @media screen and (max-width: $size-md)
    .title-ellipsis
      width 8rem !important
  @media screen and (max-width: $size-sm)
    .article-container
      padding 1rem 1rem 1rem 1rem !important
    .article-common
      margin 0 0 $footer-height-pageContent 0 !important
      padding 1rem 1rem 1rem 1rem !important
    >>>.content-container,.common-container
      width 100% !important
</style>
