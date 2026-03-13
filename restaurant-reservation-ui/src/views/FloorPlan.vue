<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '@/api/api';
import type { RestaurantTable } from '@/types';
import RestaurantTableComponent from '@/components/RestaurantTableComponent.vue'


const tables = ref<RestaurantTable[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);

const fetchTables = async () => {
  try {
    const response = await api.get('/tables');
    tables.value = response.data;
  } catch (err) {
    error.value = "Andmete laadimine ebaõnnestus";
    console.error(err);
  } finally {
    loading.value = false;
  }
};
onMounted(fetchTables);
</script>

<template>
  <div class="floor-plan-container">
    <h2>Restorani saaliplaan</h2>

    <div v-if="loading" class="status">Laadin laudu...</div>
    <div v-else-if="error" class="status error">{{ error }}</div>

    <div v-else class="floor-canvas">
      <RestaurantTableComponent
        v-for="singleTable in tables"
        :key="singleTable.id"
        :table="singleTable"
      />
    </div>
  </div>
</template>

<style scoped>
.floor-canvas {
  position: relative;
  width: 800px;
  height: 600px;
  background-color: #f8f9fa;
  border: 2px solid #dee2e6;
  border-radius: 8px;
  margin: 20px auto;
}
</style>
