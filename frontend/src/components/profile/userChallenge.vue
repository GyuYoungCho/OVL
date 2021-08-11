<template>
<v-container>
        <div class="px-7 mt-4">
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
    btnActive: {0:true,1:false,2:false},
    }
},
methods: {
    moveDetail(idx) { // 게시글 상세보기
    this.$router.push({path:"/article_detail/"+this.postList[idx].postId.postId});
    },
    selectTypeIcon(num){
      //console.log(num);
    this.post.step = this.allSteps[num]
     // console.log(this.btnActive[num])
    if(!this.btnActive[num]){
        this.btnActive[num] = true;
        for(var i = 0; i< 3; i++){
        if(num != i){
            this.btnActive[i] = false;
        }
        }
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
