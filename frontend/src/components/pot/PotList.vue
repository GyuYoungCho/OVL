/<template>
    <div>
        <v-data-table
        :headers="fields"
        :items="potitems"
        :page.sync="page"
        :items-per-page="itemsPerPage"
        hide-default-footer
        class="elevation-1"
        @page-count="pageCount = $event"
        
      ></v-data-table>
      <div class="text-center pt-2">
        <v-pagination
          v-model="page"
          :length="pageCount"
        ></v-pagination>
      </div>
    </div> 
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
export default {
    data() {
        return {
            page: 1,
            pageCount: 0,
            itemsPerPage: 5,
            fields: [{ text: '이름', value: 'restaurantName' },
            { text: '제목', value: 'title' },
            { text: '주소', value: 'place' },
            { text: '인원', value: 'total_people' },
            ],
        };
    },
    computed: {
        ...mapGetters("pot", ["potitems"]),
        rows() {
            return this.restitems.length
        }
    },
    created() {
        this.$store.dispatch("pot/setPotItems");
    },
    methods: {
    
        ...mapActions("pot", ['setPotItems']),
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