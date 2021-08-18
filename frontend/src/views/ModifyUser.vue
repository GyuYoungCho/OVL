<template>
<v-container>
    <FlashModal :modalOpen="modalOpen" :modalContent="modalContent" />

        <div style="text-align:center">
            <img src="@/assets/image/OVL_logo.png" alt="">
        </div>
        
    <div class="text-right">
        <v-row justify="end">
            <span class="mr-3 mt-4">계정 
                <span v-if="account_open">비공개</span>
                <span v-else>공개</span>
            </span>
            <v-switch class="py-0 " v-model="account_open" color="success"></v-switch>
        </v-row>
    </div>
    
    <section class="modifyuser">
    <!-- 이름 -->
    <div class="inputOnlyRead">
        {{name}}
    </div>
    <!-- 닉네임 -->
    <div class="inputBtnDiv">
        <input type="text" v-model="nickname">
        <button class="bg-freditgreen" @click="onClickNicknameValidate()" style="width:60px">확인</button>
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
        <button class="modifyBtn" @click="onModifyUserBtnClick">회원 정보 수정</button>
        <button class="deleteBtn" @click="onClickDeleteUser">회원 탈퇴</button>
    </div>
    </section>
    </v-container>
</template>

<script>
import {mapGetters, mapState} from "vuex"
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
    components: {
        FlashModal,
    },
    data() {
        return {
            nicknameValid: false,
            name: this.name,
            nickname: '',
            email: this.email,
            phone: '',
            password: '',
            passwordCheck: '',
            account_open: '',
            modalOpen: false,
            modalContent: '',
        }
    },
    created() {
        console.log("회원정보 수정한다!! : ",this.userinfo.nickname);
        this.userid = this.userinfo.userid;
        this.name = this.userinfo.name;
        this.nickname = this.userinfo.nickname;
        this.phone = this.userinfo.phone;
        this.email = this.userinfo.email;
        this.account_open = this.userinfo.account_open
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
        accountopen(){
            return this.userinfo.account_open == 1
        }
    },
    methods: {
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
            console.log("회원정보수정 버튼 클릭!");
            const URL = API.url + userAPI.modify_user()
            // const formData = new FormData()

            // formData.append('phone', this.phone)
            // formData.append('nickname', this.nickname)
            // formData.append('password', this.password)
            // formData.append const { email, name, nickname, password, phone } = this
            //console.log("챌린지 정보:" , this.userinfo.challengeId)
            let send_account = 0;
            //비공개 한다.
            if(this.account_open){
                send_account = 1;
            }else if(this.account_open === 1){
                send_account = 1;
            }
            //아닐 경우 공개 한다.
            console.log("계정 비공개.공개 정보:", typeof this.account_open)
            console.log("계정 비공개.공개 정보2:", typeof send_account)
            
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
                "email": "string",
                "challengeId": this.userinfo.challengeId,
                "account_open" : send_account,
            }

            axios.put(URL, payload).then(res => {
                console.log("회원정보 수정 결과 : ",res.data);

                this.$store.dispatch('user/getUpdateUserInfo', this.userinfo.userid);
                this.$router.push({ name: 'Profile', params: {userid: this.userinfo.userid} })
                })
                .catch(err => 
                console.error(err))
        },
        onClickDeleteUser(){
            this.$store.dispatch('user/deleteUser', this.userinfo.userid);

            this.modalContent = "탈퇴가 정상적으로 처리 되었습니다."
            this.modalOpen = true
            setTimeout(() => {
            this.modalOpen = false
            //this.logout()
            }, 1000);
            this.$router.push({name: 'Main'})
        },
        // onModifyAccountOpenToggle(open) {
        //     // axios.put(API.url + userAPI.lock(this.userinfo.userid,val))
        //     // .then(res => {
        //     //     console.log(res.data)
        //     //     })
        //     // .catch(err => 
        //     //     console.error(err)
        //     // )
        //     console.log("계정 여부", open)
            
        // }
    },
    // watch:{
    //     account_open(val){
    //        // let open = val? 0 : 1
    //         //this.onModifyAccountOpenToggle(open)

    //         console.log("계정 여부",val)
    //         console.log("account-open", this.account_open)
    //     }
    // }
}
</script>

<style>

</style>