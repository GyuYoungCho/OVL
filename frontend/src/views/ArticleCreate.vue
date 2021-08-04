<template>
  <div>
    <v-container>
      <section class="article">
        <h4>게시글 작성</h4>
        <div class="categoryBtn_box">
          <button @click="foodClick" :class="{'articleBtnNotSelected':!foodSelected, 'articleBtnSelected':foodSelected}">음식</button>
          <button @click="clothClick" :class="{'articleBtnNotSelected':!clothSelected, 'articleBtnSelected':clothSelected}">옷</button>
          <button @click="cosmeticClick" :class="{'articleBtnNotSelected':!cosmeticSelected, 'articleBtnSelected':cosmeticSelected}">화장품</button>
        </div>
        <div class='articlePic'>
          <label for="file"><v-icon>mdi-plus</v-icon></label>
          <input id="file" type="file" ref="files" multiple @input="fileUpload">
        </div>
        <textarea v-model="content" placeholder="게시글 입력..."></textarea>
        <button @click="send" class=articleSubmit>등록</button>
      </section>
    </v-container>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    data () {
      return {
        // 버튼 관련 css class list toggle 용 변수들 설정
        foodSelected: false,
        clothSelected: false,
        cosmeticSelected: false,
        btnSelected: false,
        // 백에 보내기 위한 데이터들
        categori: "",
        content: "",
        // userId 는 vuex로 관리될거니까 작성.vue 에선 보일 필요 없음
        userId: "",
        sendList: [],
      }
    },
    methods: {
      foodClick () {
        // css 토글용
        this.foodSelected = true
        this.clothSelected = false
        this.cosmeticSelected = false
        // 백 데이터 설정용
        this.categori = '1'
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
      fileUpload (event) {
        console.log(this.$refs.files.files)
        // const inputFiles = this.$refs.files.files
        // console.log(inputFiles)
        console.log(event)
      },
      // @@@@@@@@@@@@ 미충족시 막을 로직 필요함 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
      send() { // 게시글 등록을 위해 백으로 게시글 정보 보내는 함수
        const formData = new FormData();
        for (var index=0;index<this.sendList.length;index++) {  // 등록할 사진들 저장
          formData.append('files', this.sendList[index]);
        }
        this.sendList=[]; // formData에 append 후 이미지 리스트 비워주기
        formData.append('categori', this.categori); // 카테고리 
        formData.append('content', this.content); // 내용
        formData.append('userId', this.userId); // 유저 아이디

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