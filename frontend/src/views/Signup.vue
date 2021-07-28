<template>
  <v-container>
    <h1 class="text-center">
      회원가입
    </h1>
    <KakaoLogin />

    <v-form
      ref="form"
      v-model="valid"
      lazy-validation
    >
      <NickName @nicknameCheck="nicknameCheck" />
      <Email @emailCheck="emailCheck" />
      <PhoneNumber @phoneNumberChanged="phoneNumberChanged" />
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
import Password from '@/components/signup/Password.vue'
import PhoneNumber from '@/components/signup/PhoneNumber.vue'
import NickName from '@/components/signup/NickName.vue'
import Email from '@/components/signup/Email.vue'
import KakaoLogin from '@/components/KakaoLogin.vue'

  export default {
    components: {
      Password, PhoneNumber, NickName, Email, KakaoLogin,
    },
    data: () => ({
      valid: false,
      nicknameValid: false,
      emailValid: false,

      nickname: '',
      email: '',
      phoneNumber: '',
      password: '',
    }),
    methods: {
      onSignupBtnClick () {
        console.log(this.$data)
        // this.$router.push({ name: 'Login' })
      },
      nicknameCheck (nickname) {
        this.nicknameValid = true
        this.nickname = nickname
      },
      emailCheck (email) {
        this.emailValid = true
        this.email = email
      },
      phoneNumberChanged (phoneNumber) {
        this.phoneNumber = phoneNumber
      },
      passwordChanged (password) {
        this.password = password
      }
    },
    computed: {
      allValid () {
        return this.emailValid && this.nicknameValid && this.nickname && this.phoneNumber && this.password
      }
    }
    
  }
</script>

<style>

</style>