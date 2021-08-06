<template>
  <v-container>
    <section class="findEmail">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <div>
        <input type="text" placeholder="이름" v-model="name">
      </div>
      <div>
        <input type="text" placeholder="전화번호" v-model="phone">
      </div>
      <div>
        <button class=" finalBtn" :class="{ 'bg-freditgreen': allExist, 'disabledBtn': !allExist }" @click="onFindEmailBtnClick">
          이메일 찾기
        </button>
      </div>
      <div v-if="requestSent">
        <span v-if="!!email">{{ name }}님의 이메일은 {{ email }}입니다.</span>
        <span v-else>해당 정보와 일치하는 회원이 존재하지 않습니다.</span>
      </div>
      <div>
        <hr>
      </div>
      <div class="infoBelow">
        <p>
          <RouterLink :to="{ name: 'Login'}" class="grey-link">로그인 | </RouterLink>
          <RouterLink :to="{ name: 'Signup'}" class="grey-link">회원가입 | </RouterLink>
          <RouterLink :to="{ name: 'FindPassword'}" class="grey-link">비밀번호찾기</RouterLink>
        </p>
      </div>
    </section>
  </v-container>
</template>

<script>
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
  data: () => ({
    name: "",
    phone: "",
    email: "",
    requestSent: false,
  }),
  methods: {
    onFindEmailBtnClick () {
      const URL = API.url + userAPI.search_id(this.name, this.phone)
      axios.get(URL)
        .then(res => {
          console.log(res)
          this.requestSent = true
          if (res.data) {
            this.email = res.data
          }
        })
        .catch(err => console.error(err))
    }
  },
  computed: {
    allExist () {
      return !!this.name && this.phone
    }
  }
}
</script>

<style>

</style>