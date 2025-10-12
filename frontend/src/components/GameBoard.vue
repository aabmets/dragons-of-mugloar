<script setup lang="ts">
import { ref } from 'vue'
import MessageBoard from '@/components/MessageBoard.vue'
import type * as t from '@/types'

const sortKey = ref<t.SortKey>('probability')
const sortDir = ref<t.SortDir>('desc')

const sortItems = [
  { title: 'Reward', value: 'reward' },
  { title: 'Turns Expiry', value: 'expiresIn' },
  { title: 'Difficulty', value: 'probability' },
] as const
</script>

<template>
  <v-sheet class="mx-auto my-6 glass-sheet" max-width="600" rounded="lg">
    <div class="board-toolbar">
      <div class="toolbar-top">
        <v-select
          v-model="sortKey"
          :items="sortItems"
          label="Sort by"
          variant="solo"
          density="comfortable"
          class="sort-control"
        />
        <v-btn
          @click="sortDir = sortDir === 'asc' ? 'desc' : 'asc'"
          variant="flat"
          class="ml-2">
            {{ sortDir === 'asc' ? 'Asc' : 'Desc' }}
        </v-btn>
      </div>
    </div>

    <MessageBoard
      class="message-board"
      :sort-dir="sortDir"
      :sort-key="sortKey"
    />
  </v-sheet>
</template>

<style scoped>
.message-board {
  width: 100%;
  height: 67vh;
  background: none;
}

.glass-sheet {
  min-width: 1000px;
  min-height: 400px;
  background-color: rgba(255, 255, 255, 0.9);
}

.board-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.toolbar-top {
  display: flex;
  align-items: center;
}

.sort-control {
  max-width: 220px;
}
</style>