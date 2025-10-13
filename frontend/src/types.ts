export interface GameState {
  uuid?: string
  timestamp?: string
  username?: string
  gameId?: string
  lives?: number
  gold?: number
  level?: number
  score?: number
  highScore?: number
  turn?: number
  success?: boolean
  message?: string
}

export interface Advertisement {
  adId?: string
  message?: string
  reward?: number
  expiresIn?: number
  encrypted?: number
  probability?: string
}

export type MessageBoard = Advertisement[]

export type SortKey = 'probability' | 'reward' | 'expiresIn'
export type SortDir = 'asc' | 'desc'

export interface Reputation {
    people: number
    state: number
    underworld: number
}
