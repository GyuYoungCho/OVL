<template>
  <v-row>
    <v-col cols="12">
      <v-text-field
        v-model="name"
        label="이름"
        required
        placeholder="이름을 적어주세요"
        :rules="nameRules"
        @change="onNameChange"
      >

      </v-text-field>
    </v-col>
  </v-row>
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