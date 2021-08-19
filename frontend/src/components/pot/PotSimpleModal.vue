<template>
  <!-- 커스텀 모달 -->
    <v-dialog v-model="potwarning" @click:outside="warningCancel()" max-width="300">
      <v-card>
        <!-- 모달 타이틀 영역 -->
        <v-toolbar dense color="#49784B">
          <v-toolbar-title class="modalTitle">채식팟 이용안내</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="warningCancel()">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <!-- 모달 컨텐츠 영역 -->
        <v-container>
        <div class="modalContent">
          <div class="mb-3">
            <span class="modalContentMessage">
              다른 유저들에게 경고를 3회 이상 받은 유저는 채식팟 이용이 제한됩니다!
            </span>
          </div>
        </div>
        </v-container>
      </v-card>
    </v-dialog>
</template>

<script>
export default {
  props:{
    potwarning : Boolean,
  },
  data () {
    return {

    }
  },
  methods: {
    warningCancel () {
      this.$emit('warning-cancel', false)
      this.$router.push({name:'Main'})
    }
  }

}
</script>

<style>

</style>