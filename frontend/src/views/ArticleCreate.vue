<template>
  <div>
    <v-container>
      <!-- 게시글 등록 성공 시 -->
      <FlashModal :modalOpen="isPostRegist" :title="modalTitle" :modalContent="modalContent" />

      <section class="article">
        <div v-if="type==0">
          <h4>게시글 작성</h4>
        </div>
        <div v-else>
          <h4>게시글 수정</h4>
        </div>
        <!-- 버튼들 -->
        <div class="categoryBtn_box">
          <button @click="foodClick" :class="{'articleBtnNotSelected':!foodSelected, 'articleBtnSelected':foodSelected}">음식</button>
          <button @click="clothClick" :class="{'articleBtnNotSelected':!clothSelected, 'articleBtnSelected':clothSelected}">옷</button>
          <button @click="cosmeticClick" :class="{'articleBtnNotSelected':!cosmeticSelected, 'articleBtnSelected':cosmeticSelected}">화장품</button>
        </div>
        <!-- 파일 업로드 영역  -->
        <span style="text-align:right;">사진 {{imgCnt}} / 10</span>
        <div v-if="type==0">
          <div class='articlePic' v-if="!sendList.length">
            <label for="file"><v-icon>mdi-plus</v-icon></label>
            <input id="file" type="file" ref="files" multiple @input="fileUpload(0)" 
            accept="image/jpeg, image/png">
          </div>
          <!-- 캐러셀 영역 -->
          <v-carousel class="carouselBorder" hide-delimiters v-else height="30vh">
            <v-carousel-item v-for="(previewItem,idx) in previewItems" :key="idx" :src="previewItem.src" style="text-align:right">
              <!-- 사진 추가 -->
              <span>
                <div v-if="imgCnt<10" class="inline">
                  <label for="newFile"><v-icon class="photoClick">mdi-plus-circle-outline</v-icon></label>
                  <input id="newFile" type="file" ref="plusFiles" multiple @input="newFileUpload(0)" style="width:0; height:0"
                  accept="image/jpeg, image/png">
                </div>
              </span>
              <!-- 사진 수정 -->
              <span @click="photoModify(idx)">
                <label for="modifyFile"><v-icon class="photoClick">mdi-check-circle-outline</v-icon></label>
                <input id="modifyFile" type="file" ref="modifyFiles" @input="modifyFileUploadRegist" style="width:0; height:0"
                accept="image/jpeg, image/png">  
              </span>
              <!-- 사진 삭제 -->
              <span @click="photoDelete(idx)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>  
            </v-carousel-item>
          </v-carousel>
        </div>
        <!-- 수정 시 캐러셀 영역 -->
        <div v-else>
          <div class='articlePic' v-if="!photoList.length">
            <label for="file"><v-icon>mdi-plus</v-icon></label>
            <input id="file" type="file" ref="files" multiple @input="fileUpload(1)" style="width:0; height:0"
              accept="image/jpeg, image/png">
          </div>
          <v-carousel v-else class="carouselBorder" hide-delimiters height="30vh">
            <v-carousel-item v-for="(info,idx) in photoList" :key="idx" :src="photoPath(idx)" style="text-align:right">
              <!-- 사진 추가 -->
              <span>
                <div v-if="imgCnt<10" class="inline">
                  <label for="newFile"><v-icon class="photoClick">mdi-plus-circle-outline</v-icon></label>
                  <input id="newFile" type="file" ref="plusFiles" multiple @input="newFileUpload(1)" 
                  accept="image/jpeg, image/png" style="width:0; height:0">
                </div>
              </span>
              <!-- 사진 수정 -->
              <span @click="photoModify(idx)">
                <label for="modifyFile"><v-icon class="photoClick">mdi-check-circle-outline</v-icon></label>
                <input id="modifyFile" type="file" ref="modifyFiles" @input="modifyFileUpload" style="width:0; height:0"
                  accept="image/jpeg, image/png">  
              </span>
              <!-- 사진 삭제 -->
              <span @click="photoDelete(idx)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>  
            </v-carousel-item>
            
          </v-carousel>
          
        </div>
        <span style="text-align:right;">png, jpg, jpeg만 가능</span>
        
        <!-- 게시글 컨텐츠 입력 영역 -->
        <textarea v-model="content" placeholder="게시글 입력..."></textarea>
        
        <div v-if="type==0">
          <div  :class="{'buttonDiv': articleCreateFormValid, 'disabledBtnDiv': !articleCreateFormValid}">
              <button style="width:100%" :disabled='!articleCreateFormValid'  @click="send">등록</button>
          </div>
        </div>
        <div v-else >
          <div  
            :class="{'buttonDiv': articleModifyFormValid, 'disabledBtnDiv': !articleModifyFormValid}">
         <button style="width:100%" :disabled='!articleModifyFormValid' @click="modify">수정</button>
          </div>
        </div>
      </section>
    </v-container>
  </div>
</template>

<script>
  import axios from "axios";
  import { mapState } from "vuex";
  import FlashModal from '@/components/signup/FlashModal.vue'
  import fileUpload from "@/api/fileUpload.js";
  import API from "@/api/index.js";

  export default {
    components: {
      FlashModal
    },
    data () {
      return {
        // 버튼 관련 css class list toggle 용 변수들 설정
        foodSelected: false,
        clothSelected: false,
        cosmeticSelected: false,
        // preview 위한 item 들
        previewItems : [],
        // 백에 보내기 위한 데이터들
        category: "",
        content: "",
        // userId 는 vuex로 관리될거니까 작성.vue 에선 보일 필요 없음
        sendList: [],

        // ↓ 여기서부터 게시글 수정 관련 변수
        type:0, // 등록인지 수정인지 구분하기 위함 0 : 등록, 나머지(postId) : 수정
        deleteIdList:[0], // 삭제할 사진 아이디 저장하는 리스트
        photoList:[], // 게시글 수정 시 가져온 사진 리스트
        modifyPhotoList: [], // 게시글 수정 사진 저장할 리스트
        modifyIdList: [0], // 수정하는 사진 아이디 저장할 리스트
        plusPhotoList: [], // 새로 추가하는 사진 저장할 리스트
        tempIdx:0, // 임시 저장

        // 모달 관련 변수
        modalContent: '',
        modalTitle:'',
        isPostRegist:false,
        deletePhotoIdx: 0,

        imgCnt:0,
      }
    },
    computed: {
      ...mapState("user", ["userinfo", "isLogin"]),
      ...mapState("post", ["post", "postPhotoList"]),
      typeTrans() { // 수정에서 바로 등록하기 누를 때 전환하기 위함
        return this.$route.params.type;
      },
      articleCreateFormValid () {
        return !!this.category && !!this.content && !!this.sendList.length
      },
      articleModifyFormValid() {
        return !!this.category && !!this.content && !!this.photoList.length
      }
    },
    watch: {
      typeTrans(val) {
        this.type = val;
        this.content="";
        this.foodSelected = false;
        this.clothSelected = false;
        this.cosmeticSelected = false;
      },
    },
    methods: {
      foodClick () {
        // css 토글용
        this.foodSelected = true
        this.clothSelected = false
        this.cosmeticSelected = false
        this.category = '1'
      },
      clothClick () {
        this.foodSelected = false
        this.clothSelected = true
        this.cosmeticSelected = false
        this.category = '2'
      },
      cosmeticClick () {
        this.foodSelected = false
        this.clothSelected = false
        this.cosmeticSelected = true
        this.category = '3'
      },
      fileUpload (type) {
        let limitCnt = 0
        if (type==0) limitCnt = this.$refs.files.files.length+this.sendList < 10? this.$refs.files.files.length : 10-this.sendList
        else limitCnt = this.$refs.files.files.length+this.imgCnt < 10? this.$refs.files.files.length : 10- this.imgCnt
        for (let i = 0; i < limitCnt; i++)  {
          if(this.$refs.files.files[i].type =='image/jpeg'||this.$refs.files.files[i].type == 'image/png'){
            const previewUrl = URL.createObjectURL(this.$refs.files.files[i])
            if (type==0) {
              this.sendList.push(this.$refs.files.files[i])
              this.previewItems.push({src:previewUrl})
              this.imgCnt++
            } else {
              this.plusPhotoList.push(this.$refs.files.files[i])
              this.photoList.push({
                "filesize":0,
                "url":previewUrl
              })
              this.imgCnt++
            }
          }
        }
      },
      photoPath(idx) {
        if (this.photoList[idx].filesize==0) return this.photoList[idx].url;
        else return this.photoList[idx].filepath;
      },
      // @@@@@@@@@@@@ 미충족시 막을 로직 필요함 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
      async send() { // 게시글 등록을 위해 백으로 게시글 정보 보내는 함수'
        var pathList = await fileUpload.upload(this.sendList, 'post');
        
        this.sendList=[]; // formData에 append 후 이미지 리스트 비워주기
        var params = new URLSearchParams();
        params.append("category", this.category);
        params.append("content", this.content);
        params.append("userId", this.userinfo.userid);
        params.append("pathList", pathList);
        
        axios.post(API.url+'/post/regist', params)
        .then((response) => {
          console.log(response.data);

          this.modalTitle = "게시글 등록"
          this.modalContent = "게시글이 등록되었습니다"
          this.isPostRegist = true
          setTimeout(() => {
            this.isPostRegist = false
            this.$router.push({ name: 'Main' })  
          }, 1000);
        })
        .catch((error) => {
          // alert("못보냈슴!");
          console.error(error);
        })
      },
      async modify() { // 게시글 수정
        var params = new URLSearchParams();
        
        var modifyPathList = await fileUpload.upload(this.modifyPhotoList, 'post');
        var plusPathList = await fileUpload.upload(this.plusPhotoList, 'post');
        
        params.append("deleteIdList", this.deleteIdList); // 삭제하는 postId 리스트
        params.append("modifyIdList", this.modifyIdList); // 수정하는 postId 리스트
        params.append("modifyPhotoList", modifyPathList);
        params.append("plusPhotoList", plusPathList);

        params.append("category", this.category); // 카테고리 
        params.append("content", this.content); // 내용
        params.append("postId", this.post.postId); // 게시글 아이디
        
        axios.put(API.url+'/post/modify', params)
        .then((response) => {
          console.log(response.data);
          if (response.data.job=="success") {
            this.modalTitle = "게시글 수정"
            this.modalContent = "게시글이 수정되었습니다"
            this.isPostRegist = true
            setTimeout(() => {
              this.isPostRegist = false
              this.$router.push({path:"/article_detail/"+this.post.postId});
            }, 1000);            
          }
        })
        .catch((error) => {
          console.log(error);
        })
      },
      photoModify(idx) {
        if (idx!=0) this.tempIdx = idx;
      },
      modifyFileUploadRegist() { // 게시글 등록 시 사진 수정
        if(this.$refs.modifyFiles[0].files[0].type =='image/jpeg' || this.$refs.modifyFiles[0].files[0].type =='image/png'){
          const previewUrl = URL.createObjectURL(this.$refs.modifyFiles[0].files[0]);
          this.previewItems[this.tempIdx] = {"src":previewUrl};
          this.sendList[this.tempIdx] = this.$refs.modifyFiles[0].files[0];
          this.previewItems.push("지워");
          this.previewItems.splice(this.previewItems.length-1, 1);
        }
      },
      modifyFileUpload() { // 게시글 수정 시 사진 수정
        if(this.$refs.modifyFiles[0].files[0].type =='image/jpeg' || this.$refs.modifyFiles[0].files[0].type =='image/png'){
          if (this.photoList[this.tempIdx].filesize == 0) { // 수정한 사진. 또 수정하는거니까 modifyPHotoList, idList에 추가하면 안됨
            var idx = this.modifyIdList.indexOf(this.photoList[this.tempIdx].postPhotoId);
            this.modifyPhotoList[idx] = this.$refs.modifyFiles[0].files[0];
          } else {
            this.modifyPhotoList.push(this.$refs.modifyFiles[0].files[0]);
            this.modifyIdList.push(this.photoList[this.tempIdx].postPhotoId);
          }
          const previewUrl = URL.createObjectURL(this.$refs.modifyFiles[0].files[0]);
          
          this.photoList[this.tempIdx] = {
            "postPhotoId":this.photoList[this.tempIdx].postPhotoId,
            "filesize":0, // filesize로 수정 사진, 기존 사진 구분 
            "url":previewUrl
          }
          // 배열에 있는 항목을 집적 설정할 경우 변경 사항을 감지할 수 없으므로 강제 업데이트 하기 위함
          this.photoList.push("지워");
          this.photoList.splice(this.photoList.length-1, 1);
          }
      },
      newFileUpload(type) {
        let limitCnt = 0;
        if (type==0) limitCnt = this.$refs.plusFiles[0].files.length+this.sendList.length < 10? this.$refs.plusFiles[0].files.length : 10-this.sendList.length
        else limitCnt = this.$refs.plusFiles[0].files.length+this.plusPhotoList.length+this.photoList.length <10 ? this.$refs.plusFiles[0].files.length : 10-this.photoList.length-this.plusPhotoList.length
        console.log("limitCnt : ", limitCnt)
        for (let i = 0; i < limitCnt; i++) {
          if(this.$refs.plusFiles[0].files[i].type =='image/jpeg'||this.$refs.plusFiles[0].files[i].type == 'image/png'){

            const previewUrl = URL.createObjectURL(this.$refs.plusFiles[0].files[i])
            if (type==0) {
              this.sendList.push(this.$refs.plusFiles[0].files[i])
              this.previewItems.push({src:previewUrl})
              this.imgCnt++
            } else {
              this.plusPhotoList.push(this.$refs.plusFiles[0].files[i])
              const previewUrl = URL.createObjectURL(this.$refs.plusFiles[0].files[i])
              this.imgCnt++
              this.photoList.push({
                "filesize":0,
                "url":previewUrl
              })
            }
          }
        }
        console.log("sendList.len : ", this.sendList.length)
      },
      photoDelete(idx) { // 사진 삭제
        const type = this.type
        if (type!=0) { // 게시글 수정일 때
          this.deleteIdList.push(this.photoList[idx].postPhotoId);
          this.photoList.splice(idx, 1);
          this.imgCnt--
        } else { // 게시글 등록일 때
          this.sendList.splice(idx, 1);
          this.previewItems.splice(idx, 1);
          this.imgCnt--
        }
      }
    },
    async created() {
      this.type = this.$route.params.type;
      // type != 0 이면 게시글 수정이라는 뜻
      if (this.$route.params.type!=0) {
        // 스토어에 해당 게시물의 정보 가져온 후
        await this.$store.dispatch("post/getPost", this.$route.params.type);
        // 게시물의 작성자와 현재 로그인한 사용자가 다르면
        if (this.post.userId.userid !== this.userinfo.userid) {
          // 뒤로가기
          this.$router.back()
        }
        // 카테고리 선택
        if (this.post.category==1) this.foodClick();
        else if (this.post.category==2) this.clothClick();
        else this.cosmeticClick();

        // 사진 리스트
        this.photoList = this.postPhotoList;

        // 내용 보여주기
        this.content = this.post.content;
        this.imgCnt = this.photoList.length
      }
    }
  }
</script>

<style>

</style>