import { defineStore } from 'pinia'
import type * as t from '@/types'

export const useGameStore = defineStore('game-state', {
  state: () => ({
    game: null,
    reputation: {
      people: 0,
      state: 0,
      underworld: 0,
    }
  }),
  actions: {
    setGame(payload: t.GameState | null) {
      this.game = payload
    },
    setReputation(payload: t.Reputation | null) {
      this.reputation = payload
    },
    bumpTurn() {
        this.game.turn += 1
    },
  }
})
