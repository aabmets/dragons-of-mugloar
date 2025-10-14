<script setup lang="ts">
import { computed } from "vue";
import { useGameStore } from "@/stores/gameStore.ts";

const gameStore = useGameStore()

const defaultText = "The message board looms over you menacingly..."
const alertText = computed(() => gameStore.game.message || defaultText)
const isDefaultText = computed(() => alertText.value === defaultText);
const alertType = computed(() => {
  if (typeof gameStore.game.success === 'boolean') {
    return gameStore.game.success ? "success" : "error"
  }
  return undefined
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
    :style="{ fontStyle: isDefaultText ? 'italic' : 'normal' }"
  />
</template>

<style scoped>
.alert {
  margin: 15px 25px;
  font-weight: 500;
}
</style>