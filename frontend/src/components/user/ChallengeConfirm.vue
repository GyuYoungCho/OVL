<template>
  <div>
    <v-dialog
          v-model="certdialog"
          height="500"
          @click:outside="closeCertDialog()"
          transition="dialog-bottom-transition"
          scrollable
          max-height="500px"
          max-width="1000px">
        <v-card tile flat>
          <v-toolbar color="#49784B" dark>
          <v-toolbar-title >{{this.certList[0].challengeId.title}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="closeCertDialog()" justify="end">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
            <v-container>
                <div class="text-center">
                    <div><span style="font-size: large; color:  #49784B;">"{{this.letday}}"</span> 일 달성!</div>
                </div>
                <div class="text-center" >
                    
                    <v-progress-circular
                        :rotate="360"
                        :width="15"
                        :size="80"
                        :value="percent"
                        color="teal"
                    >
                    {{this.percent}}%
                    </v-progress-circular>
                </div>
            <v-divider class="mt-6"></v-divider>
            <v-row  justify="center">
                <v-col v-for="(cert,index) in certList" :key="index" cols="auto" class="px-1">
                    <v-card  height="45" width="45" class="mt-1">
                    
                    <v-img class="ml-3 mt-3" max-height="20" max-width="20" v-if="cert.certification==1" 
                    src="@/assets/image/OVLoong.png"></v-img>
                    <v-img v-else class="ml-3 mt-3" max-height="20" max-width="20" 
                    src="@/assets/image/OVLblackoong.png"></v-img>
                    <span class="ml-1 mt-1" style="font-size:11px">~{{cert_date(cert.certification_date, cert.challengeId.cycle-1)}}</span>
                    </v-card>
                </v-col>
            </v-row>
            </v-container>
            
          </v-card>
    </v-dialog>
    
  </div>
</template>

<script>
import moment from "moment"
import { mapGetters} from 'vuex';
// import ProfileName from '@/components/basic/ProfileName.vue';

export default {
  data(){
    return{
      modals : true,
    }
  },
  props:{
    certdialog : Boolean,
  },
  computed:{
    ...mapGetters("challenge", ["certList"]),
    letday(){
        let count = 0
        this.certList.forEach(item => {
            if(item.certification==1) count+=1
        });
        return count;
    },
    percent(){
        return Math.round((this.letday /this.certList.length) * 100)
    }
  },
  mounted(){
    console.log("id : ", this.$route.params.userid);
    this.$store.dispatch('challenge/getCertList', this.$route.params.userid);
  },
  methods:{

    closeCertDialog() {
      this.$emit('openCertDialog', false)
    },
    cert_date(day, cycle){
        let dday = new Date(day)
        return moment(dday.setDate(dday.getDate() + cycle)).format("MM/DD")
    },
  },

}
</script>

<style>

</style>