<template>
  <div>
    <v-container>
      <!-- 잠깐 보이는 모달 -->
      <v-dialog v-model="modalOpen" max-width="300" persistent @click:outside="nothing">
        <v-card>
          <!-- 모달 타이틀 영역 -->
          <v-toolbar dense color="#004627">
            <v-toolbar-title class="modalTitle">
              <!-- {{ modaltitle }} -->
            </v-toolbar-title>
            <v-spacer></v-spacer>
            
          </v-toolbar>
          <!-- 모달 컨텐츠 영역 -->
          <v-container>
          <div class="modalContent">
            <div class="mb-3">
              <span class="modalContentMessage">
                {{ modalContent }}
              </span>
            </div>
          </div>
          <div class="modalContentButtonArea" v-if="modalConfirm">
            <v-spacer></v-spacer>
            <v-spacer></v-spacer>
            <button class="modalContentButton" @click="photoDelete(deletePhotoIdx)">확인</button>
            <v-spacer></v-spacer>
            <button class="modalContentButton" @click="modalOpen = false">취소</button>
            <v-spacer></v-spacer>
            <v-spacer></v-spacer>
          </div>
          </v-container>
        </v-card>
      </v-dialog>



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
        <div v-if="type==0">
          <div class='articlePic' v-if="!sendList.length">
            <label for="file"><v-icon>mdi-plus</v-icon></label>
            <input id="file" type="file" ref="files" multiple @input="fileUpload">
          </div>
          <!-- 캐러셀 영역 -->
          <v-carousel class="carouselBorder" hide-delimiters v-else height="30vh">
            <v-carousel-item v-for="(previewItem,idx) in previewItems" :key="idx" :src="previewItem.src" style="text-align:right">
              <!-- 사진 수정 -->
              <span @click="photoModify(idx)">
                <label for="modifyFile"><v-icon class="photoClick">mdi-check-circle-outline</v-icon></label>
                <input id="modifyFile" type="file" ref="modifyFiles" @input="modifyFileUploadRegist" style="width:0; height:0">  
              </span>
              <!-- 사진 삭제 -->
              <!-- <span @click="photoDelete(idx, 0)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>   -->
              <span @click="phtoDeleteModalPopup(idx)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>  
              <!-- 사진 추가 -->
              <span>
                <label for="newFile"><v-icon class="photoClick">mdi-plus-circle-outline</v-icon></label>
                <input id="newFile" type="file" ref="plusFiles" multiple @input="newFileUpload(0)" style="width:0; height:0">
              </span>
            </v-carousel-item>
          </v-carousel>
        </div>
        <!-- 수정 시 캐러셀 영역 -->
        <div v-else>
          <v-carousel class="carouselBorder" hide-delimiters height="30vh">
            <v-carousel-item v-for="(info,idx) in photoList" :key="idx" :src="photoPath(idx)" style="text-align:right">
              <!-- 사진 수정 -->
              <span @click="photoModify(idx)">
                <label for="modifyFile"><v-icon class="photoClick">mdi-check-circle-outline</v-icon></label>
                <input id="modifyFile" type="file" ref="modifyFiles" @input="modifyFileUpload" style="width:0; height:0">  
              </span>
              <!-- 사진 삭제 -->
              <!-- <span @click="photoDelete(idx, 1)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>   -->
              <span @click="phtoDeleteModalPopup(idx)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>  
              <!-- 사진 추가 -->
              <span>
                <label for="newFile"><v-icon class="photoClick">mdi-plus-circle-outline</v-icon></label>
                <input id="newFile" type="file" ref="plusFiles" multiple @input="newFileUpload(1)" style="width:0; height:0">
              </span>
            </v-carousel-item>
          </v-carousel>
        </div>
        
        <!-- 게시글 컨텐츠 입력 영역 -->
        <textarea v-model="content" placeholder="게시글 입력..."></textarea>
        
        <div v-if="type==0" :class="{'buttonDiv': articleCreateFormValid, 'disabledBtnDiv': !articleCreateFormValid}">
          <button @click="send" :disabled="!articleCreateFormValid">등록</button>
        </div>
        <div v-else class=buttonDiv><button @click="modify">수정</button></div>
      </section>
    </v-container>
  </div>
</template>

<script>
  import axios from "axios";
  import {mapState} from "vuex";
  import fileUpload from "@/api/fileUpload.js";
  import API from "@/api/index.js";

  export default {
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
        modalOpen: false,
        modalContent: '',
        modalConfirm: false,
        deletePhotoIdx: 0,
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
      nothing () {
        console.log('nothing')
      },
      foodClick () {
        // css 토글용
        this.foodSelected = true
        this.clothSelected = false
        this.cosmeticSelected = false
        // 백 데이터 설정용
        this.category = '1'
        // 로그를 찍어보면 this.category 가 변함을 알 수 있어요.
        // console.log(this.category) 
      },
      clothClick () {
        this.foodSelected = false
        this.clothSelected = true
        this.cosmeticSelected = false
        this.category = '2'
        // console.log(this.category)
      },
      cosmeticClick () {
        this.foodSelected = false
        this.clothSelected = false
        this.cosmeticSelected = true
        this.category = '3'
        // console.log(this.category)
      },
      fileUpload () {
        console.log("files : ", this.$refs.files.files);
        for (let i = 0; i < this.$refs.files.files.length; i++) {
          console.log(this.$refs.files.files[i]);
          // 1. 파일 업로드를 클릭 했을 시, 백에 보낼 sendList 를 포문 돌려 완성해 줍니다.
          this.sendList.push(this.$refs.files.files[i])
          // 2. 이번엔 preview 로 띄울 URL 을 포문 돌면서 생성해주고 캐러셀에 붙여 줍니다.
          const previewUrl = URL.createObjectURL(this.$refs.files.files[i])
          this.previewItems.push({src:previewUrl})
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
          // alert("보냈슴!");
          console.log(response.data);
          this.$router.push({ name: 'Main' })
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
          if (response.data.job=="success") this.$router.push({path:"/article_detail/"+this.post.postId});
        })
        .catch((error) => {
          console.log(error);
        })
      },
      photoModify(idx) {
        if (idx!=0) this.tempIdx = idx;
      },
      modifyFileUploadRegist() { // 게시글 등록 시 사진 수정
        const previewUrl = URL.createObjectURL(this.$refs.modifyFiles[0].files[0]);
        this.previewItems[this.tempIdx] = {"src":previewUrl};
        this.sendList[this.tempIdx] = this.$refs.modifyFiles[0].files[0];
        this.previewItems.push("지워");
        this.previewItems.splice(this.previewItems.length-1, 1);
      },
      modifyFileUpload() { // 게시글 수정 시 사진 수정
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
      },
      newFileUpload(type) {
        for (let i = 0; i < this.$refs.plusFiles[0].files.length; i++) {
          const previewUrl = URL.createObjectURL(this.$refs.plusFiles[0].files[i])
          if (type==0) {
            this.sendList.push(this.$refs.plusFiles[0].files[i])
            this.previewItems.push({src:previewUrl})
          } else {
            this.plusPhotoList.push(this.$refs.plusFiles[0].files[i])
            const previewUrl = URL.createObjectURL(this.$refs.plusFiles[0].files[i])
            this.photoList.push({
              "filesize":0,
              "url":previewUrl
            })
          }
        }
      },
      phtoDeleteModalPopup(idx) {
        this.modalConfirm = true
        this.modalContent = "정말 삭제하시겠습니까?"
        this.modalOpen = true
        this.deletePhotoIdx = idx
      },
      photoDelete(idx) { // 사진 삭제
        const type = this.type
        // var result = confirm("정말 삭제하시겠습니까?");
        // if (result) {
        // if (this.modalConfirmClicked) {
        if (type!=0) { // 게시글 수정일 때
          this.deleteIdList.push(this.photoList[idx].postPhotoId);
          this.photoList.splice(idx, 1);
        } else { // 게시글 등록일 때
          this.sendList.splice(idx, 1);
          this.previewItems.splice(idx, 1); 
        }
        this.modalConfirm = false
        this.modalContent = "삭제 되었습니다."
        setTimeout(() => {
          this.modalOpen = false
          this.modalContent = ""
        }, 500);
          // alert("삭제 되었습니다.");
        // }
      }
    },
    created() {
      this.type = this.$route.params.type;
      if (this.$route.params.type!=0) {
        this.$store.dispatch("post/getPost", this.$route.params.type);
        // 카테고리 선택
        if (this.post.category==1) this.foodClick();
        else if (this.post.category==2) this.clothClick();
        else this.cosmeticClick();

        // 사진 리스트
        this.photoList = this.postPhotoList;

        // 내용 보여주기
        this.content = this.post.content;
      }
    }
  }
</script>

<style>

</style>