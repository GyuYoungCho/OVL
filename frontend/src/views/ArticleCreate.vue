<template>
  <div>
    <v-container>
      <section class="article">
        <h4>게시글 작성</h4>
        <div class="categoryBtn_box">
          <button class="articleBtnNotSelected">음식</button>
          <button class="articleBtnNotSelected">옷</button>
          <button class="articleBtnNotSelected">화장품</button>
        </div>
        <div class='articlePic'>
          <input type="file" ref="files" multiple @input="fileUpload">
          <v-icon>mdi-plus</v-icon>
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
        // 버튼 관련 class list toggle
        foodSelected: false,
        clothSelected: false,
        cosmeticSelected: false,
        btnSelected: false,
        // 백에 보내기 위한 데이터
        categori: "",
        content: "",
        // userId 는 vuex로 관리될거니까 작성.vue 에선 보일 필요 없음
        userId: "",
        sendList: [],
      }
    },
    methods: {
      fileUpload (event) {
        console.log(this.$refs.files.files)
        // const inputFiles = this.$refs.files.files
        // console.log(inputFiles)
        console.log(event)
      },
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
        console.log(formData.entries())

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