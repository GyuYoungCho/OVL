<template>
    <v-dialog v-model="modalOpen" max-width="300" @click:outside="modalOpen = false">
        <v-card>
            <!-- 모달 타이틀 영역 -->
            <v-toolbar dense color="#49784B">
                <v-toolbar-title class="modalTitle">{{title}}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon dark @click="onCancelBtnClick">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
            <!-- 모달 컨텐츠 영역 -->
            <v-container >
                <div class="modalContent" >
                <div class="mb-3" >
                    <span class="modalContentMessage">
                        <div v-if="!isReport"><textarea name="" id="" cols="30" rows="10" placeholder="신고사유를 구체적으로 적어주세요" class="ingredient-text" v-model="reason" style="height:100px"></textarea></div>
                        <div v-else style="text-align:left">신고를 취소하시겠습니까?</div>
                    </span>
                </div>
                <div class="modalContentButtonArea" v-if="!isReport" :class="{'buttonDiv': valid, 'disabledBtnDiv': !valid}">
                    <button class="modalContentButton" @click="onConfirmBtnClick" :disabled="!valid" style="width:80px">신고</button>
                </div>
                <div class="modalContentButtonArea" v-else>
                    <button class="modalContentButton" @click="onConfirmBtnClick" style="width:80px">신고 취소</button>
                </div>
                </div>
            </v-container>
        </v-card>
    </v-dialog>
</template>

<script>
import {mapActions} from "vuex"

export default {
  props: {
    modalOpen: Boolean,
    title:String,
    isReport:Boolean, // false : 신고, true : 신고 취소
    fromId:Number,
    toId:Number
  },
  data() {
      return {
          reason:"", 
      }
  },
  computed: {
      valid() {
          if (this.reason.length==0) return false;
          return true;
      }
  },
  methods: {
    ...mapActions('report', ["reportRegist", "reportCancel", "selectReport"]),
    async onConfirmBtnClick () {
        var payload = {
            "fromId":this.fromId,
            "toId":this.toId,
            "reason":this.reason
        }
        if (!this.isReport) { // 신고 해야됨
           await this.reportRegist(payload);
        } else {
           await this.reportCancel(payload);
        }
        this.$emit('onConfirmBtnClick')
    },
    onCancelBtnClick() {
        this.$emit('onCancelBtnClick')
    }
  }
}
</script>