<template>
  <div>
    <v-dialog
          v-model="pot_detail_modal"
          hide-overlay
          fullscreen
          transition="dialog-bottom-transition"
          max-width="500px"
          scrollable>
        <v-card tile>
          <v-toolbar color="#004627" dark>
            <v-btn icon dark @click="cancelDetail">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          <v-toolbar-title >{{this.potitem.title}}</v-toolbar-title>
            <v-spacer></v-spacer>
          </v-toolbar>
            <v-row>
              <v-col>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
                  <v-icon>mdi-map-marker</v-icon>
                  <span>{{this.potitem.restaurant_name}}</span>
              </v-col>
              <v-col>
                  <profile-name v-for="(auser, index) in potattendusers" :key="index" :user="auser"></profile-name>
              </v-col>
            </v-row>
          </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import { mapGetters, mapActions} from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';

export default {
  components: { 
    ProfileName 
  },
  props:{
    pot_detail_modal : Boolean,
    potitem : Object,
  },
  data(){
    return{

    }
  },
  computed:{
    ...mapGetters("pot", ["potattendusers"]),
  },
  methods:{
    ...mapActions("pot",["setPotAttendUsers"]),
    cancelDetail() {
      this.$emit('openDetailModal', false)
    },

    potattend() {
      axios.get(API.url + potAPI.attend(1,1))
        .then((res) => {
          if (res.data === "success") {
            alert("참여 성공");
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("참여 ㄴ");
          console.log(error);
        })
    },

    potmod() {
      axios.put(API.url + potAPI.modify(1))
        .then((res) => {
          if (res.data === "success") {
            alert("삭제 성공");
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("삭제 ㄴ");
          console.log(error);
        })
    },

    potdel() {
      axios.delete(API.url + potAPI.delete(1))
        .then((res) => {
          if (res.data === "success") {
            alert("삭제 성공");
            this.$router.push({ name: "VetPartyList" })
          }
        })
        .catch((error) => {
          alert("삭제 ㄴ");
          console.log(error);
        })
    },
  },

  mounted(){

  }
}
</script>

<style>

</style>