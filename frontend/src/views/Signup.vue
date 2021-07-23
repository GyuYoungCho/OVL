<template>
  <v-container>
    <h1 class="text-center">
      회원가입
    </h1>
    <v-form
      ref="form"
      v-model="valid"
      lazy-validation
    >
      <NickName @nicknameCheck="nicknameCheck" />
      <Email @emailCheck="emailCheck" />
      <PhoneNumber />
      <Password />
      <v-btn
        :disabled="!valid || !allValid"
        class="bg-freditgreen"
        raised
        @click="validate"
        block
      >
        가입하기
      </v-btn>

    </v-form>
  </v-container>
</template>

<script>
import './signup.scss'
import Password from './signup/Password.vue'
import PhoneNumber from './signup/PhoneNumber.vue'
import NickName from './signup/NickName.vue'
import Email from './signup/Email.vue'

  export default {
    components: {
      Password, PhoneNumber, NickName, Email,
    },
    data: () => ({
      valid: false,
      nicknameValid: false,
      emailValid: false,
    }),
    methods: {
      validate () {
        this.$refs.form.validate()
      },
      nicknameCheck () {
        this.nicknameValid = true
      },
      emailCheck () {
        this.emailValid = true
      }
    },
    computed: {
      allValid () {
        return this.emailValid && this.nicknameValid
      }
    }
    
  }
</script>

<style>

</style>