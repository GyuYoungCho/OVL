<template>
  <v-container class="mt-4">
    <div class="article">
      <div class="categoryBtn_box">
        <button @click="foodClick" :class="{'articleBtnNotSelected':!foodSelected, 'articleBtnSelected':foodSelected}">음식</button>
        <button @click="clothClick" :class="{'articleBtnNotSelected':!clothSelected, 'articleBtnSelected':clothSelected}">옷</button>
        <button @click="cosmeticClick" :class="{'articleBtnNotSelected':!cosmeticSelected, 'articleBtnSelected':cosmeticSelected}">화장품</button>
      </div>
    </div>
  </v-container>
</template>

<script>
export default {
  data () {
    return {
      foodSelected: false,
      clothSelected: false,
      cosmeticSelected: false,
      // 현재 선택된 카테고리가 무엇인지 가리키는 변수
      nowSelectedCategory: 0,
    }
  },
  methods: {
    foodClick () {
      // css 토글용 변수
        this.foodSelected = true
        this.clothSelected = false
        this.cosmeticSelected = false
      // 전부 보기로 돌아가는 버튼이 없는 관계로, 한번더 클릭시 해제되면서 전체 리스트가 보이게끔 함.
      },
    clothClick () {
        this.foodSelected = false
        this.clothSelected = true
        this.cosmeticSelected = false
      },
    cosmeticClick () {
        this.foodSelected = false
        this.clothSelected = false
        this.cosmeticSelected = true
      },
    
  },
  computed: {
  
  },
}
</script>

<style>

</style>