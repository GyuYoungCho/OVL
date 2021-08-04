/<template>
    <div>
        <v-data-table
        :headers="fields"
        :items="restitems"
        :page.sync="page"
        :items-per-page="itemsPerPage"
        hide-default-footer
        class="elevation-1"
        @page-count="pageCount = $event"
        @click:row="chooseRestaurant"
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
            itemsPerPage: 10,
            fields: [{ text: '이름', value: 'restaurantName' },
            { text: '주소', value: 'place' },],
        };
    },
    computed: {
        ...mapGetters("pot", ["restitems"]),
        rows() {
            return this.restitems.length
        }
    },
    created() {
        this.$store.dispatch("pot/setRestItems");
    },
    methods: {
    
        ...mapActions("pot", ['setRestItems','selectRestaurant']),
        chooseRestaurant(value) {
            this.selectRestaurant(value);
        },
        colorChange(flag) {
            this.isColor = flag;
        },
    },
}
</script>

<style>

</style>