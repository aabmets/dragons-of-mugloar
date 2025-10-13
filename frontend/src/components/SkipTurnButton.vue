<script setup lang="ts">
import axios from 'axios'
import { useGameStore } from '@/stores/gameStore'

const gameStore = useGameStore()

async function onClick() {
  try {
    await axios.post('/api/skip-turn', {}, {
      params: { gameId: gameStore.game.gameId }
    })
    gameStore.bumpTurn()
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