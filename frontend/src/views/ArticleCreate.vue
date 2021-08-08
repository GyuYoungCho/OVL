<template>
  <div>
    <v-container>
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
            <v-carousel-item
              v-for="(previewItem,i) in previewItems"
              :key="i"
              :src="previewItem.src"
            ></v-carousel-item>
          </v-carousel>
        </div>
        <!-- 수정 시 캐러셀 영역 -->
        <div v-else>
          <v-carousel class="carouselBorder" hide-delimiters height="30vh">
            <v-carousel-item v-for="(info,idx) in photoList" :key="idx" :src="photoPath(idx)" style="text-align:right">
              
              <span @click="photoModify(idx)">
                <label for="modifyFile"><v-icon class="photoClick">mdi-check-circle-outline</v-icon></label>
                <input id="modifyFile" type="file" ref="modifyFiles" @input="modifyFileUpload" style="width:0; height:0">  
              </span>
              <span @click="photoDelete(idx)"><v-icon class="photoClick">mdi-close-circle-outline</v-icon></span>  
              <span>
                <label for="newFile"><v-icon class="photoClick">mdi-plus-circle-outline</v-icon></label>
                <input id="newFile" type="file" ref="plusFiles" multiple @input="newFileUpload" style="width:0; height:0">
              </span>
            </v-carousel-item>
          </v-carousel>
        </div>
        
        <!-- 게시글 컨텐츠 입력 영역 -->
        <textarea v-model="content" placeholder="게시글 입력..."></textarea>
        
        <div v-if="type==0" class=buttonDiv><button @click="send">등록</button></div>
        <div v-else class=buttonDiv><button @click="modify">수정</button></div>
      </section>
    </v-container>
  </div>
</template>

<script>
  import axios from "axios";
  import {mapState} from "vuex";

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
        categori: "",
        content: "",
        // userId 는 vuex로 관리될거니까 작성.vue 에선 보일 필요 없음
        sendList: [],
        // ↓ 여기서부터 게시글 수정 관련 변수
        type:0,
        deleteIdList:[], // 삭제할 사진 아이디 저장하는 리스트
        photoList:[], // 게시글 수정 시 가져온 사진 리스트
        modifyPhotoList: [], // 게시글 수정 사진 저장할 리스트
        modifyIdList: [], // 수정하는 사진 아이디 저장할 리스트
        plusPhotoList: [], // 새로 추가하는 사진 저장할 리스트
        tempIdx:0, // 임시 저장
      }
    },
    computed: {
      ...mapState("user", ["userinfo", "isLogin"]),
      ...mapState("post", ["post", "postPhotoList"]),
      typeTrans() {
        return this.$route.params.type;
      },
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
        // 백 데이터 설정용
        this.categori = '1'
        // 로그를 찍어보면 this.categori 가 변함을 알 수 있어요.
        // console.log(this.categori) 
      },
      clothClick () {
        this.foodSelected = false
        this.clothSelected = true
        this.cosmeticSelected = false
        this.categori = '2'
        // console.log(this.categori)
      },
      cosmeticClick () {
        this.foodSelected = false
        this.clothSelected = false
        this.cosmeticSelected = true
        this.categori = '3'
        // console.log(this.categori)
      },
      fileUpload () {
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
        else return "http://localhost:8080/post/"+this.post.postId+"/"+this.photoList[idx].filepath.split('/').reverse()[0];
      },
      // @@@@@@@@@@@@ 미충족시 막을 로직 필요함 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
      send() { // 게시글 등록을 위해 백으로 게시글 정보 보내는 함수'
        const formData = new FormData();
        for (var index=0;index<this.sendList.length;index++) {  // 등록할 사진들 저장
          formData.append('files', this.sendList[index]);
        }
        this.sendList=[]; // formData에 append 후 이미지 리스트 비워주기
        formData.append('categori', this.categori); // 카테고리 
        formData.append('content', this.content); // 내용
        formData.append('userId', this.userinfo.userid); // 유저 아이디

        axios.post('http://localhost:8080/post/regist', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then((response) => {
          alert("보냈슴!");
          console.log(response.data);
        })
        .catch((error) => {
          alert("못보냈슴!");
          console.log(error);
        })
      },
      modify() { // 게시글 수정
        const formData = new FormData();
        var file = new File(["ex"], "ex.txt", {
          type: "text/plain",
        });

        if (this.modifyPhotoList.length==0) formData.append('modifyPhotoList', file) // 수정을 하나도 안할 때
        if (this.plusPhotoList.length==0) formData.append('plusPhotoList', file) // 추가를 하나도 안할 때

        // 수정할 사진 리스트
        for (let i=0;i<this.modifyPhotoList.length;i++) {
          formData.append('modifyPhotoList', this.modifyPhotoList[i]);
        }
        // 추가할 사진 리스트
        for (let i=0;i<this.plusPhotoList.length;i++) {
          formData.append('plusPhotoList', this.plusPhotoList[i]);
        }
        
        formData.append('category', this.categori); // 카테고리 
        formData.append('content', this.content); // 내용
        formData.append('postId', this.post.postId);
        

        var params = new URLSearchParams();
        params.append("deleteIdList", this.deleteIdList);
        params.append("modifyIdList", this.modifyIdList);
        
        axios.post('http://localhost:8080/post/modifyList'
        , params).then((res)=> {
          alert("리스트 보냈슴!");
          if (res.data=="success") {
            axios.put('http://localhost:8080/post/modify', formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            })
            .then((response) => {
              alert("보냈슴!");
              if (response.data.job=="success") this.$router.push({path:"/"});
              console.log(response.data);
            })
            .catch((error) => {
              alert("못보냈슴!");
              console.log(error);
            })
          }
        }).catch((err)=> {
          alert("리스트 못보냈슴!!");
          console.log(err);
        })
      },
      photoModify(idx) {
        if (idx!=0) this.tempIdx = idx;
      },
      modifyFileUpload() {
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
      newFileUpload() {
        for (let i = 0; i < this.$refs.plusFiles[0].files.length; i++) {
          this.plusPhotoList.push(this.$refs.plusFiles[0].files[i])
          const previewUrl = URL.createObjectURL(this.$refs.plusFiles[0].files[i])
          this.photoList.push({
            "filesize":0,
            "url":previewUrl
          })
        }
      },
      photoDelete(idx) {
        var result = confirm("정말 삭제하시겠습니까?");
        if (result) {
          this.deleteIdList.push(this.photoList[idx].postPhotoId);
          this.photoList.splice(idx, 1);
          alert("삭제 되었습니다.");
        }
      }
    },
    created() {
      this.type = this.$route.params.type;
      if (this.$route.params.type!=0) {
        this.$store.dispatch("post/getPost", this.$route.params.type);
        // 카테고리 선택
        if (this.post.categori==1) this.foodClick();
        else if (this.post.categori==2) this.clothClick();
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