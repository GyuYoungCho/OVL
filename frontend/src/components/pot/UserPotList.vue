/<template>
    <div>
      <v-list-item-content>
        <v-list-item-title v-text="userpot.step"></v-list-item-title>

        <v-list-item-subtitle
        class="text--primary"
        v-text="userpot.total_people"
        ></v-list-item-subtitle>

        <v-list-item-subtitle v-text="userpot.content"></v-list-item-subtitle>
        
    </v-list-item-content>

    <v-list-item-action>
        <v-list-item-action-text v-text="userpot.restaurantName"></v-list-item-action-text>
    </v-list-item-action>
    </div>  
</template>

<script>
import { mapGetters} from 'vuex';
export default {
    data() {
        return {
            
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