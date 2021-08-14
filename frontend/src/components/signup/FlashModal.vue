<template>
  <v-dialog v-model="modalOpen" max-width="300" persistent @click:outside="nothing">
      <v-card>
        <!-- 모달 타이틀 영역 -->
        <v-toolbar dense color="#004627">
          <v-toolbar-title class="modalTitle">
          </v-toolbar-title>
          <v-spacer></v-spacer>
        </v-toolbar>
        <!-- 모달 컨텐츠 영역 -->
        <v-container>
        <div class="modalContent">
          <div class="mb-3">
            <span class="modalContentMessage">
              {{ modalContent }}
            </span>
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
  },
  methods: {
    nothing () {
      return
    }
  }
}
</script>

<style>

</style>