<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import api from '@/api/api';
import type { RestaurantTable, Reservation } from '@/types';
import ReservationFilters from '@/components/ReservationFilters.vue';
import AvailableTables from '@/components/AvailableTables.vue';
import ReservationSummary from '@/components/ReservationSummary.vue';
import RestaurantTableComponent from '@/components/RestaurantTableComponent.vue';

// State
const groupSize = ref<number>(2);
const reservationTimeStart = ref<string>('');
const windowSeat = ref<boolean>(false);
const quietArea = ref<boolean>(false);
const availableTables = ref<RestaurantTable[]>([]);
const selectedTable = ref<RestaurantTable | null>(null);
const loading = ref<boolean>(false);
const error = ref<string>('');
const allTables = ref<RestaurantTable[]>([]);
const loadingFloorPlan = ref<boolean>(true);

// Computed
const reservationTimeEnd = computed(() => {
  if (!reservationTimeStart.value) return '';
  const start = new Date(reservationTimeStart.value);
  const end = new Date(start.getTime() + 2 * 60 * 60 * 1000); // +2 hours
  return end.toISOString().slice(0, 16);
});

// Methods
const handleFilterChange = async (filters: {
  groupSize: number;
  reservationTimeStart: string;
  windowSeat: boolean;
  quietArea: boolean;
}) => {
  groupSize.value = filters.groupSize;
  reservationTimeStart.value = filters.reservationTimeStart;
  windowSeat.value = filters.windowSeat;
  quietArea.value = filters.quietArea;

  if (reservationTimeStart.value) {
    await fetchRecommendation();
  }
};

const fetchRecommendation = async () => {
  if (!reservationTimeStart.value) {
    error.value = 'Palun vali aeg';
    return;
  }

  loading.value = true;
  error.value = '';
  selectedTable.value = null;

  try {
    const response = await api.get<RestaurantTable>('/tables/recommend', {
      params: {
        groupSize: groupSize.value,
        prefersWindow: windowSeat.value,
        time: reservationTimeStart.value
      }
    });

    if (response.data) {
      availableTables.value = [response.data];
      selectedTable.value = response.data;
    } else {
      availableTables.value = [];
      error.value = 'Vabu laudu ei leitud';
    }
  } catch (err: any) {
    console.error('Error fetching recommendation:', err);
    error.value = err.response?.data?.message || 'Viga soovituse laadimisel';
    availableTables.value = [];
  } finally {
    loading.value = false;
  }
};

const handleTableSelect = (table: RestaurantTable) => {
  selectedTable.value = table;
};

const handleReservationConfirm = async () => {
  if (!selectedTable.value || !reservationTimeStart.value) {
    error.value = 'Palun vali laud ja aeg';
    return;
  }

  loading.value = true;
  error.value = '';

  try {
    const response = await api.post<Reservation>('/reservations', null, {
      params: {
        groupSize: groupSize.value,
        reservationTimeStart: reservationTimeStart.value,
        reservationTimeEnd: reservationTimeEnd.value
      }
    });

    if (response.data) {
      alert(`Broneering loodud! ID: ${response.data.id}`);
      // Reset state
      selectedTable.value = null;
      availableTables.value = [];
      reservationTimeStart.value = '';
      // Refresh floor plan
      await fetchAllTables();
    }
  } catch (err: any) {
    console.error('Error creating reservation:', err);
    error.value = err.response?.data?.message || 'Viga broneeringu loomisel';
  } finally {
    loading.value = false;
  }
};

const fetchAllTables = async () => {
  loadingFloorPlan.value = true;
  try {
    const response = await api.get<RestaurantTable[]>('/tables');
    allTables.value = response.data;
  } catch (err) {
    console.error('Error fetching all tables:', err);
  } finally {
    loadingFloorPlan.value = false;
  }
};

onMounted(() => {
  fetchAllTables();
});
</script>

<template>
  <div class="reservation-view">
    <div class="two-column-layout">
      <!-- Left side: Floor Plan -->
      <div class="floor-plan-section">
        <h2>Restorani saaliplaan</h2>

        <div v-if="loadingFloorPlan" class="loading">Laadin laudu...</div>

        <div v-else class="floor-canvas">
          <RestaurantTableComponent
            v-for="table in allTables"
            :key="table.id"
            :table="table"
          />
        </div>
      </div>

      <!-- Right side: Reservation Form -->
      <div class="reservation-section">
        <h2>Broneering</h2>

        <div class="reservation-container">
          <ReservationFilters
            :initialGroupSize="groupSize"
            :initialReservationTimeStart="reservationTimeStart"
            :initialWindowSeat="windowSeat"
            :initialQuietArea="quietArea"
            @filter-change="handleFilterChange"
          />

          <div v-if="error" class="error-message">
            {{ error }}
          </div>

          <div v-if="loading" class="loading">
            Laadin...
          </div>

          <AvailableTables
            v-if="!loading && availableTables.length > 0"
            :tables="availableTables"
            :selectedTable="selectedTable"
            @select-table="handleTableSelect"
          />

          <ReservationSummary
            v-if="selectedTable"
            :table="selectedTable"
            :groupSize="groupSize"
            :reservationTimeStart="reservationTimeStart"
            :reservationTimeEnd="reservationTimeEnd"
            :loading="loading"
            @confirm="handleReservationConfirm"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.reservation-view {
  max-width: 1600px;
  margin: 0 auto;
  padding: 2rem;
}

.two-column-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  align-items: start;
}

.floor-plan-section,
.reservation-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

h2 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
  font-size: 1.75rem;
}

.floor-canvas {
  position: relative;
  width: 100%;
  max-width: 800px;
  height: 600px;
  background-color: #f8f9fa;
  border: 2px solid #dee2e6;
  border-radius: 8px;
}

.reservation-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.error-message {
  padding: 1rem;
  background-color: #fee;
  border: 1px solid #fcc;
  border-radius: 4px;
  color: #c33;
}

.loading {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #666;
}

@media (max-width: 1200px) {
  .two-column-layout {
    grid-template-columns: 1fr;
  }

  .floor-canvas {
    max-width: 100%;
  }
}
</style>
