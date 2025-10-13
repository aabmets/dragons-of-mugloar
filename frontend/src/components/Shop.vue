<script setup lang="ts">
import { mdiBarn } from '@mdi/js'
import Product from '@/components/Product.vue'
import SkipTurnButton from "@/components/SkipTurnButton.vue";
</script>

<template>
  <v-dialog max-width="500">
  <template v-slot:activator="{ props: activatorProps }">
    <v-btn
      v-bind="activatorProps"
      class="shop-btn"
      text="Go to Tavern"
      variant="flat"
      :icon="true"
      elevation="2"
      color="teal-lighten-1">
        <v-icon :icon="mdiBarn" size="36" />
    </v-btn>
  </template>

  <template v-slot:default="{ isActive }">
    <v-card title="Tavern" subtitle="Hover to read product details, click to buy.">
      <v-card-text style="padding-bottom: 10px">
        <v-container>
          <v-row>
            <v-col>
              <v-row>
                <Product itemId="ch" />
                <Product itemId="rf" />
                <Product itemId="iron" />
                <Product itemId="mtrix" />
                <Product itemId="wingpotmax" />
              </v-row>
              <v-row>
                <Product itemId="cs" />
                <Product itemId="gas" />
                <Product itemId="wax" />
                <Product itemId="tricks" />
                <Product itemId="wingpot" />
              </v-row>
            </v-col>
            <v-col class="hpot-col">
              <v-row class="hpot-row">
                <Product itemId="hpot" />
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <SkipTurnButton />
        <v-btn text="Exit Tavern" variant="tonal" border @click="isActive.value = false"/>
      </v-card-actions>
    </v-card>
  </template>
</v-dialog>
</template>

<style scoped>
.shop-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
}
.shop-btn:hover {
  transform: scale(1.2);
}
.hpot-col {
  align-content: center;
  flex: 0 0 auto !important;
  width: auto !important;
}
.hpot-row {
  justify-content: center;
  flex-wrap: nowrap;
}
</style>