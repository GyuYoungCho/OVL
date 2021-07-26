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
      >
        인증 번호 발송
      </v-btn>
      <v-text-field
        label="인증번호"
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
          @click="goToLogin"
          class="bg-freditgreen"
        >
          확인
        </v-btn>

      </v-form>
    </v-container>
  </div>
</template>

<script>
import './findpassword.scss'
export default {
  data: () => ({
    // valid라고 되어있는 부분은 v-form 안에 컴포넌트에 바인딩 시키면
    // v-form 내부의 이야기에 따라 
    valid: false,
    email: '',
    newPassword: '',
    newPasswordRules: [
      v => !!v || '비밀번호를 꼭 기입해주세요',
      v => (v && v.length > 7) || '8자리 이상 적어주세요',
      v => (v && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(v)) || '문자/숫자/특수문자의 조합으로 적어주세요'
    ],
    authChecked: false
  }),
  methods: {
    // validate () {
    //     this.$refs.form.validate()
    //   },
    authCheck () {
      this.authChecked = true
    },
    goToLogin () {
      this.$router.push({ name : "Login" })
    }
  }
}
</script>

<style>

</style>