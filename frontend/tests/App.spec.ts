import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import App from '../src/App.vue'

describe('App', () => {
  it('mounts and shows the landing UI', () => {
    const wrapper = mount(App)
    expect(wrapper.text()).toContain('New Game')
    expect(wrapper.text()).toContain('Leaderboard')
  })
})
