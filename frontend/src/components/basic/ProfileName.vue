<template>
    <div class="d-flex">
        <v-list-item-avatar size=25>
            <v-img
            :src="profile"
            ></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
            <v-list-item-subtitle v-text="user.nickname">
            </v-list-item-subtitle>
        </v-list-item-content>
    </div>
</template>

<script>
import API from "@/api/index.js";

export default {
    props: {
        user : Object,
    },
    computed:{
        profile() {
            return API.url + "/profile" + this.user.userid + "/"+ this.user.stored_file_path.split('/').reverse()[0]
        },
    }
}
</script>