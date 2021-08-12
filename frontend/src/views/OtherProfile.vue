<template>
<v-container>
    <section class="profilepage">
            <div centered class="container d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center">
                            <img :src="otheruserinfo.filepath" class="profile-img" width="100" height="100" style="border-radius: 50%;">
                        <div class="mb-0 mt-0">{{this.otheruserinfo.nickname}}</div>
                            <div v-if="isNotChallenging" style="font-size:x-small">
                                <span class="ingdate">&nbsp;</span>
                            </div>
                            <div v-else style="font-size:x-small"> 
                                <span class="ingdate">{{time}} </span> 일째 챌린지 중
                            </div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 20px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="rank">rank</span> 
                                        <span class="number1" style="font-size:medium;" @click="rankOpen = true">{{Rank}}</span> 
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="following">following</span>
                                        <div @click="openDialog(0)">
                                        <span class="number2" style="font-size:medium">{{following}}</span>
                                        </div>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 10px"><div class="d-flex flex-column">
                                    <span class="follower">follower</span>
                                    <div @click="openDialog(1)">
                                    <span class="number3" style="font-size:medium">{{follower}}</span>
                                    </div>
                                </div></div>
                            </div>
                            <div class="d-flex justify-content-center">
                                <div v-if="this.isFollowing" width=""> 
                                    <button class="unfollowBtn" @click="onClickUnFollowBtn()">unFollow</button>
                                </div>
                                <div v-else-if="!this.isFollowing"> 
                                    <button class="followBtn" @click="onClickFollowBtn()">Follow</button>
                                </div>
                                <!--<div v-else-if="this.requestforfollow" style="backgroung-color: #9A2EFE"> 
                                    <button class="requestFollow" @click="onClickRequestBtn()">Follow</button>
                                </div>-->
                                <div v-if="!isReported" class="ms-1">
                                    <button class="reportBtn" @click="onClickReport()">신고</button>
                                </div>
                                <div v-else class="ms-1 unreport">
                                    <button class="reportBtn" @click="onClickNotReport()" style="font-size:x-small">신고취소</button>
                                </div>
                            </div>
                            
                        </div>
                    </div>
                </div>
                <!-- 커스텀 모달  for 팔로잉 팔로워 목록-->
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
                                v-for="(auser, index) in detailFollowUser" :key="index" cols="12" class="grid-cell" :user="auser">
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
                <!-- 커스텀 모달  for 랭크 확인-->
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
                <!-- 커스텀 모달 for 신고버튼-->
                <v-dialog v-model="isReporting" max-width="300" @click:outside="isReporting = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#CF5555">
                    <v-toolbar-title class="modalTitle">Report! </v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="isReporting = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-toolbar>
                    <!-- 모달 컨텐츠 영역 -->
                    <v-container>
                    <div class="modalContent">
                    <div class="mb-3">
                        <span class="modalContentMessage">
                            신고사유를 적어주세요.
                            <textarea name="" id="" cols="30" rows="10" placeholder="신고사유." class="ingredient-text" v-model="reason"></textarea>
                        </span>
                    </div>
                    <div class="modalContentButtonArea">
                        <button class="modalContentButton" @click="OnclickExitReporting()" style="background-color: #FAFFFB; color: #004627; border: 1px solid gray; border-radius: 5px;">확인</button>
                        <!--<button class="ms-1 modalContentButton" @click="isReporting = false" style="background-color: #EBF4ED; color: #004627; border: 1px solid gray; border-radius: 5px;">취소</button>-->
                    </div>
                    </div>
                    </v-container>
                </v-card>
                </v-dialog>
                <!-- 커스텀 모달 for 신고버튼-->
                <v-dialog v-model="isCancelReporting" max-width="300" @click:outside="isCancelReporting = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#CF5555">
                    <v-toolbar-title class="modalTitle">Reported! </v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="isCancelReporting = false">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-toolbar>
                    <!-- 모달 컨텐츠 영역 -->
                    <v-container>
                    <div class="modalContent">
                    <div class="mb-3">
                        <span class="modalContentMessage">
                            <span class="modalContentMessage"> 이미 신고한 계정입니다. 신고를 취소하시겠습니까? </span>
                        </span>
                    </div>
                    <div class="modalContentButtonArea">
                        <button class="modalContentButton" @click="onClickisCancelReporting()" style="background-color: #FAFFFB; color: #004627; border: 1px solid gray; border-radius: 5px;">확인</button>
                        <!--<button class="ms-1 modalContentButton" @click="isReporting = false" style="background-color: #EBF4ED; color: #004627; border: 1px solid gray; border-radius: 5px;">취소</button>-->
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
                    <LockImg v-if="this.isLocked"/>
                    <OtherUserPosts v-else/>
                </v-tab-item>
            <!-- 레시피  -->
                <v-tab-item>
                    <LockImg v-if="this.isLocked"/>
                    <OtherUserRecipes v-else/>
                </v-tab-item>
            <!-- 챌린지  -->
                <v-tab-item>
                <LockImg v-if="this.isLocked"/>
                <OtherUserChallenges v-else/>
                </v-tab-item>
        </v-tabs>
    </div> 
    </section>
</v-container>  
</template>

<script>
import {mapGetters} from "vuex"
import OtherUserPosts from '@/components/profile/OtheruserPost.vue'
import LockImg from '@/components/profile/Lockuser.vue'
import OtherUserRecipes from '@/components/profile/OtheruserRecipe.vue'
import OtherUserChallenges from '@/components/profile/OtheruserChallenge.vue'
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment';
import axios from "axios";
import API from '@/api/index.js'
import followAPI from '@/api/follow.js'
import userAPI from '@/api/user.js'
import reportAPI from '@/api/report.js'
export default {
components: { OtherUserPosts, OtherUserRecipes, OtherUserChallenges, ProfileName, LockImg},

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
            filepath: "",
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
        rankOpen: false,
        isReporting: false,
        isReported: false,
        isCancelReporting: false,
        isNotChallenging: false,
        reason: "",
        requestforfollow : false,
        }
    },
    computed :{
        ...mapGetters("user",(["userinfo","isLogin"])),
        time() {
            const start = moment(this.start_date);
            const now = moment(new Date());
            // console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
            return now.diff(start, 'days') + 1;
        },
    },
    created() {
        if(this.userinfo.challengeId.challengeId ===1){
            this.isNotChallenging = true;
        }else{
            this.isNotChallenging = false;
        }
        axios({
                method: "get",
                url: API.url + reportAPI.select(this.userinfo.userid),
            }).then((res)=> {
                if(res.data.length === 0){
                    console.log("nothing selected: ", res.data)
                    //아직 신고 안됨
                    this.isReported = false;
                }else{
                    console.log("selected: ", res.data)
                    //이미 신고한 계정
                    this.isReported = true;
                }
            }).catch((err)=> {
                console.log(err)
            })
    },
    methods: {
        //신고 모달창 열기
        onClickReport(){
            this.isReported = true;
            this.isReporting = true;
        },
        //신고취소 모달창 열기
        onClickNotReport(){
            this.isReported = false;
            this.isCancelReporting = true;
        },
        //신고 모달창 나가기
        OnclickExitReporting(){
            
            axios({
                method: "post",
                url: API.url + reportAPI.report(this.userinfo.userid,this.$route.params.userid),
                params: {
                    "user_id": this.userinfo.userid,
                    "report_id": this.$route.params.userid,
                    "reason": this.reason,
                }
            }).then((res)=>{
                console.log("신고 성공", res.data)
            }).catch((err)=>{
                console.log(err)
            })
            this.isReported = true;
            this.isReporting = false;
        },
        //계정 신고 취소 
        onClickisCancelReporting(){

            axios({
                method: "get",
                url: API.url + reportAPI.unreport(this.userinfo.userid,this.$route.params.userid),
            }).then((res) =>{
                console.log("신고취소 성공", res.data)
            }).catch((err)=>{
                console.log(err)
            }),

            this.isCancelReporting = false;
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
                     //언팔로우 버튼을 누르면 타계정의 팔로워 증감
                    axios({
                            method: "get",
                            url: API.url + followAPI.select_follower(this.$route.params.userid),
                        }).then((res) => {
                            this.followerList = res.data;
                            this.follower = this.followerList.length;
                        }).catch((err) => {
                            console.log("실패");
                            console.log(err);
                        })
                        //언팔 후 비공개 계정일 경우 아래 잠금 화면 표시
                        
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
                    //팔로우 버튼을 누르면 타계정의 팔로워 증가
                    axios({
                            method: "get",
                            url: API.url + followAPI.select_follower(this.$route.params.userid),
                        }).then((res) => {
                            this.followerList = res.data;
                            this.follower = this.followerList.length;
                        }).catch((err) => {
                            console.log("실패");
                            console.log(err);
                        })
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
                if(this.otheruserinfo.challengeId.challengeId === 1){
                    this.isNotChallenging = true;
                }else{
                    this.isNotChallenging = false;
                }
                this.start_date = this.otheruserinfo.challengeId.start_date;
                //비공개 확인 용도
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
                                this.requestforfollow = true;
                            }
                            else{
                                this.isLocked = true;
                                this.requestforfollow = false;
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
}
}
</script>