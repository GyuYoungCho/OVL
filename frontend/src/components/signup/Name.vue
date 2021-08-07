<template>
  <v-text-field
    v-model="name"
    rounded
    required
    placeholder="이름"
    :rules="nameRules"
    @input="onNameChange"
  >

  </v-text-field>
</template>

<script>
export default {
  data: () => ({
    name: '',
    nameRules: [
      v => !!v || '이름을 꼭 적어주세요',
    ]
  }),
  methods: {
    onNameChange () {
      this.$emit('nameChanged', this.name)
    }
  }
}
</script>

<style>

</style>