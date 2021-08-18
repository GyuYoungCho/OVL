// @/store/modules/user.js
import axios from "axios";
import API from "@/api/index.js";
import postAPI from "@/api/post.js";

export default {
  namespaced: true,
  state: {
    postList: [],
    postAll: [],
    postLikeList: [],
    post: {},
    postPhotoList: [],
    postMyList: [],
    foodPostList: [],
    clothPostList: [],
    cosmeticPostList: [],
    recommendCh: [],
    recommendRe: [],
  },
  getters: {
    postList(state) {
      return state.postList;
    },
    postAll(state) {
      return state.postAll;
    },
    postLikeList(state) {
      return state.postLikeList;
    },
    post(state) {
      return state.post;
    },
    postPhotoList(state) {
      return state.postPhotoList;
    },
    postMyList(state) {
      return state.postMyList;
    },
    foodPostList(state) {
      return state.foodPostList;
    },
    clothPostList(state) {
      return state.clothPostList;
    },
    cosmeticPostList(state) {
      return state.cosmeticPostList;
    },
    recommendRe(state) {
      return state.recommendRe;
    },
    recommendCh(state) {
      return state.recommendCh;
    },
  },
  mutations: {
    GET_POST_LIST(state, payload) {
      state.postList = state.postList.concat(payload);
    },
    GET_POST_ALL(state, payload) {
      state.postAll = state.postAll.concat(payload);
    },
    GET_POST_LIKE_LIST(state, payload) {
      state.postLikeList = payload;
    },
    GET_POST(state, payload) {
      state.post = payload;
    },
    GET_PHOTO_LIST(state, payload) {
      state.postPhotoList = payload;
    },
    SET_USER_POST_LIST(state, payload) {
      state.postMyList = payload;
      state.foodPostList = payload.filter((eachList) => eachList.postId.category === 1);
      state.clothPostList = payload.filter((eachList) => eachList.postId.category === 2);
      state.cosmeticPostList = payload.filter((eachList) => eachList.postId.category === 3);
    },
    GET_RECOMMEND_CHALLENGE(state, payload) {
      state.recommendCh = payload;
    },
    GET_RECOMMEND_RECIPE(state, payload) {
      state.recommendRe = payload;
    },
    MODIFY_POST_LIST(state, payload) {
      state.postList[payload.idx].postId = state.post;
    },
    MODIFY_POST_ALL(state, payload) {
      state.postAll[payload.idx].postId = state.post;
    },
    RESET_POST_LIST(state) {
      state.postList = [];
    },
    RESET_POST_ALL(state) {
      state.postAll = [];
    },
  },
  actions: {
    getPostList(store, payload) {
      // axios({
      //     method:"get",
      //     url: API.url + postAPI.select_my(payload),
      // }).then((res)=>{
      store.commit("GET_POST_LIST", payload);
      // }).catch((err)=>{
      //     console.log(err);
      // })
    },
    getPostAll(store, payload) {
      store.commit("GET_POST_ALL", payload);
    },
    getUserPostList(store, payload) {
      console.log("개인 게시글 가져오기!!");
      axios({
        method: "get",
        url: API.url + postAPI.select_user(payload),
      })
        .then((res) => {
          console.log("개인 게시글 가져오기 결과 : ", res.data);
          store.commit("SET_USER_POST_LIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async getPost(store, payload) {
      await axios({
        method: "get",
        url: API.url + postAPI.select_detail_photo(payload),
      })
        .then((res) => {
          store.commit("GET_PHOTO_LIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });

      await axios({
        method: "get",
        url: API.url + postAPI.select_detail(payload),
      })
        .then((res) => {
          store.commit("GET_POST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getPostLikeList(store, payload) {
      axios({
        method: "get",
        url: API.url + postAPI.like_list(payload),
      })
        .then((res) => {
          store.commit("GET_POST_LIKE_LIST", res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async postLike(store, payload) {
      await axios({
        method: "get",
        url: API.url + postAPI.like(payload.userId, payload.postId),
      })
        .then((res) => {
          if (res.data == "success") {
            // store.dispatch("getPostLikeList", payload.userId);
            // if (payload.type == 1) store.dispatch("getPostList", payload.userId);
            // store.dispatch("getPost", payload.postId);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    postDelete(store, payload) {
      axios({
        method: "delete",
        url: API.url + postAPI.delete(payload.postId),
      })
        .then((res) => {
          if (res.data.job == "success") {
            // store.dispatch("getPostList", payload.userId);
            // alert("삭제가 완료되었습니다.");
            return true;
          } else {
            alert("삭제에 실패했습니다.");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getRecommend(store) {
      let URL = API.url + postAPI.recommendCh();
      axios
        .get(URL)
        .then((res) => {
          store.commit("GET_RECOMMEND_CHALLENGE", res.data);
        })
        .catch((err) => console.error(err));

      URL = API.url + postAPI.recommendRe();
      axios
        .get(URL)
        .then((res) => {
          store.commit("GET_RECOMMEND_RECIPE", res.data);
        })
        .catch((err) => console.error(err));
    },
    resetPostList(store) {
      store.commit("RESET_POST_LIST");
    },
    resetPostAll(store) {
      store.commit("RESET_POST_ALL");
    },
    modifyPostList(store, payload) {
      store.commit("MODIFY_POST_LIST", payload);
    },
    modifyPostAll(store, payload) {
      store.commit("MODIFY_POST_ALL", payload);
    },
  },
};
