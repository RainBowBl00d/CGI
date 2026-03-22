<script setup lang="ts">
import { ref, watch, computed } from 'vue';

interface Props {
  initialGroupSize?: number;
  initialReservationTimeStart?: string;
  initialWindowSeat?: boolean;
  initialQuietArea?: boolean;
  initialZone?: string;
}

const props = withDefaults(defineProps<Props>(), {
  initialGroupSize: 2,
  initialReservationTimeStart: '',
  initialWindowSeat: false,
  initialQuietArea: false,
  initialZone: ''
});

const emit = defineEmits<{
  'filter-change': [{
    groupSize: number;
    reservationTimeStart: string;
    windowSeat: boolean;
    quietArea: boolean;
    zone: string;
  }]
}>();

// Local state
const groupSize = ref<number>(props.initialGroupSize);
const reservationTimeStart = ref<string>(props.initialReservationTimeStart);
const windowSeat = ref<boolean>(props.initialWindowSeat);
const quietArea = ref<boolean>(props.initialQuietArea);
const zone = ref<string>(props.initialZone);

// Separate date and time for better UX
const selectedDate = ref<string>('');
const selectedHour = ref<string>('');

// Initialize separate date/time from initial value
if (props.initialReservationTimeStart) {
  const [datePart, timePart] = props.initialReservationTimeStart.split('T');
  selectedDate.value = datePart;
  selectedHour.value = timePart?.split(':')[0] || '';
}

// Available hours (10:00 - 22:00)
const availableHours = [
  '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22'
];

// Min date (today)
const minDate = computed(() => {
  const today = new Date();
  return today.toISOString().split('T')[0];
});

// Max date (3 months ahead)
const maxDate = computed(() => {
  const future = new Date();
  future.setMonth(future.getMonth() + 3);
  return future.toISOString().split('T')[0];
});

// Combine date and hour into datetime-local format
const updateReservationTime = () => {
  if (selectedDate.value && selectedHour.value) {
    reservationTimeStart.value = `${selectedDate.value}T${selectedHour.value}:00`;
  } else {
    reservationTimeStart.value = '';
  }
};

// Emit changes when any filter changes
const emitFilters = () => {
  emit('filter-change', {
    groupSize: groupSize.value,
    reservationTimeStart: reservationTimeStart.value,
    windowSeat: windowSeat.value,
    quietArea: quietArea.value,
    zone: zone.value
  });
};

// Watch for date/hour changes and update combined datetime
watch([selectedDate, selectedHour], () => {
  updateReservationTime();
});

// Watch for changes and emit
watch([groupSize, reservationTimeStart, windowSeat, quietArea, zone], () => {
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
        <label for="reservationDate">Kuupäev</label>
        <input
          id="reservationDate"
          v-model="selectedDate"
          type="date"
          class="filter-input"
          :min="minDate"
          :max="maxDate"
        />
      </div>

      <div class="filter-group">
        <label for="reservationHour">Kellaaeg</label>
        <select
          id="reservationHour"
          v-model="selectedHour"
          class="filter-input"
        >
          <option value="">Vali kellaaeg</option>
          <option v-for="hour in availableHours" :key="hour" :value="hour">
            {{ hour }}:00
          </option>
        </select>
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

      <div class="filter-group">
        <label for="zone">Tsoon</label>
        <select
          id="zone"
          v-model="zone"
          class="filter-input"
        >
          <option value="Main Hall">Sisesaal</option>
          <option value="Terrace">Terrass</option>
          <option value="Private Room">Privaatruum</option>
        </select>
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

select.filter-input {
  cursor: pointer;
  background-color: white;
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

.input-hint {
  font-size: 0.85rem;
  color: #6c757d;
  font-style: italic;
}

@media (max-width: 768px) {
  .filters-grid {
    grid-template-columns: 1fr;
  }
}
</style>
