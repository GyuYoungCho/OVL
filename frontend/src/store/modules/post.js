// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import postAPI from '@/api/post.js'

export default {
namespaced: true,
	state: {
        postList:[],
        postLikeList:[],
        post:{},
        postPhotoList:[],
	},
	mutations: {
        GET_POST_LIST(state, payload) {
            state.postList = payload;
        },
        GET_POST_LIKE_LIST(state, payload) {
            state.postLikeList = payload;
        },
        GET_POST(state, payload) {
            state.post = payload;
        }, 
        GET_PHOTO_LIST(state, payload) {
            state.postPhotoList = payload
        }
	},
	actions: {
        getPostList(store, payload) {
            axios({
                method:"get",
                url: API.url + postAPI.select_all(payload),
            }).then((res)=>{
                store.commit("GET_POST_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        getPost(store, payload) {
            axios({
                method:"get",
                url: API.url + postAPI.select_detail_photo(payload),
            }).then((res)=>{
                store.commit("GET_PHOTO_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })

            axios({
                method:"get",
                url: API.url + postAPI.select_detail(payload),
            }).then((res)=>{
                store.commit("GET_POST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        getPostLikeList(store, payload) {
            axios({
                method:"get",
                url: API.url + postAPI.like_list(payload),
            }).then((res)=>{
                store.commit("GET_POST_LIKE_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        postLike(store, payload) {
            axios({
                method:"get",
                url: API.url + postAPI.like(payload.userId, payload.postId),
            }).then((res)=>{
                if (res.data=="success") {
                    store.dispatch("getPostLikeList", payload.userId);
                    if (payload.type==1) store.dispatch("getPostList", payload.userId);
                    else store.dispatch("getPost", payload.postId);
                }
            }).catch((err)=>{
                console.log(err);
            })
        },
    }
}