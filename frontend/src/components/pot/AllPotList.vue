/<template>
    <div style="text-align: left"  @click="openDetailModal(true)">
        <v-divider></v-divider>
        
        <v-container class="d-flex pt-0 pb-0 pl-0">
            <v-list-item-avatar>
                
                <v-img
                :alt="`${potitem.step} avatar`"
                :src="profile"
                ></v-img>
            </v-list-item-avatar>
            <v-list-item-content>
                <v-list-item-title v-text="potitem.userid.nickname">
                </v-list-item-title>
            </v-list-item-content>
        
        <v-row align="center" justify="end">
        <v-icon>mdi-map-marker</v-icon>
            <span class="p">{{this.potitem.restaurant_name}}</span>
        
        </v-row>
        </v-container>
    
    <v-row>
        <v-container class="d-flex pb-1">
            <v-list-item-subtitle v-text="potitem.title"></v-list-item-subtitle>
        </v-container>
    </v-row>
    <v-row>
        <v-container class="d-flex subinfo mb-2 pt-0">
            <v-icon>mdi-calendar-month</v-icon>
            <span>{{this.meet_date}}</span>
            <v-divider vertical class="mx-2"></v-divider>
            <v-icon>mdi-clock-time-nine-outline</v-icon>
            <span>{{this.meet_time}}</span>
            <v-divider vertical class="mx-2"></v-divider>
            <v-icon>mdi-account-outline</v-icon>
            <span>{{this.rows}}명/</span>
            <span>{{this.potitem.total_people}}명</span>
            <v-divider vertical class="mx-3"></v-divider>
            <v-img src="@/assets/icon/meat.png" alt="" v-if="btnActive[4]"> </v-img>
            <v-img src="@/assets/icon/fish.png" alt="" v-else-if="btnActive[3]"> </v-img>
            <v-img src="@/assets/icon/milk.png" alt="" v-else-if="btnActive[2]"> </v-img>
            <v-img src="@/assets/icon/egg.png" alt="" v-else-if="btnActive[1]"> </v-img>
            <v-img src="@/assets/icon/vege.png" alt="" v-else> </v-img>

            <v-spacer></v-spacer>
        <button class="py-0 px-2 BtnComp mt-0" style="font-size:14px">참여하기</button>
        
        </v-container>
    </v-row>
    <vet-party-detail :potitem="potitem" :pot_detail_modal="modalOpen"></vet-party-detail>
    </div>  
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';
import API from "@/api/index.js";
import VetPartyDetail from '@/components/pot/VetPartyDetail.vue'

export default {
    components:{
        VetPartyDetail,
    },
    data() {
        return {
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            profile : API.url + "/post/1/32894420239799.png",
            modalOpen : false,
        };
    },
    props: {
        potitem: Object,
    },

    computed: {
        ...mapGetters("pot", ["potattendusers"]),
        // profile(){
        //     return API.url + "profile" + this.potitem.userid.userid + "/" + this.potitem.userid.stored_file_path.split('/').reverse()[0]
        // },
        meet_date(){
            return moment(this.potitem.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.potitem.time).format("hh:mm")
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
                    console.log(this.btnActive)
                    break
                }
            }
        },
        colorChange(flag) {
            this.isColor = flag;
        },
    },
    mounted(){
        this.setPotAttendUsers(this.potitem.potid)
        this.stepToIcon(this.potitem.step)
    },
}
</script>

<style>

</style>