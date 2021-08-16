<template>
    <div class="d-flex" @click="clickProfile" style="max-width:120px;">
        <v-list-item-avatar class="px-0" size=27>
            <v-img
            :src="user.filepath"
            ></v-img>
        </v-list-item-avatar>
        <v-list-item-content class="px-0">
            <v-list-item-subtitle v-text="user.nickname" style="font-size:17px" min-width="10px">
            </v-list-item-subtitle>
        </v-list-item-content>
    </div>
</template>

<script>
import { mapGetters} from 'vuex';
export default {
    props: {
        user : Object,
    },
    computed:{
        ...mapGetters("user", ["userinfo"]),
    },
    methods:{
        clickProfile(){
            if(this.user.userid==this.userinfo.userid){
                this.$router.push('/profile/'+this.user.userid)
            }else{
                this.$router.push('/other_profile/'+this.user.userid)
            }
        }
    }
}
</script>

<style scoped>

</style>