<template>
  <div>
    <FlashModal :modalOpen="modalOpen" :modalContent="modalContent" />
    <!-- dense 하면 좀더 좁은 nav bar (48px 이 가능함) -->
      <v-app-bar color="white" dense class="navBar">
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
        <!-- justify content between 해주는 태그가 v-spacer -->
        <v-spacer></v-spacer>
        <span class="navbarTitle">OVL</span>
        <v-spacer></v-spacer>
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </template>
          <v-list>
          <v-list-item v-for="(item, index) in items" :key="index">
            <v-list-item-title>
              <RouterLink class='text-dec' :to="{ name: item.routerName, params: { type: 0 } }">
                {{ item.title }}
              </RouterLink>
            </v-list-item-title>
          </v-list-item>
          </v-list>
        </v-menu>
      </v-app-bar>

      <v-navigation-drawer v-model="drawer" absolute temporary>
        <v-list nav dense>
          <!-- <v-list-item-group v-model="group" active-class="deep-purple--text text--accent-4"> -->
          <v-list-item-group v-model="group" active-class="green">

            <RouterLink :to="{ name: 'Main' }">
             <v-list-item class="mb-2 text-dec">
               <v-icon>mdi-home-export-outline</v-icon>
              <v-list-item-title class="ms-4">홈</v-list-item-title>
              </v-list-item>
            </RouterLink>
            
            <RouterLink :to="{ name: 'Profile', params: { userid: userinfo.userid} }">           
              <v-list-item class="mb-2 text-dec">
                <v-icon>mdi-card-account-details-outline</v-icon>
                <v-list-item-title class="ms-4">내 프로필</v-list-item-title>
              </v-list-item>
            </RouterLink>

            <RouterLink :to="{name:'VetPartyList'}">
              <v-list-item class="mb-2 text-dec">
                <v-icon>mdi-sprout-outline</v-icon>
                <v-list-item-title class="ms-4">채식팟</v-list-item-title>
              </v-list-item>
            </RouterLink>

            <RouterLink :to="{name:'RecipeSearch'}">
              <v-list-item class="mb-2 text-dec">
                <v-icon>mdi-file-outline</v-icon>
                <v-list-item-title class="ms-4">레시피</v-list-item-title>
              </v-list-item>
            </RouterLink>

            <RouterLink :to="{name:'ChallengeList'}">
              <v-list-item class="mb-2 text-dec">
                <v-icon>mdi-controller-classic-outline</v-icon>
                <v-list-item-title class="ms-4">챌린지</v-list-item-title>
              </v-list-item>
            </RouterLink>

            <RouterLink :to="{name:'Main'}">
              <v-list-item class="mb-2 text-dec">
                <v-icon>mdi-map-marker-radius-outline</v-icon>
                <v-list-item-title class="ms-4">지도</v-list-item-title>
              </v-list-item>
            </RouterLink>

            <v-list-item class="mb-2">
              <v-icon>mdi-home-remove-outline</v-icon>
              <v-list-item-title class="ms-4" @click.prevent="onClickLogout()">로그아웃</v-list-item-title>
            </v-list-item>

            
          </v-list-item-group>
        </v-list>
      </v-navigation-drawer>
  </div>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import FlashModal from '@/components/signup/FlashModal.vue'


  export default {
    components: {
      FlashModal,
    },
    data: () => ({
      drawer: false,
      group: null,
      items: [
        { title: '게시글 생성', routerName: 'ArticleCreate'},
        { title: '레시피 생성', routerName: 'RecipeCreate' },
        { title: '채식팟 생성', routerName: 'VetPartyCreate' },
      ],
      modalOpen: false,
      modalContent: '',
    }),

    watch: {
      group () {
        this.drawer = false
      },
    },
    computed:{
        ...mapGetters("user", ["userinfo", "isLogin"]),
    },
    methods: {
      ...mapActions ("user", ["getTokenUserInfo", "logout"]),

      onClickLogout(){
        this.modalContent = "로그아웃 되었습니다."
        this.modalOpen = true
        setTimeout(() => {
          this.modalOpen = false
          this.logout()
        }, 1000);
      }
    },
  }
</script>

<style lang="scss" scoped>
.text-dec {
  text-decoration: none !important;
  color: black !important;
}

</style>