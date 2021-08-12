<template>
<v-container>
    <section  class="profilepage">
            <div centered class="container mt-5 d-flex justify-content-center">
                <div class="card p-3">
                    <div class="d-flex align-items-center">
                        <div class="image text-center">
                            <button class="imgChange" >
                            <!--<img :src="previewItem.src" class="profile-img2" width="100" height="100" v-if="isChanged">-->
                                <label for="file"><img :src="!!picture ? picture.previewURL : userPath" class="profile-img2" width="100" height="100"></label>
                                <input id="file" type="file" ref="files" multiple @input="fileUpload">
                            </button>
                                <div class="mb-0 mt-0">{{nickname}}</div><div style="font-size:x-small"> <span class="ingdate">{{time}} </span> 일째 챌린지 중</div>
                                    </div>
                                    <div class="rankingbox" style="font-size:xx-small; margin: 30px">
                                        <div class="d-flex justify-content-center">
                                            <div style="margin: 10px">
                                                <div class="d-flex flex-column"> 
                                                    <span class="rank">rank</span>
                                                    <span class="number1" style="font-size:medium">{{rank}}</span> 
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
                                            <div style="margin: 10px">
                                                <div class="d-flex flex-column">
                                                    <span class="follower">followers</span>
                                                    <div @click="openDialog(1)">
                                                    <span class="number3" style="font-size:medium">{{follower}}</span>
                                                </div>
                                            </div>
                                            </div>
                                        </div>

                                        <div> 
                                            <button class="bg-freditgreen" @click="onClickEditUser()">회원 정보 수정</button>
                                        </div>
                                        
                                    </div>
                                </div>
                </div>
                <v-dialog ref="modal1" v-model="dialog" persistent max-width="300px">
                    <v-card>
                        <v-card-title>
                        <template>
                            <v-icon style="margin-right:10px;" large color="#41B883"  >List</v-icon>  <!--"#41B883" -->
                            <!--<span class="headline" large>팔로우 목록</span>-->
                        </template>
                        <v-spacer></v-spacer>
                        <v-btn icon @click="closeDialog('modal1')"> <!-- closeDialog 클릭 이벤트 -->
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                        </v-card-title>
                        <v-card-text>
                        <v-row>
                            <v-col cols="12" sm="12" md="12" style="position: relative; border:1px solid #004627; border-style:dashed; ">
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
import {mapGetters, mapState, mapActions} from "vuex"
import UserPosts from '@/components/profile/userPost.vue'
import UserRecipes from '@/components/profile/userRecipe.vue'
import UserChallenges from '@/components/profile/userChallenge.vue'
import ProfileName from '@/components/basic/ProfileName.vue'
import moment from 'moment';
import API from '@/api/index.js'
import userAPI from '@/api/user.js'
import axios from 'axios'

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
        profileImg : '',
        picture: null,
        }
    },
    computed:{
        ...mapGetters("challenge", ["challengeList", "foodChallengeList", "clothChallengeList", "cosmeticChallengeList"]),
        ...mapGetters("user",(["userinfo", "challenge"])),
        ...mapState("follow", (["followerList", "followingList", "detailFollowUser"])),
        ...mapState("user", (["isLogin", "rank"])),
        time() {
            const start = moment(this.start_date);
            const now = moment(new Date());
            console.log(`Difference is ${now.diff(start, 'days') + 1} day(s)`);
            return now.diff(start, 'days') + 1;
        },
        userPath() { // 프로필 사진 이미지 출력
            return API.url + "/profile/" + this.userinfo.userid + "/"+ this.userinfo.stored_file_path.split('/').reverse()[0]
        },
    },
    created() {
        //this.$store.dispatch("user/getTokenUserInfo"),
        this.nickname = this.userinfo.nickname;
        this.userid = this.userinfo.userid;
        this.$store.dispatch("user/getUserRank", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowingList", this.userinfo.userid);
        this.$store.dispatch("follow/getFollowerList", this.userinfo.userid);

        //console.log(this.followerList)
        this.follower = this.followerList.length;
        this.following = this.followingList.length;
        
        this.start_date = this.userinfo.challengeId.start_date;
        
        
    },
    methods: {
            ...mapActions("challenge", ["fetchChallengeList", "challengeAttend"]),
        onClickEditUser(){
            this.$router.push({ name: "ModifyUser" });
        },
        fileUpload(){
            console.log("files : ", this.$refs.files.files[0]);
            // 파일 업로드를 클릭 했을 시, 백에 보낼 사진
            const picture = this.$refs.files.files[0]
            //프리뷰 url 만들기
            picture.previewURL = URL.createObjectURL(picture)
            this.picture = picture


            const formData = new FormData()
            formData.append('picture', picture)
            console.log(API.url + userAPI.modify_pic(this.userinfo.userid))
            axios.post(API.url + userAPI.modify_pic(this.userinfo.userid), formData,
                {headers: {'Content-Type' : 'multipart/form-data'}
                    
                }
            ).then((res)=>{
               // if(res.status === success)
                this.$store.dispatch("user/getUpdateUserInfo", this.userinfo.userid)
                console.log(res.status)
            }).catch((err)=>{
                console.error(err)
                console.log("실패")
            })
            
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