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

            <div class="mt-10" style="color: #a4bca5">
                현재 참여중인 챌린지가 없습니다.
                <div>챌린지 탭을 열어 챌린지를 확인하고 참여해보세요!</div>
            </div>
</v-container>
</template>

<script>

import {mapState, mapActions} from "vuex";


export default {
    data() {
        return {
        showAll: true,
        btnActive: {0:false,1:false,2:false},
        }
    },
    methods: {

        ...mapActions("challenge", ["fetchUserChallengeList", "challengeAttend"]),
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
        ...mapState("user", (["userinfo"])),
    },

    }
</script>