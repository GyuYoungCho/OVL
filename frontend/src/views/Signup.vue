<template>
  <v-container>
    <!-- 회원가입 관련 모달 -->
    <v-dialog v-model="modalOpen" max-width="300" persistent @click:outside="nothing">
      <v-card>
        <!-- 모달 타이틀 영역 -->
        <v-toolbar dense color="#004627">
          <v-toolbar-title class="modalTitle">
            <!-- {{ modaltitle }} -->
          </v-toolbar-title>
          <v-spacer></v-spacer>
          
        </v-toolbar>
        <!-- 모달 컨텐츠 영역 -->
        <v-container>
        <div class="modalContent">
          <div class="mb-3">
            <span class="modalContentMessage">
              {{ modalContent }}
            </span>
          </div>
        </div>
        </v-container>
      </v-card>
    </v-dialog>


    <section class="signup">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <!-- 이름 -->
      <div>
        <input type="text" placeholder="이름" v-model="name">
      </div>
      <!-- 닉네임 -->
      <div class="inputBtnDiv">
        <input type="text" placeholder="닉네임" v-model="nickname">
        <button class="bg-freditgreen" @click="onNicknameBtnClick">확인</button>
      </div>
      <!-- 이메일 -->
      <div class="inputBtnDiv"  >
        <input type="text" placeholder="이메일" v-model="email">
        <button class="bg-freditgreen" @click="onEmailBtnClick">인증</button>
      </div>
      <p class="invalidTxt" v-if="!emailFormValid">
        이메일 양식을 확인해주세요.
      </p>
      <!-- 이메일 인증 -->
      <div class="inputBtnDiv" v-if="emailAuthNumberSent">
        <input type="text" placeholder="인증번호" v-model="emailAuthNumber">
        <button class="bg-freditgreen" @click="onEmailAuthBtnClick">확인</button>
      </div>
      <!-- 전화번호 -->
      <div>
        <input type="text" placeholder="전화번호" v-model="phone">
      </div>
      <!-- 비밀번호 -->
      <div>
        <input type="password" placeholder="비밀번호" v-model="password">
      </div>
      <p class="invalidTxt" v-if="!passwordFormValid">
        숫자와 특수문자를 포함하여 8자 이상으로 적어주세요.
      </p>
      <!-- 비밀번호 확인 -->
      <div>
        <input type="password" placeholder="비밀번호 확인" v-model="passwordCheck">
      </div>
      <p class="invalidTxt" v-if="!passwordCheckFormValid">
        비밀번호와 일치하지 않습니다.
      </p>
      <!-- 회원가입 버튼 -->
      <div>
        <button class="finalBtn" :class="{ 'bg-freditgreen': signupFormValid, 'disabledBtn': !signupFormValid }" :disabled="!signupFormValid" @click="onSignupBtnClick">회원가입</button>
      </div>
    </section>
  </v-container>
</template>

<script>
// import '@/assets/css/index.scss' 이걸 안써도 되는걸 발견함.
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'
import 'url-search-params-polyfill';

export default {
  data: () => ({
    nicknameValid: false,
    emailValid: false,
    emailAuthNumberSent: false,
    emailAuthNumber: null,

    modalOpen: false,
    modalContent: "",

    name: '',
    nickname: '',
    email: '',
    phone: '',
    password: '',
    passwordCheck: '',
  }),
  methods: {
    nothing () {
      console.log('Nothing!')
    },
    onNicknameBtnClick () {
      this.modalContent = "닉네임 중복 여부 확인 중입니다."
      this.modalOpen = true
      const URL = API.url + userAPI.nickname_check(this.nickname)
      axios.get(URL)
        .then(res => {
          if (res.data === "success") {
            this.nicknameValid = true
            this.modalContent = '사용 가능한 닉네임입니다.'
            setTimeout(() => {
              this.modalOpen = false
            }, 1000)
          } else {
            this.modalContent = `${this.nickname}은(는) 이미 사용중인 닉네임입니다.`
            setTimeout(() => {
              this.modalOpen = false
            }, 1000)
          }
        })
        .catch(err => {
          console.error(err)
        })
    },
    onEmailBtnClick () {
      this.modalContent = '인증번호를 보내고 있습니다. 잠시만 기다려주세요.'
      this.modalOpen = true
      const URL = API.url + userAPI.email_auth('join', this.email)
      axios.get(URL)
        .then(res => {
          console.log(res)
          if (res.data === "success") {
            this.modalContent = '인증번호가 이메일로 전송되었습니다.'
            setTimeout(() => {
              this.modalOpen = false
              this.emailAuthNumberSent = true
            }, 1000);
          } else {
            this.modalContent = '이미 존재하는 이메일입니다.'
            setTimeout(() => {
              this.modalOpen = false
            }, 1000);
          }
        })
        .catch(err => console.error(err))
    },
    onEmailAuthBtnClick () {
    const URL = API.url + userAPI.email_auth_check(this.email, this.emailAuthNumber)
    axios.get(URL)
      .then(res => {
        if (res.data === "success") {
          this.modalContent = '이메일 인증이 완료되었습니다.'
          this.modalOpen = true
          setTimeout(() => {
            this.modalOpen = false
            this.emailValid = true
          }, 1000)
        } else {
          this.modalContent = '인증번호가 일치하지 않습니다. 다시 인증번호를 전송해주세요.'
          this.modalOpen = true
          setTimeout(() => {
            this.modalOpen = false
          }, 1000)
        }
      })
      .catch(err => console.error(err))
      
    },
    onSignupBtnClick () {
      const url = API.url + userAPI.join();
      const { email, name, nickname, password, phone } = this
      const data = { email, name, nickname, password, phone }

      axios.post(url, data)
        .then((res)=>{
            if (res.data=="success") this.$router.push({name:'Login'}) 
        }).catch((err)=> {
            console.log(err);
        })
    },

  },
  computed: {
    emailFormValid () {
      return !this.email || /.+@.+\..+/.test(this.email)
    },
    passwordFormValid () {
      return !this.password || ((this.password.length > 7) && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(this.password))
    },
    passwordCheckFormValid () {
      return !this.passwordCheck || (this.password===this.passwordCheck)
    },
    signupFormValid () {
      const allExist = !!this.name && !!this.nickname && !!this.email && !!this.phone && !!this.password && !!this.passwordCheck
      const allValid = this.nicknameValid && this.emailValid  && this.emailFormValid && this.passwordFormValid && this.passwordCheckFormValid
      return allExist && allValid
    },
  }
}
</script>

<style>

</style>