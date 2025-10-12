<script setup lang="ts">
import DifficultyRating from '@/components/DifficultyRating.vue'
import type * as t from '@/types'

const props = defineProps<{
  ad: t.Advertisement
}>()
</script>

<template>
  <v-card class="ad-card" variant="elevated">
    <div class="ad-row">
      <div class="ad-message">
        <div class="ad-message">{{ ad.message }}</div>
      </div>

      <div class="ad-meta">
        <div class="pill">
          <span class="pill-value">{{ ad.reward }}</span>
          <img src="/coin.png" alt="coin" class="pill-icon" />
        </div>
        <div class="pill">
          <span class="pill-value">{{ ad.expiresIn }}</span>
          <img src="/stopwatch.png" alt="stopwatch" class="pill-icon" />
        </div>
      </div>

      <DifficultyRating :probability="props.ad.probability" />
    </div>
  </v-card>
</template>

<style scoped>
.ad-card {
  border-radius: 16px;
  padding: 8px 12px;
}

.ad-row {
  display: grid;
  grid-template-columns: 1fr auto auto;
  align-items: center;
  gap: 12px;
}

.ad-message {
  min-width: 0;
}

.ad-message {
  font-weight: 500;
  line-height: 1.2;
  overflow: hidden;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  white-space: normal;
  word-break: auto-phrase;
}

.ad-meta {
  display: flex;
  gap: 8px;
  align-items: center;
}

.pill {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(0, 0, 0, 0.06);
}
.pill-value { font-weight: 600; }
.pill-icon {
  width: 20px;
  height: 20px;
  display: block;
}
</style>