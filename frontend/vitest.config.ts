import { fileURLToPath } from 'node:url'
import { mergeConfig, defineConfig, configDefaults } from 'vitest/config'
import viteConfig from './vite.config'

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      environment: 'happy-dom',
      setupFiles: ['./tests/setup.ts'],
      exclude: [...configDefaults.exclude, 'e2e/**'],
      server: {
        deps: {
          inline: ['vuetify']
        }
      },
      root: fileURLToPath(new URL('./', import.meta.url)),
      css: true,
    },
  }),
)
