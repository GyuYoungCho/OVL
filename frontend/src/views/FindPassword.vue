<template>
  <v-container>
    <!-- 모달 -->
    <FlashModal :modalOpen="modalOpen" :title="modalTitle" :modalContent="modalContent" />
    
    <section class="findPassword">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <!-- 이메일 -->
      <div class="inputBtnDiv">
        <input type="text" placeholder="이메일" v-model="email">
        <button :class="{'bg-freditgreen': email && emailFormValid, 'disabledBtn': !email || !emailFormValid}"
        :disabled="!email || !emailFormValid" @click="onEmailBtnClick">인증</button>
      </div>
      <p class="invalidTxt" v-if="!emailFormValid">
        이메일 양식을 확인해주세요.
      </p>
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
        숫자와 특수문자를 포함하여 8자 이상 20자 이하로 적어주세요.
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
        <button class=" finalBtn" :class="{ 'bg-freditgreen': findPasswordFormValid, 'disabledBtn': !findPasswordFormValid }" 
        :disabled="!findPasswordFormValid" @click="onPasswordBtnClick">
          비밀번호 변경
        </button>
      </div>
      <div>
        <hr>
      </div>
      <div class="infoBelow">
        <p>
          <RouterLink :to="{ name: 'Login'}" class="grey-link">로그인 &nbsp;|&nbsp; </RouterLink>
          <RouterLink :to="{ name: 'Signup'}" class="grey-link">회원가입 &nbsp;|&nbsp; </RouterLink>
          <RouterLink :to="{ name: 'FindEmail'}" class="grey-link">이메일 찾기</RouterLink>
        </p>
      </div>
    </section>
  </v-container>
</template>

<script>
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
  components: {
    FlashModal,
  },
  data: () => ({
    email: '',
    emailValid: false,
    authNumber: '',
    authNumberValid: false,
    newPassword: '',
    newPasswordCheck: '',

    modalOpen: false,
    modalContent: '',
    modalTitle:'',
  }),
  methods: {
    nothing () {
      console.log('nothing')
    },
    onEmailBtnClick () {
      this.modalTitle = "이메일 인증"
      this.modalContent = '이메일을 확인하는 중입니다. 잠시만 기다려주세요'
      this.modalOpen = true
      const URL = API.url + userAPI.email_auth('password', this.email)
      axios.get(URL)
        .then(res => {
          if (res.data==="success") {
            this.modalTitle = "이메일 인증"
            this.modalContent = "인증번호가 이메일로 발송되었습니다"
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
            this.emailValid = true
          } else {
            this.modalTitle = "이메일 인증"
            this.modalContent = "등록 정보가 없는 이메일입니다"
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
          }
        })
        .catch(err => {
          console.error(err)
          this.modalTitle = "이메일 인증"
          this.modalContent = "등록 정보가 없는 이메일입니다"
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
        })
    },

    onEmailAuthBtnClick () {
      this.modalTitle = "이메일 인증"
      this.modalContent = '이메일을 확인하는 중입니다. 잠시만 기다려주세요'
      this.modalOpen = true
      const URL = API.url + userAPI.email_auth_check(this.email, this.authNumber)
      axios.get(URL)
        .then(res => {
          if (res.data==="success") {
            this.modalTitle = "이메일 인증"
            this.modalContent = "이메일 인증이 완료되었습니다"
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
            this.authNumberValid = true
          } else {
            this.modalTitle = "이메일 인증"
            this.modalContent = '인증번호가 일치하지 않습니다. 다시 인증번호를 확인해주세요'
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
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
          this.modalTitle = "비밀번호 변경"
          this.modalContent = '비밀번호가 무사히 변경되었습니다'
          this.modalOpen = true
          setTimeout(() => {
            this.modalOpen = false
            this.$router.push({ name: "Login" })
          }, 1000);
          console.log(res)
        })
        .catch(err => console.error(err))
    }
  },
  computed: {
    emailFormValid () {
      return !this.email || /.+@.+\..+/.test(this.email)
    },
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