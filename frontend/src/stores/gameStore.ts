import { defineStore } from 'pinia'
import type * as t from '@/types'

export const useGameStore = defineStore('game-state', {
  state: () => ({
    game: null,
  }),
  actions: {
    setGame(payload: t.GameState | null) {
      this.game = payload
    }
  }
})
