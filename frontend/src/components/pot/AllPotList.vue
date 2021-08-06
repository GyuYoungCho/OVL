/<template>
    <div style="text-align: left">
        <v-row>
            <v-container class="d-flex pa-ma-0">
            <v-list-item-avatar>
                <v-img
                :alt="`${potitem.step} avatar`"
                :src="profile"
                ></v-img>
            </v-list-item-avatar>
        <v-list-item-title v-text="potitem.userid.nickname"></v-list-item-title>

          <v-spacer></v-spacer>
  
            
        <v-icon>mdi-map-marker</v-icon>dk
        <v-text-field>{{potitem.type}}</v-text-field>
        </v-container>
    </v-row>
    <v-row class="pa-ma-0">
        <v-container class="d-flex">
            <v-list-item-title v-text="potitem.title"></v-list-item-title>
        </v-container>
    </v-row>
    <v-row>
        <v-spacer></v-spacer>
        <v-list-item-action>
            <button class=BtnComp>참여하기</button>
        </v-list-item-action>
        <v-spacer></v-spacer>
    </v-row>
    </div>  
</template>

<script>
// import { mapActions} from 'vuex';
import API from "@/api/index.js";

export default {
    data() {
        return {
            // page: 1,
            // pageCount: 0,
            // itemsPerPage: 5,
            // fields: [{ text: '이름', value: 'restaurantName' },
            // { text: '제목', value: 'title' },
            // { text: '주소', value: 'place' },
            // { text: '인원', value: 'total_people' },
            // ],
            profile : API.url + "/post/1/32894420239799.png"
        };
    },
    props: {
        potitem: Object,
    },

    computed: {
        // ...mapGetters("pot", ["potitems"]),
        // profile(){
        //     return API.url + "profile" + this.potitem.userid.userid + "/" + this.potitem.userid.stored_file_path.split('/').reverse()[0]
        // },
        rows() {
            return this.restitems.length
        }
    },
    methods: {
    
        colorChange(flag) {
            this.isColor = flag;
        },
    },
}
</script>

<style>

</style>