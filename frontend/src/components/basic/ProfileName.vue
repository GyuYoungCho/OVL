<template>
    <div class="d-flex" @click="clickProfile" style="max-width:120px;">
        <!-- 동그란 프사 영역 -->
        <v-list-item-avatar class="px-0 mx-2" size=25>
            <v-img
            :src="user.filepath"
            ></v-img>
        </v-list-item-avatar>
        <!-- 유저 이름 -->
        <v-list-item-content class="mx=0" >
            <v-list-item-subtitle v-text="user.nickname" min-width="10px"></v-list-item-subtitle>
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