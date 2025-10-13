<script setup lang="ts">
import axios from 'axios'
import {useGameStore} from '@/stores/gameStore'
import * as c from '@/const'

const gameStore = useGameStore()

const props = defineProps<{
  itemId: string
}>()

const item = c.PRODUCTS[props.itemId]

async function onClick() {
  try {
    const resp = await axios.post('/api/buy-item', {}, {
      params: { uuid: gameStore.game.uuid, itemId: props.itemId }
    })
    const result = resp.data.message.split(' ')[0]
    resp.data.message = c.PURCHASE[result].replace('{item}', item.name)
    gameStore.setGame(resp.data)
  } catch (e) {
    console.log(e)
  }
}
</script>

<template>
  <v-tooltip :text="`${item.name}: ${item.price} gold`" location="top" open-delay="100">
    <template #activator="{ props: act }">
      <v-card v-bind="act" class="product-card" variant="elevated" border @click="onClick">
        <img :src="item.image" :alt="item.name" class="product-image" />
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