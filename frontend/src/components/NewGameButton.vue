<script setup lang="ts">
import { ref } from 'vue'
import { useMotion } from '@vueuse/motion'

interface Props {
  color?: string
  size?: string
  disabled?: boolean
  shrinkDurationMs?: number
}

const props = withDefaults(defineProps<Props>(), {
  color: 'primary',
  size: 'large',
  disabled: false,
  shrinkDurationMs: 400,
})

const emit = defineEmits<{
  (e: 'click'): void
}>()

const btnRef = ref<HTMLElement | null>(null)

const motion = useMotion(btnRef, {
  initial: {
    scale: 1.15,
  },
  enter: {
    scale: [1, 1.15],
    transition: {
      duration: 800,
      repeat: Infinity,
      repeatType: 'mirror',
      easing: 'easeInOut'
    },
  },
  shrink: {
    scale: 0,
    transition: {
      duration: props.shrinkDurationMs,
      easing: 'easeIn'
    },
  },
})

const onClick = () => {
  motion.apply('shrink')
  emit('click')
}
</script>

<template>
  <div ref="btnRef">
    <v-btn
      :color="color"
      :size="size"
      :disabled="disabled"
      @click="onClick">
        <slot>New Game</slot>
    </v-btn>
  </div>
</template>

<style scoped>
</style>
