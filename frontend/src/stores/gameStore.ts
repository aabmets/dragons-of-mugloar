import { defineStore } from 'pinia'
import type { GameState } from '@/types'

export const useGameStore = defineStore('game-state', {
  state: () => ({
    game: null,
  }),
  actions: {
    setGame(payload: GameState | null) {
      this.game = payload
    }
  }
})
