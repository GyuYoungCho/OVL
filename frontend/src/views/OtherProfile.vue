<template>
<v-container>
    <section class="profilepage">
            <div centered class="container mt-5 d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> <img src="@/assets/image/gyu.png" class="profile-img" width="100" style="border-radius: 50%;">
                    <div class="mb-0 mt-0">{{this.otheruserinfo.nickname}}</div><div style="font-size:x-small"> <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px"><div class="d-flex flex-column"> <span class="rank">rank</span> <span class="number1">{{Rank}}</span> </div></div>
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
                            <div class="d-flex justify-content-center">
                            <div v-if="this.isFollowing"> 
                                <button class="unfollowBtn" @click="onClickUnFollowBtn()">unFollow</button>
                            </div>
                            <div v-else> 
                                <button class="followBtn" @click="onClickFollowBtn()">Follow</button>
                            </div>
                            <div>
                                <button class="reportBtn" @click="onClickReport()">신고</button>
                            </div>
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
                        <v-btn icon @click="closeDialog()"> <!-- closeDialog 클릭 이벤트 -->
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
                    <LockImg v-if="this.isLocked"/>
                    <UserPosts v-else/>
                </v-tab-item>
            <!-- 레시피  -->
                <v-tab-item>
                    <LockImg v-if="this.isLocked"/>
                    <UserRecipes v-else/>
                </v-tab-item>
            <!-- 챌린지  -->
                <v-tab-item>
                <LockImg v-if="this.isLocked"/>
                <UserChallenges v-else/>
                </v-tab-item>
        </v-tabs>
    </div> 
    </section>
</v-container>  
</template>

<script>
import {mapGetters} from "vuex"
import UserPosts from '@/components/profile/userPost.vue'
import LockImg from '@/components/profile/Lockuser.vue'
import UserRecipes from '@/components/profile/userRecipe.vue'
import UserChallenges from '@/components/profile/userChallenge.vue'
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment';
import axios from "axios";
import API from '@/api/index.js'
import followAPI from '@/api/follow.js'
import userAPI from '@/api/user.js'
export default {
components: { UserPosts, UserRecipes, UserChallenges, ProfileName, LockImg},

    data () {
        
        return{
        file: "",
		lists: [],
		path:'',
        isModalFollower: false,
        isModalFollowing: false,
        start_date: 0,
        dialog: false,
        otheruserinfo: {
            userid: "",
            email: "",
            password: "",
            name: "",
            nickname: "",
            phone: "",
            stored_file_path: "",
            challengeId: {
			start_date: null,
            },
        },
        followingList:[],
        followerList: [],
        UserfollowingList: [],
        detailFollowUser: [],
        follower: '',
        following: '',
        Rank : null,
        isLocked: false,
        isFollowing: false,
        }
    },
    computed :{
    ...mapGetters("user",(["userinfo","isLogin"])),
    time() {
        const start = moment(this.start_date);
        const now = moment(new Date());
        // console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
        return now.diff(start, 'days') + 1;
    }

    },
    created() {

    },
    methods: {
        onClickEditUser(){
            this.$router.push({ name: "ModifyUser" });
        },
        onClickUnFollowBtn(){
            axios({
                method: "delete",
                url: API.url + followAPI.unfollow(this.userinfo.userid, this.$route.params.userid),
            }).then((res)=>{
                if(res.data == "success"){
                    console.log("Unfollow")
                    this.isFollowing = false;
                    this.$store.dispatch("user/getUpdateUserInfo", this.userinfo.userid);
                }
            }).catch((err)=>{
                console.log(err)
            })
        },
        onClickFollowBtn(){
            axios({
                method: "post",
                url: API.url + followAPI.follow(this.userinfo.userid, this.$route.params.userid),
            }).then((res)=>{
                if(res.data == "success"){
                    console.log("follow")
                    this.isFollowing = true;
                    this.$store.dispatch("user/getUpdateUserInfo", this.userinfo.userid);
                    
                }
            }).catch((err)=>{
                console.log(err)
            })
        },
        openDialog(num) { //Dialog 열리는 동작
        if(num == 0){
                axios({
                    method: "post",
                    url: API.url + followAPI.select_detail(),
                    data: this.followingList,
                }).then((res) => {
                    console.log("Detail: " + res.data);
                    this.detailFollowUser = res.data;
                }).catch((err) => {
                    console.log("실패");
                    console.log(err);
                })
        }else{
                axios({
                    method: "post",
                    url: API.url + followAPI.select_detail(),
                    data: this.followerList,
                }).then((res) => {
                    console.log("Detail: " + res.data);
                    this.detailFollowUser = res.data;
                }).catch((err) => {
                    console.log("실패");
                    console.log(err);
                })
        }
        this.dialog = true;

        },
        closeDialog() { //Dialog 닫히는 동작
            this.dialog = false;
        },
    },
mounted(){
            //url userid 체크
        let userid = this.$route.params.userid;
        let isOpened = 0;
        //다른 유저 정보 가져오기
        axios({
            method: "get",
            url: API.url + userAPI.select(userid),
        }).then((res) => {
                console.log(" 다른 유저 정보 : ", res.data);
                this.otheruserinfo = res.data;
                this.start_date = this.otheruserinfo.challengeId.start_date;
                this.isOpened = this.otheruserinfo.account_open;
                axios({
                        method: "get",
                        url: API.url + followAPI.select_following(this.userinfo.userid),
                    }).then((res) => {
                        //팔로잉 중 - unfollow 버튼 활성화, 무조건 띄움
                        if(res.data != null){
                        this.UserfollowingList = res.data;
                        let mount = this.UserfollowingList.length;
                            for(var i = 0; i< mount; i++){
                                
                                if(this.UserfollowingList[i] == this.$route.params.userid){
                                    this.isFollowing = true;

                                    console.log("팔로잉중 볼 수 있어", this.UserfollowingList)
                                    break;
                                }
                            }
                            if(!this.isFollowing){
                                this.isFollowing = false;
                                    if(this.isOpened == 0){
                                        this.isLocked = false;
                                    }
                                    else{
                                        this.isLocked = true;
                                    }
                            }
                        }
                        else{
                            console.log("팔로우 해야 합니다.")
                            if(isOpened == 0){
                                this.isLocked = false;
                            }
                            else{
                                this.isLocked = true;
                            }
                        }
                    }).catch((err) => {
                        console.log("실패");
                        console.log(err);
                    })
               // console.log("otherprofilepage : ", this.otheruserinfo)
            })
            .catch((err) => {
            console.log("ohter User 정보 조회 실패"); 
            console.log(err);
            }),
        
        //팔로워
            axios({
                method: "get",
                url: API.url + followAPI.select_follower(userid),
            }).then((res) => {
                this.followerList = res.data;
                this.follower = this.followerList.length;
            }).catch((err) => {
                console.log("실패");
                console.log(err);
            }),
            //팔로잉
            axios({
                method: "get",
                url: API.url + followAPI.select_following(userid),
            }).then((res) => {
                this.followingList = res.data;
                
                this.following = this.followingList.length;
            }).catch((err) => {
                console.log("실패");
                console.log(err);
            }),
            //랭크 
            axios({
            method: "get",
            url: API.url + userAPI.rank(userid),
            }).then((res) => {
            if(res){
                this.Rank = res.data.rank
            }else{
                console.log("랭크 가져오기 실패")
            }
        }).catch((err) => {
            console.log(err)
        })

            //console.log(new Date(this.userinfo.challengeId.start_date))
}
}
</script>