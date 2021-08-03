<template>
  <div>
    <h1>레시피 생성 페이지</h1>
    <input type="text" v-model="title" placeholder="요리 이름">
    <div>
      <label for="pictureImageInput" v-if="!picture">대표 사진 추가</label>
      <input type="file" ref="file" multiple @change="pictureInputChange" id="pictureImageInput">
      <img :src="picture.previewURL" v-if="!!picture" alt="" class="preview-img">
      
    </div>
    <textarea cols="30" rows="10" placeholder="요리 간단 설명" v-model="content"></textarea>
    <v-tabs
      v-model="tab"
      align-with-title
      color="green darken-4"
      centered
    >
      <v-tabs-slider color="green darken-4"></v-tabs-slider>
      <v-tab>재료</v-tab>
      <v-tab>과정</v-tab>

    </v-tabs>
    <v-tabs-items v-model="tab">
      <!-- 재료 탭 내용 -->
      <v-tab-item>
        <!-- 재료는 글로만 처리할 수도, 사진을 첨부할 수도 있음 -->
        <!-- 일단 사진 첨부 안 한다고 가정! -->
        <textarea name="" id="" cols="30" rows="10" v-model="ingredient" placeholder="재료 쓰는 곳"></textarea>

        <!-- 재료에 사진을 첨부할 경우 -->
        <!-- <div class="py-6">
          <label for="previewIngredientImageInput">재료 이미지 추가</label>
          <input type="file" ref="files" multiple @change="onIngredientInputChange" id="previewIngredientImageInput">
        </div> -->

        <!-- 사진 미리보기 -->
        <!-- <div v-for="(ingredientImgFile, index) in ingredientImgFiles" :key="index" >
          <img class="preview-img" :src="ingredientImgFile.previewURL" alt="" @click="deleteIngredientImg(ingredientImgFile.number)">
        </div> -->

      </v-tab-item>
      
      <!-- 과정 탭 내용 -->
      <v-tab-item>
        <!-- 과정에 사진을 첨부(필수) -->
        <div class="py-6">
          <label for="previewProcessImageInput">과정 이미지 추가</label>
          <input type="file" ref="files" multiple @change="onProcessInputChange" id="previewProcessImageInput">
        </div>

        <!-- 과정 사진 미리보기 -->
        <div v-for="(processImgFile, index) in processImgFiles" :key="index" >
          <img class="preview-img" :src="processImgFile.previewURL" alt="" @click="deleteProcessImg(processImgFile.number)">
          <textarea cols="30" rows="10" v-model="processImgFile.text"></textarea>
        </div>

      </v-tab-item>
    </v-tabs-items>

    <div>
      <v-btn
        @click="onRecipeBtnClick"
        :disabled="!valid"
      >
        <button >레시피 작성 완료</button>
      </v-btn>
    </div>
  </div>
</template>



<script>
import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'

export default {
  data: () => ({
    title: "",
    picture: null,
    content: "",
    ingredient: "",
    processImgFiles: [],
    processLastIdx: 0,

    tab: null,
  }),
  methods: {
    pictureInputChange () {
      const picture = this.$refs.file.files[0]
      picture.previewURL = URL.createObjectURL(picture)
      this.picture = picture
    },

    onProcessInputChange(event) {
      const inputFiles = this.$refs.files.files
      for(let i=0; i < inputFiles.length; i++) {
        let inputFile = inputFiles[i]
        inputFile.previewURL = URL.createObjectURL(inputFile)
        inputFile.number = this.processLastIdx
        inputFile.text = ''
        this.processLastIdx++
        this.processImgFiles.push(inputFile)
      }
      event.target.value = ""
    },

    deleteProcessImg(number) {
      this.processImgFiles = this.processImgFiles.filter(data => data.number !== number)
    },
    
    onRecipeBtnClick() {
      let contentList = []
      let formData = new FormData()

      formData.append('title', this.title)
      formData.append('picture', this.picture)
      formData.append('content', this.content)
      formData.append('ingredient', this.ingredient)
      formData.append('userId', 7) // 임의로 넣은 값
      for(let i=0; i < this.processImgFiles.length; i++ ) {
        contentList.push(this.processImgFiles[i].text)
        formData.append('files', this.processImgFiles[i])
      }


      const URL = API.url + recipeAPI.regist()
      axios.post(URL, formData, 
        { headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(res => {
          console.log(res)
          axios.post(API.url + recipeAPI.contentList, contentList)
          .then(res => console.log(res))
          .catch(err => console.error(err))
        })
        .catch(err => console.error(err))

      // this.$router.push({ name: 'Profile' })
    }
  },
  computed: {
    valid () {
      return !!this.title & !!this.picture & !!this.content & !!this.ingredient & !!this.processImgFiles.length
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