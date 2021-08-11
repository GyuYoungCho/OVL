<template>
    <div class="d-flex">
        <v-list-item-avatar size=25>
            <v-img
            :src="user.filepath"
            ></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
            <v-list-item-subtitle v-text="user.nickname">
            </v-list-item-subtitle>
        </v-list-item-content>
    </div>
</template>

<script>

export default {
    props: {
        user : Object,
    },
}
</script>