<script setup lang="ts">
import { ref } from 'vue'
import { useMotion } from '@vueuse/motion'
import { useGameStore } from '@/stores/gameStore'
import type * as t from '@/types'
import axios from "axios";

const gameStore = useGameStore()

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
  (e: 'new-game-started', payload?: t.GameState): void
}>()

const btnRef = ref<HTMLElement | null>(null)
const showingForm = ref(false)
const isLoading = ref(false)
const playerName = ref('')

const motion = useMotion(btnRef, {
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
  shrink: {
    scale: 0,
    transition: {
      duration: props.shrinkDurationMs,
      easing: 'easeIn'
    }
  }
})

async function onClick() {
  await motion.apply('shrink')
  showingForm.value = true
}

async function onSubmit() {
  if (isLoading.value) return
  isLoading.value = true
  try {
    const resp = await axios.post('/api/new-game', {}, {
      params: { username: playerName.value }
    })
    gameStore.setGame(resp.data)
    emit('new-game-started', resp.data)
  } catch (e) {
    isLoading.value = false
    console.log(e)
  }
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
          @keyup.enter="onSubmit"
        />
        <v-btn
          color="primary"
          width="50px"
          :height="56"
          :disabled="isLoading"
          @click="onSubmit">
            <span v-if="!isLoading">PLAY</span>
            <v-progress-circular v-else indeterminate />
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
