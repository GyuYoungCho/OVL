<template>
    <v-dialog v-model="modalOpen" max-width="300" @click:outside="modalOpen = false">
        <v-card>
            <!-- 모달 타이틀 영역 -->
            <v-toolbar dense color="#49784B">
            <v-toolbar-title class="modalTitle">{{title}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon dark @click="modalCancelBtnClick">
                <v-icon>mdi-close</v-icon>
            </v-btn>
            </v-toolbar>
            <!-- 모달 컨텐츠 영역 -->
            <v-container>
            <div class="modalContent">
                <div class="mb-3">
                    <span class="modalContentMessage">
                        <!-- 업로드 컴포넌트 -->
                        <ProfileName
                            v-for="(auser, index) in followList" :key="index" :user="auser">
                        </ProfileName>
                    </span>
                </div>
            </div>
            </v-container>
        </v-card>
    </v-dialog>
</template>

<script>
import ProfileName from '@/components/basic/ProfileName.vue'

export default {
  components:{ProfileName},

  props: {
    title: String,
    modalOpen: Boolean,
    followList: Array,
  },
  methods: {
    modalCancelBtnClick () {
      this.$emit('modalCancelBtnClick')
    },
  }
}
</script>