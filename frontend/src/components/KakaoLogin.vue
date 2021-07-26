<template>
  <div>
    <h1>Kakao Login</h1>
    <img src="../assets/image/kakao_login_medium_narrow.png" alt="" class="clickable" @click="onKakaoBtnClick">
  </div>
</template>


<script>
// 카카오 로그아웃 함수는 Kakao.Auth.logout(cb: optional)
// 토큰을 만료시키는 함수이고 인자로 들어온 콜백 함수가 실행됨, 로그아웃 직접 구현해야 함

export default {
  methods: {
    onKakaoBtnClick () {
      Kakao.Auth.login({
        success: (auth) => {
          console.log('로그인 성공', auth)
          Kakao.API.request({
            url: "/v2/user/me",
            success: (res) => {
              console.log(res)
            },
            fail: (err) => console.log(err),
          })
        },
        fail: (err) => {
          console.error(err)
        }
      })
    }
  },
  mounted () {
    // SDK를 초기화 합니다. 사용할 앱의 JavaScript 키를 설정해 주세요.
    const JAVASCRIPT_KEY = '6f9b75625d6cfdbf5d9f212fec6cba02'
    Kakao.init(JAVASCRIPT_KEY)
  }
}
</script>

<style>
 .clickable {
   cursor: pointer;
 }
</style>