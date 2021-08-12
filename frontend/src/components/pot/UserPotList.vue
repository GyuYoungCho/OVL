/<template>
    <div style ="text-align:center">
        <v-container class="d-flex subinfo mb-2 pa-0">
            
            <v-divider vertical class="mx-1"></v-divider>
            <v-col cols="3" class="pa-0 mt-1 ma-0 pr-1">
                <v-icon>mdi-calendar-month</v-icon>
                <span>{{this.meet_date}}</span>    
            </v-col>
            <v-divider vertical class="mx-1 "></v-divider>
            <v-col cols="2" class="pa-0 mt-1">
                <v-icon>mdi-clock-time-nine-outline</v-icon>
                <span>{{this.meet_time}}</span>
            </v-col>
            <v-divider vertical class="ml-1"></v-divider>

            <v-col cols="3" class="pa-0 mt-1 ma-0">
                <v-icon>mdi-account-outline</v-icon>
                <span>{{this.userpot.pot_count}}명/</span>
                <span>{{this.userpot.total_people}}명</span>
            </v-col>
            <v-divider vertical class="mx-2"></v-divider>
            <v-col cols="1" class="pa-0 mt-0">
            <div class="icon_frame">
                <v-img src="@/assets/icon/meat.png" alt="" v-if="btnActive[4]"> </v-img>
                <v-img src="@/assets/icon/fish.png" alt="" v-else-if="btnActive[3]"> </v-img>
                <v-img src="@/assets/icon/milk.png" alt="" v-else-if="btnActive[2]"> </v-img>
                <v-img src="@/assets/icon/egg.png" alt="" v-else-if="btnActive[1]"> </v-img>
                <v-img src="@/assets/icon/vege.png" alt="" v-else> </v-img>
            </div>
            </v-col>

            <v-divider vertical class="mx-1"></v-divider>
            <div class="mt-0">
                <v-col cols="1" class="pa-0 icon_frame add">
                    <v-btn small class="pa-0"  icon size="17" @click="openDetailModal(true)">
                        <v-icon size="17">mdi-message-processing-outline</v-icon>
                    </v-btn>
                </v-col>
            </div>
        </v-container>
    </div>  
</template>

<script>

import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';

export default {
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
        ...mapGetters("user", ["userinfo"]),
        meet_date(){
            return moment(this.userpot.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.userpot.time).format("HH:mm")
        },
    },
    created(){
        this.stepToIcon(this.userpot.step)
    },

    methods: {
        ...mapActions('pot', ['selectPot','potAttendUsers']),
        
        openDetailModal(val){
            this.selectPot(this.userpot)
            this.potAttendUsers(this.userpot.potid)
            this.$emit('openDetailModal', val)
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
    
}
</script>

<style>

</style>