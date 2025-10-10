<script setup lang="ts">
import { mdiStar } from '@mdi/js'
</script>

<template>
  <v-app>
    <v-main class="pa-6">
      <v-btn color="primary" :prepend-icon="mdiStar">Hello Vuetify</v-btn>
    </v-main>
  </v-app>
</template>

<style scoped></style>
