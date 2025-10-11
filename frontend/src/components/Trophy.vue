<script setup lang="ts">
import { computed } from 'vue'
import { mdiTrophy } from '@mdi/js'

const props = defineProps<{
  rank: 1 | 2 | 3
  size?: number | string
  muted?: boolean
}>()

const color = computed(() => {
  switch (props.rank) {
    case 1: return '#FFD700' // gold
    case 2: return '#C0C0C0' // silver
    case 3: return '#CD7F32' // bronze
  }
})

const computedSize = computed(() => {
  const base =
    typeof props.size === 'number'
      ? props.size
      : props.size
        ? parseFloat(props.size as string)
        : 28

  const scale = props.rank === 1 ? 1 : props.rank === 2 ? 0.85 : 0.7
  return Math.max(12, Math.round(base * scale))
})

const style = computed(() => ({
  color: color.value,
  opacity: props.muted ? 0.7 : 1,
}))
</script>

<template>
  <v-icon
      :icon="mdiTrophy"
      :size="computedSize"
      :style="style"
      :aria-label="`Rank ${rank} trophy`"
  />
</template>
