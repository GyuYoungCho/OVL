<template>
<v-container>
    <section  class="profilepage">
        <ChallengeConfirm :user="userinfo" :certdialog="certdialog" @openCertDialog="openCertDialog"/>
            <div centered class="container d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> 
                            <img :src="userinfo.filepath" class="profile-img" width="100" height="100" style="border-radius: 50%;">
                            <div class="mb-0 mt-0">{{nickname}}</div>
                            <div v-if="isNotChallenging" style="font-size:x-small">
                            <span class="ingdate">&nbsp;</span></div>
                            <div v-else style="font-size:x-small"
                            @click="openCertDialog(true)">
                            <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="rank">rank</span>
                                        <span v-if="this.step===1" class="number1" ><img src="@/assets/image/OVLKoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                        <span v-else-if="this.step===2" class="number1" ><img src="@/assets/image/OVLoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                        <span v-else-if="this.step===3" class="number1" ><img src="@/assets/image/OVLoong.png" alt="" width="30px"  @click="onClickRank"/></span>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px" class="justify-content-center" >
                                    <div class="d-flex flex-column"> 
                                        <span class="following">following</span>
                                        <div @click="openDialog(0)">
                                        <span class="number2" style="font-size:medium" width="40px">{{following}}</span>
                                        </div>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column">
                                    <span class="follower">follower</span>
                                        <div @click="openDialog(1)">
                                            <span class="number3" style="font-size:medium" width="40px" >{{follower}}</span>
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
                            <div>
                                <span v-if="this.step===1" class="number1" ><img src="@/assets/image/OVLKoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                <span v-else-if="this.step===2" class="number1" ><img src="@/assets/image/OVLoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                <span v-else-if="this.step===3" class="number1" ><img src="@/assets/image/OVLoong.png" alt="" width="30px"  @click="onClickRank"/></span>
                                <span > 쿵야가 아직 아기에요! 좀 더 많은 게시물을 올려보세요! 머리에 새싹이 자라날 거에요!</span>
                            </div>
                            <br/>
                            <div class="text-center">
                                <span style="color: #49784B; font-size: Large">"{{nickname}}" 님의 </span>
                                <div> Rank는 <span style="font-size: large; color:  #49784B;">"{{rank}}"</span> 등!! </div>
                                <div> Vegan Score 는 <span style="font-size: large; color:  #49784B;">"{{experience}}"</span>  점 입니다!</div>
                            </div>
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
                    <NoneChallenging v-if="isNotChallenging"/>
                    <UserChallenges v-else/>

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
import ChallengeConfirm from '@/components/user/ChallengeConfirm.vue'
import ProfileName from '@/components/basic/ProfileName.vue'
import NoneChallenging from '@/components/profile/NoneChallenging.vue'
import moment from 'moment'
import axios from 'axios'
import API from '@/api/index.js'
import userAPI from '@/api/user.js'

export default {
components: { UserPosts, UserRecipes, UserChallenges, ChallengeConfirm, ProfileName, NoneChallenging},

    data () {
        
        return{
            file: "",
            lists: [],
            path:'',
            isModalFollower: false,
            isModalFollowing: false,
            start_date: 0,
            dialog: false,
            certdialog: false,
            rankOpen: false,
            isNotChallenging: false,
            experience:'',
            step:'',
        }
    },
    computed:{
        ...mapGetters("user",(["userinfo","isLogin", "challenge"])),
        ...mapState("follow", (["followerList", "followingList", "detailFollowUser"])),
        ...mapState("user", (["isLogin", "rank", "percent"])),
        time() {
            const start = moment(this.start_date);
            const now = moment(new Date());
            // console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
            return now.diff(start, 'days') + 1;
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

        //쿵야 셋팅
        console.log(this.percent)
        if(this.percent < 31 ){
            this.step = 1;
        }else if( this.percent > 30 && this.percent < 61){
            this.step = 2;
        }else{
            this.step = 3;
        }
    },

    methods: {
        onClickEditUser(){
            this.$router.push({ name: "ModifyPic" });
        },
        //랭크 클릭시 보이는 모달
        onClickRank() {
            
            axios({
                method: "get",
                url: API.url + userAPI.select(this.userinfo.userid)
            }).then((res)=>{
                if(res.data !== null){
                    console.log("유저 경험점수" , res.data)
                    this.experience = res.data.experience;
                }
            }).catch((err)=>{
                console.log(err)
            })

            this.rankOpen = true;
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
        openCertDialog(val){
            if(this.userinfo.challengeId.challengeId!=1)
                this.certdialog = val
        }
    },

}
</script>