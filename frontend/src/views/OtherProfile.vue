<template>
<div>
        <v-container>
        <!-- 다른 사람 profile -->
        <ProfilePage v-if="this.isFollowing"/>
        <Joinus v-else/>
        </v-container>
        </div>
</template>

<script>
import {mapGetters, mapState} from "vuex"
import ProfilePage from '@/components/user/Profilepage.vue'
import Joinus from '@/components/JoinUs.vue'
export default {
    data() {
        return {
        isFollowing: false,
        follower: [],
        following: [],
        }
    },
    computed:{
    ...mapGetters('user',["userinfo","isLogin"]),
    ...mapState("follow", (["followerList", "followingList"])),
    ...mapState('user', ["isLogin"]),
    },
    components: {
        ProfilePage,Joinus,
    },
    created(){
    //userid 검증
    //console.log(this.$route.params.userid)
    this.$store.dispatch("follow/getFollowingList", this.userinfo.userid);
    this.$store.dispatch("follow/getFollowerList", this.userinfo.userid);
        this.follower = this.followerList;
        this.following = this.followingList;

    
    //토큰 검증
    this.$store.dispatch("user/getTokenUserInfo");

},
mounted(){
    //console.log(new Date(this.userinfo.challengeId.start_date))
    let amount = this.following.length;
    //팔로우한 사람이면 그냥 보여준다
    for(var i = 0; i < amount; i++){
        if(this.$route.params.userid == this.following[i].userid){
            this.isFollowing = true;
            break;
        }
        else {
            this.isFollowing = false;
        }
    }

}


}
</script>