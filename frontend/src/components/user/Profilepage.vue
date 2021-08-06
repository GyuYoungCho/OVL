<template>
<v-contaier>
    <div>
        <h1>test</h1>
    </div>  
</v-contaier>  
</template>

<script>
import {mapGetters, mapState} from "vuex"
export default {
    data () {
        return{
            file: "",
		lists: [],
		path:'',
        }

        
    },
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState('user', ["isLogin"]),

    },
    created() {
        this.$store.dispatch("user/getTokenUserInfo"),
        this.nickname = this.userinfo.nickname;
         this.userid = this.userinfo.userid;
    },


}
</script>