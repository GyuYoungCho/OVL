<template>
    <v-dialog v-model="modalOpen" max-width="300" @click:outside="modalOpen = false">
        <v-card>
            <!-- 모달 타이틀 영역 -->
            <v-toolbar dense color="#49784B">
            <v-toolbar-title class="modalTitle">랭킹</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="modalCancelBtnClick">
                <v-icon>mdi-close</v-icon>
            </v-btn>
            </v-toolbar>
            <!-- 모달 컨텐츠 영역 -->
            <v-container>
            <div class="modalContent">
                <div class="mb-3">
                    <span class="modalContentMessage">
                        <div>
                            <div v-if="step==1" class="number1" >
                                <img src="@/assets/image/OVLKoongya.png" alt=""/>
                                <div> 쿵야가 아직 아기에요! 좀 더 많은 게시물을 올려보세요! 머리에 새싹이 자라날 거에요! </div>
                            </div>
                            <div v-else-if="step==2" class="number1" >
                                <img src="@/assets/image/OVLoongya.png" alt=""/>
                                <div> 쿵야가 많이 자랐어요! 쿵야를 더 키워보세요! <br> 머리에 꽃이 자라날 거에요! </div>
                            </div>
                            <div v-else-if="step==3" class="number1" >
                                <img src="@/assets/image/OVLoong.png" alt=""/>
                                <div> 쿵야가 다 자랐어요! 지금도 충분히 잘하고 있으니 더 열심히 참여해주세요! </div>
                            </div>
                                    
                        </div>
                        <br/>
                        <div class="text-center">
                            <div> &lt; <span style="color: #49784B; font-size:large">{{user.nickname}}</span> 님의 랭킹 &gt;</div> 
                            <div> Rank는 <span style="font-size: large; color:  #49784B;">{{rank}}</span> 등 !! </div>
                            <div> Vegan Score는 <span style="font-size: large; color:  #49784B;">{{user.experience}}</span>  점 입니다 ! </div>
                        </div>
                    </span>
                </div>
            </div>
            </v-container>
        </v-card>
    </v-dialog>
</template>

<script>
export default {
  props: {
    modalOpen: Boolean,
    step: Number,
    rank: Number,
    user: Object,
  },
  methods: {
    modalCancelBtnClick () {
      this.$emit('modalCancelBtnClick')
    },
  }
}
</script>

<style>

</style>