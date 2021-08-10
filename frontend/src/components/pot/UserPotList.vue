/<template>
    <div style="text-align: left">
        <v-container class="d-flex subinfo mb-2 pa-0">
            
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="3" class="pa-0">
                <v-icon>mdi-calendar-month</v-icon>
                <span>{{this.meet_date}}</span>    
            </v-col>
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="2" class="pa-0">
                <v-icon>mdi-clock-time-nine-outline</v-icon>
                <span>{{this.meet_time}}</span>
            </v-col>
            <v-divider vertical class="mx-1"></v-divider>

            <v-col cols="3" class="pa-0">
                <v-icon>mdi-account-outline</v-icon>
                <span>{{this.rows}}명/</span>
                <span>{{this.userpot.total_people}}명</span>
            </v-col>
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="1" class="pa-0">
            <div class="icon_frame">
                <v-img src="@/assets/icon/meat.png" alt="" v-if="btnActive[4]"> </v-img>
                <v-img src="@/assets/icon/fish.png" alt="" v-else-if="btnActive[3]"> </v-img>
                <v-img src="@/assets/icon/milk.png" alt="" v-else-if="btnActive[2]"> </v-img>
                <v-img src="@/assets/icon/egg.png" alt="" v-else-if="btnActive[1]"> </v-img>
                <v-img src="@/assets/icon/vege.png" alt="" v-else> </v-img>
            </div>
            </v-col>
            <v-col  class="pa-0">
                <v-btn icon @click="openDetailModal(true)">
                  <v-icon>mdi-menu-left-outline</v-icon>
                </v-btn>
            </v-col>
        </v-container>
        <!-- <vet-party-detail :potitem="userpot" :pot_detail_modal="modalOpen"></vet-party-detail> -->

    </div>  
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';
// import VetPartyDetail from '@/components/pot/VetPartyDetail.vue'

export default {
    components:{
        // VetPartyDetail,
    },
    data() {
        return {
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            modalOpen : false,
        };
    },
    props: {
        userpot: Object,
    },

    computed: {
        ...mapGetters("pot", ["potattendusers"]),
        meet_date(){
            return moment(this.userpot.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.userpot.time).format("HH:mm")
        },
        rows() {
            return this.potattendusers.length
        },
    },

    methods: {
        ...mapActions("pot",["setPotAttendUsers"]),
        openDetailModal(val){
            this.modalOpen = val;
        },
        stepToIcon(item) {
            for (let i=0;i<5;i++){
                if(this.allSteps[i]==item){
                    this.btnActive[i] = true;
                    break
                }
            }
        },
        colorChange(flag) {
            this.isColor = flag;
        },
    },

    created(){
        this.$store.dispatch("pot/setPotAttendUsers",this.userpot.potid)
    },

    mounted(){
        this.stepToIcon(this.userpot.step)
    },
    
}
</script>

<style>

</style>