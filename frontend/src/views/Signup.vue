<template>
  <v-container>
    <!-- 모달 -->
    <FlashModal :modalOpen="modalOpen" :title="modalTitle" :modalContent="modalContent" />

    <section class="signup">
      <img src="@/assets/image/OVL_logo.png" alt="" @click="onClickLogo">
      <!-- 이름 -->
      <div>
        <input type="text" placeholder="이름" v-model="name">
      </div>
      <!-- 닉네임 -->
      <div class="inputBtnDiv">
        <input type="text" placeholder="닉네임" v-model="nickname">
        <button :class="{'bg-freditgreen': nicknameFormValid && !!this.nickname, 'disabledBtn': !nicknameFormValid || !this.nickname }" 
        @click="onNicknameBtnClick" :disabled="!nicknameFormValid || !this.nickname">확인</button>
      </div>
      <p class="invalidTxt" v-if="!nicknameFormValid">
        닉네임은 영어와 숫자만 포함하여 10글자 이내로 적어주세요.
      </p>
      <!-- 이메일 -->
      <div class="inputBtnDiv"  >
        <input type="text" placeholder="이메일" v-model="email">
        <button :class="{'bg-freditgreen': emailFormValid && !!this.email, 'disabledBtn': !emailFormValid || !this.email}" 
        @click="onEmailBtnClick" :disabled="!emailFormValid || !this.email">인증</button>
      </div>
      <p class="invalidTxt" v-if="!emailFormValid">
        이메일 양식을 확인해주세요.
      </p>
      <!-- 이메일 인증 -->
      <div class="inputBtnDiv" v-if="emailAuthNumberSent">
        <input type="text" placeholder="인증번호" v-model="emailAuthNumber">
        <button :class="{'bg-freditgreen': !!this.emailAuthNumber, 'disabledBtn': !this.emailAuthNumber}" 
        @click="onEmailAuthBtnClick" :disabled="!this.emailAuthNumber">확인</button>
      </div>
      <!-- 전화번호 -->
      <div>
        <input type="tel" placeholder="전화번호" v-model="phone" maxlength="11">
      </div>
      <p class="invalidTxt" v-if="!phoneFormValid">
        "-" 없이 숫자로만 적어주세요. 예) 01012345678
      </p>
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
import FlashModal from '@/components/signup/FlashModal.vue'
import 'url-search-params-polyfill';

export default {
  components: {
    FlashModal,
  },
  data: () => ({
    nicknameValid: false,
    emailValid: false,
    emailAuthNumberSent: false,
    emailAuthNumber: null,

    modalOpen: false,
    modalContent: "",
    modalTitle:"",

    name: '',
    nickname: '',
    email: '',
    phone: '',
    password: '',
    passwordCheck: '',
  }),
  methods: {
    onClickLogo(){
      this.$router.push({name: 'Main'})
    },
    nothing () {
      console.log('Nothing!')
    },
    onNicknameBtnClick () {
      this.modalTitle = "닉네임 중복 확인"
      this.modalContent = "닉네임 중복 여부 확인 중입니다"
      this.modalOpen = true
      const URL = API.url + userAPI.nickname_check(this.nickname)
      axios.get(URL)
        .then(res => {
          if (res.data === "success") {
            this.nicknameValid = true
            this.modalTitle = "닉네임 중복 확인"
            this.modalContent = '사용 가능한 닉네임입니다'
            setTimeout(() => {
              this.modalOpen = false
            }, 1000)
          } else {
            this.modalTitle = "닉네임 중복 확인"
            this.modalContent = `${this.nickname}은(는) 이미 사용중인 닉네임입니다`
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
      this.modalTitle = "이메일 인증"
      this.modalContent = '인증번호를 보내고 있습니다 잠시만 기다려주세요'
      this.modalOpen = true
      const URL = API.url + userAPI.email_auth('join', this.email)
      axios.get(URL)
        .then(res => {
          console.log(res)
          if (res.data === "success") {
            this.modalTitle = "이메일 인증"
            this.modalContent = '인증번호가 이메일로 전송되었습니다'
            setTimeout(() => {
              this.modalOpen = false
              this.emailAuthNumberSent = true
            }, 1000);
          } else {
            this.modalTitle = "이메일 인증"
            this.modalContent = '이미 존재하는 이메일입니다'
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
          this.modalTitle = "이메일 인증 확인"
          this.modalContent = '이메일 인증이 완료되었습니다'
          this.modalOpen = true
          setTimeout(() => {
            this.modalOpen = false
            this.emailValid = true
          }, 1000)
        } else {
          this.modalTitle = "이메일 인증 확인"
          this.modalContent = '인증번호가 일치하지 않습니다. 다시 인증번호를 확인해주세요'
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

      console.log("비밀번호 ; "+this.password);
      console.log("data : ",data);

      axios.post(url, data)
        .then((res)=>{
          if (res.data=="success") {
              this.modalTitle = "회원가입"
              this.modalContent = "회원가입이 완료되었습니다"
              this.modalOpen = true
              setTimeout(() => {
                this.modalOpen = false
                this.$router.push({name:'Login'})
              }, 1000);
            }
        }).catch((err)=> {
            console.log(err);
        })
    },

  },
  computed: {
    nicknameFormValid () {
      return !this.nickname || (this.nickname.length < 10 && /^[a-zA-Z0-9]*$/.test(this.nickname))
    }, 
    emailFormValid () {
      return !this.email || /.+@.+\..+/.test(this.email)
    },
    phoneFormValid () {
      return !this.phone || (/^[\d]+$/.test(this.phone) && !/[-+]+$/.test(this.phone))
    },
    passwordFormValid () {
      return !this.password || ((this.password.length > 7) && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(this.password))
    },
    passwordCheckFormValid () {
      return !this.passwordCheck || (this.password===this.passwordCheck)
    },
    signupFormValid () {
      const allExist = !!this.name && !!this.nickname && !!this.email && !!this.phone && !!this.password && !!this.passwordCheck
      const allValid = this.nicknameValid && this.emailValid  && this.emailFormValid && this.phoneFormValid && this.passwordFormValid && this.passwordCheckFormValid
      return allExist && allValid
    },
  }
}
</script>

<style>

</style>