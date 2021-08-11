<template>
<v-container>
    <section  class="profilepage">
            <div centered class="container mt-5 d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> <img :src="userinfo.filepath" class="profile-img" width="100" style="border-radius: 50%;">
                    <div class="mb-0 mt-0">{{nickname}}</div><div style="font-size:x-small"> <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px"><div class="d-flex flex-column"> <span class="rank">rank</span> <span class="number1">{{rank}}</span> </div></div>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="following">following</span>
                                        <div @click="openDialog(0)">
                                        <span class="number2">{{following}}</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="margin: 10px"><div class="d-flex flex-column">
                                    <span class="follower">followers</span>
                                    <div @click="openDialog(1)">
                                    <span class="number3">{{follower}}</span>
                                    </div>
                                </div></div>
                            </div>

                            <div> 
                                <button class="bg-freditgreen" @click="onClickEditUser()">Edit Profile</button>
                            </div>
                            
                        </div>
                    </div>
                </div>
                <v-dialog ref="modal1" v-model="dialog" persistent max-width="900px">
                    <v-card>
                        <v-card-title>
                        <template>
                            <v-icon style="margin-right:10px;" large color="#41B883" >List</v-icon> 
                            <span class="headline" large>팔로잉 목록</span>
                        </template>
                        <v-spacer></v-spacer>
                        <v-btn icon @click="closeDialog('modal1')"> <!-- closeDialog 클릭 이벤트 -->
                            <v-icon>x</v-icon>
                        </v-btn>
                        </v-card-title>
                        <v-card-text>
                        <v-row>
                            <v-col cols="12" sm="12" md="12" style="position: relative; border:1px solid #41B883; border-style:dashed; ">
                            <!-- 업로드 컴포넌트 -->
                            <ProfileName
                                v-for="(auser, index) in detailFollowUser" :key="index" :user="auser">
                            </ProfileName>
                            </v-col>
                        </v-row>
                        </v-card-text>
                    </v-card>
                </v-dialog>
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
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment';

export default {
components: { UserPosts, UserRecipes, UserChallenges, ProfileName},

    data () {
        
        return{
        file: "",
		lists: [],
		path:'',
        isModalFollower: false,
        isModalFollowing: false,
        start_date: 0,
        dialog: false,
        }
    },
    computed:{
    ...mapGetters("user",(["userinfo","isLogin", "challenge"])),
    ...mapState("follow", (["followerList", "followingList", "detailFollowUser"])),
    ...mapState("user", (["isLogin", "rank"])),
    time() {
        const start = moment(this.start_date);
        const now = moment(new Date());
        // console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
        return now.diff(start, 'days') + 1;
    }

    },
    created() {
        //url userid 체크
        console.log("profilepage : ", this.userinfo.nickname);
        this.nickname = this.userinfo.nickname;
        this.userid = this.userinfo.userid;
        this.$store.dispatch("user/getUserRank", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowingList", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowerList", this.userinfo.userid);

        //console.log(this.followerList)
        this.follower = this.followerList.length;
        this.following = this.followingList.length;
        //00일째 챌린지 계산
        this.start_date = this.userinfo.challengeId.start_date;
    },
    methods: {
        onClickEditUser(){
            this.$router.push({ name: "ModifyUser" });
        },
    openDialog(num) { //Dialog 열리는 동작
    if(num == 0){
        this.$store.dispatch("follow/getDetailFollowUser", this.followingList);
    }else{
        this.$store.dispatch("follow/getDetailFollowUser", this.followerList);
    }
    this.dialog = true;

    },
    closeDialog() { //Dialog 닫히는 동작
      this.dialog = false;
    },
    },

}
</script>