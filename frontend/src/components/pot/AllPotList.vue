/<template>
    <div style="text-align: left"  @click="openDetailModal(true)">
        <v-divider></v-divider>
        
        <v-container class="d-flex pt-0 pb-0 pl-0">
            <profile-name :user="potitem.userid"></profile-name>
        
        <v-row align="center" justify="end">
        <v-icon>mdi-map-marker</v-icon>
            <span class="p">{{this.potitem.restaurant_name}}</span>
        
        </v-row>
        </v-container>
    
    <v-row>
        <v-container class="d-flex pb-1 ml-3">
            <v-list-item-subtitle v-text="potitem.title"></v-list-item-subtitle>
        </v-container>
    </v-row>
    <v-row>
        <v-container class="d-flex subinfo mb-2 pt-0">
            <v-col cols="3" class="pa-0">
                <v-icon>mdi-calendar-month</v-icon>
                <span>{{this.meet_date}}</span>
            </v-col>
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="2" class="pa-0">
                <v-icon>mdi-clock-time-nine-outline</v-icon>
                <span>{{this.meet_time}}</span>
            </v-col>
            <v-divider vertical class="mx-2"></v-divider>
            <v-col cols="auto" class="pa-0">
            <v-icon>mdi-account-outline</v-icon>
            <span>{{this.rows}}명/</span>
            <span>{{this.potitem.total_people}}명</span>
            </v-col>
            <v-divider vertical class="mx-2"></v-divider>
            <v-col cols="1" class="pa-0">
            <div class="icon_frame">
                <v-img src="@/assets/icon/meat.png" alt="" v-if="btnActive[4]"> </v-img>
                <v-img src="@/assets/icon/fish.png" alt="" v-else-if="btnActive[3]"> </v-img>
                <v-img src="@/assets/icon/milk.png" alt="" v-else-if="btnActive[2]"> </v-img>
                <v-img src="@/assets/icon/egg.png" alt="" v-else-if="btnActive[1]"> </v-img>
                <v-img src="@/assets/icon/vege.png" alt="" v-else> </v-img>
            </div>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="2" class="pa-0">
                <button class="py-0 px-3 BtnComp mt-0" style="font-size:12px"
                    @click.stop="openAttendModal(true)">참여</button>
            </v-col>
        </v-container>
    </v-row>
    <vet-party-detail :potitem="potitem" :pot_detail_modal="modalDetail"
        @openDetailModal="openDetailModal"></vet-party-detail>
    <attend-modal :potitem="potitem" :modalopen="AttendModalT"
              @openAttendModal="openAttendModal" @openSnackBar="openSnackBar"></attend-modal>
   
    </div>  
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';
import axios from "axios";
import API from '@/api/index.js'
import potAPI from '@/api/pot.js'
import VetPartyDetail from '@/components/pot/VetPartyDetail.vue'
import ProfileName from '@/components/basic/ProfileName.vue';
import AttendModal from '@/components/pot/AttendModal.vue'
// import ConfirmSnack from '@/components/basic/ConfirmSnack.vue';

export default {
    components:{
        VetPartyDetail,
        ProfileName,
        AttendModal,
        // ConfirmSnack,
    },
    data() {
        return {
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            modalDetail : false,
            AttendModalT : false,
        };
    },
    props: {
        potitem: Object,
    },
    created(){
        this.snackbar=false
        this.AttendModalT=false
        this.modal=false
    },
    computed: {
        ...mapGetters("pot", ["potattendusers"]),
        ...mapGetters("user", ["userinfo"]),
        
        meet_date(){
            return moment(this.potitem.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.potitem.time).format("HH:mm")
        },
        rows() {
            return this.potattendusers.length
        },
        
    },
    methods: {
        ...mapActions("pot",["setPotAttendUsers"]),
        openDetailModal(val){
            this.modalDetail = val;
        },
        openAttendModal(val){
            this.AttendModalT = val;
        },

        stepToIcon(item) {
            for (let i=0;i<5;i++){
                if(this.allSteps[i]==item){
                    this.btnActive[i] = true;
                    break
                }
            }
        },

        potattend() {
            axios.get(API.url + potAPI.attend(this.potitem.potid,this.userinfo.userid))
            .then((res) => {
                if (res.data === "success") {
                    this.confirmModal()
                }
            })
            .catch((error) => {
                alert("참여 ㄴ");
                console.log(error);
            })

            this.snackbar = true;
            this.AttendModalT = false;
        },

        openSnackBar(val){
            this.$emit('openSnackBar', val)
        }
    },
    mounted(){
        this.setPotAttendUsers(this.potitem.potid)
        this.stepToIcon(this.potitem.step)
        console.log(this.potitem.userid.challengeId.start_date)
    },
    
}
</script>

<style>

</style>