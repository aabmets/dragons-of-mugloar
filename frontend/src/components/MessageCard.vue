<script setup lang="ts">
import axios from 'axios'
import DifficultyRating from '@/components/DifficultyRating.vue'
import { useGameStore } from '@/stores/gameStore'
import type * as t from '@/types'
import * as utils from '@/utils'

const props = defineProps<{
  ad: t.Advertisement
}>()

const gameStore = useGameStore()
const isTrap = utils.isTrapAdvert(props.ad)

async function onClick() {
  try {
    const resp = await axios.post('/api/solve-message', {}, {
      params: { uuid: gameStore.game.uuid, adId: props.ad.adId }
    })
    gameStore.setGame(resp.data)
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <v-card class="ad-card" variant="elevated" border>
    <div class="ad-row">
      <div class="ad-message">
        <div class="ad-message">{{ ad.message }}</div>
      </div>

      <div class="ad-meta">
        <img
          v-if="ad.hasAiMessage"
          title="AI generated message"
          style="width: 25px; height: 25px;"
          src="/ai.png"
          alt="ai"
        />

        <div v-if="isTrap">
          <v-tooltip text="Ambush alert!" location="left" open-delay="300">
            <template #activator="{ props: act }">
              <div class="pill pill-small trap" v-bind="act">
                <span class="pill-value">TRAP</span>
              </div>
            </template>
          </v-tooltip>
        </div>

        <div v-if="ad.decodedWith">
          <v-tooltip :text="`Decoded with ${ad.decodedWith.toUpperCase()} algorithm`" location="left" open-delay="300">
            <template #activator="{ props: act }">
              <div class="pill pill-small" v-bind="act">
                <span class="pill-value">{{ ad.decodedWith.toUpperCase() }}</span>
              </div>
            </template>
          </v-tooltip>
        </div>

        <v-tooltip :text="`Reward: ${ad.reward} gold`" location="left" open-delay="300">
          <template #activator="{ props: act }">
            <div class="pill" v-bind="act">
              <img class="pill-icon" src="/coin.png" alt="coin" />
              <span class="pill-value">{{ ad.reward }}</span>
            </div>
          </template>
        </v-tooltip>

        <v-tooltip :text="`Expires in: ${ad.expiresIn} turns`" location="left" open-delay="300">
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
        :disabled="isTrap"
        @click="onClick">
          Solve
      </v-btn>
    </div>
  </v-card>
</template>

<style scoped>
.trap {
  background-color: #ff0000 !important;
  color: #ffffff !important;
}

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
  background: rgba(0, 0, 0, 0.1);
}
.pill-small {
  padding: 2px 8px;
  margin-bottom: 2px;
}
.pill-value {
  font-weight: 600;
}
.pill-small .pill-value {
  font-size: 12px;
}
.pill-icon {
  width: 20px;
  height: 20px;
}
</style>