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
const zone = ref<string>('');
const tableOptions = ref<RestaurantTable[][]>([]);
const selectedOptionIndex = ref<number>(-1);
const loading = ref<boolean>(false);
const error = ref<string>('');
const allTables = ref<RestaurantTable[]>([]);
const loadingFloorPlan = ref<boolean>(true);
const reservedTableIds = ref<Set<number>>(new Set());

// Computed property for filtered tables based on selected zone
const filteredTables = computed(() => {
  if (!zone.value) {
    return allTables.value;
  }
  return allTables.value.filter(table => table.zone === zone.value);
});

// Computed
const reservationTimeEnd = computed(() => {
  if (!reservationTimeStart.value) return '';
  // Parse datetime-local format (YYYY-MM-DDTHH:MM)
  const [datePart, timePart] = reservationTimeStart.value.split('T');
  const [year, month, day] = datePart.split('-').map(Number);
  const [hour, minute] = timePart.split(':').map(Number);

  const start = new Date(year, month - 1, day, hour, minute);
  const end = new Date(start.getTime() + 3 * 60 * 60 * 1000); // +3 hours

  // Format back to datetime-local format
  const endYear = end.getFullYear();
  const endMonth = String(end.getMonth() + 1).padStart(2, '0');
  const endDay = String(end.getDate()).padStart(2, '0');
  const endHour = String(end.getHours()).padStart(2, '0');
  const endMinute = String(end.getMinutes()).padStart(2, '0');

  return `${endYear}-${endMonth}-${endDay}T${endHour}:${endMinute}`;
});

// Methods
const handleFilterChange = async (filters: {
  groupSize: number;
  reservationTimeStart: string;
  windowSeat: boolean;
  quietArea: boolean;
  zone: string;
}) => {
  groupSize.value = filters.groupSize;
  reservationTimeStart.value = filters.reservationTimeStart;
  windowSeat.value = filters.windowSeat;
  quietArea.value = filters.quietArea;
  zone.value = filters.zone;

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
  selectedOptionIndex.value = -1;

  try {
    const response = await api.get<RestaurantTable[][]>('/tables/recommend', {
      params: {
        groupSize: groupSize.value,
        prefersWindow: windowSeat.value,
        prefersQuiet: quietArea.value,
        zone: zone.value || undefined,
        time: reservationTimeStart.value
      }
    });

    if (response.data && response.data.length > 0) {
      // Store table options (combinations)
      tableOptions.value = response.data;
      // Auto-select first option
      selectedOptionIndex.value = 0;
      // Update reserved tables display
      updateReservedTables();
    } else {
      tableOptions.value = [];
      error.value = 'Vabu laudu ei leitud';
    }
  } catch (err: any) {
    console.error('Error fetching recommendation:', err);
    error.value = err.response?.data?.message || 'Viga soovituse laadimisel';
    tableOptions.value = [];
  } finally {
    loading.value = false;
  }
};

const handleOptionSelect = (index: number) => {
  selectedOptionIndex.value = index;
};

const selectedTables = computed(() => {
  if (selectedOptionIndex.value >= 0 && selectedOptionIndex.value < tableOptions.value.length) {
    return tableOptions.value[selectedOptionIndex.value];
  }
  return [];
});

const handleReservationConfirm = async () => {
  if (selectedTables.value.length === 0 || !reservationTimeStart.value) {
    error.value = 'Palun vali laud ja aeg';
    return;
  }

  loading.value = true;
  error.value = '';

  try {
    const tableIds = selectedTables.value.map(t => t.id);
    const response = await api.post<Reservation>('/reservations', null, {
      params: {
        groupSize: groupSize.value,
        reservationTimeStart: reservationTimeStart.value,
        reservationTimeEnd: reservationTimeEnd.value,
        tableIds: tableIds
      }
    });

    if (response.data) {
      alert(`Broneering loodud! ID: ${response.data.id}`);
      // Reset state
      selectedOptionIndex.value = -1;
      tableOptions.value = [];
      reservationTimeStart.value = '';
      groupSize.value = 2;
      windowSeat.value = false;
      quietArea.value = false;
      zone.value = '';
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

    // Always update reserved tables display
    updateReservedTables();
  } catch (err) {
    console.error('Error fetching all tables:', err);
  } finally {
    loadingFloorPlan.value = false;
  }
};

const updateReservedTables = async () => {
  // Get tables that have reservations overlapping with selected time
  const reserved = new Set<number>();

  try {
    // Fetch all reservations and check for overlaps
    const response = await api.get<Reservation[]>('/reservations');

    if (!reservationTimeStart.value) {
      // If no time selected, show currently active reservations
      const now = new Date();
      response.data.forEach(reservation => {
        const resStart = new Date(reservation.reservationTimeStart);
        const resEnd = new Date(reservation.reservationTimeEnd);
        if (now >= resStart && now < resEnd) {
          reservation.tables.forEach(table => {
            reserved.add(table.id);
          });
        }
      });
    } else {
      // Show reservations overlapping with selected time
      const selectedStart = new Date(reservationTimeStart.value);
      const selectedEnd = new Date(reservationTimeEnd.value);

      response.data.forEach(reservation => {
        const resStart = new Date(reservation.reservationTimeStart);
        const resEnd = new Date(reservation.reservationTimeEnd);

        // Check if time ranges overlap
        if (selectedStart < resEnd && selectedEnd > resStart) {
          reservation.tables.forEach(table => {
            reserved.add(table.id);
          });
        }
      });
    }
  } catch (err) {
    console.error('Error fetching reservations:', err);
  }

  reservedTableIds.value = reserved;
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
        <p v-if="zone" class="zone-label">Tsoon: {{ zone === 'Main Hall' ? 'Sisesaal' : zone === 'Terrace' ? 'Terrass' : 'Privaatruum' }}</p>
        <p v-else class="zone-label">Kõik tsoonid</p>

        <div v-if="loadingFloorPlan" class="loading">Laadin laudu...</div>

        <div v-else class="floor-canvas">
          <RestaurantTableComponent
            v-for="table in filteredTables"
            :key="table.id"
            :table="table"
            :isReserved="reservedTableIds.has(table.id)"
            :isSelected="selectedTables.some(t => t.id === table.id)"
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
            :initialZone="zone"
            @filter-change="handleFilterChange"
          />

          <div v-if="error" class="error-message">
            {{ error }}
          </div>

          <div v-if="loading" class="loading">
            Laadin...
          </div>

          <AvailableTables
            v-if="!loading && tableOptions.length > 0"
            :tableOptions="tableOptions"
            :selectedOptionIndex="selectedOptionIndex"
            @select-option="handleOptionSelect"
          />

          <ReservationSummary
            v-if="selectedTables.length > 0"
            :tables="selectedTables"
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

.zone-label {
  margin: 0 0 1rem 0;
  color: #495057;
  font-size: 1rem;
  font-weight: 600;
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
