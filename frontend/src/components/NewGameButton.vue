<script setup lang="ts">
import { ref } from 'vue'
import { useMotion } from '@vueuse/motion'

interface Props {
  color?: string
  size?: string
  shrinkDurationMs?: number
}

const props = withDefaults(defineProps<Props>(), {
  color: 'primary',
  size: 'large',
  shrinkDurationMs: 400,
})

const emit = defineEmits<{
  (e: 'click'): void
}>()

const btnRef = ref<HTMLElement | null>(null)
const formRef = ref<HTMLElement | null>(null)
const showingForm = ref(false)
const playerName = ref('')

const shrinkMotionConfig = {
  shrink: {
    scale: 0,
    transition: {
      duration: props.shrinkDurationMs,
      easing: 'easeIn'
    }
  }
}

const formMotion = useMotion(formRef, {
  initial: { scale: 1 },
  ...shrinkMotionConfig,
})

const buttonMotion = useMotion(btnRef, {
  initial: { scale: 1.15 },
  enter: {
    scale: [1, 1.15],
    transition: {
      duration: 800,
      repeat: Infinity,
      repeatType: 'mirror',
      easing: 'easeInOut'
    },
  },
  ...shrinkMotionConfig,
})

async function onClick() {
  await buttonMotion.apply('shrink')
  showingForm.value = true
}

async function onSubmit() {
  await formMotion.apply('shrink')
  emit('click')
}
</script>

<template>
  <div v-show="!showingForm" ref="btnRef" class="space">
    <v-btn
      :color="color"
      :size="size"
      @click="onClick">
        <slot>New Game</slot>
    </v-btn>
  </div>
  <v-scale-transition>
    <v-sheet
      v-if="showingForm"
      ref="formRef"
      :height="44"
      :width="300"
      class="space transparent"
      rounded>
      <v-row>
        <v-text-field
          v-model="playerName"
          label="Enter Player Name (Optional)"
          variant="solo"
          clearable
          autofocus
          @keyup.enter="onSubmit"
        />
        <v-btn color="primary" :height="56" @click="onSubmit">
          PLAY
        </v-btn>
      </v-row>
    </v-sheet>
  </v-scale-transition>
</template>

<style scoped>
.space {
  margin: 20px 0 0 20px;
}
.transparent {
  background-color: rgba(0, 0, 0, 0);
}
</style>
