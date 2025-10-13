<script setup lang="ts">
import axios from 'axios'
import { useGameStore } from '@/stores/gameStore'

const gameStore = useGameStore()

const props = defineProps<{
  name: string
  itemId: string
  imagePath: string
  price: number
}>()

async function onClick() {
  try {
    const resp = await axios.post('/api/buy-item', {}, {
      params: { uuid: gameStore.game.uuid, itemId: props.itemId }
    })
    gameStore.setGame(resp.data)
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <v-tooltip :text="`${props.name}: ${props.price} gold`" location="top" open-delay="100">
    <template #activator="{ props: act }">
      <v-card v-bind="act" class="product-card" variant="elevated" border @click="onClick">
        <img :src="props.imagePath" :alt="props.name" class="product-image" />
      </v-card>
    </template>
  </v-tooltip>
</template>

<style scoped>
.product-card {
  transition: transform 0.1s linear;
  background-color: rgba(240, 240, 240, 1);
  border-radius: 16px;
  width: 60px;
  height: 60px;
  margin: 5px;
}
.product-card:hover {
  cursor: pointer;
  transition: transform 0.1s linear;
  transform: scale(1.1);
}
.product-image {
  width: 100%;
  height: 100%;
  padding: 10px;
}
</style>