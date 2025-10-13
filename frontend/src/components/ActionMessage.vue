<script setup lang="ts">
import { computed } from "vue";
import { useGameStore } from "@/stores/gameStore.ts";

const gameStore = useGameStore()

const alertText = computed(() => gameStore.game.message || "No actions taken yet.")
const alertType = computed(() => {
  if (typeof gameStore.game.success === 'boolean') {
    return gameStore.game.success ? "success" : "error"
  }
  return ''
})
</script>

<template>
  <v-alert
    class="alert"
    :text="alertText"
    :type="alertType"
    density="comfortable"
    variant="tonal"
    elevation="4"
  />
</template>

<style scoped>
.alert {
  margin: 15px 25px;
  font-weight: 500;
}
</style>