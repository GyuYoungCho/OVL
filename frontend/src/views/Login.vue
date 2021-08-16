<template>
  <v-container>
    <!-- 로그인 관련 모달 -->
    <FlashModal :modalOpen="modalOpen" :modalContent="modalContent" />

    <section class="login">
      <img src="@/assets/image/OVL_logo.png" alt="">
      <!-- 이메일 -->
      <div>
        <input type="text" placeholder="이메일" v-model="userinfo.email">
      </div>
      <p class="invalidTxt" v-if="!emailFormValid">
        이메일 양식을 확인해주세요.
      </p>
      <!-- 비밀번호 -->
      <div>
        <input type="password" placeholder="비밀번호" v-model="userinfo.password" @keyup.enter="login(userinfo)">
      </div>
      <p class="invalidTxt" v-if="!passwordFormValid">
        숫자와 특수문자를 포함하여 8자 이상입니다.
      </p>
      <!-- 로그인 버튼 -->
      <div>
        <button class="finalBtn" :class="{'bg-freditgreen': allValid, 'disabledBtn': !allValid}" @click="login(userinfo)">로그인</button>
      </div>

      <!-- 로그인창 아래 정보 -->
      <div>
        <hr>
      </div>
      <div class="infoBelow">
        <p>
          <RouterLink :to="{ name: 'Signup' }" class="grey-link">회원가입 | </RouterLink>  
          <RouterLink :to="{ name: 'FindEmail' }" class="grey-link">아이디 찾기 | </RouterLink>  
          <RouterLink :to="{ name: 'FindPassword' }" class="grey-link">비밀번호 찾기</RouterLink>
        </p>
      </div>

    </section>
  </v-container>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
  components: {
    FlashModal,
  },
  data () {
    return {
      userinfo: {
        email: '',
        password: '',
      },
    }
  },
  methods: {
    ...mapActions ("user",["login",]),
 },
 computed: {
   ...mapGetters('user', ['modalOpen', 'modalContent',]),
   emailFormValid () {
    return !this.userinfo.email || /.+@.+\..+/.test(this.userinfo.email)
  },
  passwordFormValid () {
    return !this.userinfo.password || ((this.userinfo.password.length > 7) && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(this.userinfo.password))
  },
  allValid () {
    return this.emailFormValid && this.passwordFormValid && this.userinfo.email && this.userinfo.password
  }
 }
}
</script>

<style>

</style>