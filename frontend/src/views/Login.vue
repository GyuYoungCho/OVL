<template>
  <section>
    <div class="login-form">
        <h1>LOG IN</h1>
        <div class="txtb">
          <input v-model="credentials.username" :class="{'focus':isUsernameFocus}" type="text" @focus="usernameFocus" @blur="usernameBlur">
          <span data-placeholder="Username"></span>
        </div>

        <div class="txtb">
          <input v-model="credentials.password" :class="{'focus':isPasswordFocus}" type="password" @focus="passwordFocus"  @blur="passwordBlur">
          <span data-placeholder="Password"></span>
        </div>
        <button class="logbtn" @click="login">로그인</button>
        <div class="bottom-text">
          아이디가 없으신가요?
        <RouterLink :to="{ name: 'Signup' }">
          회원가입
        </RouterLink>
        </div>
      </div>
  </section>
</template>

<script>
// import axios from 'axios'

export default {
  data () {
    return {
      credentials: {
        username: '',
        password: '',
      },
      isUsernameFocus : false,
      isPasswordFocus : false,
    }
  },
  methods: {
    usernameFocus () {
      if (this.credentials.username.length !== 0) {
        this.isUsernameFocus = true
      } else {
        this.isUsernameFocus = !this.isUsernameFocus 
      }
    },
    passwordFocus () {
      if (this.credentials.password.length !== 0) {
        this.isPasswordFocus = true
      } else {
        this.isPasswordFocus = !this.isPasswordFocus 
      }
    },
    usernameBlur () {
      if (this.credentials.username.length === 0) {
        this.isUsernameFocus = !this.isUsernameFocus
      } else {
        this.isUsernameFocus = true
      }
    },
    passwordBlur () {
      if (this.credentials.password.length === 0) {
        this.isPasswordFocus = !this.isPasswordFocus
      } else {
        this.isPasswordFocus = true
      }
    },
    login () {
    //   axios({
    //     method: 'post',
    //     url: '',
    //     data: this.credentials,
    //   })
    //     .then(res => {
    //       localStorage.setItem('jwt', res.data.token)
    //       this.$router.push('/')
    //       this.$store.state.isLoggedIn = true
    //     })
    //     .catch(err => console.log(err))
    // }
  }
 }
}
</script>

<style scoped>
  *{
    margin: 0;
    padding: 0;
    text-decoration: none;
    
    box-sizing: border-box;
  }

  section {
    height: calc(100vh - 48px);
    background-image: linear-gradient(120deg,#4CAF50,#004627);
  }

  .login-form{
    width: 360px;
    background: #f1f1f1;
    height: 580px;
    padding: 80px 40px;
    border-radius: 10px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
  }

  .login-form h1{
    text-align: center;
    margin-bottom: 60px;
  }

  .txtb{
    position: relative;
    margin: 30px 0;
  }

  .txtb input{
    font-size: 15px;
    color: #333;
    border-top: none;
    border-left: none;
    border-right: none;
    border-bottom: 2px solid #adadad;
    width: 100%;
    outline: none;
    background: none;
    padding: 0 5px;
    height: 40px;
  }

  .txtb span::before{
    content: attr(data-placeholder);
    position: absolute;
    top: 50%;
    left: 5px;
    color: #adadad;
    transform: translateY(-50%);
    z-index: -1;
    transition: .5s;
  }

  .txtb span::after{
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    width: 0%;
    height: 2px;
    background: linear-gradient(120deg,#4CAF50,#004627);
    transition: .5s;
  }

  .focus + span::before{
    top: -5px;
  }
  .focus + span::after{
    width: 100%;
  }

  .logbtn{
    display: block;
    width: 100%;
    height: 50px;
    border: none;
    background: linear-gradient(120deg,#4CAF50,#004627,#4CAF50);
    background-size: 200%;
    color: #fff;
    outline: none;
    cursor: pointer;
    transition: .5s;
  }

  .logbtn:hover{
    background-position: right;
  }

  .bottom-text{
    margin-top: 60px;
    text-align: center;
    font-size: 13px;
  }


</style>