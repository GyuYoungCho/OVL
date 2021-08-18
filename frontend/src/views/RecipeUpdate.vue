<template>
  <v-container>
    <FlashModal :modalOpen="isRecipeUpdate" title="레시피 수정" modalContent="레시피가 수정되었습니다" />
    <section class="recipeCreate">
      <h1>레시피 수정</h1>
      <!-- 요리 이름 -->
      <div>
        <input type="text" v-model="title" placeholder="요리 이름">
      </div>
      <!-- 대표 사진 -->
      <input type="file" ref="file" multiple @change="pictureInputChange" id="pictureImageInput">
      <label for="pictureImageInput">
        <div class="img-container">
            <img :src="picture.previewURL" alt="" v-if="!!picture">
            <img :src="recipe.filepath" alt="" v-else>
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
          <div v-for="(processFile, index) in processFiles" :key="index" >
            <div class="process-index">
              <span># {{ index + 1 }}</span>
              <button class="cancelBtn" @click="deleteProcessImg(index)">X</button>
            </div>
            <div class="process-data">
              <label for="processImgInput" @click="onExistingProcessPicClick(index)">
                <div class="preivew-img-container">
                  <img class="preview-img" :src="!!processFile.filepath ? processFile.filepath : processFile.previewURL" alt="" >
                </div>
              </label>
              <textarea cols="30" rows="10" v-model="processFile.content" @input="onProcessFileContentChange(processFile)"></textarea>
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
        <button @click="onRecipeUpdateBtnClick" class="finalBtn bg-freditgreen">
          레시피 수정
        </button>
      </div>
    </section>
  </v-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import axios from 'axios'
import API from '@/api/index.js'
import recipeAPI from '@/api/recipe.js'
import fileUpload from '@/api/fileUpload.js'
import FlashModal from '@/components/signup/FlashModal.vue'

export default {
  components: {FlashModal},
  data () {
    return {
      isIngredient: true,
      title: "",
      picture: null,
      content: "",
      ingredient: "",
      processFiles: [],
      changeProcessPhotoIdx: -1,
      processLastIdx: 0,

      // 백으로 넘겨줘야 하는 리스트들
      contentList: [],
      processIds: [],
      deleteIdList: [0],
      plusPhotoList: [],
      modifyPhotoList: [],
      modifyPhotoIdList: [0],

      plusPhotoNumberList: [],
      modifyContentIdList: [0],
      modifyContentObj: {},
      plusContentList: [],

      isRecipeUpdate:false
    }
  },

  methods: {
    ...mapActions('recipe', ['fetchRecipeDetail', 'fetchRecipeComments']),
    onTabBtnClick () {
      this.isIngredient = !this.isIngredient
    },

    pictureInputChange () {
      const picture = this.$refs.file.files[0]
      picture.previewURL = URL.createObjectURL(picture)
      this.picture = picture
    },

    deleteProcessImg(index) {
      const deletedFile = this.processFiles.splice(index, 1)[0]
      if (!this.plusPhotoNumberList.includes(deletedFile.number)) {
        this.deleteIdList.push(deletedFile.recipeProcessId)
      }
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
          inputFile.content = ''
          this.processLastIdx++
          this.processFiles.push(inputFile)
          this.plusPhotoList.push(inputFile)
          this.plusPhotoNumberList.push(inputFile.number)
        }
      } else {
        const newFile = this.$refs.files.files[0]
        newFile.previewURL = URL.createObjectURL(newFile)
        newFile.number = this.processLastIdx
        newFile.recipeProcessId = this.processFiles[this.changeProcessPhotoIdx].recipeProcessId
        newFile.content = this.processFiles[this.changeProcessPhotoIdx].content
        this.processLastIdx++
        this.modifyPhotoList.push(newFile)
        this.modifyPhotoIdList.push(newFile.recipeProcessId)
        this.processFiles[this.changeProcessPhotoIdx] = newFile
        // 리스트가 업데이트됐다는 이벤트가 발생하도록 임의의 값을 push하고 pop
        this.processFiles.push(0)
        this.processFiles.pop()
      }
      event.target.value = ""
      this.changeProcessPhotoIdx = -1
    },

    onProcessFileContentChange (processFile) {
      if (!processFile.type) {
        if (!this.modifyContentIdList.includes(processFile.recipeProcessId)) {
          this.modifyContentIdList.push(processFile.recipeProcessId)
        }
        this.modifyContentObj[processFile.recipeProcessId] = processFile.content
      }
    },

    async onRecipeUpdateBtnClick () {
      const params = new URLSearchParams()
      let tempPic = [];
      tempPic.push(this.picture);
      var picPathList = await fileUpload.upload(tempPic, 'recipe');
      var modifyPathList = await fileUpload.upload(this.modifyPhotoList, 'recipe');
      var plusPathList = await fileUpload.upload(this.plusPhotoList, 'recipe');
      
      const plusContentList = []

      plusContentList.push("temp")

      for (let i=0; i < this.plusPhotoList.length; i++) {
        plusContentList.push(this.plusPhotoList[i].content)
      }
      // 타이틀, 내용, 재료, 레시피아이디
      params.append('title', this.title)
      params.append('content', this.content)
      params.append('ingredient', this.ingredient)
      params.append('recipeId', this.recipe.recipeId)

      // 대표사진, 수정 사진, 새로운 사진 리스트
      params.append('picPathList', picPathList)
      params.append('modifyPathList', modifyPathList)
      params.append('plusPathList', plusPathList)

      // 수정 과정 아이디 리스트
      const modifyContentIdList = this.modifyContentIdList
      console.log('수정 과정 아이디 리스트;', modifyContentIdList)
      // 수정 과정 내용 리스트
      const modifyContentList = []

      modifyContentList.push("temp")
      for (let i=1; i < modifyContentIdList.length; i++) {
        modifyContentList.push(this.modifyContentObj[modifyContentIdList[i]])
      }
      console.log('수정 과정 내용 리스트;', modifyContentList)

      // 수정 사진 아이디 리스트
      const modifyPhotoIdList = this.modifyPhotoIdList
      // 삭제된 사진 아이디 리스트
      const deleteIdList = this.deleteIdList

      params.append('deleteIdList', deleteIdList)
      params.append('modifyContentIdList', modifyContentIdList)
      params.append('modifyContentList', modifyContentList)
      params.append('modifyPhotoIdList', modifyPhotoIdList)
      params.append('plusContentList', plusContentList)

      const URL = API.url + recipeAPI.modify();
      axios.put(URL, params)
      .then((response) => {
        if (response.data=="success") {
          this.fetchRecipeDetail(this.recipe.recipeId)
          this.fetchRecipeComments(this.recipe.recipeId)
          this.isRecipeUpdate = true;
          setTimeout(() => {
            this.isRecipeUpdate = false
            this.$router.push({ name: 'RecipeDetail', params: {recipeId: this.recipe.recipeId} })
          }, 1000)
          
        }
      })
      .catch((error) => {
        alert('문제가 발생했습니다.')
        console.error(error);
      })
    }
  },

  computed: {
    ...mapGetters('user', ['userinfo', ]),
    ...mapGetters('recipe', ['recipe', 'recipeDetail',]),
  },

  async created () {
    await this.fetchRecipeDetail(this.$route.params.recipeId)
    if (this.recipe.userid.userid !== this.userinfo.userid) {
      this.$router.back()
    } else {
      this.title = this.recipe.title
      this.content = this.recipe.content
      this.ingredient = this.recipe.ingredient
      for (let i=0; i < this.recipeDetail.length; i++) {
        this.processFiles.push(this.recipeDetail[i])
      }
    }
  }
  
}
</script>

<style>

</style>