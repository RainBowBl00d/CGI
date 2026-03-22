<script setup lang="ts">
import type { RestaurantTable } from '@/types';

interface Props {
  tableOptions: RestaurantTable[][];
  selectedOptionIndex: number;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  'select-option': [index: number]
}>();

const selectOption = (index: number) => {
  emit('select-option', index);
};

const isSelected = (index: number): boolean => {
  return props.selectedOptionIndex === index;
};
</script>

<template>
  <div class="available-tables">
    <h2>Soovitatud lauad</h2>

    <div v-if="tableOptions.length === 0" class="no-tables">
      Vabu laudu ei leitud. Proovi muuta filtreid.
    </div>

    <div v-else class="options-list">
      <div
        v-for="(option, index) in tableOptions"
        :key="index"
        class="option-card"
        :class="{ selected: isSelected(index) }"
        @click="selectOption(index)"
      >
        <div class="option-header">
          <h3>Valik {{ index + 1 }}</h3>
          <span class="capacity-badge">
            {{ option.reduce((sum, t) => sum + t.capacity, 0) }} kohta kokku
          </span>
        </div>

        <div class="tables-in-option">
          <div v-for="table in option" :key="table.id" class="table-info">
            <div class="table-name">Laud #{{ table.id }}</div>
            <div class="table-meta">
              <span class="meta-item">{{ table.capacity }} in.</span>
              <span class="meta-item">{{ table.zone }}</span>
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
        </div>

        <div v-if="isSelected(index)" class="selected-indicator">
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

.options-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.option-card {
  border: 2px solid #e9ecef;
  border-radius: 8px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;
}

.option-card:hover {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  transform: translateY(-2px);
}

.option-card.selected {
  border-color: #28a745;
  background: #f0fff4;
  box-shadow: 0 4px 8px rgba(40, 167, 69, 0.3);
}

.option-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e9ecef;
}

.option-header h3 {
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

.tables-in-option {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.table-info {
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #007bff;
}

.table-name {
  font-weight: 600;
  font-size: 1rem;
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.table-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.meta-item {
  font-size: 0.9rem;
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
</style>
