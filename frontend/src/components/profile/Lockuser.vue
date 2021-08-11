<template>
<v-container>
        <div class="px-7 mt-4">
            <div class="text-center">
                <img src="@/assets/icon/padlock.png" class="unfollow-lockimage" width="80">
                <h5>비공개</h5>
               <div style="color: gray"> <h6>해당 계정은 비공개 계정입니다.</h6> </div>
            </div>
        </div>
</v-container>
</template>

<script>
import {mapState} from "vuex";

export default {
computed: {
    ...mapState("user", (["userinfo"])),
},
}
</script>
