<template>
    <div>
        <v-dialog
        v-model="modalopen"
        persistent
        max-width="300"
        >
        <v-card>
          <v-spacer></v-spacer>
          <v-card-title class="text-h6">
            팟을 삭제하시겠습니까?
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="cancelDeleteModal()">
              아니오
            </v-btn>
            <v-btn color="green darken-1" text @click="potdel()">
              예
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
</template>

<script>
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import { mapGetters} from 'vuex';
export default {

    props:{
        potitem : Object,
        modalopen : Boolean,
    },
    computed:{
        ...mapGetters("user", ["userinfo"]),
    },
    methods:{
        
        cancelDeleteModal(){
            this.$emit('openDeleteModal', false)
        },
        potdel() {
          axios.delete(API.url + potAPI.delete(this.potitem.potid))
            .then((res) => {
              if (res.data === "success") {
                
                this.$emit('openSnackBar', true , "delete")
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