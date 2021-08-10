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
            팟에 참여하시겠습니까?
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="cancelAttendModal()">
              아니오
            </v-btn>
            <v-btn color="green darken-1" text @click="potattend()">
              예
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
</template>

<script>
// import axios from "axios";
// import API from '@/api/index.js'
// import potAPI from '@/api/pot.js'
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
        
        cancelAttendModal(){
            this.$emit('openAttendModal', false)
        },
        potattend() {
            // axios.get(API.url + potAPI.attend(this.potitem.potid,this.userinfo.userid))
            // .then((res) => {
            //     if (res.data === "success") {
            //         this.confirmModal()
            //     }
            // })
            // .catch((error) => {
            //     alert("참여 ㄴ");
            //     console.log(error);
            // })

            this.$emit('openSnackBar', true)
        },
    }
}
</script>

<style>

</style>