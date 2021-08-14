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
      <!-- 비밀번호 -->
      <div>
        <input type="password" placeholder="비밀번호" v-model="userinfo.password" @keyup.enter="login(userinfo)">
      </div>
      <!-- 로그인 버튼 -->
      <div>
        <button class="bg-freditgreen finalBtn" @click="login(userinfo)">로그인</button>
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
   ...mapGetters('user', ['modalOpen', 'modalContent',])
 }
}
</script>

<style>

</style>