<script setup lang="ts">
import { mdiStar } from '@mdi/js'
</script>

<template>
  <v-app>
    <v-main class="site-bg pa-6">
      <div class="d-flex justify-center site-logo-container">
        <v-img src="/logo.png" class="site-logo" alt="Dragons of Mugloar"/>
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
</style>
