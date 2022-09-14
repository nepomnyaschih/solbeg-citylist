import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../pages/LoginPage.vue'
import ListPage from '../pages/ListPage.vue'

import store from '../store/index.js'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: LoginPage
    },
    {
      path: '/',
      name: 'List',
      component: ListPage,
      meta: {
        requiresAuth: true
      }
    }
  ]
})

function isLoggedIn() {
  return store.getters["loginModule/getLoginStatus"].loggedIn;
}

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isLoggedIn()) {
      next({ name: 'Login' })
    } else {
      next()
    }
  } else if (to.name==='Login' && isLoggedIn()) {
    next({ name: 'List' })
  } else {
    next()
  }
})

export default router
