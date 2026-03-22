<script setup lang="ts">
import type { RestaurantTable } from '@/types';
import { computed } from 'vue';

const props = defineProps<{
  table: RestaurantTable;
  isReserved?: boolean;
  isSelected?: boolean;
}>();

// Calculate size based on capacity
const tableSize = computed(() => {
  const baseSize = 50;
  const sizePerPerson = 10;
  return baseSize + (props.table.capacity * sizePerPerson);
});

// Calculate dimensions based on shape
const tableDimensions = computed(() => {
  const size = tableSize.value;
  if (props.table.shape === 'RECTANGLE') {
    return {
      width: size * 1.5,
      height: size * 0.7
    };
  }
  return {
    width: size,
    height: size
  };
});
</script>

<template>
  <div
    class="restaurantTable"
    :class="[
      'shape-' + table.shape.toLowerCase(),
      { reserved: isReserved, selected: isSelected }
    ]"
    :style="{
      left: table.x + 'px',
      top: table.y + 'px',
      width: tableDimensions.width + 'px',
      height: tableDimensions.height + 'px'
    }"
  >
    <span class="id">{{ table.id }}</span>
    <span class="capacity">{{ table.capacity }} in.</span>
  </div>
</template>

<style scoped>
.restaurantTable {
  position: absolute;
  background-color: #2ecc71; /* Green - available table */
  border: 2px solid #27ae60;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.restaurantTable.shape-circle {
  border-radius: 50%;
}

.restaurantTable.shape-square {
  border-radius: 8px;
}

.restaurantTable.shape-rectangle {
  border-radius: 12px;
}

/* Reserved table - red */
.restaurantTable.reserved {
  background-color: #e74c3c;
  border-color: #c0392b;
  cursor: not-allowed;
}

/* Selected table - yellow */
.restaurantTable.selected {
  background-color: #ffea00;
  border-color: #c0912b;
  color: #333;
  z-index: 10;
  transform: scale(1.1);
}

.restaurantTable:hover:not(.reserved) {
  transform: scale(1.05);
  box-shadow: 0 6px 12px rgba(0,0,0,0.15);
  z-index: 10;
}

.id {
  font-weight: bold;
  font-size: 1.1rem;
}

.capacity {
  font-size: 0.8rem;
  opacity: 0.9;
}
</style>
