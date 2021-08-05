// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import commentAPI from '@/api/post_comment.js'

export default {
namespaced: true,
	state: {
        commentList:[],
        commentLikeList:[],
	},
	mutations: {
        GET_COMMENT_LIST(state, payload) {
            state.commentList = payload;
        },
        GET_COMMENT_LIKE_LIST(state, payload) {
            state.commentLikeList = payload;
        }
	},
	actions: {
        getCommentList(store, payload) {
            axios({
                method:"get",
                url: API.url + commentAPI.select_all(payload),
            }).then((res)=>{
                store.commit("GET_COMMENT_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        getCommentLikeList(store, payload) {
            axios({
                method:"get",
                url: API.url + commentAPI.like_list(payload.userId, payload.postId),
            }).then((res)=>{
                store.commit("GET_COMMENT_LIKE_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        commentLike(store, payload) {
            axios({
                method:"get",
                url: API.url + commentAPI.like(payload.userId, payload.commentId),
            }).then((res)=>{
                if (res.data=="success") {
                    store.dispatch("getCommentLikeList", payload);
                    store.dispatch("getCommentList", payload.postId);
                }
            }).catch((err)=>{
                console.log(err);
            })
        },
        commentAdd(store, payload) {
            axios({
                method:"post",
                url: API.url + commentAPI.regist(),
                params:{
                    "userId":payload.userId,
                    "postId":payload.postId,
                    "content":payload.content,
                }
            }).then((res)=>{
                if (res.data=="success") {
                    // 성공하면 댓글 리스트 다시 불러오기
                    store.dispatch("getCommentList", payload.postId); 
                    store.dispatch("post/getPost", payload.postId, { root: true });
                }
            }).catch((err)=>{
                console.log(err);
            })
        }
    }
}