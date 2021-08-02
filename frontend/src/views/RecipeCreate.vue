<template>
  <div>
    <h1>레시피 생성 페이지</h1>
    <div class="py-6">
      <label for="previewImageInput">이미지 추가</label>
      <input type="file" ref="files" multiple @change="onInputChange" id="previewImageInput">
    </div>

    <!-- 사진 미리보기 -->
    <div v-for="(imgFile, index) in imgFiles" :key="index" >
      <img class="preview-img" :src="imgFile.previewURL" alt="" @click="deleteImg(imgFile.number)">
      <textarea name="" id="" cols="30" rows="10"></textarea>
    </div>

    <v-tabs
      v-model="tab"
      align-with-title
      color="green darken-4"
    >
      <v-tabs-slider color="green darken-4"></v-tabs-slider>
      <v-tab>재료</v-tab>
      <v-tab>과정</v-tab>

    </v-tabs>
    <v-tabs-items v-model="tab">
      <v-tab-item>
        <textarea name="" id="" cols="30" rows="10" v-model="ingredients" placeholder="재료 쓰는 곳"></textarea>
      </v-tab-item>
      <v-tab-item>
        <textarea name="" id="" cols="30" rows="10" v-model="process" placeholder="과정 쓰는 곳"></textarea>
      </v-tab-item>
    </v-tabs-items>

    <div>
      <button @click="onRecipeBtnClick">레시피 작성 완료</button>
    </div>
  </div>
</template>



<script>
export default {
  data: () => ({
    imgFiles: [],
    lastIndex: 0,

    tab: null,
    ingredients: "",
    process: "",
  }),
  methods: {
    onInputChange(event) {
      // this.previewURL = URL.createObjectURL(this.$refs.files.files[0])
      const inputFiles = this.$refs.files.files
      for(let i=0; i < inputFiles.length; i++) {
        let inputFile = inputFiles[i]
        inputFile.previewURL = URL.createObjectURL(inputFile)
        inputFile.number = this.lastIndex
        this.lastIndex++
        this.imgFiles = this.imgFiles.concat(inputFile)
      }
      console.log(this.imgFiles)
      event.target.value = ""
    },
    deleteImg(number) {
      this.imgFiles = this.imgFiles.filter(data => data.number !== number)
    },
    onRecipeBtnClick() {
      this.$router.push({ name: 'Profile' })
    }
  }
}
</script>

<style scoped>
  input[type='file'] {
      position: absolute;
      width: 0;
      height: 0;
      padding: 0;
      overflow: hidden;
      border: 0;
  }
  label {
    cursor: pointer;
    /* padding: 10px 20px;
    background-color: darkgreen;
    color: #fff;
    vertical-align: middle;
    font-size: 15px;
    cursor: pointer;
    border-radius: 5px; */
  }
  .preview-img {
    height: 100px;
    display: inline-block;
  }
</style>