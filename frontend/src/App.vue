<script setup lang="ts">
import { ref } from "vue";
import { useMotion } from '@vueuse/motion'
import Leaderboard from '@/components/Leaderboard.vue'
import NewGameButton from '@/components/NewGameButton.vue'
import * as utils from '@/utils'

const playingGame = ref(false)
const ldbRef = ref<HTMLElement | null>(null)

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
  await utils.sleep(300)
  playingGame.value = true
}
</script>

<template>
  <v-app>
    <v-main class="site-bg pa-6">
      <div class="d-flex justify-center site-logo-container">
        <v-img src="/logo.png" class="site-logo" alt="Dragons of Mugloar"/>
      </div>
      <div v-if="!playingGame" class="cta-group" ref="ldbRef">
        <NewGameButton :shrinkDurationMs=400 @new-game-started="hideLeaderboard" />
        <Leaderboard />
      </div>
      <div v-else>
        GAME PLACEHOLDER
      </div>
    </v-main>
  </v-app>
</template>

<style scoped>
html, body { height: 100%; }
.site-bg {
  min-height: 100vh;
  background: url('/bg.jpg') center / cover no-repeat fixed;
}
.site-logo-container {
  margin-top: 60px;
  padding: 0 10%;
}
.site-logo {
  height: auto;
  max-height: 195px;
  max-width: 1200px;
  aspect-ratio: 1200 / 195;
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
