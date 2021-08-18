<template>
  <!-- 삭제 모달 -->
  <v-dialog v-model="modalOpen" hide-overlay max-width="300">
    <v-card>
      <!-- 모달 타이틀 영역 -->
      <v-toolbar dense color="#004627">
        <v-toolbar-title class="modalTitle">{{modalTitle}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon dark @click="modalCancelBtnClick">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <!-- 모달 컨텐츠 영역 -->
      <v-container>
      <div class="modalContent">
        <div class="mb-3">
          <span class="modalContentMessage">
            {{ modalContent }}
          </span>
        </div>
        <div class="modalContentButtonArea">
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <button class="modalContentButton" @click="modalConfirmBtnClick">확인</button>
          <v-spacer></v-spacer>
          <button class="modalContentButton" @click="modalCancelBtnClick">취소</button>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
        </div>
      </div>
      </v-container>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    modalOpen: Boolean,
    modalContent:String,
    modalTitle:String,
    post:Object,
  },
  methods: {
    modalConfirmBtnClick () {
      console.log("확인!!!");
      this.$emit('modalConfirmBtnClick')
    },
    modalCancelBtnClick () {
      this.$emit('modalCancelBtnClick')
    },
  }
}
</script>

<style>

</style>