<template>
  <v-row justify="center">
    <v-col
    cols="12">
      <v-text-field
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
      this.emailBtnClicked = true
      // this.$emit('emailCheck')
      },
    onAuthBtnClick () {
      this.$emit('emailCheck', this.email)
    }
  }
}
</script>

<style>

</style>