<template>
  <v-container>
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
import defaultImg from '@/assets/image/defaultImg.png'

export default {
  data: () => ({
    nicknameValid: false,
    emailValid: false,
    emailAuthNumberSent: false,
    emailAuthNumber: null,

    name: '',
    nickname: '',
    email: '',
    phone: '',
    password: '',
    passwordCheck: '',
  }),
  methods: {
    onNicknameBtnClick () {
    const URL = API.url + userAPI.nickname_check(this.nickname)
    axios.get(URL)
      .then(res => {
        if (res.data === "success") {
          this.nicknameValid = true
          alert('사용 가능한 닉네임입니다.')
        } else {
          alert(`${this.nickname}은(는) 이미 사용중인 닉네임입니다.`)
        }
      })
      .catch(err => {
        console.log(err)
      })

    },
    onEmailBtnClick () {
    const URL = API.url + userAPI.email_auth('join', this.email)
    axios.get(URL)
      .then(res => {
        // alert('이메일이 전송되었습니다.')
        console.log(res)
        if (res.data === "success") {
          alert('인증번호가 이메일로 전송되었습니다.')
          this.emailAuthNumberSent = true
        } else {
          alert('이미 존재하는 이메일입니다.')
        }
      })
      .catch(err => console.error(err))
    },
    onEmailAuthBtnClick () {
    const URL = API.url + userAPI.email_auth_check(this.email, this.emailAuthNumber)
    axios.get(URL)
      .then(res => {
        if (res.data === "success") {
          alert('이메일 인증이 완료되었습니다.')
          this.emailValid = true
        } else {
          alert('인증번호가 일치하지 않습니다. 다시 인증번호를 전송해주세요.')
        }
      })
      .catch(err => console.error(err))
      
    },
    onSignupBtnClick () {

      const dataURLtoFile = (dataurl, fileName) => {

      var arr = dataurl.split(','),
          mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]), 
          n = bstr.length, 
          u8arr = new Uint8Array(n);
          
        while(n--){
            u8arr[n] = bstr.charCodeAt(n);
        }
        
        return new File([u8arr], fileName, {type:mime});
      }



      const URL = API.url + userAPI.join()
      const { email, name, nickname, password, phone } = this
      const data = { email, name, nickname, password, phone }
      // let receive_id = ""
      console.log(data)
      axios.post(URL, data)
        .then(res => {
          var file = dataURLtoFile(defaultImg,'default.png');

          const pro_URL = API.url + userAPI.join_profile()
          
          var formData = new FormData();
          formData.append("picture",file)
          formData.append("user_id",String(res.data))
          
          

          axios.post(pro_URL, formData)
            .then(res => {
              console.log(res)
              this.$router.push({ name: 'Login' })
        })
        .catch(err => console.error(err))
          // receive_id = "8"
          // this.$router.push({ name: 'Login' })
        })
      .catch(err => console.error(err))
    
    //Usage example:
    
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