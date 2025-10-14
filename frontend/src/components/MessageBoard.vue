<script setup lang="ts">
import { onMounted, ref, computed, watch } from 'vue'
import axios from 'axios'
import MessageCard from '@/components/MessageCard.vue'
import { useGameStore } from '@/stores/gameStore'
import type * as t from '@/types'
import * as utils from '@/utils'
import * as c from '@/const'

const emit = defineEmits<{ (e: 'loaded'): void }>()
const props = defineProps<{
  sortKey: t.SortKey,
  sortDir: t.SortDir,
}>()

const gameStore = useGameStore()
const rowsRaw = ref<t.MessageBoard>([])
const error = ref<string | null>(null)
const aiMessages = ref<string[] | null>(null)
const isRefilling = ref<boolean>(false)

function probToNumber(p?: string) {
  const idx = c.PROBABILITIES.findIndex(x => x.toLowerCase() === (p || '').toLowerCase())
  return idx >= 0 ? idx + 1 : -1
}

const rowsSorted = computed<t.MessageBoard>(() => {
  const arr = [...rowsRaw.value]
  const key = props.sortKey
  const dir = props.sortDir === 'asc' ? 1 : -1

  return arr.sort((a, b) => {
    let va: number, vb: number
    if (key === 'probability') {
      va = probToNumber(a.probability)
      vb = probToNumber(b.probability)
    } else if (key === 'reward') {
      va = a.reward ?? 0
      vb = b.reward ?? 0
    } else {
      va = a.expiresIn ?? 0
      vb = b.expiresIn ?? 0
    }
    if (va === vb) return 0
    return va > vb ? dir : -dir
  })
})

async function fetchMessages() {
  if (!gameStore.game?.gameId) return
  try {
    const res = await axios.get('/api/messages', {
      params: { gameId: gameStore.game.gameId }
    })
    const resData = res.data || []
    if (aiMessages.value !== null && aiMessages.value.length > 0) {
        utils.maybeReplaceMessage(rowsRaw.value, resData, aiMessages.value)
    }
    rowsRaw.value = resData
    error.value = null
  } catch (e: any) {
    error.value = e?.message ?? 'Failed to fetch message board'
  }
}

async function refillAIGeneratedMessages(count: number) {
  if (isRefilling.value === true) return
  console.log("Fetching AI generated messages...")

  isRefilling.value = true
  const prompt = c.GEN_MSG_PROMPT.replace('{count}', count)
  const res = await axios.post('/api/query-ai', prompt, {
    headers: { 'Content-Type': 'text/plain' },
  })
  if (typeof res.data === 'string') {
    res.data = JSON.parse(res.data || [])
  }
  const base = aiMessages.value ?? []
  aiMessages.value = base.concat(res.data)
  isRefilling.value = false

  console.log("AI generated messages fetched:", aiMessages.value)
}

onMounted(() => {
  fetchMessages()
  refillAIGeneratedMessages(10)
})

watch(
  () => gameStore.game?.turn,
  (newTurn, oldTurn) => {
    if (newTurn !== oldTurn && newTurn != null) {
      fetchMessages()
      const count = aiMessages.value?.length
      if (!count || count <= 5) {
        refillAIGeneratedMessages(count)
      }
    }
  },
  { immediate: false }
)

watch(
  rowsRaw,
  (newVal, oldVal) => {
    if ((oldVal?.length ?? 0) === 0 && (newVal?.length ?? 0) > 0) {
      emit('loaded')
    }
  },
  { immediate: true }
)
</script>

<template>
  <div class="message-board">
    <div v-if="error && gameStore.game.lives !== 0" class="error">{{ error }}</div>

    <TransitionGroup name="ads" tag="div" class="ads-list">
      <div v-for="ad in rowsSorted" :key="ad.adId" class="ad-wrapper">
        <MessageCard :ad="ad" />
      </div>
    </TransitionGroup>

    <div v-if="!rowsSorted.length && !error" class="empty">
      No messages yet.
    </div>
  </div>
</template>

<style scoped>
.message-board {
  width: 100%;
  padding: 0 20px 0 20px;
}

/* Animated list */
.ads-enter-active, .ads-leave-active {
  transition: all 0.4s ease;
}
.ads-enter-from, .ads-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
.ads-move {
  transition: transform 0.4s ease;
}

.ads-list {
  display: flex;
  flex-direction: column;
  max-height: 100%;
  overflow-y: auto;
}

.ad-wrapper {
  will-change: transform, opacity;
  padding: 0 5px 7px 5px;
}

.error {
  color: #b00020;
  margin: 8px 0 12px;
}

.empty {
  text-align: center;
  padding: 24px 0;
  opacity: 0.8;
}
</style>