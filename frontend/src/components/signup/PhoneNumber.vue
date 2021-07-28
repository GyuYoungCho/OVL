<template>
  <v-text-field
    v-model="phoneNumber"
    :rules="phoneNumberRules"
    label="전화번호"
    required
    placeholder="01012341234 형식으로 적어주세요"
    @input="onPhoneNumberChanged"
  >
  </v-text-field>
</template>

<script>
export default {
  data: () => ({
    phoneNumber: '',
    phoneNumberRules: [
      v => !!v || '전화번호를 꼭 기입해주세요',
      v => (v && v.length > 8) || '번호가 너무 짧아요'
    ],
  }),
  methods: {
    onPhoneNumberChanged () {
      this.$emit('phoneNumberChanged', this.phoneNumber)
    }
  }
}
</script>

<style>

</style>