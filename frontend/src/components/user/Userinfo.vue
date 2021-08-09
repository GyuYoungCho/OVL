<template>
<v-container>
    <section class="modifyuser">
        <img src="@/assets/image/OVL_logo.png" alt="">
    <!-- 이름 -->
    <div class="inputOnlyRead">
        {{name}}
    </div>
    <!-- 닉네임 -->
    <div class="inputBtnDiv">
        <input type="text" v-model="nickname">
        <button class="bg-freditgreen" @click="onClickNicknameValidate()">확인</button>
    </div>
    <!-- 이메일 -->
    <div class="inputOnlyRead">
        {{email}}
    </div>
    <!-- 전화번호 -->
    <div>
        <input type="text" v-model="phone">
    </div>
    <!-- 비밀번호 -->
    <div>
        <input type="password" v-model="password" placeholder="비밀번호 확인">
    </div>
    <p class="invalidTxt" v-if="!passwordFormValid">
        숫자와 특수문자를 포함하여 8자 이상으로 적어주세요.
    </p>
    <!-- 비밀번호 확인 -->
    <div>
        <input type="password" v-model="passwordCheck">
    </div>
    <p class="invalidTxt" v-if="!passwordCheckFormValid">
        비밀번호와 일치하지 않습니다.
    </p>
    <!-- 회원가입 버튼 -->
    <div>
        <button class="finalBtn" :class="{ 'bg-freditgreen': modifyFormValid, 'disabledBtn': !modifyFormValid }" :disabled="!modifyFormValid" @click="onModifyUserBtnClick">회원 정보 수정</button>
    </div>
    </section>
    </v-container>
</template>

<script>
import {mapGetters, mapState} from "vuex"
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
    data() {
        return {
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
        }
    },
    created() {
        this.$store.dispatch("user/getTokenUserInfo");

        this.userid = this.userinfo.userid;
        this.name = this.userinfo.name;
        this.nickname = this.userinfo.nickname;
        this.phone = this.userinfo.phone;
        this.password = this.userinfo.password;
        this.email = this.userinfo.email;
    },
    computed: {
        ...mapGetters("user", ["userinfo"]),
        ...mapState("user", ["isLogin"]),

        passwordFormValid () {
            return !this.password || ((this.password.length > 7) && /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{6,20}$/.test(this.password))
        },
        passwordCheckFormValid () {
            return !this.passwordCheck || (this.password===this.passwordCheck)
        },
        modifyFormValid () {
            const allExist = !!this.name && !!this.nickname && !!this.email && !!this.phone && !!this.password && !!this.passwordCheck
            const allValid = this.nicknameValid && this.emailValid  && this.emailFormValid && this.passwordFormValid && this.passwordCheckFormValid
            return allExist && allValid
        }
    },
    methods: {
        onClickModify(){
            this.$router.push({ name : "ModifyUser"})
        },
    onClickNicknameValidate () {
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
    },
}
</script>

<style>

</style>