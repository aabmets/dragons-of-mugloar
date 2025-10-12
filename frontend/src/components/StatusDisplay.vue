<script setup lang="ts">
import { useGameStore } from '@/stores/gameStore'

const gameStore = useGameStore()
</script>

<template>
  <v-container style="margin-top: 22px;">
    <v-row>
      <img src="/heart.png" alt="heart" class="pill-icon" title="Lives"/>
      <span class="pill-value">{{ gameStore.game?.lives }}</span>
    </v-row>
    <v-row>
      <img src="/coin.png" alt="coins" class="pill-icon" title="Coins"/>
      <span class="pill-value">{{ gameStore.game?.gold }}</span>
    </v-row>
    <v-row>
      <img src="/experience.png" alt="level" class="pill-icon" title="Level"/>
      <span class="pill-value">{{ gameStore.game?.level }}</span>
    </v-row>
    <v-row>
      <img src="/stopwatch.png" alt="turn" class="pill-icon" title="Turn"/>
      <span class="pill-value">{{ gameStore.game?.turn }}</span>
    </v-row>
  </v-container>
</template>

<style scoped>
.pill-icon {
  width: 20px;
  height: 20px;
  display: block;
  transform: translateY(2px);
}

.pill-value {
  margin-left: 10px;
  font-size: 16px;
  font-weight: bold;
}
</style>