<template>
  <!-- 닉네임 -->
  <v-row align="center">
    <v-col cols="12">
      <v-text-field
        v-model="nickname"
        :counter="10"
        :rules="nicknameRules"
        label="닉네임"
        required
        placeholder="홍길동"
      ></v-text-field>
    </v-col>
    <v-col cols="12">
      <v-btn
      :disabled="!nickname || !(nickname.length <= 10)"
      block
      @click="onNicknameBtnClick"
      >
        중복확인
      </v-btn>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data: () => ({
    nickname: '',
    nicknameRules: [
      v => !!v || 'Name is required',
      v => (v && v.length <= 10) || 'Name must be less than 10 characters',
    ],
  }),
  methods: {
    onNicknameBtnClick () {
        this.$emit('nicknameCheck', this.nickname)
      },
  }
}
</script>

<style>

</style>