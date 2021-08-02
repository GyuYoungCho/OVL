<template>
  <v-row justify="center">
    <v-col
    cols="12">
      <v-text-field
        color="green lighten-4"
        v-model="email"
        :rules="emailRules"
        label="이메일"
        required
        placeholder="example@example.com"
      >
      </v-text-field>
    </v-col>
    <v-col
    cols="12"
    >
      <v-btn
      block
      @click="onEmailBtnClick"
      :disabled="!email || !(/.+@.+\..+/.test(email))"
      >
        인증번호 발송
      </v-btn>
    </v-col>
    <div v-if="emailBtnClicked">
      
        <v-text-field
          v-model="authKey"
          label="이메일 인증번호"
          required
        >
        </v-text-field>
        <v-btn
          :disabled="!authKey"
          block
          @click="onAuthBtnClick"
        >
          인증하기
        </v-btn>
    </div>
  </v-row> 
</template>

<script>
import API from '@/api/index.js'
import userAPI from '@/api/user.js'
import axios from 'axios'

export default {
  data: () => ({
    authKey: '',
    authKeyRules: [
      v => !!v || '인증키를 입력해주세요',

    ],
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required',
      v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
    ],

    emailBtnClicked: false
  }),
  methods: {
    onEmailBtnClick () {
      const URL = API.url + userAPI.email_auth('join', this.email)
      axios.get(URL)
        .then(res => {
          alert('이메일이 전송되었습니다.')
          console.log(res)
          this.emailBtnClicked = true
          this.$emit('emailCheck')
        })
        .catch(err => console.error(err))
      },
    onAuthBtnClick () {
      const URL = API.url + userAPI.email_auth_check(this.email, this.authKey)
      axios.get(URL)
        .then(res => {
          console.log(res)
          alert('이메일 인증이 완료되었습니다.')
          this.$emit('emailCheck', this.email)
        })
        .catch(err => console.error(err))
    },
  }
}
</script>

<style>

</style>