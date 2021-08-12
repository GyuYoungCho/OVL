/<template>
    <div class="mt-3">
    <div style="text-align: left" :disabled="avail" class="potlist"
        :class="{ 'abledArea': !avail, 'disabledArea': avail , }"
        @click="openDetailModal(true)" >
        <v-divider></v-divider>
        
        <v-container class="d-flex pt-0 pb-0 pl-0">
        
            <profile-name :user="potitem.userid"></profile-name>
        
        <v-row align="center" justify="end">
        <button v-if="avail" class="py-0 px-3 mt-0 notBtnComp" style="font-size:12px"
                >마감</button>
        
        <v-icon>mdi-map-marker</v-icon>
        
            <span class="p">{{this.potitem.restaurant_name}}</span>
        
        </v-row>
        
        </v-container>
    
    <v-row>
        <v-container class="d-flex pb-1 ml-3">
            <v-list-item-subtitle v-text="potitem.title"></v-list-item-subtitle>
        </v-container>
    </v-row>
    <v-row class="mt-4">
        <v-container class="d-flex subinfo mb-2 pt-0 pb-0">
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
            <span>{{this.potitem.pot_count}}명/</span>
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
   
    </div>  
    </div>
</template>

<script>
import moment from 'moment';
import { mapGetters, mapActions} from 'vuex';
import ProfileName from '@/components/basic/ProfileName.vue';

export default {
    components:{
        ProfileName,
        
    },
    data() {
        return {
            isColor: false,
            allSteps: [
                "과일채소", "계란","유제품","생선","고기"
            ],
            
            btnActive: {0:false,1:false,2:false,3:false,4:false},
            
            snackbar : false,
        };
    },
    props: {
        potitem: Object,
        
    },
    created(){
        this.stepToIcon(this.potitem.step)
    },
    computed: {
        
        ...mapGetters("user", ["userinfo"]),
        
        meet_date(){
            return moment(this.potitem.time).format("ddd MM/DD")
        },
        meet_time(){
            return moment(this.potitem.time).format("HH:mm")
        },
        avail(){
            
            return (this.potitem.total_people <= this.potitem.pot_count) || 
                    ((new Date(this.potitem.time)).getTime() < (new Date()).getTime())
        }
        
    },
    methods: {
        ...mapActions('pot', ['selectPot','potAttendUsers']),
        openDetailModal(val){
            this.selectPot(this.potitem)
            this.potAttendUsers(this.potitem.potid)
            this.$emit('openDetailModal', val)
        },
        openAttendModal(val){
            this.selectPot(this.potitem)
            this.potAttendUsers(this.potitem.potid)
            this.$emit('openAttendModal', val)
        },
        
        stepToIcon(item) {
            for (let i=0;i<5;i++){
                if(this.allSteps[i]==item){
                    this.btnActive[i] = true;
                    break
                }
            }
        },
    },
    
}
</script>

<style>

</style>