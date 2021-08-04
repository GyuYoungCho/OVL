<template>
  <div>
    <v-dialog
          v-model="rest_list_modal"
          hide-overlay
          transition="dialog-bottom-transition"
          max-width="500px"
          scrollable
        >
        <v-card>
            <v-card-title>
              식당 검색
            
            </v-card-title>
            
          </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'

export default {
  methods:{

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
  }
}
</script>

<style>

</style>