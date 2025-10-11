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