<template>
  <div>
    <v-container>
      <h1 class="text-center">비번을 까먹냐 ㅉㅉ</h1>
      <v-text-field
      v-model="email"
        label="email"
      ></v-text-field>
      <v-btn
        block
        @click="sendNumber"
      >
        인증 번호 발송
      </v-btn>
      <v-text-field
        label="인증번호"
        v-model="authNumber"
      ></v-text-field>
      <v-btn
        block
        @click="authCheck"
      >
        인증 확인
      </v-btn>
      <v-form
        v-if="authChecked"
        v-model="valid"
        ref="form"
        lazy-validation
      >
        <v-text-field
          label="새로운 비밀번호"
          v-model="newPassword"
          :rules="newPasswordRules"
          required
        ></v-text-field>
        <v-btn
          :disabled="!valid || !newPassword"
          raised
          block
          @click="onPasswordBtnClick"
          class="bg-freditgreen"
        >
          확인
        </v-btn>

      </v-form>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
  data: () => ({
    // valid라고 되어있는 부분은 v-form 안에 컴포넌트에 바인딩 시키면
    // v-form 내부의 이야기에 따라 
    valid: false,
    email: '',
    authNumber: '',
    newPassword: '',
    newPasswordRules: [
      v => !!v || '비밀번호를 꼭 기입해주세요',
      v => (v && v.length > 7) || '8자리 이상 적어주세요',
      v => (v && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(v)) || '문자/숫자/특수문자의 조합으로 적어주세요'
    ],
    authChecked: false
  }),
  methods: {
    sendNumber () {
      const URL = API.url + userAPI.email_auth('password', this.email)
      axios.get(URL)
        .then(res => {
          console.log(res)
        })
        .catch(err => console.error(err))
    },

    authCheck () {
      const URL = API.url + userAPI.email_auth_check(this.email, this.authNumber)
      axios.get(URL)
        .then(res => {
          console.log(res)
          this.authChecked = true
        })
        .catch(err => console.error(err))
    },
    onPasswordBtnClick () {
      const URL = API.url + userAPI.modify_pw(this.email, this.newPassword)
      axios.put(URL, null, {
        headers:{
          "access-token" : localStorage.getItem('access-token')
        }
      })
        .then(res => {
          console.log(res)
          this.$router.push({ name : "Login" })
        })
        .catch(err => console.error(err))
    }
  }
}
</script>

<style>

</style>