<template>
<v-container>
    <!-- 프로필 사진 수정 완료 안내 모달 -->
    <ModifyModal :modalOpen="isModifyComplete" title="회원 정보 수정" modalContent="수정이 완료되었습니다" type=2 />
    <ModifyModal :modalOpen="isUserDelete" title="회원 탈퇴" modalContent="탈퇴가 정상적으로 처리되었습니다" type=2 />

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
import ModifyModal from '@/components/profile/ModifyModal.vue'

export default {
    components: {
        ModifyModal
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
            isUserDelete: false,
            modalContent: '',
            isModifyComplete: false
        }
    },
    created() {
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

            this.isModifyComplete = true;
                console.log("isModifyComplete : ", this.isModifyComplete)
                setTimeout(() => {
                    this.isModifyComplete = false
                }, 1000)

            axios.put(URL, payload).then(res => {
                console.log("회원정보 수정 결과 : ",res.data);

                this.$store.dispatch('user/getUpdateUserInfo', this.userinfo.userid);


                setTimeout(() => {
                    this.$router.push({ name: 'Profile', params: {userid: this.userinfo.userid} })
                }, 1000)
                
                })
                .catch(err => 
                console.error(err))
        },
        onClickDeleteUser(){
            this.$store.dispatch('user/deleteUser', this.userinfo.userid);
            
            this.isUserDelete = true
            setTimeout(() => {
                this.isUserDelete = false
                this.$router.push({name: 'Main'})
            }, 1000);
        },
    },
}
</script>

<style>

</style>