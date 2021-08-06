/<template>
    <div>
      <v-list-item-content>
        <v-list-item-title v-text="potitem.title"></v-list-item-title>

        <v-list-item-subtitle
        class="text--primary"
        v-text="potitem.total_people"
        ></v-list-item-subtitle>

        <v-list-item-subtitle v-text="potitem.content"></v-list-item-subtitle>
    </v-list-item-content>

    <v-list-item-action>
        <v-list-item-action-text v-text="potitem.restaurantName"></v-list-item-action-text>
    </v-list-item-action>
    </div>  
</template>

<script>
// import { mapActions} from 'vuex';
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
        };
    },
    props: {
        potitem: Object,
    },

    // computed: {
    //     ...mapGetters("pot", ["potitems"]),
    //     rows() {
    //         return this.restitems.length
    //     }
    // },
    methods: {
    
        colorChange(flag) {
            this.isColor = flag;
        },
    },
}
</script>

<style>

</style>