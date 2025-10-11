import { defineStore } from 'pinia'

export interface Game {
  uuid: string
  timestamp: string
  username: string
  gameId: string
  lives: number
  gold: number
  level: number
  score: number
  highScore: number
  turn: number
}

export const useGameStore = defineStore('game-state', {
  state: () => ({
    game: null,
  }),
  actions: {
    setGame(payload: Game | null) {
      this.game = payload
    }
  }
})
