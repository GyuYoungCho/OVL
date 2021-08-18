<template>
  <v-dialog v-model="modalOpen" max-width="300" persistent>
    <v-card>
      <!-- 모달 타이틀 영역 -->
      <v-toolbar dense color="#49784B">
        <v-toolbar-title class="modalTitle">{{ modalTitle }}</v-toolbar-title>
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
          &nbsp;&nbsp;&nbsp;
          <button class="modalContentButton" @click="modalCancelBtnClick">취소</button>
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
    modalContent: String,
    modalTitle: String, 
  },
  methods: {
    modalConfirmBtnClick () {
      this.$emit('modalConfirmBtnClick')
    },
    modalCancelBtnClick () {
      this.$emit('modalCancelBtnClick')
    }
  }
}
</script>

<style>

</style>