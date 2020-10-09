import Index from 'components/content/index/Index'

// 公共区域(懒加载)
const HomeSideBar = () => import('views/HomeSideBar')

// 首页（懒加载）
const ArticleAbstractList = () => import('views/ArticleAbstractList')
const ArticleTimeLine = () => import('views/ArticleTimeLine')
const TagList = () => import('views/TagList')

export default [
  {
    path: '/',
    component: Index,
    children: [
      {
        path: '/',
        name: 'index',
        components: {
          sideBar: HomeSideBar,
          content: ArticleAbstractList
        },
        meta: {
          title: 'metaTitle.index'
        }
      },
      {
        path: '/timeline',
        name: 'timeline',
        components: {
          sideBar: HomeSideBar,
          content: ArticleTimeLine
        },
        meta: {
          title: 'metaTitle.timeline'
        }
      },
      {
        path: '/tags',
        name: 'tags',
        components: {
          sideBar: HomeSideBar,
          content: TagList
        },
        meta: {
          title: 'metaTitle.tags'
        }
      }
    ]
  }
]
