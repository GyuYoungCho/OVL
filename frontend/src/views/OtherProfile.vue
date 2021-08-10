<template>
<v-container>
    <section  class="profilepage">
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

                            <div> 
                                <button class="followBtn" @click="onClickEditUser()">Follow</button>
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
import {mapGetters} from "vuex"
import UserPosts from '@/components/profile/userPost.vue'
import UserRecipes from '@/components/profile/userRecipe.vue'
import UserChallenges from '@/components/profile/userChallenge.vue'
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment';
import axios from "axios";
import API from '@/api/index.js'
import followAPI from '@/api/follow.js'
import userAPI from '@/api/user.js'
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
        detailFollowUser: [],
        follower: '',
        following: '',
        Rank : null,

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
        //다른 유저 정보 가져오기
        axios({
            method: "get",
            url: API.url + userAPI.select(userid),
        }).then((res) => {
                console.log(" 다른 유저 정보 : ", res.data);
                this.otheruserinfo = res.data;
                this.start_date = this.otheruserinfo.challengeId.start_date;
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
    let amount = this.following.length;
    //팔로우한 사람이면 그냥 보여준다
    //console.log("보는 사람", this.userinfo.userid);
    for(var i = 0; i < amount; i++){
        console.log(amount);
        if(this.$route.params.userid == this.follower[i]){
            console.log("testfollowingdetailOther: ",this.follower[i])
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