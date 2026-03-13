import { createRouter, createWebHistory } from 'vue-router'
import ReservationView from '../views/ReservationView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'reservation',
      component: ReservationView
    }
  ]
})

export default router
