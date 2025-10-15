import { config } from '@vue/test-utils'
import { beforeAll, afterAll, afterEach } from 'vitest'
import { setupServer } from 'msw/node'
import { http, HttpResponse } from 'msw'
import { createPinia } from 'pinia'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

config.global.plugins = [
  createPinia(),
  createVuetify({ components, directives })
]

const server = setupServer(
  http.get('/api/games-history', () => {
    return HttpResponse.json([])
  }),

  http.all('*', ({ request }) => {
    const message = `Unhandled ${request.method} request to ${request.url}`
    console.log(message)
    return HttpResponse.json(
      { error: message },
      { status: 501 }
    )}
  )
)

beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
afterEach(() => server.resetHandlers())
afterAll(() => server.close())