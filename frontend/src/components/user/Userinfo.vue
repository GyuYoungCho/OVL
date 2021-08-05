<template>
<div>
    <h1>User info</h1>
    <div>
        {{nickname}}
    </div>
              <v-btn class="mx-3" @click="onClickModify()">
            회원 정보 수정하기
          </v-btn>
</div>
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