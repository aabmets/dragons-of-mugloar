<script setup lang="ts">
import { mdiShoppingOutline } from '@mdi/js'
import Product from '@/components/Product.vue'
</script>

<template>
  <v-dialog max-width="500">
  <template v-slot:activator="{ props: activatorProps }">
    <v-btn
      v-bind="activatorProps"
      class="shop-btn"
      text="Open Shop"
      variant="flat"
      :icon="true"
      elevation="2"
      color="teal-lighten-1">
        <v-icon :icon="mdiShoppingOutline" size="36" />
    </v-btn>
  </template>

  <template v-slot:default="{ isActive }">
    <v-card title="Shop" subtitle="Hover to read product details, click to buy.">
      <v-card-text style="padding-bottom: 10px">
        <v-container>
          <v-row>
            <v-col>
              <v-row>
                <Product name="Claw Honing" itemId="ch" imagePath="/claw-honing.png" :price=300 />
                <Product name="Rocket Fuel" itemId="rf" imagePath="/rocket-fuel.png" :price=300 />
                <Product name="Iron Plating" itemId="iron" imagePath="/iron-plating.png" :price=300 />
                <Product name="Book of Megatricks" itemId="mtrix" imagePath="/book-of-megatricks.png" :price=300 />
                <Product name="Potion of Awesome Wings" itemId="wingpotmax" imagePath="/potion-of-awesome-wings.png" :price=300 />
              </v-row>
              <v-row>
                <Product name="Claw Sharpening" itemId="cs" imagePath="/claw-sharpening.png" :price=100 />
                <Product name="Gasoline" itemId="gas" imagePath="/gasoline.png" :price=100 />
                <Product name="Copper Plating" itemId="wax" imagePath="/copper-plating.png" :price=100 />
                <Product name="Book of Tricks" itemId="tricks" imagePath="/book-of-tricks.png" :price=100 />
                <Product name="Potion of Stronger Wings" itemId="wingpot" imagePath="/potion-of-stronger-wings.png" :price=100 />
              </v-row>
            </v-col>
            <v-col class="hpot-col">
              <v-row class="hpot-row">
                <Product name="Healing Potion" itemId="hpot" imagePath="/healing-potion.png" :price=50 />
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-btn text="Close Shop" variant="tonal" border @click="isActive.value = false"/>
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