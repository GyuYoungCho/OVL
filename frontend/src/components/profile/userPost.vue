<template>
    <v-container class="mb-4">
      <div class="icon mb-4">
            <button class="icon-btn" v-if="!btnActive[0]" @click="selectTypeIcon(0)" >
            <img src="@/assets/icon/cream.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(0)">
            <img src="@/assets/icon/cream_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[1]" @click="selectTypeIcon(1)">
            <img src="@/assets/icon/recipe.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(1)">
            <img src="@/assets/icon/recipe_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[2]" @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger_selected.png" alt="" class="icon-btn-img"></button>
      </div>
            <!-- 사진 배열하기 -->
            <section class="postArea">
            <div class="wrapper d-flex">
              <br/>
                <div v-for="(info, idx) in postList" :key="idx">
                    
                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="box">
                        <img :src="postPath(idx)" width=80px;>  

                    </div>
                </div>
            </div>
        </section>
    </v-container>
</template>

<script>
import {mapState} from "vuex";

export default {
  data() {
    return {
        post : {
          type : "포스트",
          step : "화장품",
        },
        allSteps: [
          "화장품", "레시피", "옷",
        ],
      showAll: false,
      btnActive: {0:true,1:false,2:false},
    }
  },
  methods: {
    postPath(idx){ // 대표 이미지 출력
      return "http://localhost:8080/post/"+this.postList[idx].postId.postId+"/"+this.postList[idx].filepath.split('/').reverse()[0];
    },
    moveDetail(idx) { // 게시글 상세보기
      this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
    },
    selectTypeIcon(num){
        if(this.btnActive[num] === true){
            this.btnActive[num] = false;
            this.showAll = true;
            console.log(num)
            console.log("btn",this.btnActive)
            console.log("show",this.showAll)
        }else if(this.btnActive[num] ===false){
            this.btnActive[num] = true;
            this.showAll = false;
            for(var i = 0; i < 3; i++){
              if(i !== num){
                this.btnActive[i] = false;
              }
            }
            console.log(num)
            console.log("btn",this.btnActive)
            console.log("show",this.showAll)
        }
    },
  },
  computed: {
    ...mapState("post", (["postList", "postLikeList"])),
    ...mapState("user", (["userinfo"])),
  },
  created() {
    this.$store.dispatch("post/getPostList", this.userinfo.userid);
    this.$store.dispatch("post/getPostLikeList", this.userinfo.userid);
  },
}
</script>
