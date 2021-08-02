<template>
  <div>
    <v-container>
      <v-select
          :items="items"
          label="카테고리"
          outlined
          color="#49784B"
          item-color="green"
          v-model="selectedItem"
        ></v-select>
        <button @click="getGet">getget</button>
      <textarea name="" id="" cols="30" rows="10"></textarea>
    </v-container>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        items: ['식단', '화장품', '의류', '생활용품 etc'],
        selectedItem : ''
      }
    },
    methods: {
      getGet () {
        console.log(this.selectedItem)
      }
    }
  }
</script>

<style>

</style>