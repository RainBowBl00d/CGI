<script setup lang="ts">
import { ref, watch } from 'vue';

interface Props {
  initialGroupSize?: number;
  initialReservationTimeStart?: string;
  initialWindowSeat?: boolean;
  initialQuietArea?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  initialGroupSize: 2,
  initialReservationTimeStart: '',
  initialWindowSeat: false,
  initialQuietArea: false
});

const emit = defineEmits<{
  'filter-change': [{
    groupSize: number;
    reservationTimeStart: string;
    windowSeat: boolean;
    quietArea: boolean;
  }]
}>();

// Local state
const groupSize = ref<number>(props.initialGroupSize);
const reservationTimeStart = ref<string>(props.initialReservationTimeStart);
const windowSeat = ref<boolean>(props.initialWindowSeat);
const quietArea = ref<boolean>(props.initialQuietArea);

// Emit changes when any filter changes
const emitFilters = () => {
  emit('filter-change', {
    groupSize: groupSize.value,
    reservationTimeStart: reservationTimeStart.value,
    windowSeat: windowSeat.value,
    quietArea: quietArea.value
  });
};

// Watch for changes and emit
watch([groupSize, reservationTimeStart, windowSeat, quietArea], () => {
  emitFilters();
});
</script>

<template>
  <div class="reservation-filters">
    <h2>Broneeringu andmed</h2>

    <div class="filters-grid">
      <div class="filter-group">
        <label for="groupSize">Inimeste arv</label>
        <input
          id="groupSize"
          v-model.number="groupSize"
          type="number"
          min="1"
          max="20"
          class="filter-input"
        />
      </div>

      <div class="filter-group">
        <label for="reservationTime">Broneeringu algusaeg</label>
        <input
          id="reservationTime"
          v-model="reservationTimeStart"
          type="datetime-local"
          class="filter-input"
        />
      </div>

      <div class="filter-group checkbox-group">
        <label>
          <input
            v-model="windowSeat"
            type="checkbox"
            class="filter-checkbox"
          />
          <span>Eelistab aknakoht</span>
        </label>
      </div>

      <div class="filter-group checkbox-group">
        <label>
          <input
            v-model="quietArea"
            type="checkbox"
            class="filter-checkbox"
          />
          <span>Eelistab vaikset ala</span>
        </label>
      </div>
    </div>
  </div>
</template>

<style scoped>
.reservation-filters {
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  margin: 0 0 1.5rem 0;
  font-size: 1.5rem;
  color: #2c3e50;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-weight: 600;
  color: #495057;
  font-size: 0.95rem;
}

.filter-input {
  padding: 0.75rem;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.filter-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.checkbox-group {
  flex-direction: row;
  align-items: center;
}

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
  font-weight: normal;
}

.filter-checkbox {
  width: 1.2rem;
  height: 1.2rem;
  cursor: pointer;
}

@media (max-width: 768px) {
  .filters-grid {
    grid-template-columns: 1fr;
  }
}
</style>
