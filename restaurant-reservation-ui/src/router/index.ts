import { createRouter, createWebHistory } from 'vue-router'
import FloorPlan from '../views/FloorPlan.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'floorplan',
      component: FloorPlan
    }
  ]
})

export default router
