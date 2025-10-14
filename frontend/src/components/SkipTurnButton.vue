<script setup lang="ts">
import axios from 'axios'
import { useGameStore } from '@/stores/gameStore'

const gameStore = useGameStore()

async function onClick() {
  try {
    const resp = await axios.get('/api/reputation', {
      params: { gameId: gameStore.game.gameId }
    })
    gameStore.setReputation(resp.data[0])
    gameStore.setGame({
      ...gameStore.game,
      turn: gameStore.game.turn + 1,
      success: null,
      message: null
    })
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <v-btn variant="tonal" border @click="onClick">Skip A Turn</v-btn>
</template>

<style scoped>

</style>