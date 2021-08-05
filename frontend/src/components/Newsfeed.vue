<template>
  <div>
    <v-container>
      <!-- 캐러셀 영역 -->
      <!-- <v-carousel hide-delimiters>
        <v-carousel-item
          v-for="(previewItem,i) in previewItems"
          :key="i"
          :src="previewItem.src"
        ></v-carousel-item>
      </v-carousel> -->

      <div v-for="(info, idx) in postList" :key="idx" @click="moveDetail(idx)">
        <!-- <img :src="userPath(idx)"> -->
        {{info.postId.userId.nickname}} <br>
        <img :src="postPath(idx)" height="30vh"> <br>
        {{info.postId.content}} <br>
        {{info.postId.like_count}} | {{info.postId.comment_count}} <br>
      </div>
    </v-container>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  data() {
    return {

    }
  },
  methods: {
    postPath(idx){ // 이미지 출력을 위한 경로 설정
      return "http://localhost:8080/post/"+this.postList[idx].postId.postId+"/"+this.postList[idx].filepath.split('/').reverse()[0];
    },
    userPath(idx) {
      console.log(this.postList[idx].postId.userId.userid);
      return "http://localhost:8080/post/"+this.postList[idx].postId.userId.userid+"/"+this.postList[idx].postId.userId.stored_file_path.split('/').reverse()[0];
    },
    moveDetail(idx) {
      console.log("postId : "+this.postList[idx].postId.postId);
      this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
    }
  },
  computed: {
    ...mapState("post", (["postList"])),
    ...mapState("user", (["userinfo"])),
  },
  created() {
    this.$store.dispatch("post/getPostList", this.userinfo.userid);
  }
}
</script>

<style>

</style>