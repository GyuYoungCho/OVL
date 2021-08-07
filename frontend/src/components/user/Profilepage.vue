<template>
<v-container>
    <section  class="profilepage">
        <h1>프로필 부분</h1>
            <div centered class="container mt-5 d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> <img src="@/assets/image/default.jpg" class="profile-img" width="100" style="border-radius: 50%;">
                        <h4 class="mb-0 mt-0">{{nickname}}</h4> <span>Senior Journalist</span>
                        </div>
                        <div class="ml-3 w-100">
                            <div class="p-2 mt-2 d-flex justify-content-center profile-img text-white stats">
                                <div class="d-flex flex-column"> <span class="articles">rank</span> <span class="number1">{{rank}}</span> </div>
                                <div class="d-flex flex-column"> <span class="followers">following</span> <span class="number2">{{following}}</span> </div>
                                <div class="d-flex flex-column"> <span class="rating">followers</span> <span class="number3">{{follower}}</span> </div>
                            </div>

                            <div> 
                                <button class="bg-freditgreen">Edit Profile</button>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    <div class="text-center">
        <h1>post 부분</h1>
        <v-tabs
            color="green darken-4"
            centered
            grow
            font-weight-bold text-h2
        >
            <v-tab>게시글</v-tab>
            <v-tab>레시피</v-tab>
            <v-tab>챌린지</v-tab>
            <!-- 탭 내용 -->
            <!-- 게시글  -->
                <v-tab-item>
                    <UserPosts/>
                </v-tab-item>
            <!-- 레시피  -->
                <v-tab-item>
                    <UserRecipes/>
                    <h1>test1</h1>
                </v-tab-item>
            <!-- 챌린지  -->
                <v-tab-item>
                        <UserChallenges/>
                </v-tab-item>
        </v-tabs>
    </div> 
    </section>
</v-container>  
</template>

<script>
import {mapGetters, mapState} from "vuex"
import UserPosts from '@/components/profile/userPost.vue'
import UserRecipes from '@/components/profile/userRecipe.vue'
import UserChallenges from '@/components/profile/userChallenge.vue'

export default {
  components: { UserPosts, UserRecipes, UserChallenges},
    data () {
        return{
        file: "",
		lists: [],
		path:'',
        }

        
    },
    computed:{
    ...mapGetters("user",(["userinfo","isLogin"])),
    ...mapState("follow", (["followerList", "followingList"])),
    ...mapState("user", (["isLogin", "rank"])),

    },
    created() {
        this.$store.dispatch("user/getTokenUserInfo"),
        this.nickname = this.userinfo.nickname;
        this.userid = this.userinfo.userid;
        this.$store.dispatch("user/getUserRank", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowingList", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowerList", this.userinfo.userid);
        this.follower = this.followerList.length;
        this.following = this.followingList.length;
    },


}
</script>