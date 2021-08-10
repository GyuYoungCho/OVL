<template>
  <v-container>
    <section class="recipeCreate">
      <h1>레시피 작성</h1>
      <!-- 요리 이름 -->
      <div>
        <input type="text" v-model="title" placeholder="요리 이름">
      </div>
      <!-- 대표 사진 -->
      <div class="recipeCreatePic" v-show="!picture">
        <label for="pictureImageInput"><v-icon>mdi-plus</v-icon></label>
        <input type="file" ref="file" multiple @change="pictureInputChange" id="pictureImageInput">
      </div>
      <label for="pictureImageInput">
        <div v-if="!!picture" class="img-container">
            <img :src="picture.previewURL" alt="">
        </div>
      </label>

      <!-- 요리 설명 -->
      <textarea cols="30" rows="10" placeholder="요리 간단 설명" v-model="content"></textarea>

      <!-- 재료와 과정 구분 탭 -->
      <article>
        <button :class="{ 'tabBtn': isIngredient, 'unselectedTabBtn': !isIngredient }" @click="onTabBtnClick">재료</button>
        <button :class="{ 'tabBtn': !isIngredient, 'unselectedTabBtn': isIngredient }" @click="onTabBtnClick">과정</button>
      </article>
      <div class="tab-div">
        <!-- 재료 탭 -->
        <div v-if="isIngredient">
          <textarea name="" id="" cols="30" rows="10" placeholder="재료를 적어주세요." class="ingredient-text" v-model="ingredient">
          </textarea>
        </div>
        <!-- 과정 탭 -->
        <div class="process-tab" v-else>
          <div v-for="(processImgFile, index) in processImgFiles" :key="index" >
            <div class="process-index">
              <span># {{ index + 1 }}</span>
              <button class="cancelBtn" @click="deleteProcessImg(processImgFile.number)">X</button>
            </div>
            <div class="process-data">
              <label for="processImgInput" @click="onExistingProcessPicClick(index)">
                <div class="preivew-img-container">
                  <img class="preview-img" :src="processImgFile.previewURL" alt="" >
                </div>
              </label>
              <textarea cols="30" rows="10" v-model="processImgFile.text"></textarea>
            </div>
          </div>
          <div>
            <p># next</p>
            <div class="process-input">
              <label for="processImgInput"><v-icon>mdi-plus</v-icon></label>
              <input type="file" id="processImgInput" ref="files" multiple @change="onProcessInputChange">
              <textarea name="" id="" cols="30" rows="10"></textarea>
            </div>
          </div>
        </div>
      </div>
      <div>
        <button @click="onRecipeBtnClick" class="finalBtn" :class="{ 'bg-freditgreen': valid, 'disabledBtn': !valid }">
          레시피 작성 완료
        </button>
      </div>
    </section>
  </v-container>
</template>



<script>
import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import { mapGetters } from 'vuex'

export default {
  data: () => ({
    isIngredient: true,
    title: "",
    picture: null,
    content: "",
    ingredient: "",
    processImgFiles: [],
    processLastIdx: 0,
    changeProcessPhotoIdx: -1,

    tab: null,
  }),
  methods: {
    onTabBtnClick () {
      this.isIngredient = !this.isIngredient
    },

    pictureInputChange () {
      const picture = this.$refs.file.files[0]
      picture.previewURL = URL.createObjectURL(picture)
      this.picture = picture
    },

    onExistingProcessPicClick (index) {
      this.changeProcessPhotoIdx = index
    },

    onProcessInputChange(event) {
      if (this.changeProcessPhotoIdx===-1) {
        const inputFiles = this.$refs.files.files
        for(let i=0; i < inputFiles.length; i++) {
          let inputFile = inputFiles[i]
          inputFile.previewURL = URL.createObjectURL(inputFile)
          inputFile.number = this.processLastIdx
          inputFile.text = ''
          this.processLastIdx++
          this.processImgFiles.push(inputFile)
        }
      } else {
        const newFile = this.$refs.files.files[0]
        newFile.previewURL = URL.createObjectURL(newFile)
        newFile.number = this.processLastIdx
        newFile.text = ''
        this.processLastIdx++
        this.processImgFiles[this.changeProcessPhotoIdx] = newFile
        // 리스트가 업데이트됐다는 이벤트가 발생하도록 임의의 값을 push하고 pop
        this.processImgFiles.push(0)
        this.processImgFiles.pop()
      }
      event.target.value = ""
      this.changeProcessPhotoIdx = -1
    },

    deleteProcessImg(number) {
      this.processImgFiles = this.processImgFiles.filter(data => data.number !== number)
    },
    
    onRecipeBtnClick() {
      let contentList = []
      const formData = new FormData()

      formData.append('title', this.title)
      formData.append('picture', this.picture)
      formData.append('content', this.content)
      formData.append('ingredient', this.ingredient)
      formData.append('userId', this.userinfo.userid) // 임의로 넣은 값
      for(let i=0; i < this.processImgFiles.length; i++ ) {
        contentList.push(this.processImgFiles[i].text)
        formData.append('files', this.processImgFiles[i])
      }

      
      const URL = API.url + recipeAPI.regist()
      console.log(API.url + recipeAPI.contentList())
      console.log(URL)

      axios.post(API.url + recipeAPI.contentList(), contentList, {
            headers: {
              "access-token": localStorage.getItem('access-token')
            }
          })
          .then(res =>  {
            console.log(res)
            axios.post(URL, formData, 
              { headers: {
                  "access-token": localStorage.getItem('access-token'),
                  "Content-Type": 'multipart/form-data',
                }
              })
              .then(res => {
                console.log(res)
                this.$router.push({ name: 'RecipeSearch' })
              })
              .catch(err => console.error(err))
          })
          .catch(err => console.error(err))

    }
  },
  computed: {
    ...mapGetters('user', ['userinfo',]),
    valid () {
      return !!this.title & !!this.picture & !!this.content & !!this.ingredient & !!this.processImgFiles.length
    }
  }
}
</script>




<style>
</style>