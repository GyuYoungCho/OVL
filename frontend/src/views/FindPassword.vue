<template>
  <v-container>
    <section class="findPassword">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <!-- 이메일 -->
      <div class="inputBtnDiv">
        <input type="text" placeholder="이메일" v-model="email">
        <button class="bg-freditgreen" @click="onEmailBtnClick">인증</button>
      </div>
      <!-- 인증번호 -->
      <div class="inputBtnDiv" v-if="emailValid">
        <input type="text" placeholder="인증번호" v-model="authNumber">
        <button class="bg-freditgreen" @click="onEmailAuthBtnClick">확인</button>
      </div>
      <!-- 새 비밀번호 -->
      <div v-if="authNumberValid">
        <input type="password" placeholder="새로운 비밀번호 입력" v-model="newPassword">
      </div>
      <p class="invalidTxt" v-if="!newPasswordFormValid">
        숫자와 특수문자를 포함하여 8자 이상으로 적어주세요.
      </p>
      <!-- 새 비밀번호 확인 -->
      <div v-if="authNumberValid">
        <input type="password" placeholder="새로운 비밀번호 확인" v-model="newPasswordCheck">
      </div>
      <p class="invalidTxt" v-if="!newPasswordCheckFormValid">
        비밀번호와 일치하지 않습니다.
      </p>
      <!-- 비밀번호 변경 버튼 -->
      <div v-if="authNumberValid">
        <button class=" finalBtn" :class="{ 'bg-freditgreen': findPasswordFormValid, 'disabledBtn': !findPasswordFormValid }" @click="onPasswordBtnClick">
          비밀번호변경
        </button>
      </div>
      <div>
        <hr>
      </div>
      <div class="infoBelow">
        <p>
          <RouterLink :to="{ name: 'Login'}" class="grey-link">로그인 | </RouterLink>
          <RouterLink :to="{ name: 'Signup'}" class="grey-link">회원가입 | </RouterLink>
          <RouterLink :to="{ name: 'FindEmail'}" class="grey-link">이메일찾기</RouterLink>
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
    email: '',
    emailValid: false,
    authNumber: '',
    authNumberValid: false,
    newPassword: '',
    newPasswordCheck: '',
  }),
  methods: {
    onEmailBtnClick () {
      const URL = API.url + userAPI.email_auth('password', this.email)
      axios.get(URL)
        .then(res => {
          if (res.data==="success") {
            alert("인증번호가 이메일로 발송되었습니다.")
            this.emailValid = true
          } else {
            alert("등록 정보가 없는 이메일입니다.")
          }
        })
        .catch(err => console.error(err))
    },

    onEmailAuthBtnClick () {
      const URL = API.url + userAPI.email_auth_check(this.email, this.authNumber)
      axios.get(URL)
        .then(res => {
          if (res.data==="success") {
            alert('이메일 인증이 완료되었습니다.')
            this.authNumberValid = true
          } else {
            alert('인증번호가 틀렸습니다. 다시 인증번호 발송을 해주세요.')
            this.authNumber = ''
          }
        })
        .catch(err => console.error(err))
    },
    onPasswordBtnClick () {
      const URL = API.url + userAPI.modify_pw(this.email, this.newPassword)
      axios.put(URL, null, {
        headers:{
          "access-token" : localStorage.getItem('access-token')
        }
      })
        .then(res => {
          console.log(res)
          alert('비밀번호가 무사히 변경되었습니다.')
          this.$router.push({ name : "Login" })
        })
        .catch(err => console.error(err))
    }
  },
  computed: {
    newPasswordFormValid () {
      return !this.newPassword || ((this.newPassword.length > 7) && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(this.newPassword))
    },
    newPasswordCheckFormValid () {
      return !this.newPasswordCheck || (this.newPassword===this.newPasswordCheck)
    },
    findPasswordFormValid () {
    const dataValid = !!this.email && this.emailValid && !!this.authNumber && this.authNumberValid && !!this.newPassword && !!this.newPasswordCheck
    const formValid = this.newPasswordFormValid && this.newPasswordCheckFormValid
    return dataValid && formValid
    },
  }
}
</script>

<style>

</style>