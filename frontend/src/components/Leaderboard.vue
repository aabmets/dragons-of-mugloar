<script setup lang="ts">
import { onMounted, onUnmounted, ref, computed } from 'vue'
import Trophy from '@/components/Trophy.vue'
import * as fmt from '@/utils/formatters'
import axios from 'axios'

const REFRESH_MS = 3000

type GameState = {
  uuid?: string
  timestamp?: string
  username?: string
  gameId?: string
  lives?: number
  gold?: number
  level?: number
  score?: number
  highScore?: number
  turn?: number
}

const isFirstLoad = ref(true)
const isRefreshing = ref(false)
const error = ref<string | null>(null)
const rowsRaw = ref<GameState[]>([])
const lastUpdated = ref<Date | null>(null)
const dateFormatter = new Intl.DateTimeFormat(undefined, {
  hour: '2-digit', minute: '2-digit', second: '2-digit'
})

let intervalId: ReturnType<typeof setInterval> | null = null

const rows = computed(() =>
  [...rowsRaw.value]
    .sort((a, b) => (b.highScore ?? 0) - (a.highScore ?? 0))
    .map((rr) => {
      return {
        username: rr.username,
        highScore: rr.highScore,
        gold: rr.gold,
        level: rr.level,
        uuid: rr.uuid,
      }
    }),
)

async function fetchLeaderboard() {
  if (!isFirstLoad.value) {
    if (isRefreshing.value) return
    isRefreshing.value = true
  }
  try {
    const res = await axios.get(`/api/games-history`, { params: { limit: 10 } })
    rowsRaw.value = Array.isArray(res.data) ? res.data : []
    lastUpdated.value = new Date()
    error.value = null
    if (isFirstLoad.value) isFirstLoad.value = false
  } catch (e: any) {
    error.value =
      e?.response?.data?.message ||
      e?.message ||
      'Failed to load the leaderboard.'
  } finally {
    isRefreshing.value = false
  }
}

onMounted(() => {
  fetchLeaderboard()
  intervalId = setInterval(fetchLeaderboard, REFRESH_MS)
})

onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId)
    intervalId = null
  }
})
</script>

<template>
  <v-card class="mx-auto my-6" max-width="600" style="opacity: 0.9;">
    <v-card-title class="d-flex align-center justify-space-between">
      <span class="lb-title text-h5">Leaderboard</span>
      <small v-if="lastUpdated" class="text-medium-emphasis" style="font-size: 0.9rem;">
        Updated {{ dateFormatter.format(lastUpdated) }}
      </small>
    </v-card-title>

    <v-card-text class="position-relative">
      <div v-if="isFirstLoad" class="d-flex justify-center py-6">
        <v-progress-circular indeterminate />
      </div>

      <template v-else>
        <v-alert
          v-if="error"
          type="error"
          variant="tonal"
          density="comfortable"
          class="mb-4"
        >
          {{ error }}
        </v-alert>

        <div class="position-relative">
          <v-table density="comfortable">
            <thead>
              <tr>
                <th class="text-center rank-col">Rank</th>
                <th class="text-left">Player</th>
                <th class="text-right">High&nbsp;Score</th>
                <th class="text-right">Gold</th>
                <th class="text-right">Level</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(r, idx) in rows" :key="r.uuid">
                <td class="rank-col">
                  <div class="d-flex justify-center">
                    <Trophy v-if="idx < 3" :rank="(idx + 1) as 1 | 2 | 3" />
                    <span v-else>{{ idx + 1 }}</span>
                  </div>
                </td>
                <td>{{ r.username }}</td>
                <td class="text-right">{{ fmt.formatThousands(r.highScore) }}</td>
                <td class="text-right">{{ fmt.formatThousands(r.gold) }}</td>
                <td class="text-right">{{ fmt.formatThousands(r.level) }}</td>
              </tr>
            </tbody>
          </v-table>
        </div>
      </template>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.lb-title {
  font-size: 1.5rem;
  font-weight: 500;
  color: #2d2d2d;
}
.rank-col {
  width: 80px;
  padding-left: 0;
  padding-right: 0;
  text-align: center;
}
</style>