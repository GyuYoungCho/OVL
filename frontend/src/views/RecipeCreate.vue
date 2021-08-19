<template>
  <v-container>
    <!-- 레시피 등록 성공 시 -->
    <FlashModal :modalOpen="isRecipeRegist" title="레시피 등록" modalContent="레시피가 등록되었습니다" />
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
        <button :class="{ 'tabBtn': isIngredient, 'unselectedTabBtn': !isIngredient }" @click="onTabBtnClick"><span style="font-size:large">재료</span></button>
        <button :class="{ 'tabBtn': !isIngredient, 'unselectedTabBtn': isIngredient }" @click="onTabBtnClick"><span style="font-size:large">과정</span></button>
      </article>
      <div class="tab-div">
        <!-- 재료 탭 -->
        <div v-if="isIngredient">
          <textarea name="" id="" cols="30" rows="10" placeholder="재료를 적어주세요." class="ingredient-text" v-model="ingredient">
          </textarea>
        </div>
        <!-- 과정 탭 -->
        <div class="process-tab" v-else>
          <div v-for="(processImgFile, index) in processImgFiles" :key="index">
            
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
          <div v-if="processImgFiles.length<15">
            <p># next</p>
            <div class="process-input">
              <label for="processImgInput"><v-icon>mdi-plus</v-icon></label>
              <input type="file" id="processImgInput" ref="files" multiple @change="onProcessInputChange">
              <textarea name="" id="" cols="30" rows="10"></textarea>
            </div>
          </div>
          <div v-if="processImgFiles.length==15" class="mb-2">
            <p>과정은 15개만 등록할 수 있습니다</p>
          </div>
        </div>
      </div>
      <div>
        <button @click="onRecipeBtnClick" class="finalBtn" :class="{ 'bg-freditgreen': valid, 'disabledBtn': !valid }">
          레시피 등록
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
import fileUpload from '@/api/fileUpload.js'
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
  components: {FlashModal},
  data: () => ({
    isIngredient: true,
    title: "",
    picture: null,
    content: "",
    ingredient: "",
    processImgFiles: [],
    processLastIdx: 0,
    changeProcessPhotoIdx: -1,

    isRecipeRegist:false, 

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
        const limitInputFilesCount = inputFiles.length <=15 ? inputFiles.length : 15 
        for(let i=0; i < limitInputFilesCount; i++) {
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
        newFile.text = this.processImgFiles[this.changeProcessPhotoIdx].text
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
    
    async onRecipeBtnClick() {
      let contentList = []
      let tempPic = []
      var params = new URLSearchParams();
      tempPic.push(this.picture);

      var picPathList = await fileUpload.upload(tempPic, 'recipe');
      var processPathList = await fileUpload.upload(this.processImgFiles, 'recipe');

      params.append('title', this.title);
      params.append('content', this.content)
      params.append('ingredient', this.ingredient)
      params.append('userId', this.userinfo.userid)

      params.append('picPathList', picPathList)
      contentList.push('0')
      for(let i=0; i < this.processImgFiles.length; i++ ) {
        contentList.push(this.processImgFiles[i].text)
      }

      params.append('processPathList', processPathList)
      params.append('contentList', contentList)

      
      const URL = API.url + recipeAPI.regist()

      axios.post(URL, params)
      .then((response) => {
        // alert("보냈슴!");
        if (response.data.job=="success") {
          this.isRecipeRegist = true
          setTimeout(() => {
            this.isRecipeRegist = false
            this.$router.push({name:"RecipeSearch"});
          }, 1000);
        }
      })
      .catch((error) => {
        // alert("못보냈슴!");
        console.log(error);
      })
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