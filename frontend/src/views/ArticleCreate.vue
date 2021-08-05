<template>
  <div>
    <v-container>
      <section class="article">
        <h4>게시글 작성</h4>
        <!-- 버튼들 -->
        <div class="categoryBtn_box">
          <button @click="foodClick" :class="{'articleBtnNotSelected':!foodSelected, 'articleBtnSelected':foodSelected}">음식</button>
          <button @click="clothClick" :class="{'articleBtnNotSelected':!clothSelected, 'articleBtnSelected':clothSelected}">옷</button>
          <button @click="cosmeticClick" :class="{'articleBtnNotSelected':!cosmeticSelected, 'articleBtnSelected':cosmeticSelected}">화장품</button>
        </div>
        <!-- 파일 업로드 영역  -->
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
        <!-- 게시글 컨텐츠 입력 영역 -->
        <textarea v-model="content" placeholder="게시글 입력..."></textarea>
        <button @click="send" class=articleSubmit>등록</button>
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
      }
    },
    computed: {
      ...mapState("user", ["userinfo", "isLogin"]),
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
          // 1. 파일 업로드를 클릭 했을 시, 백에 보낼 sendList 를 포문 돌려 완성해 줍니다.
          this.sendList.push(this.$refs.files.files[i])
          // 2. 이번엔 preview 로 띄울 URL 을 포문 돌면서 생성해주고 캐러셀에 붙여 줍니다.
          const previewUrl = URL.createObjectURL(this.$refs.files.files[i])
          this.previewItems.push({src:previewUrl})
        }
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

        // console.log(formData)
        // console.log(formData.entries())

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
      }
    }
  }
</script>

<style>

</style>