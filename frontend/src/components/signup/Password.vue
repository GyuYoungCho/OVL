<template>
  <v-text-field
    v-model="password"
    rounded
    :rules="passwordRules"
    required
    placeholder="문자/숫자/특수문자의 조합으로 적어주세요"
    class='mb-6'
    @input="onPasswordChange"
  ></v-text-field>
</template>

<script>
export default {
  data: () => ({
    password: '',
    passwordRules: [
      v => !!v || '비밀번호를 꼭 기입해주세요',
      v => (v && v.length > 7) || '8자리 이상 적어주세요',
      v => (v && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(v)) || '문자/숫자/특수문자의 조합으로 적어주세요'
    ],
  }),
  methods: {
    onPasswordChange () {
      this.$emit('passwordChanged', this.password)
    }
  }
}
</script>

<style>

</style>