<script setup lang="ts">
import { computed } from 'vue'
import * as c from '@/const'

const probCount = c.PROBABILITIES.length

const props = defineProps<{
  probability?: string | null
}>()

const ratingValue = computed(() => {
  const idx = c.PROBABILITIES.findIndex(
      x => x.toLowerCase() === (props.probability || ''
  ).toLowerCase())
  return idx >= 0 ? idx + 1 : 0
})

const starColor = computed(() => {
  const val = Math.min(probCount, Math.max(1, ratingValue.value || 1))
  const t = (val - 1) / (probCount - 1)
  const hue = Math.round(120 - 120 * t)
  return `hsl(${hue} 90% 45%)`
})

const displayProbability = computed(() =>
  props.probability || c.PROBABILITIES[(ratingValue.value || 1) - 1] || ''
)
</script>

<template>
  <v-tooltip :text="displayProbability" location="center" open-delay="80">
    <template #activator="{ props: tip }">
      <div class="difficulty" v-bind="tip" :aria-label="displayProbability">
        <v-rating
          :length="probCount"
          :model-value="ratingValue"
          readonly
          size="small"
          density="compact"
          class="mr-2"
        >
          <template #item="{ isFilled, props: iconProps, index }">
            <v-icon
              v-bind="iconProps"
              :color="isFilled ? starColor : 'grey-darken-2'"
            />
          </template>
        </v-rating>
      </div>
    </template>
  </v-tooltip>
</template>

<style scoped>
.difficulty {
  display: inline-flex;
  align-items: center;
}
</style>