<template>
  <v-container>
    <h1 class="text-center">
      회원가입
    </h1>
    <!-- <KakaoLogin /> -->

    <v-form
      ref="form"
      v-model="valid"
      lazy-validation
    >
      <Name @nameChanged="nameChanged" />
      <NickName @nicknameCheck="nicknameCheck" />
      <Email @emailCheck="emailCheck" />
      <PhoneNumber @phoneNumberChanged="phoneChanged" />
      <Password @passwordChanged="passwordChanged" />
      <v-btn
        :disabled="!valid || !allValid"
        class="bg-freditgreen"
        raised
        block
        @click="onSignupBtnClick"
      >
        가입하기
      </v-btn>

    </v-form>
  </v-container>
</template>

<script>
// import '@/assets/css/index.scss' 이걸 안써도 되는걸 발견함.
import Name from '@/components/signup/Name.vue'
import Password from '@/components/signup/Password.vue'
import PhoneNumber from '@/components/signup/PhoneNumber.vue'
import NickName from '@/components/signup/NickName.vue'
import Email from '@/components/signup/Email.vue'
// import KakaoLogin from '@/components/KakaoLogin.vue'

import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

  export default {
    components: {
      Name, Password, PhoneNumber, NickName, Email, 
      // KakaoLogin,
    },
    data: () => ({
      valid: false,
      nicknameValid: false,
      emailValid: false,

      name: '',
      nickname: '',
      email: '',
      phone: '',
      password: '',
    }),
    methods: {
      onSignupBtnClick () {
        const URL = API.url + userAPI.join()
        const { email, name, nickname, password, phone } = this
        const data = { email, name, nickname, password, phone }
        console.log(data)
        axios.post(URL, data)
          .then(res => {
            console.log(res)
            this.$router.push({ name: 'Login' })
          })
          .catch(err => console.error(err))
      },
      nicknameCheck (nickname) {
        this.nicknameValid = true
        this.nickname = nickname
      },
      emailCheck (email) {
        this.emailValid = true
        this.email = email
      },
      nameChanged (name) {
        this.name = name
      },
      phoneChanged (phone) {
        this.phone = phone
      },
      passwordChanged (password) {
        this.password = password
      }
    },
    computed: {
      allValid () {
        return this.emailValid && this.nicknameValid && this.nickname && this.phone && this.password
      }
    }
    
  }
</script>

<style>

</style>