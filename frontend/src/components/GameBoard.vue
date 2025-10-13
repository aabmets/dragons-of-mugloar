<script setup lang="ts">
import { ref } from 'vue'
import StatusDisplay from '@/components/StatusDisplay.vue'
import MessageBoard from '@/components/MessageBoard.vue'
import Shop from '@/components/Shop.vue'
import { mdiArrowDown } from '@mdi/js'
import type * as t from '@/types'

const sortKey = ref<t.SortKey>('probability')
const sortDir = ref<t.SortDir>('desc')

const sortItems = [
  { title: 'Reward', value: 'reward' },
  { title: 'Expiry', value: 'expiresIn' },
  { title: 'Difficulty', value: 'probability' },
] as const
</script>

<template>
  <v-sheet class="mx-auto my-6 glass-sheet" max-width="600" rounded="lg">
    <div class="toolbar-top">
      <div class="toolbar-left">
        <img src="/avatar.png" alt="avatar" class="avatar" />
        <StatusDisplay />
      </div>

      <div class="toolbar-center">
        <Shop />
      </div>

      <div class="toolbar-right">
        <div class="sort-stack">
          <v-select
            v-model="sortKey"
            :items="sortItems"
            label="Sort by"
            variant="solo"
            density="comfortable"
            class="sort-control"
            hide-details
          />
          <v-btn
            size="small"
            variant="tonal"
            class="sort-control"
            @click="sortDir = sortDir === 'asc' ? 'desc' : 'asc'">
              <v-icon :icon="mdiArrowDown" class="mr-2" />
              {{ sortDir === 'asc' ? 'Increasing' : 'Decreasing' }}
          </v-btn>
        </div>
      </div>
    </div>

    <MessageBoard class="message-board" :sort-dir="sortDir" :sort-key="sortKey" />
  </v-sheet>
</template>

<style scoped>
.toolbar-top {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
  justify-self: start;
}

.toolbar-center {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 18px;
}

.toolbar-right {
  justify-self: end;
}

.avatar {
  margin: 20px 0 0 20px ;
  padding: 3px;
  border: 2px solid #ccc;
  border-radius: 4px;
  width: 100px;
  height: 100px;
  display: block;
  background-image: linear-gradient(135deg, #52C2F4, #7444C7);
}

.message-board {
  width: 100%;
  height: 65.5vh;
  padding-top: 5px;
  background: none;
}

.glass-sheet {
  min-width: 1000px;
  min-height: 400px;
  background-color: rgba(255, 255, 255, 0.9);
}

.toolbar-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.sort-stack {
  display: flex;
  padding-right: 20px;
  margin-top: 18px;
  flex-direction: column;
  align-items: flex-end;
  gap: 10px;
}

.sort-control {
  width: 130px;
}
</style>