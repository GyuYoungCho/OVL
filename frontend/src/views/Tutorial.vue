<template>
  <div class="tutorial">
      <FlashModal :modalOpen="modalOpen" title="회원가입" modalContent="회원가입 페이지로 이동합니다" />

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

  </div>
</template>

<script>
import TutorialNewsfeed from '@/components/tutorials/TutorialNewsfeed.vue';
import TutorialRecipe from '@/components/tutorials/TutorialRecipe.vue';
import TutorialPot from '@/components/tutorials/TutorialPot.vue';
import TutorialChallenge from '@/components/tutorials/TutorialChallenge.vue';
import TutorialProfile from '@/components/tutorials/TutorialProfile.vue';
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
  components: {
    TutorialNewsfeed, TutorialRecipe, TutorialPot, TutorialChallenge, TutorialProfile, FlashModal
  },

  data () {
    return {
      page: 1,
      modalOpen: false
    }
  },

  methods: {
    skipClick () {
      this.modalOpen = true
      setTimeout(()=>{
        this.modalOpen = false
        this.$router.push({name:'Signup'})
      }, 1000)
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
        this.modalOpen = true
        setTimeout(()=>{
          this.modalOpen = false
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