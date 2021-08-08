<template>
<v-container>
    <section class="modifyuser">
    <div class="logo">
        <img src="@/assets/logo.png" class="logo-img"><br>
    </div>
    <!-- 이름 -->
    <div class="inputOnlyRead">
        {{name}}
    </div>
    <!-- 닉네임 -->
    <div class="inputBtnDiv">
        <input type="text" v-model="nickname">
        <button class="bg-freditgreen" @click="onNicknameBtnClick">중복 확인</button>
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
        <input type="password" v-model="password">
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
        <button class="finalBtn" :class="{ 'bg-freditgreen': signupFormValid, 'disabledBtn': !signupFormValid }" :disabled="!signupFormValid" @click="onSignupBtnClick">회원 정보 수정</button>
    </div>
    </section>
    </v-container>
</template>

<script>
import {mapGetters, mapState} from "vuex"

export default {
    data() {
        return {
            userid:"",
            name:"",
			nickname: "",
			phone: "",
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
    },
    methods: {
        onClickModify(){
            this.$router.push({ name : "ModifyUser"})
        },

    },
}
</script>

<style>

</style>