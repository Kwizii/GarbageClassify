// eslint-disable-next-line
import {BasicLayout, PageView, RouteView, UserLayout} from '../layouts'
import {examAdmin, examList, questionAdmin} from '../core/icons'

export const asyncRouterMap = [

  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: {title: '智能垃圾分类系统'},
    redirect: '/dashboard/',
    children: [
      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/',
        component: RouteView,
        hideChildrenInMenu: true,
        meta: {title: '首页', keepAlive: true, icon: 'home', permission: ['dashboard']},
        children: [
          {
            path: '/dashboard/',
            name: 'carousel',
            component: () => import('../views/Carousel'),
            meta: {title: '首页', keepAlive: true, permission: ['dashboard']}
          }
        ]
      },
      {
        path: '/ai',
        name: 'ai',
        redirect: '/ai/classify',
        component: PageView,
        hideChildrenInMenu: true,
        meta: { title: '在线识别', keepAlive: true, icon: 'search', permission: ['dashboard'] },
        children: [
          {
            path: '/ai/classify',
            name: 'classify',
            component: () => import('../views/Classify'),
            meta: { title: '人工智能在线识别', keepAlive: true, permission: ['dashboard'] }
          }
        ]
      },
      {
        path: '/exam-card',
        name: 'exam-card',
        redirect: '/list/exam-card',
        component: PageView,
        hideChildrenInMenu: false,
        meta: { title: '知识问答', keepAlive: true, icon: examList, permission: ['exam-card'] },
        children: [
          {
            path: '/list/exam-card',
            name: 'ExamCardList',
            component: () => import('../views/list/ExamCardList'),
            meta: { title: '在线答题', icon: examList, keepAlive: true, permission: ['exam-card'] }
          }, {
            path: '/list/question-table-list',
            name: 'QuestionTableListWrapper',
            component: () => import('../views/list/QuestionTableList'),
            meta: { title: '题库管理', icon: questionAdmin, keepAlive: true, permission: ['question-admin'] }
          }, {
            path: '/list/exam-table-list',
            name: 'ExamTableListWrapper',
            hideChildrenInMenu: true, // 强制显示 MenuItem 而不是 SubMenu
            component: () => import('../views/list/ExamTableList'),
            meta: { title: '考试管理', icon: examAdmin, keepAlive: true, permission: ['exam-table-list'] }
          }, {
            path: '/list/exam-record-list',
            name: 'ExamRecordList',
            component: () => import('../views/list/ExamRecordList'),
            meta: { title: '答题历史', icon: 'user', keepAlive: true, permission: ['exam-record-list'] }
          }
        ]
      },
      {
        path: '/shop',
        name: 'shop',
        redirect: '/shop/list',
        component: PageView,
        hideChildrenInMenu: false,
        meta: { title: '积分商城', keepAlive: true, icon: 'gift', permission: ['dashboard'] },
        children: [
          {
            path: '/shop/list',
            name: 'ShopList',
            component: () => import('../views/list/ShopList'),
            meta: { title: '积分商城', keepAlive: true, icon: 'shop', permission: ['dashboard'] }
          },
          {
            path: '/shop/manage',
            name: 'ShopManageList',
            component: () => import('../views/list/ShopManageList'),
            meta: { title: '商品管理', keepAlive: true, icon: 'form', permission: ['question-admin'] }
          },
          {
            path: '/shop/order/list',
            name: 'ShopOrderList',
            component: () => import('../views/list/ShopOrderList'),
            meta: { title: '商城历史订单', keepAlive: true, icon: 'profile', permission: ['dashboard'] }
          }
        ]
      }
    ]
  },
  {
    // 所有访问不到的路径最终都会落到404里
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由，不在主菜单上展示，独立的路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '../views/user/RegisterResult')
      }
    ]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '../views/exception/404')
  },
  {
    path: '/exam/:id',
    component: () => import(/* webpackChunkName: "fail" */ '../views/list/ExamDetail')
  },
  {
    path: '/exam/record/:exam_id/:record_id',
    component: () => import(/* webpackChunkName: "fail" */ '../views/list/ExamRecordDetail')
  }
]
