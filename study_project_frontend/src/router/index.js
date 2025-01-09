import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'welcome',
      component: () => import('@/views/WelcomeVue.vue'),
      children: [
        {
          path: '',
          name: 'welcome-login',
          component: () => import('@/components/Welcome/LoginPage.vue')
        },{
          path: 'register',
          name: 'welcome-register',
          component: () => import('@/components/Welcome/RegisterPage.vue')
        },{
          path: 'forget',
          name: 'welcome-forget',
          component: () => import('@/components/Welcome/ForgetPage.vue')
        }
      ]
    },{
       path:'/index',
       name:'index',
       component:()=>import('@/views/IndexVue.vue')
    }
  ],
})

export default router
