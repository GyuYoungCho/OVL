<template>
  <div class="tutorial">
    <!-- skip 버튼 영역 => 어떤 컴포넌트에도 동일하다-->
    <div v-if="page!==5" @click="skipClick" class="tutorialHeader">
      <span>skip</span>
    </div>
    <section>
    <!-- 왼쪽으로 가는 버튼 -->
      <div v-if="page!==1" class="tutorialBtnDivLeft" @click="clickLeft">
        <v-icon class="tutorialChevron">mdi-chevron-left</v-icon>
      </div>
    <!-- 튜토리얼 페이지들 컴포넌트 -->
    <transition name='component-fade'>
      <TutorialNewsfeed v-if="page===1" />
      <TutorialRecipe v-else-if="page===2" />
      <TutorialPot v-else-if="page===3" /> 
      <TutorialChallenge v-else-if="page===4" />
      <TutorialProfile v-else/>
    </transition>
    <!-- 오른쪽으로 가는 버튼 -->
      <div class="tutorialBtnDivRight" @click="clickRight">
        <v-icon class="tutorialChevron">mdi-chevron-right</v-icon>
      </div>
    </section>

    <!-- 커스텀 모달 영역-->
    <v-dialog v-model="willYouJoin" max-width="300">
      <v-card>
        <!-- 모달 타이틀 영역 -->
        <v-toolbar dense color="#49784B">
          <v-toolbar-title class="modalTitle">회원가입</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon dark @click="willYouJoin = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <!-- 모달 컨텐츠 영역 -->
        <v-container>
        <div class="modalContent">
          <div class="mb-3">
            <span class="modalContentMessage">
              회원가입 페이지로 이동합니다
            </span>
          </div>
        </div>
        </v-container>
      </v-card>
    </v-dialog>

  </div>
</template>

<script>
import TutorialNewsfeed from '@/components/tutorials/TutorialNewsfeed.vue';
import TutorialRecipe from '@/components/tutorials/TutorialRecipe.vue';
import TutorialPot from '@/components/tutorials/TutorialPot.vue';
import TutorialChallenge from '@/components/tutorials/TutorialChallenge.vue';
import TutorialProfile from '@/components/tutorials/TutorialProfile.vue';


export default {
  components: {
    TutorialNewsfeed, TutorialRecipe, TutorialPot, TutorialChallenge, TutorialProfile
  },

  data () {
    return {
      page: 1,
      willYouJoin: false
    }
  },

  methods: {
    skipClick () {
      this.$router.push({name:'Signup'})
    },
    clickLeft () {
      if (this.page <= 1 ) {
        return
      } else {
        this.page--
      }
    },
    clickRight () {
      if (this.page === 5 ) {
        this.willYouJoin = true
        setTimeout(()=>{
          this.willYouJoin = false
          this.$router.push({name:'Signup'})
        }, 1000)
      } else {
        this.page++
      }
    },
    finallyJoin () {
      this.$router.push({name:'Signup'})
    }

  }

}
</script>

<style>

</style>