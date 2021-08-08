<template>
<v-container>
    <section  class="profilepage">
            <div centered class="container mt-5 d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> <img src="@/assets/image/gyu.png" class="profile-img" width="100" style="border-radius: 50%;">
                    <div class="mb-0 mt-0">{{nickname}}</div><div style="font-size:x-small"> <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px"><div class="d-flex flex-column"> <span class="rank">rank</span> <span class="number1">{{rank}}</span> </div></div>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="following">following</span>
                                            <v-dialog
                                            v-if="isModalFollower"

                                                transition="dialog-bottom-transition"
                                                >
                                                <v-card>
                                                    <v-toolbar
                                                    color="#004627"
                                                    dark>
                                                    <v-toolbar-title>식당 검색</v-toolbar-title>
                                                    <v-spacer></v-spacer>
                                                    </v-toolbar>
                                                        <v-card-title>

                                                        </v-card-title>
                                                        
                                                        
                                                </v-card>
                                            </v-dialog>
                                        <button class="number2" @click="isModalFollowing=true">{{following}}</button>
                                    </div>
                                </div>
                                <div style="margin: 10px"><div class="d-flex flex-column">
                                    <span class="follower">followers</span>
                                    <ModalView v-if="isModalFollower" @close-modal="isModalFollower = false"> <Follower/> </ModalView>
                                    <span class="number3" @click="isModalFollower=true">{{follower}}</span> </div></div>
                            </div>

                            <div> 
                                <button class="bg-freditgreen" @click="onClickEditUser()">Edit Profile</button>
                            </div>
                            
                        </div>
                    </div>
                </div>
        </div>
    <div class="text-center">
        <v-tabs
            color="green darken-4"
            centered
            grow
            text-h2
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
import Follower from '@/components/profile/follower.vue'
import ModalView from '@/components/profile/ModalView.vue'
import moment from 'moment';
export default {
  components: { UserPosts, UserRecipes, UserChallenges, Follower, ModalView},

    data () {
        return{
        file: "",
		lists: [],
		path:'',
        isModalFollower: false,
        isModalFollowing: false,
        start_date: 0,
        }

        
    },
    computed:{
    ...mapGetters("user",(["userinfo","isLogin", "challenge"])),
    ...mapState("follow", (["followerList", "followingList"])),
    ...mapState("user", (["isLogin", "rank"])),
    time() {
        const start = moment(this.start_date);
        const now = moment(new Date());
        console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
        return now.diff(start, 'days') + 1;
    }

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
        
        this.start_date = this.userinfo.challengeId.start_date;
        
        
    },
    methods: {
        onClickEditUser(){
            this.$router.push({ name: "ModifyUser" });
        },
    },

}
</script>