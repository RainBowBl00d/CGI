<script setup lang="ts">
import type { RestaurantTable } from '@/types';

interface Props {
  tables: RestaurantTable[];
  selectedTable: RestaurantTable | null;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  'select-table': [table: RestaurantTable]
}>();

const selectTable = (table: RestaurantTable) => {
  emit('select-table', table);
};

const isSelected = (table: RestaurantTable): boolean => {
  return props.selectedTable?.id === table.id;
};
</script>

<template>
  <div class="available-tables">
    <h2>Saadaolevad lauad</h2>

    <div v-if="tables.length === 0" class="no-tables">
      Vabu laudu ei leitud. Proovi muuta filtreid.
    </div>

    <div v-else class="tables-grid">
      <div
        v-for="table in tables"
        :key="table.id"
        class="table-card"
        :class="{ selected: isSelected(table) }"
        @click="selectTable(table)"
      >
        <div class="table-header">
          <h3>Laud #{{ table.id }}</h3>
          <span class="capacity-badge">{{ table.capacity }} kohta</span>
        </div>

        <div class="table-details">
          <div class="detail-item">
            <span class="detail-label">Tsoon:</span>
            <span class="detail-value">{{ table.zone || 'N/A' }}</span>
          </div>

          <div class="detail-item">
            <span class="detail-label">Asukoht:</span>
            <span class="detail-value">X: {{ table.x }}, Y: {{ table.y }}</span>
          </div>

          <div class="features">
            <span v-if="table.windowSeat" class="feature-badge window">
              🪟 Aknakoht
            </span>
            <span v-if="table.quietArea" class="feature-badge quiet">
              🤫 Vaikne ala
            </span>
          </div>
        </div>

        <div v-if="isSelected(table)" class="selected-indicator">
          ✓ Valitud
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.available-tables {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  margin: 0 0 1.5rem 0;
  font-size: 1.5rem;
  color: #2c3e50;
}

.no-tables {
  padding: 2rem;
  text-align: center;
  color: #6c757d;
  background: #f8f9fa;
  border-radius: 4px;
}

.tables-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.table-card {
  border: 2px solid #e9ecef;
  border-radius: 8px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;
}

.table-card:hover {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  transform: translateY(-2px);
}

.table-card.selected {
  border-color: #28a745;
  background: #f0fff4;
  box-shadow: 0 4px 8px rgba(40, 167, 69, 0.3);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e9ecef;
}

.table-header h3 {
  margin: 0;
  font-size: 1.25rem;
  color: #2c3e50;
}

.capacity-badge {
  background: #007bff;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.85rem;
  font-weight: 600;
}

.table-details {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  font-size: 0.95rem;
}

.detail-label {
  font-weight: 600;
  color: #495057;
}

.detail-value {
  color: #6c757d;
}

.features {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.feature-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
}

.feature-badge.window {
  background: #e7f3ff;
  color: #0066cc;
}

.feature-badge.quiet {
  background: #f0e7ff;
  color: #6600cc;
}

.selected-indicator {
  margin-top: 1rem;
  padding: 0.5rem;
  background: #28a745;
  color: white;
  text-align: center;
  border-radius: 4px;
  font-weight: 600;
}

@media (max-width: 768px) {
  .tables-grid {
    grid-template-columns: 1fr;
  }
}
</style>
