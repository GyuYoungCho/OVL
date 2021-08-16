<template>
    <div>
      <v-dialog
        v-model="modalOpen"
        persistent
        max-width="300"
        >
        <v-card>
          <v-spacer></v-spacer>
          <v-card-title class="text-h6">
            {{this.modalContent}}
          </v-card-title>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text >
              아니오
            </v-btn>
            <v-btn color="green darken-1" text @click="goNext()">
              예
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
</template>


<script>
export default {
    // 참여 취소 삭제에 대한 내용을 받아옴
    props:{
        modalContent : String,
        modalOpen : Boolean,
    },
    methods:{
        goNext() {
            this.$emit('changeNext', true)
        }
    }
}
</script>

<style>

</style>