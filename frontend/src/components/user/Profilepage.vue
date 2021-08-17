<template>
<v-container>
    <section  class="profilepage">
        <!-- 프로필 사진 수정 안내 모달 -->
        <ModifyModal :modalOpen="isModifyPicModal" modalContent="사진을 수정하시겠습니까?" type=1
        @modalConfirmBtnClick="onUploadPicModalClick" @modalCancelBtnClick="isModifyPicModal = false" />
        <!-- 프로필 사진 수정 완료 안내 모달 -->
        <ModifyModal :modalOpen="isModifyPicComplete" modalContent="수정이 완료되었습니다" type=2 />

        <ChallengeConfirm :user="userinfo" :certdialog="certdialog" @openCertDialog="openCertDialog"/>
            <div centered class="container d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center"> 
                            <!-- 프로필 사진 vs 프로필 사진 수정 -->
                            <div v-if="!isModifyPic">
                                <img :src="userinfo.filepath" class="profile-img" width="100" height="100" style="border-radius: 50%;" @click="onClickEditPic">
                            </div>
                            <div v-else>
                                <button class="imgChange">
                                    <!-- 기본 이미지 일 때는 바로 사진 선택 -->
                                    <div v-if="isDefaultImg">
                                        <label for="file"><img :src="!!picture ? picture.previewURL : userinfo.filepath" class="profile-img2" width="100" height="100"></label>
                                        <input id="file" type="file" ref="files" multiple @input="fileUpload">
                                    </div>
                                    <!-- 기본 이미지가 아닐 때에는 기본 이미지 vs 새로운 사진 선택 -->
                                    <div v-else>
                                        <v-menu offset-y class="inline" v-if="isModifyPic">
                                        <template v-slot:activator="{ on, attrs }">
                                            <img :src="!!picture ? picture.previewURL : userinfo.filepath"  v-bind="attrs" v-on="on" class="profile-img2" width="100" height="100">
                                        </template>
                                        <v-list>
                                            <!-- 기본 이미지 -->
                                            <v-list-item @click="onClickDefaultImg">
                                                <v-list-item-title>기본 이미지</v-list-item-title>
                                            </v-list-item>
                                            <!-- 이미지 선택 -->
                                            <v-list-item @click="onClickNewImg">
                                                <v-list-item-title>
                                                    <label for="file">이미지 선택</label>
                                                    <input id="file" type="file" ref="files" multiple @input="fileUpload" style="width:0; height:0">
                                                </v-list-item-title>
                                            </v-list-item>
                                        </v-list>
                                        </v-menu>
                                    </div>
                                </button>
                            </div>
                            <!-- 닉네임 -->
                            <div class="mb-0 mt-0" style="font-size:large">{{nickname}}</div>
                            <!-- 챌린지 -->
                            <div v-if="time <= 0" style="font-size:x-small">
                                <span class="ingdate">&nbsp;</span>
                            </div>
                            <div v-else-if="time>0 && !isNotChallenging" style="font-size:x-small" @click="openCertDialog(true)">
                                <span class="ingdate">{{time}} </span> 일째 챌린지 중
                            </div>
                        </div>
                        <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                            <div class="d-flex justify-content-center">
                                <div style="margin: 0px 10px 10px">
                                    <div class="d-flex flex-column"> 
                                        <span class="rank" style="font-size:x-small">Rank</span>
                                        <span v-if="this.step===1" class="number1" ><img src="@/assets/image/OVLKoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                        <span v-else-if="this.step===2" class="number1" ><img src="@/assets/image/OVLoongya.png" alt="" width="30px"  @click="onClickRank"/></span>
                                        <span v-else-if="this.step===3" class="number1" ><img src="@/assets/image/OVLoong.png" alt="" width="30px"  @click="onClickRank"/></span>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 0px 10px 10px" class="justify-content-center" >
                                    <div class="d-flex flex-column"> 
                                        <span class="following">Following</span>
                                        <div @click="openDialog(0)" class="mt-1">
                                        <span class="number2" style="font-size:medium" width="40px">{{following}}</span>
                                        </div>
                                    </div>
                                </div>
                                <v-spacer></v-spacer>
                                <div style="margin: 0px 10px 10px">
                                    <div class="d-flex flex-column">
                                    <span class="follower">Follower</span>
                                        <div @click="openDialog(1)" class="mt-1">
                                            <span class="number3" style="font-size:medium" width="40px" >{{follower}}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div v-if="!isModifyPic"> 
                                <button class="editProfileBtn" @click="onClickEditUser()">Edit Profile</button>
                            </div>
                            <div v-else>
                                <button class="editProfileBtn" @click="onClickPicComplete()">프로필 사진 수정 완료</button>
                            </div>
                            
                        </div>
                    </div>
                </div>
                <!-- 커스텀 모달 -->
                <v-dialog v-model="rankOpen" max-width="300" @click:outside="rankOpen = false">
                <v-card>
                    <!-- 모달 타이틀 영역 -->
                    <v-toolbar dense color="#49784B">
                    <v-toolbar-title class="modalTitle">랭킹</v-toolbar-title>
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
                                    <div v-if="this.step===1" class="number1" >
                                        <img src="@/assets/image/OVLKoongya.png" alt="" @click="onClickRank"/>
                                        <div> 쿵야가 아직 아기에요! 좀 더 많은 게시물을 올려보세요! 머리에 새싹이 자라날 거에요! </div>
                                    </div>
                                    <div v-else-if="this.step===2" class="number1" >
                                        <img src="@/assets/image/OVLoongya.png" alt="" @click="onClickRank"/>
                                        <div> 쿵야가 많이 자랐어요! 쿵야를 더 키워보세요! <br> 머리에 꽃이 자라날 거에요! </div>
                                    </div>
                                    <div v-else-if="this.step===3" class="number1" >
                                        <img src="@/assets/image/OVLoong.png" alt="" @click="onClickRank"/>
                                        <div> 쿵야가 다 자랐어요! 지금도 충분히 잘하고 있으니 더 열심히 참여해주세요! </div>
                                    </div>
                                    
                                </div>
                                <br/>
                                <div class="text-center">
                                    <div> &lt; <span style="color: #49784B; font-size:large">{{nickname}}</span> 님의 랭킹 &gt;</div> 
                                    <div> Rank는 <span style="font-size: large; color:  #49784B;">{{rank}}</span> 등 !! </div>
                                    <div> Vegan Score는 <span style="font-size: large; color:  #49784B;">{{experience}}</span>  점 입니다 ! </div>
                                </div>
                            </span>
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
import ModifyModal from '@/components/profile/ModifyModal.vue'
import filepath from '@/api/fileUpload.js';

export default {
components: { UserPosts, UserRecipes, UserChallenges, ChallengeConfirm, ProfileName, NoneChallenging, ModifyModal},

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

            isModifyPicModal:false, // 모달창
            isModifyPic:false, // 프로필 사진 수정
            isModifyPicComplete:false, // 프로필 사진 수정 완료
            picture: null,
            defailtImgFilePath:"https://ovl-bucket.s3.ap-northeast-2.amazonaws.com/defaultImg.jpg",
            isDefaultImg: false, // 기본 이미지 vs 새로운 이미지
        }
    },
    computed:{
        ...mapGetters("user",(["userinfo","isLogin", "challenge", "rank", "percent"])),
        ...mapState("follow", (["followerList", "followingList", "detailFollowUser"])),
        time() {
            const start = moment(this.start_date);
            const now = moment(new Date());
            if((now.diff(start, 'days') + 1) <= 0){
                return 0;
            }
            else{
                return now.diff(start, 'days') + 1
            }
            
        },

    },
    created() {
        this.nickname = this.userinfo.nickname;
        this.$store.dispatch("user/getUserRank", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowingList", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowerList", this.userinfo.userid);

        console.log("challenge : ", this.userinfo.challengeId);

        this.follower = this.followerList.length;
        this.following = this.followingList.length;
        //00일째 챌린지 계산
        if(this.userinfo.challengeId.challengeId ===1){
            this.isNotChallenging = true;
        }else{
            this.isNotChallenging = false;
        }
        console.log("isNotChallenging : ", this.isNotChallenging);
    
        this.start_date = this.userinfo.challengeId.start_date;

        //쿵야 셋팅
        if(this.percent < 31 ){
            this.step = 1;
        }else if( this.percent > 30 && this.percent < 61){
            this.step = 2;
        }else{
            this.step = 3;
        }
    },

    methods: {
        onClickDefaultImg() {
            this.isDefaultImg = true
            this.userinfo.filepath = this.defailtImgFilePath
            this.picture = null
        },
        onClickNewImg() {
            this.isDefaultImg = false
        },
        onClickEditPic(){
            if (this.userinfo.filepath==this.defailtImgFilePath) this.isDefaultImg = true
            this.isModifyPicModal = true
        },
        onUploadPicModalClick() {
            this.isModifyPicModal = false
            this.isModifyPic = true
        },
        onClickEditUser() {
            this.$router.push({ name: "ModifyUser" })
        },
        async onClickPicComplete() {

            var pfname = []
            if (this.isDefaultImg) {
                pfname.push("temp");
                pfname.push(this.defailtImgFilePath)
            } else {
                if (this.picture==null) {
                    this.modifyPicCompleteModal()
                    return;
                }
                var pflist = []
                pflist.push(this.picture)
                pfname = await filepath.upload(pflist, 'user')
            }
            
            axios.post(API.url + userAPI.modify_pic(this.userinfo.userid), pfname,
            ).then((res)=>{
                if (res.data=="success") {
                    this.$store.dispatch("user/getUpdateUserInfo", this.userinfo.userid)
                    this.modifyPicCompleteModal()
                }
            }).catch((err)=>{
                console.error(err)
                console.log("실패")
            })
        },
        modifyPicCompleteModal() {
            this.isModifyPicComplete = true
            setTimeout(() => {
                this.isModifyPicComplete = false
                this.isModifyPic = false
            }, 1000)
        },
        //랭크 클릭시 보이는 모달
        onClickRank() {
            
            axios({
                method: "get",
                url: API.url + userAPI.select(this.userinfo.userid)
            }).then((res)=>{
                if(res.data !== null){
                    //console.log("유저 경험점수" , res.data)
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
            console.log("챌린지 선택 !")
            if(this.userinfo.challengeId.challengeId!=1)
                this.certdialog = val
        },
        fileUpload(){
            // 파일 업로드를 클릭 했을 시, 백에 보낼 사진
            const picture = this.$refs.files.files[0]
            //프리뷰 url 만들기
            picture.previewURL = URL.createObjectURL(picture)
            this.picture = picture
            this.isDefaultImg = false;
        },
    },

}
</script>