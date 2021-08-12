<template>
    <div>
        <v-dialog
        v-model="modalMessage"
        persistent
        max-width="300"
        >
        <v-card>
          <v-spacer></v-spacer>
          <v-card-title class="text-h6">
            {{this.message}}
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="cancelAttendModal()">
              아니오
            </v-btn>
            <v-btn color="green darken-1" text @click="potprocess(sign)">
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
        sign : String,
        modalMessage : Boolean,
    },
    
    computed:{
        ...mapGetters("user", ["userinfo"]),
        ...mapGetters("pot", ["selectpot","potattendusers","userpots"]),
        message(){
          if(this.sign=="attend"){
            return "팟에 참여하시겠습니까?"
          }else if(this.sign=="cancel"){
            return "참여를 취소하시겠습니까?"
          }else if(this.sign=="delete"){
            return "팟을 삭제하시겠습니까?"
          }else return "by"
        }
    },
    methods:{
        
        cancelAttendModal(){
            this.$emit('openMessageModal', false)
        },
        async potprocess(val) {
          if(val=="attend"){
            if(this.potattendusers.length >= this.selectpot.total_people){
              this.$emit('openSnackBar', true , "not_attend")
            }
            else if(this.userpots.length >=3){
              this.$emit('openSnackBar', true , "dis_attend")
            }
            else{
              axios.post(API.url + potAPI.attend(this.selectpot.potid,this.userinfo.userid))
              .then((res) => {
                  if (res.data === "success") {
                      
                      this.$emit('openSnackBar', true , val)
                      this.updateStore()
                  }
              })
              .catch((error) => {
                  alert("참여x");
                  console.log(error);
              })
            }
          }

          else if(val=="cancel"){
            axios.delete(API.url + potAPI.attend_cancel(this.selectpot.potid, this.userinfo.userid))
            .then((res) => {
              if (res.data === "success") {
                
                this.$emit('openSnackBar', true , val)
                this.updateStore()
              }
            })
            .catch((error) => {
              alert("삭제 ㄴ");
              console.log(error);
            })
          }

          else if(val=="delete"){
            axios.delete(API.url + potAPI.delete(this.selectpot.potid))
            .then((res) => {
              if (res.data === "success") {
                
                this.$emit('openSnackBar', true , val)
                this.updateStore()
              }
            })
            .catch((error) => {
              alert("삭제 ㄴ");
              console.log(error);
            })
          }
        },

        async updateStore(){
          
            await this.$store.dispatch("pot/setUsersPots", this.userinfo.userid)
            await this.$store.dispatch("pot/setPotItems")
        }
    }
}
</script>

<style>

</style>