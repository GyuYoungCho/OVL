/<template>
    <div style="text-align: left">
        <v-row>
 
            <v-list-item-content>
        fd
            <v-list-item-subtitle
            
            v-text="userpot.total_people"
            ></v-list-item-subtitle>

            
            </v-list-item-content>
        </v-row>
        <v-row>
        
            <v-list-item-subtitle v-text="userpot.content"></v-list-item-subtitle>
        </v-row>
        <v-row>
        
        
        </v-row>
    </div>  
</template>

<script>
import { mapGetters} from 'vuex';
export default {
    data() {
        return {
            step :'',
        };
    },
    props: {
        userpot: Object,
    },

    computed: {
        ...mapGetters("pot", ["potitems"]),
        rows() {
            return this.restitems.length
        }
    },
    methods: {
        // choosePot() {
        //     this.selectPot(this.rest);
        // },
        colorChange(flag) {
            this.isColor = flag;
        },
    },
}
</script>

<style>

</style>