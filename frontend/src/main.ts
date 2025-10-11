import 'unfonts.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import { MotionPlugin } from '@vueuse/motion'
import { GesturePlugin } from '@vueuse/gesture'

const app = createApp(App)

app.use(createPinia())
app.use(vuetify)
app.use(MotionPlugin)
app.use(GesturePlugin)

app.mount('#app')
