<script setup lang="ts">
import axios from 'axios'
import { computed, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useGameStore } from '@/stores/gameStore'

const MAX = 10

const gameStore = useGameStore()
const { reputation } = storeToRefs(gameStore)

const items = computed(() => [
  { key: 'people',     label: 'People',     value: reputation.value.people },
  { key: 'state',      label: 'State',      value: reputation.value.state },
  { key: 'underworld', label: 'Underworld', value: reputation.value.underworld }
])

function clamp(n: number) {
  return Math.max(-MAX, Math.min(MAX, Number(n) || 0))
}
function posPct(v: number) {
  return `${(Math.max(0, clamp(v)) / MAX) * 100}%`
}
function negPct(v: number) {
  return `${(Math.max(0, -clamp(v)) / MAX) * 100}%`
}
function valueLabel(v: number) {
  return v.toFixed(0)
}

async function fetchReputation() {
  try {
    const resp = await axios.get('/api/reputation', {
      params: { gameId: gameStore.game.gameId }
    })
    gameStore.setReputation(resp.data[0])
  } catch (e) {
    console.log(e)
  }
}

watch(
  () => gameStore.game?.turn,
  (newTurn, oldTurn) => {
    if (newTurn !== oldTurn && newTurn != null) {
      fetchReputation()
    }
  },
  { immediate: false }
)
</script>

<template>
  <div class="rep-bars" role="group" aria-label="Reputations">
    <div v-for="bar in items" :key="bar.key" class="rep-row">
      <div class="rep-bar" :title="`${bar.label}: ${valueLabel(bar.value)}`">

          <div class="rep-track rep-track-left">
            <div class="rep-track-content rep-track-content-left">
              <div class="rep-fill rep-fill-neg" :style="{ width: negPct(bar.value) }" />
            </div>
          </div>

          <div class="rep-badge">{{ valueLabel(bar.value) }}</div>

          <div class="rep-track rep-track-right">
            <div class="rep-track-content rep-track-content-right">
              <div class="rep-fill rep-fill-pos" :style="{ width: posPct(bar.value) }" />
            </div>
          </div>

      </div>
    </div>
  </div>
</template>

<style scoped>
.rep-bars {
  --track-height: 14px;
  --border: 2px;
  --badge: 26px;
  --rep-border: #bdbdbd;
  --rep-track-bg: white;
  --rep-red: #ef4444;
  --rep-green: #22c55e;

  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 160px;
  margin-top: 18px;
}

.rep-row {
  display: block;
}

.rep-bar {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 0;
  position: relative;
}

.rep-track {
  position: relative;
  height: var(--track-height);
  background: var(--rep-track-bg);
  border: var(--border) solid var(--rep-border);
  border-radius: 9999px;
  overflow: hidden;
  z-index: 1;
}

.rep-track-left  { margin-right: calc(var(--badge) / -2); }
.rep-track-right { margin-left:  calc(var(--badge) / -2); }

.rep-track-content {
  position: relative;
  height: 100%;
}

.rep-track-content-left  { padding-right: calc(var(--badge) / 2); }
.rep-track-content-right { padding-left:  calc(var(--badge) / 2); }

.rep-fill {
  position: absolute;
  top: 0;
  bottom: 0;
}

.rep-fill-neg {
  right: 0;
  width: 0;
  background: var(--rep-red);
}

.rep-fill-pos {
  left: 0;
  width: 0;
  background: var(--rep-green);
}

.rep-badge {
  width: var(--badge);
  height: var(--badge);
  min-width: var(--badge);
  min-height: var(--badge);
  border-radius: 50%;
  background: var(--rep-track-bg);
  border: var(--border) solid var(--rep-border);
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
  font-weight: 700;
  font-size: 0.8rem;
  color: #111827;
  user-select: none;
  z-index: 2;
}
</style>
