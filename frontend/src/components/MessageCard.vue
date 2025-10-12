<script setup lang="ts">
import axios from 'axios'
import DifficultyRating from '@/components/DifficultyRating.vue'
import { useGameStore } from '@/stores/gameStore'
import type * as t from '@/types'

const props = defineProps<{
  ad: t.Advertisement
}>()

const gameStore = useGameStore()

async function onClick() {
  try {
    const resp = await axios.post('/api/solve-message', {}, {
      params: { uuid: gameStore.game.uuid, adId: props.ad.adId }
    })
    console.log(resp.data)
    gameStore.setGame(resp.data)
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <v-card class="ad-card" variant="elevated">
    <div class="ad-row">
      <div class="ad-message">
        <div class="ad-message">{{ ad.message }}</div>
      </div>

      <div class="ad-meta">
        <v-tooltip :text="`Reward: ${ad.reward} gold`" location="left" open-delay="80">
          <template #activator="{ props: act }">
            <div class="pill" v-bind="act">
              <img class="pill-icon" src="/coin.png" alt="coin" />
              <span class="pill-value">{{ ad.reward }}</span>
            </div>
          </template>
        </v-tooltip>
        <v-tooltip :text="`Expires in: ${ad.expiresIn} turns`" location="left" open-delay="80">
          <template #activator="{ props: act }">
            <div class="pill" v-bind="act">
              <img class="pill-icon" src="/stopwatch.png" alt="stopwatch" />
              <span class="pill-value">{{ ad.expiresIn }}</span>
            </div>
          </template>
        </v-tooltip>
      </div>

      <DifficultyRating :probability="props.ad.probability" />
      <v-btn
        color="brown-lighten-4"
        size="small"
        rounded="xl"
        @click="onClick">
          Solve
      </v-btn>
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
  grid-template-columns: 1fr auto auto auto;
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