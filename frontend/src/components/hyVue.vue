<template>
  <div class="home">
      카테고리 : <input type="text" v-model="categori">
      내용 : <input type="text" v-model="content">
      파일 : <input type="file" id="file" ref="files" multiple="multiple" @change="upload"/>
      유저 아이디 : <input type="text" v-model="searchUser"> 
      <button @click="search">검색</button>
      <button @click="send">전송~</button>
      <hr>
      미리보기 : <img :src="this.filename" width="100%"><br>
      <hr>
      <!-- 게시글 불러오기 -->
      <button @click="getList">불러오기</button>
      <hr>
      <div v-for="(info,idx) of list" :key="idx">
        <h3>프로필사진 {{info.postId.userId.nickname}}</h3>
        <img :src="srcPath(idx)" width="100%" @click="detail(idx)"><br>
        {{categoriTrnas(info.postId.categori)}} >> 
        {{info.postId.content}} <br>
        좋아요 {{info.postId.like_count}} | 댓글 {{info.postId.comment_count}}
        <hr>
      </div>
  </div>
</template>

<style>
img {
  width:200px;
  height: 200px;
}
</style>

<script>
// @ is an alias to /src
import rest from "@/js/httpCommon.js";
import axios from "axios";

export default {
  name: "Home",
  data() {
    return {
      categori:"", // 카테고리 1 : 음식, 2 : 옷, 3 : 화장품
      content:"", // 내용
      searchUser:"", // 유저 아이디로 검색
      filename:"", // 미리보기를 위한 변수
      userId:"", // 게시글 작성하는 유저 아이디
      user:{}, // 게시글 작성하는 유저 객체
      list:[], // 백에서 가져온 게시글 리스트
      sendList:[], // 백에 보내는 이미지 정보를 담은 리스트
    }
  },
  methods: {
    categoriTrnas(postCategori) { // 카테고리 타입 변환
      let type = "";

      if (postCategori==1) type="음식";
      else if (postCategori==2) type="옷";
      else type="화장품";

      return type;
    },
    detail(idx) { // 사진 누르면 게시글 상세보기
      console.log(this.list[idx].postId.postId);
      this.$router.push({
        path:"/about/"+this.list[idx].postId.postId,
      })
    },
    srcPath(idx){ // 이미지 출력을 위한 경로 설정
      return "http://localhost:8080/post/"+this.list[idx].postId.postId+"/"+this.list[idx].filepath.split('/').reverse()[0];
      },
    getList() { // 뉴스피드 화면을 위해 게시글 리스트 받아오는 함수
      rest.axios({
        url:"/post/select_all/"+this.searchUser,
        method:"get",
      }).then((res)=>{
        console.log(res.data);
        this.list = res.data;
        console.log("list : ",this.list);
        console.log(this.list[0]);
      }).catch((err)=>{
        alert("에러발생");
        console.log(err);
      })
    },
    search() { // 유저 검색하는 함수
      rest.axios({
        url:"/user/select/"+this.searchUser,
        method:"get",
      }).then((res)=>{
        console.log(res.data);
        this.user = res.data;
        this.userId = res.data.userid;
      }).catch((err)=>{
        alert("에러발생");
        console.log(err);
      })
    },
    upload() { // 이미지 업로드 관련 함수
      this.sendList.push(this.$refs.files.files[0]); // 이미지 업로드 할 때마다 sendList에 이미지 정보 추가
      this.filename = URL.createObjectURL(this.$refs.files.files[0]); // 미리보기를 위해 url 만들어주기
      console.log("filename : ", this.$refs.files.files[0]);
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
  },
};
</script>