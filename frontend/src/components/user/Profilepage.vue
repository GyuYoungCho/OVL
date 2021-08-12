<template>
<v-container>
    <section  class="profilepage">
            <div centered class="container d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> 
                            <img :src="userinfo.filepath" class="profile-img" width="100" height="100" style="border-radius: 50%;">
                            <div class="mb-0 mt-0">{{nickname}}</div>
                            <div v-if="isNotChallenging" style="font-size:x-small">
                            <span class="ingdate">&nbsp;</span></div>
                            <div v-else style="font-size:x-small">
                            <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 20px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="rank">rank</span>
                                        <span class="number1" style="font-size:medium" @click="rankOpen = true">{{rank}}</span>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="following">following</span>
                                        <div @click="openDialog(0)">
                                        <span class="number2" style="font-size:medium" >{{following}}</span>
                                        </div>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column">
                                    <span class="follower">follower</span>
                                        <div @click="openDialog(1)">
                                            <span class="number3" style="font-size:medium">{{follower}}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div> 
                                <button class="bg-freditgreen" @click="onClickEditUser()">Edit Profile</button>
                            </div>
                            
                        </div>
                    </div>
                </div>
                <!-- 커스텀 모달 -->
                <v-dialog v-model="rankOpen" max-width="300" @click:outside="rankOpen = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#49784B">
                    <v-toolbar-title class="modalTitle">Challenge</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="rankOpen = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-toolbar>
                    <!-- 모달 컨텐츠 영역 -->
                    <v-container>
                    <div class="modalContent">
                    <div class="mb-3">
                        <span class="modalContentMessage">
                            랭크
                        </span>
                    </div>
                    <div class="modalContentButtonArea">
                        <button class="modalContentButton" @click="rankOpen = false">확인</button>
                    </div>
                    </div>
                    </v-container>
                </v-card>
                </v-dialog>
                                <!-- 커스텀 모달 -->
                <v-dialog v-model="dialog" max-width="300" @click:outside="dialog = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#49784B">
                    <v-toolbar-title class="modalTitle">Challenge</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="dialog = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-toolbar>
                    <!-- 모달 컨텐츠 영역 -->
                    <v-container>
                    <div class="modalContent">
                    <div class="mb-3">
                        <span class="modalContentMessage">
                            <!-- 업로드 컴포넌트 -->
                            <ProfileName
                                v-for="(auser, index) in detailFollowUser" :key="index" :user="auser">
                            </ProfileName>

                        </span>
                    </div>
                    <div class="modalContentButtonArea">
                        <button class="modalContentButton" @click="dialog = false">확인</button>
                    </div>
                    </div>
                    </v-container>
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
import API from '@/api/index.js'

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
        rankOpen: false,
        isNotChallenging: false,
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
    },
        userPath() { // 프로필 사진 이미지 출력
            return API.url + "/profile/" + this.userinfo.userid + "/"+ this.userinfo.stored_file_path.split('/').reverse()[0]
        },

    },
    created() {
        //url userid 체크
        let userid = this.$route.params.userid;
        //this.$store.dispatch('user/getUpdateUserInfo', userid);
        console.log("profilepage : ", this.userinfo.nickname);
        this.nickname = this.userinfo.nickname;
        this.$store.dispatch("user/getUserRank", userid);
        this.$store.dispatch("follow/getFollowingList", userid);
        this.$store.dispatch("follow/getFollowerList", userid);

        //console.log(this.followerList)
        this.follower = this.followerList.length;
        this.following = this.followingList.length;
        //00일째 챌린지 계산
        if(this.userinfo.challengeId.challengeId ===1){
            this.isNotChallenging = true;
        }else{
            this.isNotChallenging = false;
        }
    
        this.start_date = this.userinfo.challengeId.start_date;


    },
    methods: {
        onClickEditUser(){
            this.$router.push({ name: "ModifyPic" });
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