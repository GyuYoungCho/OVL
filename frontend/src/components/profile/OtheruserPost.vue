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
                <v-col v-for="(info, idx) in otherpostList" :key="idx" cols="4" class="grid-cell">

                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="box">
                        <img :src="info.filepath" width=80px;>   

                    </div>

                </v-col>
              </v-row>
            </v-container>
            <v-container v-else-if="btnActive[0]">
              <v-row>
                <v-col v-for="(info, idx) in otherfoodPostList" :key="idx" cols="4" class="grid-cell">

                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="box">
                        <img :src="info.filepath" width=80px;>    

                    </div>

                </v-col>
              </v-row>
            </v-container> 
            <v-container v-else-if="btnActive[2]">
              <v-row>
                <v-col v-for="(info, idx) in otherclothPostList" :key="idx" cols="4" class="grid-cell">

                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="box">
                        <img :src="info.filepath" width=80px;>  

                    </div>

                </v-col>
              </v-row>
            </v-container> 
            <v-container v-else-if="btnActive[1]">
              <v-row>
                <v-col v-for="(info, idx) in othercosmeticPostList" :key="idx" cols="4" class="grid-cell">

                    <!-- post 대표 사진, 내용-->
                    <div @click="moveDetail(idx)" class="box">
                        <img :src="info.filepath" width=80px;>  

                    </div>

                </v-col>
              </v-row>
            </v-container> 
    </v-container>
</template>

<script>
import {mapGetters, mapActions} from "vuex";
import axios from "axios";
import API from '@/api/index.js'
import postAPI from '@/api/post.js'

export default {
  data() {
    return {

      showAll: true,
      btnActive: {0:false,1:false,2:false},
      otherpostList: [],
      otherfoodPostList: [],
      otherclothPostList: [],
      othercosmeticPostList: [],
    }
  },
  methods: {
        ...mapActions("post", ["getPostList", ]),
    moveDetail(idx) { // 게시글 상세보기
      this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
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
            // console.log(num)
            // console.log("btn",this.btnActive)
            // console.log("show",this.showAll)
        }
    },
  },
  computed: {
    ...mapGetters("user", ["userinfo"]),
  },
  created() {
      axios({
        method: "get",
        url: API.url + postAPI.select_all(this.$route.params.userid),
      }).then((res)=>{
        if(res.data){
          this.otherpostList = res.data
          this.otherfoodPostList = res.data.filter((eachList)=> eachList.postId.category===1)
          this.otherclothPostList = res.data.filter((eachList)=> eachList.postId.category===2)
          this.othercosmeticPostList = res.data.filter((eachList)=> eachList.postId.category===3)
        }

      })
  },
}
</script>
