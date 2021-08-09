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
        <input type="password" v-model="password" style="font-size:small" placeholder="비밀번호 변경 시 입력, 아닐 경우 빈칸으로 두셔도 됩니다.">
    </div>
    <p class="invalidTxt" v-if="!passwordFormValid">
        숫자와 특수문자를 포함하여 8자 이상으로 적어주세요.
    </p>
    <!-- 비밀번호 확인 -->
    <div>
        <input type="password" v-model="passwordCheck" style="font-size:small" placeholder="비밀번호 확인">
    </div>
    <p class="invalidTxt" v-if="!passwordCheckFormValid">
        비밀번호와 일치하지 않습니다.
    </p>
    <!-- 회원가입 버튼 -->
    <div>
        <button class="finalBtn" @click="onModifyUserBtnClick">회원 정보 수정</button>
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
            name: this.name,
            nickname: '',
            email: this.email,
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
        // modifyFormValid () {
        //     const allExist = !!this.nickname && !!this.phone
        //     const allValid = this.nicknameValid
        //     return allExist && allValid
        // }
    },
    methods: {
        // onClickModify(){
        //     this.$router.push({ name : "ModifyUser"})
        // },
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
        onModifyUserBtnClick () {
            const URL = API.url + userAPI.modify_user()
            // const formData = new FormData()

            // formData.append('phone', this.phone)
            // formData.append('nickname', this.nickname)
            // formData.append('password', this.password)
            // formData.append const { email, name, nickname, password, phone } = this
            let pw = '';
            if(this.password.length > 0){
                pw = this.password;
            }
            var payload = {
                "userid": this.userinfo.userid,
                "nickname": this.nickname,
                "password": pw,
                "phone": this.phone,
                "name": '',
                "account_open": 0,
                "challenge_id": 0,
                "email": "string",
                "experience": 0,
                "warning": 0
            }

            axios.put(URL, payload).then(res => {
                console.log(res)
                this.$router.push({ name: 'Login' })
                })
                .catch(err => 
                console.error(err))
        },
    },
}
</script>

<style>

</style>