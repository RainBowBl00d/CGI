<script setup lang="ts">
import type { RestaurantTable } from '@/types';

interface Props {
  tables: RestaurantTable[];
  groupSize: number;
  reservationTimeStart: string;
  reservationTimeEnd: string;
  loading?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
});

const emit = defineEmits<{
  'confirm': []
}>();

const formatDateTime = (dateTimeString: string): string => {
  if (!dateTimeString) return '';
  const date = new Date(dateTimeString);
  return date.toLocaleString('et-EE', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const handleConfirm = () => {
  emit('confirm');
};
</script>

<template>
  <div class="reservation-summary">
    <h2>Broneeringu kokkuvõte</h2>

    <div class="summary-content">
      <div class="summary-section">
        <h3>Valitud laud(ud)</h3>
        <div v-for="table in tables" :key="table.id" class="table-summary">
          <div class="summary-details">
            <div class="detail-row">
              <span class="label">Laua number:</span>
              <span class="value">#{{ table.id }}</span>
            </div>
            <div class="detail-row">
              <span class="label">Mahutavus:</span>
              <span class="value">{{ table.capacity }} inimest</span>
            </div>
            <div class="detail-row">
              <span class="label">Tsoon:</span>
              <span class="value">{{ table.zone || 'N/A' }}</span>
            </div>

            <div v-if="table.windowSeat || table.quietArea" class="features-summary">
              <span class="label">Omadused:</span>
              <div class="features-list">
                <span v-if="table.windowSeat" class="feature">🪟 Aknakoht</span>
                <span v-if="table.quietArea" class="feature">🤫 Vaikne ala</span>
              </div>
            </div>
          </div>
        </div>
        <div v-if="tables.length > 1" class="total-capacity">
          <span class="label">Kokku mahutavus:</span>
          <span class="value">{{ tables.reduce((sum, t) => sum + t.capacity, 0) }} inimest</span>
        </div>
      </div>

      <div class="summary-section">
        <h3>Broneeringu detailid</h3>
        <div class="summary-details">
          <div class="detail-row">
            <span class="label">Inimeste arv:</span>
            <span class="value">{{ groupSize }}</span>
          </div>
          <div class="detail-row">
            <span class="label">Algusaeg:</span>
            <span class="value">{{ formatDateTime(reservationTimeStart) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">Lõppaeg:</span>
            <span class="value">{{ formatDateTime(reservationTimeEnd) }}</span>
          </div>
        </div>
      </div>

      <button
        class="confirm-button"
        :disabled="loading"
        @click="handleConfirm"
      >
        <span v-if="loading">Salvestan...</span>
        <span v-else>Kinnita broneering</span>
      </button>
    </div>
  </div>
</template>

<style scoped>
.reservation-summary {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 2px solid #28a745;
}

h2 {
  margin: 0 0 1.5rem 0;
  font-size: 1.5rem;
  color: #2c3e50;
}

.summary-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.summary-section {
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.summary-section h3 {
  margin: 0 0 1rem 0;
  font-size: 1.1rem;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
  padding-bottom: 0.5rem;
}

.table-summary {
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: white;
  border-radius: 4px;
  border-left: 3px solid #28a745;
}

.table-summary:last-of-type {
  margin-bottom: 0.5rem;
}

.summary-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.total-capacity {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  background: white;
  border-radius: 4px;
  font-weight: bold;
  margin-top: 0.5rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
}

.label {
  font-weight: 600;
  color: #495057;
  font-size: 0.95rem;
}

.value {
  color: #2c3e50;
  font-size: 0.95rem;
}

.features-summary {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  padding-top: 0.5rem;
}

.features-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.feature {
  padding: 0.25rem 0.75rem;
  background: #e7f3ff;
  color: #0066cc;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
}

.confirm-button {
  width: 100%;
  padding: 1rem;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.confirm-button:hover:not(:disabled) {
  background: #218838;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(40, 167, 69, 0.3);
}

.confirm-button:disabled {
  background: #6c757d;
  cursor: not-allowed;
  opacity: 0.6;
}

.confirm-button:active:not(:disabled) {
  transform: translateY(0);
}

@media (max-width: 768px) {
  .detail-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.25rem;
  }
}
</style>
