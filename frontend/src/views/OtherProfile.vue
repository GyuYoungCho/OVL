<template>
<v-container>
    <section class="profilepage">
        <!-- Rank 선택 시 등급 안내 모달 -->
        <RankModal :modalOpen="isRankModalOpen" :user="profileUser" :step="step" :rank="rank" @modalCancelBtnClick="isRankModalOpen = false" />    
        <!-- 팔로우 선택 시 팔로우, 팔로워 모달 -->
        <FollowModal v-if="detailFollowUser.length>0" :modalOpen="isFollowOpen" :followList="detailFollowUser" :title="followModalTitle" @modalCancelBtnClick="followModalCancel" />
        <!-- 신고 모달 -->
        <ReportModal :modalOpen="isReportOpen" :isReport="isReport" :title="reportModalTitle" :fromId="userinfo.userid" :toId="profileUser.userid"
        @onCancelBtnClick="isReportOpen = false" @onConfirmBtnClick="reportRegistOrCancel"/>
        <ChallengeConfirm :user="profileUser" :certdialog="isCertOpen" @openCertDialog="openCertDialog"/>

        <div centered class="container d-flex justify-content-center">
            <div class="card p-3">
                <div class="d-flex align-items-center">
                    <!-- 프로필 사진, 닉네임, 챌린지 -->
                    <div class="image text-center"> 
                        <img :src="profileUser.filepath" class="profile-img" width="100" height="100" style="border-radius: 50%;">
                        <!-- 닉네임 -->
                        <div class="mb-0 mt-0" style="font-size:large">{{profileUser.nickname}}</div>
                        <!-- 챌린지 -->
                        <div v-if="time <= 0" style="font-size:x-small">
                            <span class="ingdate">&nbsp;</span>
                        </div> 
                        <div v-else-if="time>0 && !isNotChallenging" style="font-size:x-small" @click="openCertDialog(true)">
                            <span class="ingdate">{{time}} </span> 일째 챌린지 중
                        </div>
                    </div>
                    
                    <!-- 랭킹 박스 --> 
                    <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                        <div class="d-flex justify-content-center">
                            <div style="margin: 0px 10px 10px">
                                <div class="d-flex flex-column" @click="openRankDialog"> 
                                    <span class="rank" style="font-size:x-small">Rank</span>
                                    <span v-if="this.step===1" class="number1" ><img src="@/assets/image/OVLKoongya.png" alt="" width="30px"/></span>
                                    <span v-else-if="this.step===2" class="number1" ><img src="@/assets/image/OVLoongya.png" alt="" width="30px"/></span>
                                    <span v-else-if="this.step===3" class="number1" ><img src="@/assets/image/OVLoong.png" alt="" width="30px"/></span>
                                </div>
                            </div>
                            <v-spacer></v-spacer>
                            <!-- 팔로잉 -->
                            <div style="margin: 0px 10px 10px" class="justify-content-center" >
                                <div class="d-flex flex-column"> 
                                    <span class="following">Following</span>
                                    <div @click="onClickFollow(0)" class="mt-1">
                                    <span class="number2" style="font-size:medium" width="40px">{{followingCnt}}</span>
                                    </div>
                                </div>
                            </div>
                            <v-spacer></v-spacer>
                            <!-- 팔로워 -->
                            <div style="margin: 0px 10px 10px">
                                <div class="d-flex flex-column">
                                <span class="follower">Follower</span>
                                    <div @click="onClickFollow(1)" class="mt-1">
                                        <span class="number3" style="font-size:medium" width="40px" >{{followerCnt}}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 팔로우 or 언팔로우 버튼, 신고 버튼 -->
                        <div class="d-flex justify-content-center">
                            <div v-if="profileUser.account_open==1" style="backgroung-color: #9A2EFE"> 
                                <button class="requestFollow">비공개 계정</button> 
                            </div>
                            <div v-else>                                
                                <div v-if="isFollowing" width=""> 
                                    <button class="unfollowBtn" @click="onClickUnFollowBtn()">Unfollow</button>
                                </div>
                                <div v-else-if="!isFollowing"> 
                                    <button class="followBtn" @click="onClickFollowBtn()">Follow</button>
                                </div>
                            </div>

                            <div v-if="!isReport" class="ms-1">
                                <button class="reportBtn" @click="onClickReport">신고</button>
                            </div>
                            <div v-else class="ms-1 unreport">
                                <button class="reportBtn" @click="onClickReport" style="font-size:x-small">신고취소</button>
                            </div>
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
                        <NoneChallenging v-else-if="this.isNotChallenging"/>
                        <OtherUserChallenges v-else/>
                    </v-tab-item>
            </v-tabs>
        </div> 
    </section>
</v-container>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import moment from 'moment'
import LockImg from '@/components/profile/Lockuser.vue'
import OtherUserPosts from '@/components/profile/OtheruserPost.vue'
import OtherUserRecipes from '@/components/profile/OtheruserRecipe.vue'
import OtherUserChallenges from '@/components/profile/OtheruserChallenge.vue'
import NoneChallenging from '@/components/profile/NoneChallenging.vue'
import RankModal from '@/components/profile/RankModal.vue'
import FollowModal from '@/components/profile/FollowModal.vue'
import ReportModal from '@/components/profile/ReportModal.vue'
import ChallengeConfirm from '@/components/user/ChallengeConfirm.vue'

export default {
    components: {RankModal, LockImg, OtherUserPosts, OtherUserRecipes, OtherUserChallenges, NoneChallenging, FollowModal, ReportModal, ChallengeConfirm},
    data() {
        return {
            // 모달 관련
            isRankModalOpen: false, // 랭킹 모달 오픈 
            isCertOpen: false, // 챌린지 인증 모달 오픈
            isFollowOpen: false, // 팔로우 모달 오픈
            isReportOpen: false, // 신고 모달 오픈
            isFollowModalClick:false,

            isNotChallenging: false, // 챌린지 진행 여부

            step:'', // 비건 등급
            followModalTitle:"Follow", // 팔로우 모달 타이틀
            reportModalTitle:"신고", // 신고 모달 타이틀
            reportType:0, // 신고 타입 0 : 신고, 1 : 신고 취소

            isFollowing: false, // 팔로우 확인
            isReport: false, // 신고 확인 
            isLocked: false, // 계정 공개 여부 (팔로우 했으면 false, 팔로우 안되어 있으면 true)

            followerCnt:0,
            followingCnt:0,
            
        }
    },
    computed:{
        ...mapGetters('user', ["userinfo", "profileUser", "rank", "percent"]),
        ...mapGetters('follow', ["followerList", "followingList", "detailFollowUser"]),
        ...mapGetters('report', ["reportList"]),
        time() {
            const start = moment(this.profileUser.challengeId.start_date);
            const now = moment(new Date());
            if((now.diff(start, 'days') + 1) <= 0){
                return 0;
            }
            else{
                return now.diff(start, 'days') + 1
            }
        },  
        report() {
            return this.reportList;
        },
        follower() {
            return this.followerList;
        },
        following() {
            return this.followingList;
        },
        ProFileUser() {
            return this.proFileUser;
        },
        detailFollow() {
            return this.detailFollowUser;
        },
        userPercent() {
            return this.percent;
        }
    },
    watch: {
        report(val) {
            if (val.includes(this.profileUser.userid)) this.isReport = true;
            else this.isReport = false;

            if(this.isReportOpen) this.isReportOpen = false;
        },
        follower(val) {
            if (val.includes(this.userinfo.userid)) this.isFollowing = true;
            else this.isFollowing = false;

            if (this.profileUser.account_open==1 && !val.includes(this.userinfo.userid)) this.isLocked = true;
            
            this.followerCnt = val.length;
        },
        following(val) {
            this.followingCnt = val.length
        },
        ProFileUser(val) {
            console.log("ProfileUser 변경 : ", val);
            if (val.account_open==1 && !this.followerList.includes(this.userinfo.userid)) this.isLocked = true;
            else this.isLocked = false;

            if (val.challengeId.challengeId==1) this.isNotChallenging = true;
            else this.isNotChallenging = false;
        },
        detailFollow(val) {
            if (this.isFollowModalClick && val.length>0 && !this.isLocked) this.isFollowOpen = true;
        },
        userPercent(val) {
            if(val < 31 ){
                this.step = 1;
            }else if( val > 30 && val < 61){
                this.step = 2;
            }else{
                this.step = 3;
            }
        }
    }, 
    methods: {
        ...mapActions('user', ["getSelectUser", "getUserRank"]),
        ...mapActions('follow', ["getFollowerList", "getFollowingList", "getDetailFollowUser", "follow", "unfollow"]),
        ...mapActions('report', ["reportRegist", "selectReport", "reportCancel"]),
        followModalCancel() {
            this.isFollowOpen = false;
            this.isFollowModalClick = false;
        },
        openCertDialog(val){
            if(this.profileUser.challengeId.challengeId!=1)
                this.isCertOpen = val
        },
        openRankDialog() { // 랭킹 모달 오픈
            this.isRankModalOpen = true
        },
        onClickFollow(num) { // follow Dialog 열리는 동작
            if (this.isLocked) return // 비공개 계정 -> 팔로우, 팔로워 보이면 안됨

            this.isFollowModalClick = true;
            if(num == 0){ // following
                this.getDetailFollowUser(this.followingList)
            }else { // follower
                this.$store.dispatch("follow/getDetailFollowUser", this.followerList);
                this.followModalTitle = "Follower" // 팔로우 모달 타이틀
            } 
        },
        onClickFollowBtn() { // 팔로우 등록
            var payload = {
                "fromId": this.userinfo.userid,
                "toId":this.profileUser.userid
            }
            this.follow(payload)
        },
        onClickUnFollowBtn() { // 팔로우 취소
            var payload = {
                "fromId": this.userinfo.userid,
                "toId": this.profileUser.userid
            }
            this.unfollow(payload)
        },
        onClickReport() { 
            if (!this.isReport) { // 신고 안했음 이제 해야됨
                this.reportModalTitle = "회원 신고"
            } else { // 신고 했음 이제 취소 해야됨
                this.reportModalTitle = "회원 신고 취소"
            }
            this.isReportOpen = true;
        },
        reportRegistOrCancel() {
            // this.isReportOpen = false;
        }
    },
    created() {
        console.log("created !! : ", this.$route.params.userid);
        const userid = this.$route.params.userid;
        // 해당 프로필 유저 아이디와 내 아이디가 같다면 나의 프로필로 이동
        if(userid == this.userinfo.userid){
            this.$router.push({ name : 'Profile', params: {userid: this.userinfo.userid}})
        }

        // 해당 유저 정보 가져오기
        this.getSelectUser(userid);
        // 해당 랭킹 정보 가져오기
        this.getUserRank(userid);
        // 팔로잉 리스트 가져오기
        this.getFollowingList(userid);
        // 팔로워 리스트 가져오기
        this.getFollowerList(userid);
        // 신고 리스트 가져오기 (로그인 된 유저가 신고한 유저 아이디 리스트)
        this.selectReport(this.userinfo.userid);

        if (this.profileUser.challengeId.challengeId==1) this.isNotChallenging = true;
        else this.isNotChallenging = false;

        console.log("초기 isNotChallenging : ", this.isNotChallenging)
        console.log("초기 profileUser : ", this.profileUser)

        // 비건 등급
        if(this.percent < 31 ) this.step = 1;
        else if( this.percent > 30 && this.percent < 61) this.step = 2;
        else this.step = 3;

        // 팔로우 확인 -> 내가 팔로잉 하는 사람 중 현재 프로필 유저의 아이디가 있다면 팔로우 한 것
        if (this.followerList.includes(this.userinfo.userid)) this.isFollowing = true;
        if (this.profileUser.account_open==1 && !this.followerList.includes(this.userinfo.userid)) this.isLocked = true;
        // 신고 확인 -> 내가 신고한 사람 중 현재 프로필 유저의 아이디가 있다면 신고한 것
        if (this.reportList.includes(this.profileUser.userid)) this.isReport = true;

        console.log("초기 isLocked : ", this.isLocked)
    }
}
</script>