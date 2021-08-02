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
        placeholder="닉네임을 적어주세요"
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
import userAPI from '@/api/user.js'
import API from '@/api/index.js'
import axios from 'axios'

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
      const URL = API.url + userAPI.url + userAPI.nickname_check(this.nickname)
      axios.get(URL)
        .then(res => {
          console.log(res)
          alert('사용 가능한 닉네임입니다.')
          this.$emit('nicknameCheck', this.nickname)
        })
        .catch(err => {
          console.log(err)
        })
    },
  }
}
</script>

<style>

</style>