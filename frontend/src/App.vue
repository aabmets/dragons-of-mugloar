<script setup lang="ts">
import { ref } from "vue";
import { useMotion } from '@vueuse/motion'
import GameBoard from '@/components/GameBoard.vue'
import LeaderBoard from '@/components/LeaderBoard.vue'
import NewGameButton from '@/components/NewGameButton.vue'
import FloatingDock from '@/components/FloatingDock.vue'
import { useGameStore } from '@/stores/gameStore'
import * as utils from '@/utils'

const playingGame = ref(false)
const ldbRef = ref<HTMLElement | null>(null)
const gameStore = useGameStore()

const motion = useMotion(ldbRef, {
  initial: { y: 0, opacity: 1 },
  shrink: {
    y: 100,
    opacity: 0,
    transition: {
      duration: 600,
      easing: 'easeIn'
    }
  }
})

async function hideLeaderboard() {
  await motion.apply('shrink')
  await utils.sleep(200)
  playingGame.value = true
}

function resetGame() {
  playingGame.value = false
  gameStore.reset()
}
</script>

<template>
  <v-app>
    <v-main class="site-bg pa-6">
      <div
        class="d-flex justify-center"
        :class="['site-logo-container', { 'site-logo-container--small': playingGame }]">
          <v-img
            src="/logo.png"
            :class="['site-logo', { 'site-logo--small': playingGame }]"
            alt="Dragons of Mugloar"
            @click="resetGame"
          />
      </div>
      <div v-if="!playingGame" class="cta-group" ref="ldbRef">
        <NewGameButton :shrinkDurationMs=400 @new-game-started="hideLeaderboard" />
        <LeaderBoard />
      </div>
      <v-fade-transition>
        <GameBoard v-if="playingGame"/>
      </v-fade-transition>
    </v-main>
  </v-app>
  <FloatingDock />
</template>

<style scoped>
html, body { height: 100%; }
.site-bg {
  min-height: 100vh;
  background: url('/bg.jpg') center / cover no-repeat fixed;
}
.site-logo-container {
  transition: margin-top 0.5s ease, transform 0.5s ease;
  margin-top: 60px;
  padding: 0 10%;
}
.site-logo-container--small {
  margin-top: 10px;
}
.site-logo {
  transition: max-height 0.5s ease, transform 0.5s ease;
  height: auto;
  max-height: 195px;
  max-width: 1200px;
  aspect-ratio: 1200 / 195;
}
.site-logo--small {
  max-height: 100px;
  cursor: pointer;
}
.cta-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
  width: 100%;
}
</style>
