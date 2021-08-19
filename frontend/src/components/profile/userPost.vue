<template>
    <v-container class="mb-4">
      <div class="icon mb-4">
            <button class="icon-btn" v-if="!btnActive[0]" @click="selectTypeIcon(0)">
            <img src="@/assets/icon/recipe.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(0)">
            <img src="@/assets/icon/recipe_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[1]" @click="selectTypeIcon(1)" >
            <img src="@/assets/icon/cream.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(1)">
            <img src="@/assets/icon/cream_selected.png" alt="" class="icon-btn-img"></button>

            <button class="icon-btn" v-if="!btnActive[2]" @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger.png" alt="" class="icon-btn-img"></button>
            <button class="icon-btn" v-else @click="selectTypeIcon(2)">
            <img src="@/assets/icon/hanger_selected.png" alt="" class="icon-btn-img"></button>
      </div>
            <!-- 사진 배열하기 -->
            <v-container v-if="showAll">
              <v-row>
                <v-col v-for="(info, idx) in postMyList" :key="idx" cols="4" class="grid-cell-2">
                    <!-- post 대표 사진, 내용-->
                      <!-- <div v-if="NoPost" class="mt-10" style="color: #a4bca5">
                        현재 작성한 포스트가 없습니다.
                      <div>게시글을 생성해 점수를 획득해보세요!</div>
                    </div> -->
                    <div @click="moveDetail(idx)" class="postImg">
                        <img :src="info.filepath" width="90px" height="90px">   
                    </div>

                </v-col>
              </v-row>
            </v-container>
            <v-container v-else-if="btnActive[0]">
              <v-row v-if="this.foodPostlistd">
                <v-col v-for="(info, idx) in foodPostList" :key="idx" cols="4" class="grid-cell-2">
                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="postImg">
                        <img :src="info.filepath" width="90px" height="90px">    
                    </div>
                </v-col>
              </v-row>
            </v-container> 
            <v-container v-else-if="btnActive[2]">
              <v-row>
                <v-col v-for="(info, idx) in clothPostList" :key="idx" cols="4" class="grid-cell-2">
                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="postImg">
                        <img :src="info.filepath" width="90px" height="90px">  
                    </div>
                </v-col>
              </v-row>
            </v-container> 
            <v-container v-else-if="btnActive[1]">
              <v-row>
                <v-col v-for="(info, idx) in cosmeticPostList" :key="idx" cols="4" class="grid-cell-2">
                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="postImg">
                        <img :src="info.filepath" width="90px" height="90px">  
                    </div>
                </v-col>
              </v-row>
            </v-container> 
    </v-container>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  data() {
    return {
      NoPost: false,
      NoFoodPost: false,
      NoCosmeticPost: false,
      NoClothPost:false,
      showAll: true,
      btnActive: {0:false,1:false,2:false},
    }
  },
  methods: {
        //...mapActions("post", ["getUserPostList"]),
    moveDetail(idx) { // 게시글 상세보기
      this.$router.push({path:"/article_detail/"+this.postMyList[idx].postId.postId});
    },
    selectTypeIcon(num){
        if(this.btnActive[num] === true){
            this.btnActive[num] = false;
            this.showAll = true;
            // console.log(num)
            // console.log("btn",this.btnActive)
            // console.log("show",this.showAll)
        }else if(this.btnActive[num] ===false){
            this.btnActive[num] = true;
            this.showAll = false;
            for(var i = 0; i < 3; i++){
              if(i !== num){
                this.btnActive[i] = false;
              }
            }
        }
    },
  },
  computed: {
    ...mapGetters("post", ["postMyList", "foodPostList", "clothPostList", "cosmeticPostList"]),
    ...mapGetters("user", ["userinfo"]),
    
  },
  created() {
    this.$store.dispatch("post/getUserPostList", this.userinfo.userid);
  },
}
</script>
