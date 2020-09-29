import Index from 'components/content/index/Index'

// 公共区域(懒加载)
const HomeSideBar = () => import('views/HomeSideBar')

// 首页（懒加载）
const ArticleAbstractList = () => import('views/ArticleAbstractList')

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
          title: '首页'
        }
      }
    ]
  }
]
