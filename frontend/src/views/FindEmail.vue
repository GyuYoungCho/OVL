<template>
  <v-container>
    <section class="findEmail">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <div>
        <input type="text" placeholder="이름" @change="nameInputChanged" maxlength="10" v-model="userName">
      </div>
      <p class="invalidTxt" v-if="!nameFormValid">
        이름은 한글만 입력 가능합니다
      </p>
      <div>
        <input type="tel" placeholder="전화번호" v-model="phone" maxlength="11">
        <p class="invalidTxt" v-if="!phoneFormValid">
          "-" 없이 숫자로만 적어주세요. 예) 01012345678
        </p>
        <p class="invalidTxt" v-if="!phoneLenFormValid">
          전화번호는 11자리 입니다.
        </p>
      </div>
      <div>
        <button class=" finalBtn" :class="{ 'bg-freditgreen': allExist, 'disabledBtn': !allExist }" @click="onFindEmailBtnClick"
        :disabled="!allExist">
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
          <RouterLink :to="{ name: 'Login'}" class="grey-link">로그인 &nbsp;|&nbsp; </RouterLink>
          <RouterLink :to="{ name: 'Signup'}" class="grey-link">회원가입 &nbsp;|&nbsp; </RouterLink>
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
    userName: "",
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
          } else {
            this.email = ''
          }
        })
        .catch(err => console.error(err))
    },
    nameInputChanged (event) {
      this.name = event.target.value
    }
  },
  computed: {
    nameFormValid() {
      return !this.userName || (this.userName.length>0 && /^[가-힣]+$/.test(this.userName))
    },
    phoneFormValid () {
      return !this.phone || ((/^[\d]+$/.test(this.phone) && !/[-+]+$/.test(this.phone)))
    },
    phoneLenFormValid() {
      if (!this.phoneFormValid) return true
      return !this.phone || this.phone.length==11
    },
    allExist () {
      return !!this.name && this.phoneFormValid && this.phoneLenFormValid && this.nameFormValid
    }
  }
}
</script>

<style>

</style>