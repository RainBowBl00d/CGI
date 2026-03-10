<script setup>
import { ref, onMounted } from 'vue';
import api from './api'; // Meie loodud axios-e fail

const tables = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await api.get('/tables');
    tables.value = response.data;
    console.log("Andmed käes:", tables.value);
  } catch (err) {
    error.value = "Backendi ühendus puudub!";
    console.error(err);
  }
});
</script>

<template>
  <div>
    <h1>Saaliplaan</h1>
    <p v-if="error" style="color: red;">{{ error }}</p>

    <div v-for="tableTop in tables" :key="tableTop.id">
      Laud nr {{ tableTop.id }} - Kohti: {{ tableTop.capacity }}
    </div>
  </div>
</template>
