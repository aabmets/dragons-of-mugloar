<template>
  <nav class="floating-dock" aria-label="Quick links">
    <a
      class="dock-btn"
      href="http://localhost:40022"
      target="_blank"
      rel="noopener noreferrer"
      aria-label="Open API interface"
      title="Open API interface"
    >
      <img src="/server.png" alt="Server" />
    </a>

    <a
      class="dock-btn"
      href="http://localhost:15540/0/browser"
      target="_blank"
      rel="noopener noreferrer"
      aria-label="Open Redis interface"
      title="Open Redis interface"
    >
      <img src="/database.png" alt="Database" />
    </a>
  </nav>
</template>

<script setup lang="ts">
</script>

<style scoped>
.floating-dock {
  position: fixed;
  right: 32px;
  bottom: 32px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 24px;
  z-index: 9999;
  pointer-events: auto;
}

.dock-btn {
  display: inline-flex;
  width: 44px;
  height: 44px;
  border-radius: 10px;
  background: rgba(128, 128, 128, 0.1);
  backdrop-filter: blur(4px);
  align-items: center;
  justify-content: center;
  text-decoration: none;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.18);
  transition: transform 120ms ease, box-shadow 120ms ease, background 120ms ease;
  transform: scale(1.2);
}

.dock-btn:hover,
.dock-btn:focus-visible {
  transform: scale(1.3);
  box-shadow: 0 8px 22px rgba(0, 0, 0, 0.22);
  background: rgba(128, 128, 128, 0.3);
  outline: none;
}

.dock-btn img {
  max-width: 70%;
  max-height: 70%;
  display: block;
}

@media (max-width: 480px) {
  .dock-btn { width: 40px; height: 40px; }
}

@media print {
  .floating-dock { display: none !important; }
}
</style>
