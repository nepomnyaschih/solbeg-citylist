import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { quasar, transformAssetUrls } from '@quasar/vite-plugin'

export default ({ mode }) => {

  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) };

  console.debug("RUN MODE: ", mode)

  return defineConfig({
    plugins: [
      vue({
        template: { transformAssetUrls }
      }),
      quasar({
        sassVariables: 'src/quasar-variables.sass'
      })],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    }
  });
}
