<script setup lang="ts">
import type { RestaurantTable } from '@/types';
const props = defineProps<{
  table: RestaurantTable
}>();
</script>

<template>
  <div
    class="restaurantTable"
    :class="{ 'occupied': table.occupied }"
    :style="{
          left: table.x + 'px',
          top: table.y + 'px'
        }"
  >
    <span class="id">{{ table.id }}</span>
    <span class="capacity">{{ table.capacity }} in.</span>
  </div>
</template>

<style scoped>
.restaurantTable {
  position: absolute;
  width: 70px;
  height: 70px;
  background-color: #2ecc71; /* Roheline vaba laud */
  border: 2px solid #27ae60;
  border-radius: 12px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.restaurantTable.occupied {
  background-color: #ff1600 ; /* Punane hõivatud laud */
  border-color: #c0392b;
}


.restaurantTable:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0,0,0,0.15);
  z-index: 10;
  background-color: #ffea00; /* Kollane Hover laud */
  border-color: #c0912b;
}

.table-content {
  display: flex;
  flex-direction: column;
  text-align: center;
}

.id { font-weight: bold; font-size: 1.1rem; }
.capacity { font-size: 0.8rem; opacity: 0.9; }
</style>
