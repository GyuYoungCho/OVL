<template>
<v-container>
        <div class="px-7 mt-4">
        </div>
            <!-- 사진 배열하기 -->
            <section  class="postArea">
                <div>
                    <div v-for="(info, idx) in postList" :key="idx" class="wrapper">
                        
                        <!-- post 대표 사진, 내용-->
                        <div @click="moveDetail(idx)" class="box">
                            <img :src="info.filepath" width=100%  style="border-radius: 7px;">  

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
    }
  },
  methods: {
    moveDetail(idx) { // 게시글 상세보기
      this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
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
