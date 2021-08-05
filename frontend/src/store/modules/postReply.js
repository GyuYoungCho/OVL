// @/store/modules/user.js
import axios from "axios";
import API from '@/api/index.js'
import replyAPI from '@/api/post_reply.js'

export default {
namespaced: true,
	state: {
        replyList:[],
        replyLikeList:[],
	},
	mutations: {
        GET_REPLY_LIST(state, payload) {
            state.replyList = payload;
        },
        GET_REPLY_LIKE_LIST(state, payload) {
            state.replyLikeList = payload;
        }
	},
	actions: {
        getReplyList(store, payload) {
            axios({
                method:"get",
                url: API.url + replyAPI.select_all(payload),
            }).then((res)=>{
                store.commit("GET_REPLY_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        getReplyLikeList(store, payload) {
            axios({
                method:"get",
                url: API.url + replyAPI.like_list(payload),
            }).then((res)=>{
                store.commit("GET_REPLY_LIKE_LIST", res.data);
            }).catch((err)=>{
                console.log(err);
            })
        },
        replyLike(store, payload) {
            axios({
                method:"get",
                url: API.url + replyAPI.like(payload.userId, payload.replyId),
            }).then((res)=>{
                if (res.data=="success") {
                    store.dispatch("getReplyLikeList", payload.userId);
                    store.dispatch("getReplyList", payload.postId);
                }
            }).catch((err)=>{
                console.log(err);
            })
        },
        replyAdd(store, payload) {
            axios({
                method:"post",
                url: API.url + replyAPI.regist(),
                params:{
                    "userId":payload.userId,
                    "commentId":payload.commentId,
                    "content":payload.content,
                }
            }).then((res)=>{
                if (res.data=="success") {
                    // 성공하면 답글 리스트 다시 불러오기
                    store.dispatch("getReplyList", payload.postId); 
                    store.dispatch("postComment/getCommentList", payload.postId, { root: true });
                    store.dispatch("post/getPost", payload.postId, { root: true });
                }
            }).catch((err)=>{
                console.log(err);
            })
        }
    }
}