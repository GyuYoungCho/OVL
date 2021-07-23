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
      >
        인증하기
      </v-btn>
    </v-col>
    <v-col>
      <v-text-field
        v-model="authKey"
        label="이메일 인증번호"
        required
      >

      </v-text-field>
    </v-col>
  </v-row> 
</template>

<script>
export default {
  data: () => ({
    authKey: '',
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required',
      v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
    ],
  }),
  methods: {
    onEmailBtnClick () {
        this.$emit('emailCheck')
      },
  }
}
</script>

<style>

</style>