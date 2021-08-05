// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import postAPI from '@/api/post.js'

export default {
namespaced: true,
	state: {
        postList:[],
        postLikeList:[],
	},
	mutations: {
        GET_POST_LIST(state, payload) {
            state.postList = payload;
        },
        GET_POST_LIKE_LIST(state, payload) {
            state.postLikeList = payload;
            console.log("좋아요 리스트 ! : "+state.postLikeList);
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
                    store.dispatch("getPostList", payload.userId);
                }
            }).catch((err)=>{
                console.log(err);
            })
        }
    }
}