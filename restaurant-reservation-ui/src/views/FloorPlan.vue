<script setup lang="ts">
import { ref, onMounted } from 'vue';
import api from '@/api/api'; // Eeldusel, et api.ts on siia liigutatud

// Kirjeldame andmete struktuuri
interface RestaurantTable {
  id: number;
  capacity: number;
  x: number;
  y: number;
  isOccupied: boolean;
  zone: string;
}

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
      <div
        v-for="table in tables"
        :key="table.id"
        class="table"
        :class="{ 'occupied': table.isOccupied }"
        :style="{
          left: table.x + 'px',
          top: table.y + 'px'
        }"
      >
        <span class="table-id">{{ table.id }}</span>
        <span class="capacity">{{ table.capacity }} in.</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.floor-canvas {
  position: relative; /* See on kriitiline, et koordinaadid töötaksid */
  width: 800px;
  height: 600px;
  background-color: #f8f9fa;
  border: 2px solid #dee2e6;
  border-radius: 8px;
  margin: 20px auto;
}

.table {
  position: absolute; /* Paigutame laua täpselt x ja y järgi */
  width: 60px;
  height: 60px;
  background-color: #4caf50; /* Roheline = vaba */
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.2s;
}

.table.occupied {
  background-color: #f44336; /* Punane = hõivatud */
}

.table:hover {
  transform: scale(1.1);
}

.table-id {
  font-weight: bold;
}

.capacity {
  font-size: 0.8rem;
}
</style>
